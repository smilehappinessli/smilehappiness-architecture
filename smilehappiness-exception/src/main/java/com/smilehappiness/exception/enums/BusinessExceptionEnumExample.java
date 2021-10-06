package com.smilehappiness.exception.enums;

/**
 * <p>
 * 业务异常枚举类定义
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 17:45
 */
public enum BusinessExceptionEnumExample implements BaseExceptionEnum<String> {

    /**
     * 获取的用户信息为空
     */
    USER_INFO_NULL("1", "#USER_INFO_NULL#", "获取的用户信息为空");

    BusinessExceptionEnumExample(String code, String bizCode, String message) {
        this.code = code;
        this.bizCode = bizCode;
        this.message = message;
    }

    private final String code;
    private final String bizCode;
    private final String message;

    /**
     * <p>
     * 获取code值
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/2 16:42
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * <p>
     * 获取业务code值，后续可能基于业务code做信息对照
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/2 16:42
     */
    @Override
    public String getBizCode() {
        return bizCode;
    }

    /**
     * <p>
     * 获取业务描述信息
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/2 16:43
     */
    @Override
    public String getMessage() {
        return message;
    }
}
