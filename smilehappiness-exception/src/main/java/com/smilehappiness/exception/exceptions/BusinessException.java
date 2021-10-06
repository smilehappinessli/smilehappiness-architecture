package com.smilehappiness.exception.exceptions;

import com.smilehappiness.exception.enums.BaseExceptionEnum;

/**
 * <p>
 * 业务异常定义
 * 这里额外添加一个业务code，为了后续扩展性更强(可以基于业务code做不同的信息对照展示，对于前端而言，基于普通的code，200或者非200即可判断是否请求接口成功)
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 16:50
 */
public final class BusinessException extends AbstractBizException {

    /**
     * <p>
     * 业务异常信息
     * <p/>
     *
     * @param message
     * @return
     * @Date 2021/10/2 16:51
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * <p>
     * 业务异常信息
     * <p/>
     *
     * @param code    基础code，前端可以基于该值判断是否请求接口成功（200或者非200）
     * @param bizCode 业务code
     * @param message 异常信息
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
