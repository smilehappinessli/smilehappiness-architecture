package com.smilehappiness.utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.Map;

/**
 * <p>
 * FreeMarkUtils
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/8/16 16:59
 */
@Component
public class FreeMarkUtils {

    @Resource
    @Qualifier("freeMarkerConfiguration")
    private Configuration freemarkerConf;

    public String renderByStringTemplate(Map param, String templateContent) {
        StringWriter writer = new StringWriter();
        try {
            // 新建一个字符串的模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            // 设置模板 ，其中
            stringTemplateLoader.putTemplate("myTemplate", templateContent);
            freemarkerConf.setTemplateLoader(stringTemplateLoader);
            freemarkerConf.clearTemplateCache();
            // 模板渲染
            Template template = freemarkerConf.getTemplate("myTemplate", "utf-8");
            template.process(param, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
