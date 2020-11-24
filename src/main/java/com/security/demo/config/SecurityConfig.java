package com.security.demo.config;

import com.security.demo.annotation.AnonymousAccess;
import com.security.demo.security.JwtAccessDeniedHandler;
import com.security.demo.security.JwtAuthenticationEntryPoint;
import com.security.demo.security.TokenProvider;
import com.security.demo.utils.enums.RequestMethodEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CorsFilter corsFilter;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private final ApplicationContext applicationContext;


    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /**
         * RequestMappingHandlerMapping会把Controller里面带有@RequestMapping注解的方法都加到一个容器里面，
         * 然后RequestMappingHandlerAdapter根据里面的自定义配置可以对经过这些方法的请求的数据做一些额外的处理
         */
        // 搜寻匿名标记 url： @AnonymousAccess
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)applicationContext.getBean("requestMappingHandlerMapping");
//        相当于把带有@RequestMapping注解的方法都添加到容器里面
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
//        将所有带有@RequestMapping注解的方法都传入里面
        Map<String, Set<String>> anonymousUrl = getAnonymousUrl(handlerMethods);


        httpSecurity
                // 禁用 CSRF
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 授权异常
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // 防止iframe 造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()
                // 不创建会话
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 静态资源等等
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**"
                ).permitAll()
                // swagger 文档
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                // 文件
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/file/**").permitAll()
                // 阿里巴巴 druid
                .antMatchers("/druid/**").permitAll()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // 不需要认证的接口
//                .antMatchers("/auth/login").permitAll()

                // 自定义匿名访问所有url放行：允许匿名和带Token访问（不加token），细腻化到每个 Request 类型
                .antMatchers(HttpMethod.GET,anonymousUrl.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST,anonymousUrl.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.PUT,anonymousUrl.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.PATCH,anonymousUrl.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.DELETE,anonymousUrl.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
                // 所有类型的接口都放行
                .antMatchers(anonymousUrl.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
                // 所有请求都需要认证
                .anyRequest().authenticated()
                .and().apply(securityConfigurerAdapter());
    }

    private Map<String, Set<String>> getAnonymousUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, Set<String>> anonymousUrls = new HashMap<>(6);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();

        for ( Map.Entry<RequestMappingInfo, HandlerMethod> bean : handlerMethodMap.entrySet() ){
//          获取传入的处理器方法
            HandlerMethod value = bean.getValue();

            AnonymousAccess methodAnnotation = value.getMethodAnnotation(AnonymousAccess.class);
            if ( methodAnnotation != null ){
//                bean.getKey().getMethodsCondition().getMethods()获取请求方法，是GET、POST、DELETE等等
                ArrayList<RequestMethod> requestMethods = new ArrayList<>(bean.getKey().getMethodsCondition().getMethods());

                RequestMethodEnum requestMethodEnum = RequestMethodEnum.find(requestMethods.size() == 0 ?
                        RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                switch (Objects.requireNonNull(requestMethodEnum)){
                    case GET:
//                        获取请求路径
                        get.addAll(bean.getKey().getPatternsCondition().getPatterns());
                        break;
                    case POST:
//                        获取请求路径
                        post.addAll(bean.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PUT:
//                        获取请求路径
                        put.addAll(bean.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PATCH:
//                        获取请求路径
                        patch.addAll(bean.getKey().getPatternsCondition().getPatterns());
                        break;
                    case DELETE:
//                        获取请求路径
                        delete.addAll(bean.getKey().getPatternsCondition().getPatterns());
                        break;
                    default:
                        all.addAll(bean.getKey().getPatternsCondition().getPatterns());
                        break;
                }
            }
        }
        anonymousUrls.put(RequestMethodEnum.GET.getType(),get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(),post);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(),delete);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(),put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(),patch);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(),all);
        return anonymousUrls;
    }

    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(tokenProvider);
    }
}
