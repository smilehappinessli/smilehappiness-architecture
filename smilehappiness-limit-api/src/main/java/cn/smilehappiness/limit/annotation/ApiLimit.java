package cn.smilehappiness.limit.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * <p>
 * User-defined, current-limiting annotation (one minute by default, current-limiting 500 times ）
 * <p/>
 *
 * @author
 * @Date 2020/7/5 20:05
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ApiLimit {

    /**
     * Limit the number of times that can be accessed in a certain period of time. The default setting is 500
     */
    int limitCounts() default 500;

    /**
     * A certain time period of restricted access, in seconds, with the default value of 1 minute 
     */
    int timeSecond() default 60;

    /**
     * To restrict an Api resource, here can be the method name or the business combined by the method name key
     */
    String limitApiName();
}
   
