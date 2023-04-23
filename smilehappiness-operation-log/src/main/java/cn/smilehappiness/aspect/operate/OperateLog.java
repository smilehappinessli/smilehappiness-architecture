package cn.smilehappiness.aspect.operate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Custom log storage annotation 
 * <p/>
 *
 * @author
 * @Date 2021/8/28 13:38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    /**
     * Log operation description information 
     */
    String value() default "";
}
