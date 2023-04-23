package cn.smilehappiness.language.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author
 * @date ：Created in 13/10/21 9:42 afternoon 
 * @description：Internationalization configuration 
 * @modified By：
 * @Version ：1.0
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "spring.messages")
public class MessageConfig {
    /**
     * Internationalized file directory 
     */
    private String baseFolder;

    /**
     * Internationalized file name 
     */
    private String basename;

    /**
     * Internationalization code 
     */
    private String encoding;

    /**
     * Cache refresh time 
     */
    private long cacheMillis;

    /**
     * Get default internationalization configuration 
     */
    private String defaultLang;

    /**
     * List of supported multilingual configurations 
     */
    private List<String> langList;

    /**
     * Name whether there are properties files 
     */
    private String fileSuffix;
}
