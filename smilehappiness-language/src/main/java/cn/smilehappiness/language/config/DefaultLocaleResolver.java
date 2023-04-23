package cn.smilehappiness.language.config;

import cn.smilehappiness.language.constant.LangConstant;
import cn.smilehappiness.language.enums.LangExtLocale;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author
 * @date ：Created in 13/10/21 9:22 afternoon 
 * @description：Custom internationalization parser 
 * @modified By：
 * @Version ：1.0
 */

public class DefaultLocaleResolver implements LocaleResolver {
    private static final Logger log = LoggerFactory.getLogger(DefaultLocaleResolver.class);

    @Autowired
    private I18nUtil i18nUtil;

    /**
     * Get the multilingual logo from the header. The default region is Indonesia 
     * headerIndicates: Accept-Language or  Lang
     *
     * @param request
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(LangConstant.Lang2);
        if (StringUtils.isEmpty(lang)) {
            lang = request.getHeader(LangConstant.Lang);
        }
        Locale defaultLocale = LangExtLocale.INDONESIA;
        Locale.setDefault(defaultLocale);
        Locale locale = Locale.getDefault();
        if (StringUtils.isNotEmpty(lang)) {
            locale = i18nUtil.getLocale(lang);
        } else {
            locale = i18nUtil.getConfigDefaultLocale();
        }
        return locale;
    }


    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
