package cn.smilehappiness.exception.exceptions;

import cn.smilehappiness.exception.enums.BaseExceptionEnum;

/**
 * <p>
 * Business exception definition
 * An additional business code is added here to make it more scalable in the future (different information comparison displays can be made based on the business code. For the front end, based on the common code, 200 or non-200 can determine whether the request interface is successful )
 * <p/>
 *
 * @author
 * @Date 2021/8/30 16:50
 */
public final class BusinessException extends AbstractBizException {

    /**
     * <p>
     * Business exception information
     * <p/>
     *
     * @param message
     * @return
     * @Date 2021/10/2 16:51
     */
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable ex) {
        super(ex);
    }

    public BusinessException(String message, Throwable ex) {
        super(message, ex);
    }


    /**
     * <p>
     * Business exception information
     * <p/>
     *
     * @param code    Basic code. The front-end can determine whether the request for the interface is successful (200 or not) based on this value 200）
     * @param bizCode business code
     * @param message Exception information
     * @return
     * @Date 2021/10/2 16:51
     */
    public BusinessException(String code, String bizCode, String message) {
        super(code, bizCode, message);
    }

    public <T extends BaseExceptionEnum> BusinessException(T businessExceptionEnum) {
        super(businessExceptionEnum);
    }

    public <T extends BaseExceptionEnum> BusinessException(T businessExceptionEnum, Object... args) {
        super(businessExceptionEnum, args);
    }

}
