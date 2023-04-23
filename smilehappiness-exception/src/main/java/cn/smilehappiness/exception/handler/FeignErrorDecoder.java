package cn.smilehappiness.exception.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.common.enums.ResultCodeEnum;
import cn.smilehappiness.exception.exceptions.BusinessException;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * Custom Feign parser 
 * <p/>
 *
 * @author
 * @Date 2021/11/18 21:57
 */
@RefreshScope
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    private final Logger logger = LoggerFactory.getLogger(FeignErrorDecoder.class);

    private static final String BIZ_CODE = "bizCode";
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String ERROR = "error";

    @Value("${logger.apiLogger.stackTraceFlag:true}")
    private boolean stackTraceFlag;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // Here we can directly get the exception information we threw 
            String message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            try {
                JSONObject jsonObject = JSONObject.parseObject(message);
                logger.warn("FeignErrorDecoder jsonObject:{}", JSON.toJSONString(jsonObject));

                //BizCode must be passed for business exceptions. There is no bizCode for system exceptions. You can simply judge which exception belongs to 
                if (StringUtils.isNotBlank(jsonObject.getString(BIZ_CODE))) {
                    return new BusinessException(jsonObject.getString(CODE), jsonObject.getString(BIZ_CODE), jsonObject.getString(MESSAGE));
                }

                //{"timestamp":"2021-11-26T08:44:08.205+00:00","status":404,"error":"Not Found",MESSAGE:"","path":"/biz/orderDetails/queryLoanDetailsCombInfo"}
                if (StringUtils.isNotBlank(jsonObject.getString(MESSAGE))) {
                    if (StringUtils.isNotBlank(jsonObject.getString(CODE))) {
                        return new SystemInternalException(jsonObject.getString(CODE), jsonObject.getString(MESSAGE));
                    } else {
                        return new SystemInternalException(jsonObject.getString(STATUS), jsonObject.getString(MESSAGE));
                    }
                }

                //codeCode is not empty 
                if (StringUtils.isNotBlank(jsonObject.getString(CODE))) {
                    return new SystemInternalException(jsonObject.getString(CODE), jsonObject.getString(ERROR));
                }

                String status = jsonObject.getString(STATUS);
                if (StringUtils.isNotBlank(status)) {
                    return new SystemInternalException(status, jsonObject.getString(ERROR));
                }

                return new SystemInternalException(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ResultCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
            } catch (JSONException exception) {
                if (stackTraceFlag) {
                    exception.printStackTrace();
                }

                logger.warn("FeignErrorDecoder JSONException:{}", exception.getMessage());
                return new SystemInternalException(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ResultCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
            }
        } catch (Exception exception) {
            if (stackTraceFlag) {
                exception.printStackTrace();
            }

            logger.warn("FeignErrorDecoder Exception:{}", exception.getMessage());
            return new SystemInternalException(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), ResultCodeEnum.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

}
