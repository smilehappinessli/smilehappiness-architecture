package com.smilehappiness.mock.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * MockServer注解
 * <p/>
 *
 * @author smilehappiness
 * @date 2021-01-13 10:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MockServer {

    /**
     * 接口描述
     */
    String value() default "";

    /**
     * 请求路径
     */
    String path() default "";

}
