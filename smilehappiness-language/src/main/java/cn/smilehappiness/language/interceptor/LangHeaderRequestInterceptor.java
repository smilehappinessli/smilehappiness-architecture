package cn.smilehappiness.language.interceptor;

import cn.smilehappiness.language.constant.LangConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @date ：Created in 08/11/21 10:16 morning 
 * @description：Global multilingual header delivery 
 * @modified By：
 * feign:
 *   client:
 *     config:
 *       default:
 *         loggerLevel: full
 *         requestInterceptors:
 *           - cn.smilehappiness.language.interceptor.LangHeaderRequestInterceptor
 *
 * @Version ：1.0
 */

public class LangHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes srat = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = srat.getRequest();
        String lang = request.getHeader(LangConstant.Lang);
        String lang2 = request.getHeader(LangConstant.Lang2);
        if (StringUtils.isNotBlank(lang2)) {
            requestTemplate.header(LangConstant.Lang2, lang2);
        } else if (StringUtils.isNoneBlank(lang)) {
            requestTemplate.header(LangConstant.Lang, lang);
        }
    }
}
