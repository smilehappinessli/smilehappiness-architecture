package cn.smilehappiness.language.config;

import com.alibaba.fastjson.JSON;
import cn.smilehappiness.language.model.MessageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author
 * @date ：Created in 14/10/21 10:09 morning 
 * @description：Internationalization configuration acquisition tool class 
 * @modified By：
 * @Version ：1.0
 */
@Slf4j
@Component
public class I18nUtil {
    private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private SpringMessageSourceConfig springMessageSourceConfig;

    public Locale getConfigDefaultLocale() {
        Locale locale = Locale.getDefault();
        if (messageConfig != null && StringUtils.isNoneBlank(messageConfig.getDefaultLang())) {
            try {
                locale = getLocale(messageConfig.getDefaultLang());
            } catch (Exception e) {
                log.error("--->>> i18n. DefaultLocaleResolver class.Get the default multi-language configuration exception from the configuration center. Default key:" + messageConfig.getDefaultLang());
            }
        }
        return locale;
    }

    /**
     * get folder path
     *
     * @return
     */
    public String getMessageSourceBaseFolderPath() {
        return System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder();
    }

    public String getString(String key, Locale customLocale) {
        try {
            return messageSource.getMessage(key, null, customLocale);
        } catch (NoSuchMessageException e) {
            //Get configuration exception! Exception information 
            log.error("===》 I18nUti. get custom locale configuration exception! get lang key:" + key + " exception message:", e);
        }
        return null;
    }

    public String getString(String key) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            return messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException e) {
            //Get configuration exception! Exception information 
            log.error("===》 I18nUtil get configuration exception! get lang key:" + key + " exception message:", e);
        }
        return null;
    }

    /**
     * get mult val of i18n mult key, as json
     *
     * @param keys
     * @return
     */
    public String getMultString(String... keys) {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String i18nFile = MessageFormat.format(getMessageSourceBaseFolderPath() + "messages_{0}.properties", locale.toString());
            Properties properties = new Properties();
            // Read properties file using InPutStream stream 
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(i18nFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            properties.load(bufferedReader);

            Map<String, String> map = new HashMap<String, String>();
            Properties prop = properties;
            if (keys != null && keys.length > 0) {
                for (String key : keys) {
                    map.put(key, prop.getProperty(key));
                }
            } else {
                for (String key : prop.stringPropertyNames()) {
                    map.put(key, prop.getProperty(key));
                }
            }
            String json = JSON.toJSONString(map);
            return json;
        } catch (Exception e) {
            //Get multilingual 
            logger.error("===》 get mult val of i18n mult key, as json ; ===>  exception", e);
            return null;
        }
    }

    public String jsonFile2String(String finalPath) {
        String jsonString;
        File file = new File(finalPath);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
            String result = JSON.toJSONString(jsonString);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO exception");
        }
    }

    public Locale getLocale(String lang) {
        Locale locale = Locale.getDefault();
        if (StringUtils.isNotBlank(lang)) {
            //if this type is zh-CN,zh;q=0.9
            if (lang.contains(";")) {
                List<Locale.LanguageRange> languageRangeList = Locale.LanguageRange.parse(lang);
                Locale.LanguageRange range = maxLangWeight(languageRangeList);
//                lang = lang.split(";")[0];
                lang = new Locale(range.getRange()).toString();
            }
            if (lang.contains("-") || lang.contains("_")) {
                lang = lang.trim().replace("-", "_");
                String[] language = lang.split("_");
                String lang1 = language[0];
                String lagn2 = StringUtils.isNotBlank(language[1]) ? language[1].toUpperCase() : "";
                locale = new Locale(lang1, lagn2);
            } else {
                locale = new Locale(lang, "");
            }
        } else {
            log.info("===> I18nUtil. Get Locale Error!!!");
        }
        return locale;
    }

    public Locale.LanguageRange maxLangWeight(List<Locale.LanguageRange> languageRangeList) {
        int i;
        double max;
        max = languageRangeList.get(0).getWeight();
        Locale.LanguageRange maxRange = languageRangeList.get(0);
        for (i = 0; i < languageRangeList.size(); i++) {
            if (languageRangeList.get(i).getWeight() > max) {
                max = languageRangeList.get(i).getWeight();
                maxRange = languageRangeList.get(i);
            }
        }
        return maxRange;
    }
}
