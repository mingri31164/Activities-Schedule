package com.mingri.prize.commons.annotition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 滑动窗口限流注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SlideLimiter {

    /**
     * 限流key前缀
     */
    String key() default "rate_limit:";

    /**
     * 时间窗口（秒）
     */
    int window() default 60;

    /**
     * 最大请求次数
     *
     */
    int limit() default 100;

    /**
     * 限流维度表达式（支持SpEL）
     */
    String dimension() default "";

}
