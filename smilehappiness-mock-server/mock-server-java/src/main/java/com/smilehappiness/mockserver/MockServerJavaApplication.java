package com.smilehappiness.mockserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * <p>
 * Mock Server服务启动类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/8/16 16:59
 */
@SpringBootApplication(scanBasePackages = "com.smilehappiness.mockserver", exclude = {DataSourceAutoConfiguration.class})
public class MockServerJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockServerJavaApplication.class, args);
        System.out.println("[MOCK-SERVER]服务启动完成!!!");
    }

}
