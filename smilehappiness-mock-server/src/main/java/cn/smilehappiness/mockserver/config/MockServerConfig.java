package cn.smilehappiness.mockserver.config;

import cn.smilehappiness.mockserver.expectation.ExpectationInit;
import lombok.extern.slf4j.Slf4j;
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
 * Mock ServerConfiguration class 
 * <p/>
 *
 * @author
 * @Date 2020/8/16 16:59
 */
@Slf4j
@Configuration
public class MockServerConfig {

    private ClientAndServer mockServer;

    @Value("${server.port}")
    private int servePort;

    /**
     * <p>
     * To start the server or proxy create a client
     * UIInterface ：http://localhost:servePort/mockserver/dashboard
     * <p/>
     *
     * @param
     * @return void
     * @Date 2020/8/16 17:35
     */
    @PostConstruct
    public void startMockServer() {
        ConfigurationProperties.enableCORSForAPI(true);
        //Initialize expected configuration class 
        ConfigurationProperties.initializationClass(ExpectationInit.class.getName());

        //Persist expected data 
        ConfigurationProperties.persistExpectations(true);

        String projectPath = System.getProperty("user.dir");
        String basePath = StringUtils.join(projectPath, "/smilehappiness-mock-server/", "persistedExpectations.json");

        //Persistent file path 
        ConfigurationProperties.persistedExpectationsPath(basePath);
        //File path to load expected data 
        ConfigurationProperties.initializationJsonPath(basePath);

        mockServer = startClientAndServer(servePort);
        log.info("mock server【{}}】 start...", mockServer);
    }

    /**
     * <p>
     * The value of @ Value annotation cannot be ignored (no error reported ）
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
