package cn.smilehappiness.cache.interceptor;

import java.lang.annotation.*;

/**
 * <p>
 * Interface idempotence verification annotation 
 * <p/>
 *
 * @author
 * @Date 2021/10/14 15:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface InterfaceIdempotency {

    /**
     * Operation Token validity period, unit: seconds, default: 90 seconds 
     */
    int operationTokenExpire() default 90;

    /**
     * TokenPrompt message on failure 
     */
    String errorMessage() default "This operation has been submitted, please do not resubmit ";

    /**
     * The unique key of the form can only be submitted once within the valid time of the same key to ensure the idempotence of the interface 
     */
    String formUniqueKey() default "formUniqueKey";

    /**
     * Whether to force verification of request headers and token validity 
     *
     * @return true or false
     */
    boolean validateToken() default true;

}
