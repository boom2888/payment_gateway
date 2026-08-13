package com.games.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API日志记录注解
 * 用于标记需要记录日志的接口方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogApi {

    /**
     * API类型描述
     * 可以是ApiType枚举值的字符串形式，如 "TRANSFER", "CREATE_ORDER" 等
     * 如果不指定，将根据方法名自动推断
     */
    String value() default "";

    /**
     * 业务描述
     */
    String description() default "";
}
