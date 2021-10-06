package com.smilehappiness.exception.exceptions;

/**
 * <p>
 * 系统级异常
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 16:33
 */
public final class SystemInternalException extends AbstractException {

    /**
     * 状态码
     */
    private String code;

    public SystemInternalException(String message) {
        super(message);
    }

    public SystemInternalException(String code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
