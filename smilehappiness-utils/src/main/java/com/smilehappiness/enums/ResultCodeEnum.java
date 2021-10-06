package com.smilehappiness.enums;

/**
 * <p>
 * Http请求时的自定义查询状态码，主要参考Http状态码
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 15:01
 */
public enum ResultCodeEnum {

    /**
     * 200请求成功
     */
    OK("200", "200请求成功"),
    /**
     * 207频繁操作
     */
    MULTI_STATUS("207", "207频繁操作"),
    /**
     * 303登录失败
     */
    LOGIN_FAIL("303", "303登录失败"),
    /**
     * 400请求参数出错
     */
    BAD_REQUEST("400", "400请求参数出错"),
    /**
     * 401没有登录
     */
    UNAUTHORIZED("401", "401没有登录"),
    /**
     * 403没有权限
     */
    FORBIDDEN("403", "403没有权限"),
    /**
     * 404找不到页面
     */
    NOT_FOUND("404", "404找不到页面"),
    /**
     * 408请求超时
     */
    REQUEST_TIMEOUT("408", "408请求超时"),
    /**
     * 409发生冲突
     */
    CONFLICT("409", "409发生冲突"),
    /**
     * 410已被删除
     */
    GONE("410", "410已被删除"),
    /**
     * 423已被锁定
     */
    LOCKED("423", "423已被锁定"),
    /**
     * 500服务器出错
     */
    INTERNAL_SERVER_ERROR("500", "500服务器出错"),
    /**
     * 参数错误
     */
    PARAM_PARAMETER_ERROR("501", "Parameter error"),
    /**
     * 参数为空
     */
    PARAM_PARAMETER_IS_NULL("502", "Parameter is null");

    private String code;
    private String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(String message) {
        return String.format(this.message, message == null ? "" : message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
