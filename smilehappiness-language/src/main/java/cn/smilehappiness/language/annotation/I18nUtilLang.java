package cn.smilehappiness.language.annotation;

import java.lang.annotation.*;

/**
 * The specific business has not yet been realized 
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nUtilLang {
    public String value() default "";
}
