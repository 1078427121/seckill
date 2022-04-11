package com.bytedance.summer.cheatdetect.aop;

import java.lang.annotation.*;

/**
 * @author pjl
 * @date 2019/8/19 22:19
 * 验证uid与sessionid是否匹配
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionCheck {
}
