package com.bytedance.summer.cheatdetect.aop;

import java.lang.annotation.*;

/**
 * @author pjl
 * @date 2019/8/19 22:19
 * 限制单个用户多次连续访问
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLimiter {
    int seconds() default 2;//统计接口调用次数的时间
    int maxCount() default 4;//最大调用次数
}
