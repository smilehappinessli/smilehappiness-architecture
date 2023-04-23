package cn.smilehappiness.cache.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * Interceptor configuration class 
 * <p/>
 *
 * @author
 * @Date 2021/10/14 17:47
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public IdempotencyInterceptor getIdempotencyInterceptor() {
        return new IdempotencyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getIdempotencyInterceptor());
    }

}
