package cn.smilehappiness.exception.handler;

import cn.smilehappiness.common.enums.ResultCodeEnum;
import cn.smilehappiness.common.result.ObjectRestResponse;
import cn.smilehappiness.exception.exceptions.BusinessException;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * Global exception handling class 
 * <p/>
 *
 * @author
 * @Date 2021/8/30 15:59
 */
@RefreshScope
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Value("${logger.stackTranceFlag:true}")
    private boolean stackTranceFlag;

    /**
     * <p>
     * Method parameter verification class exception handling 
     * <p/>
     *
     * @param httpServletResponse Response result class 
     * @param ex                  {@link MethodArgumentNotValidException}
     * @return cn.smilehappiness.common.result.ObjectRestResponse
     * @Date 2021/12/5 17:04
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ObjectRestResponse handle(HttpServletResponse httpServletResponse, MethodArgumentNotValidException ex) {
        if (stackTranceFlag) {
            ex.printStackTrace();
            logger.error("MethodArgumentNotValidException:{}", ex);
        } else {
            logger.error(ex.getMessage());
        }


        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> allErrorList = bindingResult.getAllErrors();
        StringBuilder msg = new StringBuilder();
        allErrorList.forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            msg.append(StringUtils.join("【", fieldError.getField(), "】")).append(fieldError.getDefaultMessage()).append(",");
        });

        httpServletResponse.setStatus(Integer.parseInt(ResultCodeEnum.BAD_REQUEST.getCode()));
        return new ObjectRestResponse(ResultCodeEnum.BAD_REQUEST.getCode(), null, msg.substring(0, msg.length() - 1));
    }

    /**
     * <p>
     * Illegal parameter exception handling 
     * <p/>
     *
     * @param httpServletResponse Response result class 
     * @param ex                  {@link IllegalArgumentException}
     * @return cn.smilehappiness.common.result.ObjectRestResponse
     * @Date 2021/12/5 17:11
     */
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ObjectRestResponse handle(HttpServletResponse httpServletResponse, IllegalArgumentException ex) {
        if (stackTranceFlag) {
            ex.printStackTrace();
            logger.error("IllegalArgumentException:{}", ex);
        } else {
            logger.error(ex.getMessage());
        }

        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ObjectRestResponse(ResultCodeEnum.BAD_REQUEST.getCode(), null, ex.getMessage());
    }

    /**
     * <p>
     * System exception handling 
     * <p/>
     *
     * @param httpServletResponse Response result class 
     * @param ex                  {@link BusinessException}
     * @return cn.smilehappiness.common.result.ObjectRestResponse
     * @Date 2021/12/5 17:15
     */
    @ResponseBody
    @ExceptionHandler(SystemInternalException.class)
    public ObjectRestResponse handle(HttpServletResponse httpServletResponse, SystemInternalException ex) {
        if (stackTranceFlag) {
            ex.printStackTrace();
            logger.error("SystemInternalException:{}", ex);
        } else {
            logger.error(ex.getMessage());
        }

        httpServletResponse.setStatus(Integer.parseInt(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode()));
        return new ObjectRestResponse(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), null, ex.getMessage());
    }

    /**
     * <p>
     * User-friendly business exception handling 
     * <p/>
     *
     * @param httpServletResponse Response result class 
     * @param ex                  {@link BusinessException}
     * @return cn.smilehappiness.common.result.ObjectRestResponse
     * @Date 2021/12/5 17:15
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ObjectRestResponse handle(HttpServletResponse httpServletResponse, BusinessException ex) {
        if (stackTranceFlag) {
            ex.printStackTrace();
        }

        ObjectRestResponse result;
        httpServletResponse.setStatus(Integer.parseInt(ResultCodeEnum.BAD_REQUEST.getCode()));

        //At present, there are only business exceptions and business code values 
        if (StringUtils.isNotBlank(ex.getCode())) {
            result = new ObjectRestResponse(ex.getCode(), ex.getBizCode(), ex.getMessage());
        } else {
            httpServletResponse.setStatus(Integer.parseInt(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode()));
            result = new ObjectRestResponse(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ex.getBizCode(), ex.getMessage());
        }

        if (StringUtils.isBlank(result.getBizCode())) {
            logger.warn(StringUtils.join("【", result.getCode(), "】--", result.getMessage()), "】", ex);
        } else {
            logger.warn(StringUtils.join("【", result.getCode(), "】--", "【", result.getBizCode(), "】--", result.getMessage()), "】", ex);
        }

        return result;
    }

    /**
     * <p>
     * Resource not found exception handling 
     * <p/>
     *
     * @param httpServletResponse Response result class 
     * @param ex                  {@link NoHandlerFoundException}
     * @return cn.smilehappiness.common.result.ObjectRestResponse
     * @Date 2021/12/5 17:22
     */
    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    public ObjectRestResponse handle(HttpServletResponse httpServletResponse, NoHandlerFoundException ex) {
        if (stackTranceFlag) {
            ex.printStackTrace();
            logger.error("NoHandlerFoundException:{}", ex);
        } else {
            logger.error(ex.getMessage());
        }

        httpServletResponse.setStatus(Integer.parseInt(ResultCodeEnum.NOT_FOUND.getCode()));
        return new ObjectRestResponse(ResultCodeEnum.NOT_FOUND.getCode(), null, ex.getLocalizedMessage());
    }


    /**
     * <p>
     * Other exception handling 
     * <p/>
     *
     * @param httpServletResponse Response result class 
     * @param ex                  {@link Exception}
     * @return cn.smilehappiness.common.result.ObjectRestResponse
     * @Date 2021/12/5 17:23
     */
    @ExceptionHandler(value = Exception.class)
    public ObjectRestResponse defaultErrorHandler(HttpServletResponse httpServletResponse, Exception ex) {
        if (stackTranceFlag) {
            ex.printStackTrace();
            logger.error("Exception:{}", ex);
        } else {
            logger.error(ex.getMessage());
        }

        httpServletResponse.setStatus(Integer.parseInt(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode()));
        return new ObjectRestResponse(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), null, ResultCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * <p>
     * Print key log information 
     * <p/>
     *
     * @param ex
     * @return void
     * @Date 2021/8/30 15:49
     */
    private void createLogger(Exception ex) {
        logger.info(ex.getMessage());
        logger.info(ex.getStackTrace()[0].toString());
        logger.error(ex.getMessage());
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            logger.error(stackTraceElement.toString());
        }
    }

}
