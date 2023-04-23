package cn.smilehappiness.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * smile simple security config
 * <p/>
 *
 * @author
 * @Date 2023/3/10 10:58
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConditionalOnProperty(prefix = "security", name = "simpleSign.switch", havingValue = "true")
public class SmileSimpleSecurityConfig {

    /***
     * security api merchantNo(one merchant can have multiple applications)
     */
    @Value("${security.simpleSign.merchantNo:6666XF20230306001}")
    public String merchantNo;

    /***
     * applicationId
     */
    @Value("${security.simpleSign.appId:10000000006666001}")
    public String appId;

    /***
     * The aesKey is used here as the secret key
     */
    @Value("${security.simpleSign.aesKey:5KF4efouyBWmx643Hps1uw==}")
    public String aesKey;

    /***
     * Gateway public key - JWT SHA256 (verify the validity of interface request)
     */
    @Value("${security.simpleSign.apiGwPublicKey:YyQyCaWdilrUL3oFZ6oeS8ZvWxHuGkmFsTZvq3eKYLyMY3autqJK2ik7EzuMpaCRBJ5ojwB3Ffs9vSqhT67KxG3KbAiQgf12qNZDGYfVaAEtscbJXCcGvSDIWsbuZkmyH5NzlcsZsOR90HvGPZCD9OFcIYbksqfoDcLISB8kFaONi7TwYpofSuJLYGaJvDROyeG74kvJCriGBICGZE3MDVWej7pXaUtSnofre6GFJhUD673thInTvDLFiw7yS1GF}")
    public String apiGwPublicKey;

}
