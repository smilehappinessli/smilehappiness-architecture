package cn.smilehappiness.exception.exceptions;

import cn.smilehappiness.exception.enums.BaseExceptionEnum;

/**
 * <p>
 * Business exception class
 * <p/>
 *
 * @author
 * @Date 2021/8/30 16:08
 */
public abstract class AbstractBizException extends RuntimeException {

    protected String code;
    protected String bizCode;

    public String getCode() {
        return code;
    }

    public String getBizCode() {
        return bizCode;
    }

    private AbstractBizException() {
    }

    public AbstractBizException(Throwable ex) {
        super(ex);
    }

    public AbstractBizException(String message) {
        super(message);
    }

    public AbstractBizException(String message, Throwable ex) {
        super(message, ex);
    }

    public AbstractBizException(String code, String bizCode, String message) {
        super(message);
        this.code = code;
        this.bizCode = bizCode;
    }

    public AbstractBizException(BaseExceptionEnum baseExceptionEnum) {
        super(String.format(baseExceptionEnum.getMessage()));
        this.code = baseExceptionEnum.getCode().toString();
        this.bizCode = baseExceptionEnum.getBizCode().toString();
    }

    public AbstractBizException(BaseExceptionEnum baseExceptionEnum, Object... args) {
        super(String.format(baseExceptionEnum.getMessage(), args));
        this.code = baseExceptionEnum.getCode().toString();
        this.bizCode = baseExceptionEnum.getBizCode().toString();
    }
}
