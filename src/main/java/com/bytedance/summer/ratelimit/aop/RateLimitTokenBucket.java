package com.bytedance.summer.ratelimit.aop;

import java.lang.annotation.*;

/**
 * @author pjl
 * @date 2019/8/4 11:31
 * 令牌桶切面的注解
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimitTokenBucket {
    int limitNum() default 20;
}
