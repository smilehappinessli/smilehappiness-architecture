package cn.smilehappiness.common.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MyBatis-PlusReturn to map automatic humping configuration 
 * <p/>
 *
 * @author
 * @Date 2021/9/27 16:40
 */
@Component
@ConfigurationPropertiesBinding
public class MybatisWrapperFactoryConverter implements Converter<String, ObjectWrapperFactory> {

    @Override
    public ObjectWrapperFactory convert(String source) {
        try {
            return (ObjectWrapperFactory) Class.forName(source).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*@Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(MybatisConfiguration configuration) {
                configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
            }
        };
    }*/
}
