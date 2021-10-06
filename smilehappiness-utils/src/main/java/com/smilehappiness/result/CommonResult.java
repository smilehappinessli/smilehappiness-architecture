package com.smilehappiness.result;

import com.smilehappiness.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * 结果统一返回
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 15:22
 */
@Getter
@Setter
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 3177777670245790919L;

    private String code;
    private String bizCode;
    private String message;
    private T result;
    private String dateTime;

    public CommonResult() {
        String dateFormatStr = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatStr);
        this.dateTime = dateTimeFormatter.format(LocalDateTime.now());
    }

    public CommonResult(String httpCode, String bizCode, String message) {
        this(httpCode, bizCode, message, null);
    }

    public CommonResult(String httpCode, String bizCode, T result) {
        this();
        this.code = httpCode;
        this.bizCode = bizCode;
        this.result = result;
    }

    public CommonResult(String httpCode, String bizCode, String message, T result) {
        this();
        this.code = httpCode;
        this.bizCode = bizCode;
        this.message = message;
        this.result = result;
    }

    public static CommonResult fail(String message) {
        return new CommonResult(ResultCodeEnum.BAD_REQUEST.getCode(), null, message);
    }

    /**
     * <p>
     * 当返回失败结果时，推荐使用该方法
     * <p/>
     *
     * @param httpCode
     * @param bizCode
     * @param message
     * @return com.smilehappiness.result.ObjectRestResponse
     * @Date 2021/10/2 16:58
     */
    public static CommonResult fail(String httpCode, String bizCode, String message) {
        return new CommonResult(httpCode, bizCode, message);
    }

    public static <T> CommonResult fail(String httpCode, String bizCode, String message, T result) {
        return new CommonResult(httpCode, bizCode, message, result);
    }

    public static <T> CommonResult<T> success(T result) {
        return new CommonResult(ResultCodeEnum.OK.getCode(), null, result);
    }

    public static <T> CommonResult<T> success(String code, String bizCode, T result) {
        if (StringUtils.isBlank(code)) {
            code = ResultCodeEnum.OK.getCode();
        }
        return new CommonResult<>(code, bizCode, result);
    }

    public static <T> CommonResult<T> success(String code, String bizCode, String message, T result) {
        if (StringUtils.isBlank(code)) {
            code = ResultCodeEnum.OK.getCode();
        }
        return new CommonResult<>(code, bizCode, message, result);
    }


}
