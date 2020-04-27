package com.zjc.friend.demo.security;

import java.lang.annotation.*;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 10:09 2020/4/10
 * @ Description：
 * @ Modified By：
 * @Version: $
 */

/**
 * 自定义注解 （根据角色来拦截访问）
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {
    long value();
}
