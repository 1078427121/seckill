package com.bytedance.summer.debug;

import java.lang.annotation.*;

/**
 * @author pjl
 * @date 2019/8/29 3:22
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CountTime {
}
