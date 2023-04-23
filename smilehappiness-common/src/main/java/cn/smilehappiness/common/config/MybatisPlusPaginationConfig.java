package cn.smilehappiness.common.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * PaginationConfig
 * <p/>
 *
 * @author
 * @Date 2021/10/9 19:30
 */
@Configuration
public class MybatisPlusPaginationConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //If it is a different type of library, please do not specify DbType, which will automatically determine 。
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}


