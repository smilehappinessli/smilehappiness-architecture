package com.smilehappiness.exception.exceptions;

/**
 * <p>
 * 抽象系统内部异常类，非业务系统提示内部异常类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 16:33
 */
public abstract class AbstractException extends RuntimeException {

    public AbstractException() {
    }

    public AbstractException(Throwable ex) {
        super(ex);
    }

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * <p>
     * 获取异常对应的业务编码
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/8/30 16:46
     */
    public abstract String getCode();
}