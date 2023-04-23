package cn.smilehappiness.language.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import cn.smilehappiness.language.model.MessageConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author
 * @date ：Created in 13/10/21 9:22 afternoon 
 * @description：NacosConfiguration Manager 
 * @modified By：
 * @Version ：1.0
 */

@Slf4j
@Component
public class NacosConfig {
    /**
     * Namespace 
     */
    private String dNamespace;
    /**
     * server address 
     */
    private String serverAddr;

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private I18nUtil i18nUtil;

    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    @Autowired
    public void init() {
        serverAddr = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.server-addr");
        dNamespace = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.namespace");
        //Support nacos to obtain multiple configurations and languages 
        if (messageConfig != null && messageConfig.getLangList() != null && messageConfig.getLangList().size() > 0) {
            log.info("===> I18nUtil. Support Nacos to obtain multiple configurations and multiple languages. langList:{}", JSON.toJSONString(messageConfig.getLangList()));
            for (String lang : messageConfig.getLangList()) {
                initTip(i18nUtil.getLocale(lang));
            }
        } else {
            //Multi-language support by default 
            initTip(new Locale("id", "ID")); //Indonesia will automatically convert to this  in_ID
            initTip(Locale.CHINA);
            initTip(Locale.US);
        }
        //Initialize system parameters successfully! Nacos address: {}, prompt namespace 
        log.info("===> I18nUtil.End of initializing system parameters! Nacos address:{},  Prompt namespace:{}", serverAddr, dNamespace);
    }

    private void initTip(Locale locale) {
//        String yamlSuffix = ".yaml";
//        String propertiesSuffix = ".properties";

        String splitSuffix = "";
        if (messageConfig != null && StringUtils.isNoneBlank(messageConfig.getFileSuffix())) {
            splitSuffix = messageConfig.getFileSuffix();
        }

        String content = null;
        String dataId = null;
        ConfigService configService = null;

        try {
            if (locale == null) {
                dataId = messageConfig.getBasename() + splitSuffix;
            } else {
//                i18nUtil.getLocale(locale.getLanguage());
                dataId = messageConfig.getBasename() + "_" + i18nUtil.getLocale(locale.getLanguage()) + "_" + locale.getCountry() + splitSuffix;
            }
            if (dataId.endsWith("-") || dataId.endsWith("_")) {
                dataId = dataId.substring(0, dataId.length() - 1);
            }
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            properties.put(PropertyKeyConst.NAMESPACE, dNamespace);
            configService = NacosFactory.createConfigService(properties);
            long timeoutMs = 1000 * 15;
            content = configService.getConfig(dataId, DEFAULT_GROUP, timeoutMs);
            if (StringUtils.isEmpty(content)) {
                log.warn("===> I18nUtil.Language internationalization initialization failed." +
                        "Configuration content is empty, skipping initialization !dataId:{}", dataId);
                return;
            }
            //Initialize internationalization configuration! Configuration content 
//            log.info("Initialize internationalization configuration! Configuration content :{}", content);
            saveAsFileWriter(dataId, content);
            setListener(configService, dataId, locale);
            //Configuration content is empty, skipping initialization 
            log.info("===> I18nUtil.Configuration content is empty, skipping initialization. locale:{}", locale.getLanguage());
        } catch (Exception e) {
            //Initialization internationalization configuration exception! Exception information 
            log.error("===> I18nUtil.Initialization internationalization configuration exception! Abnormal information", e);
        }
    }

    private void setListener(ConfigService configService, String dataId, Locale locale) throws com.alibaba.nacos.api.exception.NacosException {
        configService.addListener(dataId, DEFAULT_GROUP, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                //===> I18nUtil.Received new internationalization configuration! to configure dataid
                log.info("===> I18nUtil.New internationalization configuration received! Configure dataid:{}", dataId);
                try {
                    initTip(locale);
                } catch (Exception e) {
                    //I18nUtil.Initialization internationalization configuration exception! Exception information 
                    log.error("===> I18nUtil.Initialization internationalization configuration exception! Abnormal information:{}", e);
                }
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }

    private void saveAsFileWriter(String fileName, String content) {
//        String path = System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder();
        try {
            String path = i18nUtil.getMessageSourceBaseFolderPath();
            String propertiesSuffix = ".properties";
            if (fileName.contains(".properties")) {
                propertiesSuffix = "";
            }
            fileName = path + File.separator + fileName + propertiesSuffix;
            fileName = fileName.replace("//", "/");
            File file = new File(fileName);
            FileUtils.writeStringToFile(file, content);
            //Internationalization configuration has been updated! Local file path 
            log.info("===> I18nUtil.Internationalization configuration updated! Local file path:{}", fileName);
        } catch (IOException e) {
            //Initialization internationalization configuration exception! Local file path, exception 
            log.error("===> I18nUtil.Initialization internationalization configuration exception! Local file path:{}Abnormal information:{}", fileName, e);
        }
    }
}
