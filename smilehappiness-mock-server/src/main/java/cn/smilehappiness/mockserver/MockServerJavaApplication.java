package cn.smilehappiness.mockserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * <p>
 * Mock ServerService startup class 
 * <p/>
 *
 * @author
 * @Date 2020/8/16 16:59
 */
@SpringBootApplication(scanBasePackages = "cn.smilehappiness.mockserver", exclude = {DataSourceAutoConfiguration.class})
public class MockServerJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockServerJavaApplication.class, args);
        System.out.println("[MOCK-SERVER]Service startup completed !!!");
    }

}
