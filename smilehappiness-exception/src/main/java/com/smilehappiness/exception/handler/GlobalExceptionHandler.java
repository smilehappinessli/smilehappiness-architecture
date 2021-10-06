package com.smilehappiness.exception.handler;

import com.smilehappiness.enums.ResultCodeEnum;
import com.smilehappiness.exception.exceptions.AbstractBizException;
import com.smilehappiness.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 全局异常处理类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 15:59
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * <p>
     * 异常统一处理
     * <p/>
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param exception
     * @return com.smilehappiness.result.ObjectRestResponse
     * @Date 2021/10/2 15:55
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult defaultErrorHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception exception) {
        httpServletResponse.setStatus(200);
        CommonResult result;
        if (exception instanceof AbstractBizException) {
            AbstractBizException baseException = (AbstractBizException) exception;
            //目前，只有业务异常，有业务code值
            if (StringUtils.isNotBlank(baseException.getCode())) {
                result = new CommonResult(baseException.getCode(), baseException.getBizCode(), exception.getLocalizedMessage());
            } else {
                result = new CommonResult(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), baseException.getBizCode(), exception.getLocalizedMessage());
            }

        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            List<ObjectError> allErrorList = bindingResult.getAllErrors();
            StringBuilder msg = new StringBuilder();
            allErrorList.forEach(objectError -> {
                FieldError fieldError = (FieldError) objectError;
                msg.append(fieldError.getDefaultMessage()).append(",");
            });
            result = new CommonResult(ResultCodeEnum.PARAM_PARAMETER_ERROR.getCode(), null, msg.substring(0, msg.length() - 1));
        } else if (exception instanceof NoHandlerFoundException) {
            httpServletResponse.setStatus(404);
            result = new CommonResult(ResultCodeEnum.NOT_FOUND.getCode(), null, exception.getLocalizedMessage());
        } else {
            result = new CommonResult(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), null, ResultCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
        }

        log.error("[" + result.getCode() + "]" + result.getMessage(), exception);
        return result;
    }


    /**
     * <p>
     * 打印关键log信息
     * <p/>
     *
     * @param e
     * @return void
     * @Date 2021/8/30 15:49
     */
    private void createLogger(Exception e) {
        logger.info(e.getMessage());
        logger.info(e.getStackTrace()[0].toString());
        logger.error(e.getMessage());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            logger.error(stackTraceElement.toString());
        }
    }
}
