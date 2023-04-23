package cn.smilehappiness.common.result;

import cn.smilehappiness.common.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * Unified return of results 
 * <p/>
 *
 * @author
 * @Date 2021/8/30 15:22
 */
@Getter
@Setter
public class ObjectRestResponse<T> implements Serializable {

    private static final long serialVersionUID = 3177777670245790919L;

    private String code;
    private String bizCode;
    private String message;
    private T result;
    private T data;
    private String dateTime;

    public ObjectRestResponse() {
        String dateFormatStr = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatStr);
        this.dateTime = dateTimeFormatter.format(LocalDateTime.now());
    }

    /**
     * <p>
     * Judge whether the result is returned successfully 
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/10/2 17:58
     */
    public boolean successFlag() {
        return ResultCodeEnum.OK.getCode().equals(this.code);
    }

    public static <T> ObjectRestResponse<T> fail(String message) {
        return new ObjectRestResponse(ResultCodeEnum.BAD_REQUEST.getCode(), message);
    }

    /**
     * <p>
     * This method is recommended when the failure result is returned 
     * <p/>
     *
     * @param httpCode
     * @param bizCode
     * @param message
     * @return ObjectRestResponse
     * @Date 2021/10/2 16:58
     */
    public static <T> ObjectRestResponse<T> fail(String httpCode, String bizCode, String message) {
        return new ObjectRestResponse(httpCode, bizCode, message);
    }

    public static <T> ObjectRestResponse fail(String httpCode, String bizCode, String message, T result) {
        return new ObjectRestResponse(httpCode, bizCode, message, result);
    }


    public static <T> ObjectRestResponse<T> success(String message) {
        return new ObjectRestResponse(ResultCodeEnum.OK.getCode(), message);
    }

    public static <T> ObjectRestResponse<T> success(T result) {
        return new ObjectRestResponse(ResultCodeEnum.OK.getCode(), result);
    }

    /**
     * <p>
     * This method is recommended when successful results are returned 
     * <p/>
     *
     * @param message
     * @param result
     * @return ObjectRestResponse
     * @Date 2021/10/2 16:58
     */
    public static <T> ObjectRestResponse<T> success(String message, T result) {
        return new ObjectRestResponse(ResultCodeEnum.OK.getCode(), message, result);
    }

    /**
     * <p>
     * When a successful result is returned, this method is recommended (this method returns more than one data. For individual scenarios, the result cannot be parsed, but can only be parsed data)
     * <p/>
     *
     * @param message
     * @param result
     * @return ObjectRestResponse
     * @Date 2022/03/27 12:58
     */
    public static <T> ObjectRestResponse<T> success(String message, T result, T data) {
        return new ObjectRestResponse(ResultCodeEnum.OK.getCode(), message, result, data);
    }

    public static <T> ObjectRestResponse<T> success(String code, String message, T result) {
        if (StringUtils.isBlank(code)) {
            code = ResultCodeEnum.OK.getCode();
        }
        return new ObjectRestResponse<>(code, message, result);
    }

    public static <T> ObjectRestResponse<T> success(String code, String bizCode, String message, T result) {
        if (StringUtils.isBlank(code)) {
            code = ResultCodeEnum.OK.getCode();
        }
        return new ObjectRestResponse<>(code, bizCode, message, result);
    }


    public ObjectRestResponse(String httpCode, String message) {
        this();
        this.code = httpCode;
        this.message = message;
    }

    public ObjectRestResponse(String httpCode, T result) {
        this();
        this.code = httpCode;
        this.result = result;
    }

    public ObjectRestResponse(String httpCode, String bizCode, String message) {
        this(httpCode, bizCode, message, null);
    }

    public ObjectRestResponse(String httpCode, String message, T result) {
        this();
        this.code = httpCode;
        this.message = message;
        this.result = result;
    }

    public ObjectRestResponse(String httpCode, String message, T result, T data) {
        this(httpCode, message, result);
        this.data = data;
    }

    public ObjectRestResponse(String httpCode, String bizCode, String message, T result) {
        this();
        this.code = httpCode;
        this.bizCode = bizCode;
        this.message = message;
        this.result = result;
    }

}
