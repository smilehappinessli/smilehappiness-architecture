package com.smilehappiness.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * <p>
 * Spring上下文工具类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/12/25 9:53
 */
@Component
public class SpringUtil implements ApplicationContextAware, DisposableBean {

    /**
     * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext
     */
    private static ApplicationContext applicationContext = null;

    private static Logger logger = LoggerFactory.getLogger(SpringUtil.class);

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        assertContextInjected();

        if (logger.isDebugEnabled()) {
            logger.debug("========ApplicationContext配置成功,在普通类可以通过调用SpringContextUtil.getApplicationContext()获取applicationContext对象,applicationContext={}", SpringUtil.applicationContext);
        }

        return applicationContext;
    }

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            URL url = new URL("");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.getInputStream();
            connection.disconnect();
        } catch (Exception e) {
            new RuntimeException(e);
        }

        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * <p>
     * 通过name获取Bean
     * <p/>
     *
     * @param name
     * @return java.lang.Object
     * @Date 2020/12/25 10:00
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }


    /**
     * <p>
     * 通过class获取Bean
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     * <p/>
     *
     * @param clazz
     * @return T
     * @Date 2020/9/9 13:48
     */
    public static <T> T getBean(Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(clazz);
    }

    /**
     * <p>
     * 通过name,以及Clazz返回指定的Bean
     * <p/>
     *
     * @param name
     * @param clazz
     * @return T
     * @Date 2020/9/9 13:48
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        assertContextInjected();
        return applicationContext.getBean(name, clazz);
    }

    /**
     * <p>
     * 从静态变量applicationContext中取得指定类型所有的Bean
     * <p/>
     *
     * @param type
     * @return java.util.Map<java.lang.String, T>
     * @Date 2020/12/25 10:04
     */
    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        assertContextInjected();
        return applicationContext.getBeansOfType(type);
    }

    /**
     * <p>
     * 实现DisposableBean接口, 在Context关闭时清理静态变量
     * <p/>
     *
     * @param
     * @return void
     * @Date 2020/12/25 10:05
     */
    @Override
    public void destroy() throws Exception {
        SpringUtil.clearHolder();
    }

    /**
     * <p>
     * 清除SpringContextUtil中的ApplicationContext为Null
     * <p/>
     *
     * @param
     * @return void
     * @Date 2020/12/25 10:05
     */
    public static void clearHolder() {
        if (logger.isDebugEnabled()) {
            logger.debug("清除SpringContextUtil中的ApplicationContext:{}", applicationContext);
        }
        applicationContext = null;
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        Validate.validState(applicationContext != null, "applicationContext属性未注入, 请在applicationContext.xml中定义SpringContextUtil");
    }
}
