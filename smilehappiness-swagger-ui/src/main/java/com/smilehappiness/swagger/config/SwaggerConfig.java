package com.smilehappiness.swagger.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * Swagger-Ui配置类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/10/3 15:37
 */
@Configuration
@EnableSwagger2
@RefreshScope
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private Boolean enabled;
    @Value("${swagger.groupName}")
    private String groupName;

    /**
     * <p>
     * 配置了Swagger 的Docket的bean实例,扫描接口的位置
     * <p>
     * .apis
     * RequestHandlerSelectors配置swagger扫描接口的方式
     * basePackage() 指定要扫描哪些包
     * any() 全部都扫描
     * none() 全部不扫描
     * withClassAnnotation() 扫描类上的注解 参数是一个注解的反射对象
     * withMethodAnnotation() 扫描包上的注解
     * <p>
     * .paths
     * PathSelectors 路径扫描接口
     * ant 配置以xxx 开头的路径
     * <p/>
     *
     * @param environment
     * @return springfox.documentation.spring.web.plugins.Docket
     * @Date 2021/10/3 15:51
     */
    @Bean
    public Docket createRestApi(Environment environment) {
        //设置要显示的Swagger环境，prod不显示
        //在开发环境和测试环境开启SwaggerUI，生产环境是面向客户，为了程序的安全性，需要关闭SwagggerUI
        Profiles profiles = Profiles.of("dev", "test", "sit", "uat");

        //通过 environment.acceptsProfiles 返回的boolean值判断是否处在自己所设定的环境中
        boolean flag = environment.acceptsProfiles(profiles);
        System.out.println(flag);

        //如果配置文件有配置改数值，并且为true，则优先使用配置文件的值
        if (enabled) {
            flag = Boolean.TRUE;
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                //enable配置是否自动启动swagger 如果为False则为不启动，浏览器中不能访问Swagger
                .enable(flag)
                .apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.smilehappiness.framework.action"))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台接口文档")
                .description("后台管理框架")
                //联系人实体类
                //.contact(new Contact("名字", "网址", "邮箱"))
                .version("v2.0")
                .build();
    }

}
