package com.smilehappiness.mock.config;

import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 * <p>
 * springboot提供了一种扩展机制，允许你来写一个converter来完成你想要的转换
 * mybatis-plus返回map自动转驼峰配置操作
 * mybatis-plus.configuration.object-wrapper-factory
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/4/18 16:03
 */
@Component
@ConfigurationPropertiesBinding
public class ObjectWrapperFactoryConverter implements Converter<String, ObjectWrapperFactory> {

    @Override
    public ObjectWrapperFactory convert(String source) {
        try {
            return (ObjectWrapperFactory) Class.forName(source).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
