package com.smilehappiness.mock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * Mock Server服务启动类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/8/16 16:59
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.smilehappiness.mock")
@EnableDiscoveryClient
@MapperScan(basePackages = "com.smilehappiness.*.mapper")
@ComponentScan(basePackages = {"com.smilehappiness"})
public class MockServerDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockServerDatabaseApplication.class, args);
    }

}
