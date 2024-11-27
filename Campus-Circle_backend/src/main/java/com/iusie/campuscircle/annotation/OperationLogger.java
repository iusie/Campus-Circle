package com.iusie.campuscircle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author iusie
 * @description 用于需要记录访客访问日志的方法
 * @date 2024/11/27
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogger {
    /**
     * 操作描述
     */
    String value() default "";
}
