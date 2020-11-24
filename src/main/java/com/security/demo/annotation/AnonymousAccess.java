package com.security.demo.annotation;

import java.lang.annotation.*;

/**
 * @author lxc
 * 用户标记匿名访问方法，也就是登录时不带token就可以访问
 *
 * @Documented – 注解是否将包含在JavaDoc中
   @Retention – 什么时候使用该注解
   @Target – 注解用于什么地方
   @Inherited – 是否允许子类继承该注解
 */
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AnonymousAccess {
}
