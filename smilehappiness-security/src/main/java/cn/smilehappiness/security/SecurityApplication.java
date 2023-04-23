package cn.smilehappiness.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SecurityApplication
 * <p/>
 *
 * @author
 * @Date 2023/3/11 20:38
 */
@SpringBootApplication(scanBasePackages = {"cn.smilehappiness"})
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}
