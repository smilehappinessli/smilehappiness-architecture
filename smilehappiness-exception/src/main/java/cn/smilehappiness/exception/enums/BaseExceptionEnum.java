package cn.smilehappiness.exception.enums;

/**
 * <p>
 * Basic exception enumeration class 
 * <p/>
 *
 * @author
 * @Date 2021/8/30 16:06
 */
public interface BaseExceptionEnum<T> {

    /**
     * <p>
     * Get the Code value of the exception 
     * <p/>
     *
     * @param
     * @return T
     * @Date 2021/8/30 16:06
     */
    T getCode();

    /**
     * <p>
     * Get the bizCode value of the exception 
     * <p/>
     *
     * @param
     * @return T
     * @Date 2021/10/2 16:40
     */
    T getBizCode();

    /**
     * <p>
     * Get the message information of the exception 
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/8/30 16:06
     */
    String getMessage();
}
