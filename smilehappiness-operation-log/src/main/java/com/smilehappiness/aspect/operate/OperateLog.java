package com.smilehappiness.aspect.operate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 自定义日志存储注解
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/28 13:38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    /**
     * 日志操作描述信息
     */
    String value() default "";
}
