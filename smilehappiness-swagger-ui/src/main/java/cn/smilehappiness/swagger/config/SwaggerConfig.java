package cn.smilehappiness.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Swagger-UiConfiguration class 
 * EnableKnife4jAnnotations are enhanced annotations provided by knife4j. Ui provides enhanced functions such as dynamic parameters, parameter filtering, interface sorting, etc 
 * #
 * <p/>
 *
 * @author
 * @Date 2021/10/3 15:37
 */
@EnableKnife4j
@Configuration
@EnableSwagger2
@RefreshScope
public class SwaggerConfig {

    @Value("${swagger.enabled:false}")
    private Boolean enabled;
    @Value("${swagger.groupName}")
    private String groupName;

    /**
     * <p>
     * Configure the bucket bean instance of Swagger and scan the location of the interface 
     * <p>
     * .apis
     * RequestHandlerSelectorsHow to configure the swagger scanning interface 
     * basePackage() Specify which packages to scan 
     * any() Scan all 
     * none() Don't scan all 
     * withClassAnnotation() The annotation parameter on the scan class is a reflection object of the annotation 
     * withMethodAnnotation() Scan the annotation on the package 
     * <p>
     * .paths
     * PathSelectors Path scan interface 
     * ant Configure paths starting with xxx 
     * <p/>
     *
     * @param environment
     * @return springfox.documentation.spring.web.plugins.Docket
     * @Date 2021/10/3 15:51
     */
    @Bean
    public Docket createRestApi(Environment environment) {
        //Set the Swagger environment to display, and prod does not display 
        //Start the Swagger UI in the development environment and test environment. The production environment is customer-oriented. For program security, it needs to be closed SwagggerUI
        Profiles profiles = Profiles.of("local", "dev", "test", "sit", "uat");

        //Judge whether you are in the environment you set by the boolean value returned by environment.acceptsProfiles 
        boolean flag = environment.acceptsProfiles(profiles);
        //System.out.println(flag);

        //If the configuration file has a configuration change value and it is true, the value of the configuration file will be used first 
        if (enabled) {
            flag = Boolean.TRUE;
        }

        //Set global parameters 
       /* ParameterBuilder ticketParam = new ParameterBuilder();
        List<Parameter> parameterList = new ArrayList<>();
        //nameRepresents the name, and description represents the description 
        ticketParam.name("Authorization").description("Login verification ")
                .modelRef(new ModelRef("string")).parameterType("header")
                //requiredIndicates whether it is required, and defaultvalue indicates the default value 
                .required(false).defaultValue("Bearer ").build();
        parameterList.add(ticketParam.build());*/


        // excludePathhandle 
        /*List<Predicate<String>> excludePathList = new ArrayList<>();
        excludePathList.add(PathSelectors.regex("/error.*"));
        excludePathList.add(PathSelectors.regex("/actuator.*"));*/

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                //enableConfigure whether to automatically start the swagger. If it is false, it will not start. It cannot be accessed in the browser Swagger
                .enable(flag)
                .apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.basePackage("cn.smilehappiness.framework.action"))
                //.paths(Predicates.not(Predicates.or(excludePathList)))
                .build();
                //Add message header information 
                //.globalOperationParameters(parameterList);
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Background interface document ")
                .description("Background management framework ")
                //Contact entity class 
                //.contact(new Contact("First name, Web address, E-mail "))
                .version("v2.0")
                .build();
    }

}
