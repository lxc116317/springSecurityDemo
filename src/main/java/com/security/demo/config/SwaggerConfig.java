package com.security.demo.config;

import com.google.common.base.Predicates;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
/**
 * @RequiredArgsConstructor,使用后添加一个构造函数，该构造函数含有所有已声明字段属性参数
 */
@RequiredArgsConstructor
public class SwaggerConfig {

//    @Value("${jwt.header}")
//    private String tokenHeader;
//
//    @Value("${jwt.token-start-with}")
//    private String tokenStartWith;

    @Value("${swagger.enabled}")
    private Boolean enabled;

    @Autowired
    private final SecurityProperties properties;

    @Bean
    @SuppressWarnings("all")
    public Docket createRestApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name(properties.getHeader()).description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue(properties.getTokenStartWith() + " ")
                .required(true)
                .build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(apiInfo())
                .select()
                /**
                 * Predicates : 谓语
                 */
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("前后端分离的springSecurity框架")
                .title("接口文档")
                .version("2.4")
                .build();
    }

}

