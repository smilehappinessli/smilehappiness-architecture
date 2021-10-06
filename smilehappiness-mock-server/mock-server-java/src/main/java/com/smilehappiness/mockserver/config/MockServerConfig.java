package com.smilehappiness.mockserver.config;

import com.smilehappiness.mockserver.expectation.ExpectationInit;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.annotation.PostConstruct;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * <p>
 * Mock Server配置类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/8/16 16:59
 */
@Configuration
public class MockServerConfig {

    private ClientAndServer mockServer;

    @Value("${server.port:8088}")
    private int servePort;

    /**
     * <p>
     * To start the server or proxy create a client
     * UI界面：http://localhost:servePort/mockserver/dashboard
     * <p/>
     *
     * @param
     * @return void
     * @Date 2020/8/16 17:35
     */
    @PostConstruct
    public void startMockServer() {
        ConfigurationProperties.enableCORSForAPI(true);
        //初始化期望配置类
        ConfigurationProperties.initializationClass(ExpectationInit.class.getName());

        //持久化期望数据
        ConfigurationProperties.persistExpectations(true);

        String projectPath = System.getProperty("user.dir");
        String basePath = StringUtils.join(projectPath, "/smilehappiness-mock-server/mock-server-java","/src/main/resources");

        //持久化文件路径
        ConfigurationProperties.persistedExpectationsPath(basePath);
        //加载期望数据的文件路径
        ConfigurationProperties.initializationJsonPath(basePath);

        mockServer = startClientAndServer(servePort);
        System.out.println("mock server【" + mockServer + "】 start...");
    }

    /**
     * <p>
     * 设置@Value注解取值不到忽略（不报错）
     * <p/>
     *
     * @param
     * @return org.springframework.context.support.PropertySourcesPlaceholderConfigurer
     * @Date 2020/8/16 16:51
     */
//    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreUnresolvablePlaceholders(true);
        return c;
    }

    //@PostConstruct
    public void initExpectations() {
        new MockServerClient("localhost", servePort)
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/login")
                                .withContentType(MediaType.APPLICATION_JSON)
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("test response")
                );
    }

}
