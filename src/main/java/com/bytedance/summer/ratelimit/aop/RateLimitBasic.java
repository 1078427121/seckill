package com.bytedance.summer.ratelimit.aop;

import java.lang.annotation.*;

/**
 * @author pjl
 * @date 2019/8/4 11:28
 * 基于RateLimiter的切面的注解
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimitBasic {
    int limitNum() default 20;
}
