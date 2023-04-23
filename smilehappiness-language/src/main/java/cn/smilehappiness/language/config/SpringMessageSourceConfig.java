package cn.smilehappiness.language.config;

import com.alibaba.fastjson.JSON;
import cn.smilehappiness.language.model.MessageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.io.File;

/**
 * @author
 * @date ：Created in 13/10/21 9:45 afternoon 
 * @description：Springto configure 
 * @modified By：
 * @Version ：1.0
 */
@Slf4j
@Configuration
public class SpringMessageSourceConfig {

    @Autowired
    private MessageConfig messageConfig;

    @Bean
    public LocaleResolver localeResolver(){
        return new DefaultLocaleResolver();
    }

    @Primary
    @Bean(name = "messageSource")
    @DependsOn(value = "messageConfig")
    public ReloadableResourceBundleMessageSource messageSource() {
        String path = ResourceUtils.FILE_URL_PREFIX + System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder()+ File.separator + messageConfig.getBasename();
        path = path.replace("//", "/");
        log.info("===> I18nUtil. nacos internationalization configuration content:{}", JSON.toJSONString(messageConfig));
        log.info("===> I18nUtil. Internationalization configuration path:{}", path);
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(path);
        messageSource.setDefaultEncoding(messageConfig.getEncoding());
        messageSource.setCacheMillis(messageConfig.getCacheMillis());
        return messageSource;
    }

}
