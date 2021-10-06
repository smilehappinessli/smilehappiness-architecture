package com.smilehappiness.exception.enums;

/**
 * <p>
 * 基础异常枚举类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 16:06
 */
public interface BaseExceptionEnum<T> {

    /**
     * <p>
     * 获取异常的Code值
     * <p/>
     *
     * @param
     * @return T
     * @Date 2021/8/30 16:06
     */
    T getCode();

    /**
     * <p>
     * 获取异常的bizCode值
     * <p/>
     *
     * @param
     * @return T
     * @Date 2021/10/2 16:40
     */
    T getBizCode();

    /**
     * <p>
     * 获取异常的message信息
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/8/30 16:06
     */
    String getMessage();
}
