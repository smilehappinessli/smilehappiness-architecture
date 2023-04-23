package cn.smilehappiness.language.advice;

import cn.smilehappiness.common.result.ObjectRestResponse;
import cn.smilehappiness.language.config.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author
 * @date ：Created in 07/11/21 3:06 afternoon 
 * @description：
 * @modified By：
 * @Version ：1.0
 */
@Aspect
@Component
@RestControllerAdvice
public class ObjectResponseLangAdvice implements ResponseBodyAdvice<ObjectRestResponse> {
    @Value("${smilehappiness.language.response.enable:false}")
    private boolean respnseEnable;

    @Autowired
    private I18nUtil i18nUtil;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.getMethod() == null) {
            return false;
        }
        return ObjectRestResponse.class.isAssignableFrom(methodParameter.getMethod().getReturnType());
    }

    /**
     * The returned result is relatively complex. This can be analyzed and optimized later. The returned result is non-empty and can be processed here 
     *
     * @param objectRestResponse
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public ObjectRestResponse beforeBodyWrite(ObjectRestResponse objectRestResponse, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!respnseEnable) {
            return objectRestResponse;
        }
        String langKey = objectRestResponse.getMessage();
        String lang = i18nUtil.getString(langKey);
        if (StringUtils.isNoneBlank(langKey) && StringUtils.isNoneBlank(lang)) {
            objectRestResponse.setMessage(lang);
        }
        return objectRestResponse;
    }
}
