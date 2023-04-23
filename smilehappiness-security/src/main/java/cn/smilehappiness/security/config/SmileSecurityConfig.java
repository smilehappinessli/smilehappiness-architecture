package cn.smilehappiness.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Application attribute configuration information
 * <p/>
 *
 * @author
 * @Date 2021/11/2 21:38
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConditionalOnProperty(prefix = "security", name = "sign.switch", havingValue = "true")
public class SmileSecurityConfig {

    /***
     * security api merchantNo(one merchant can have multiple applications)
     */
    @Value("${security.sign.merchantNo:6666XF20230306001}")
    public String merchantNo;

    /***
     * applicationId
     */
    @Value("${security.sign.appId:10000000006666001}")
    public String appId;

    /***
     * aesKey
     */
    @Value("${security.sign.aesKey:5KF4efouyBWmx643Hps1uw==}")
    public String aesKey;

    /***
     * Gateway public key - JWT SHA256 (verify the validity of interface request)
     */
    @Value("${security.sign.apiGwPublicKey:YyQyCaWdilrUL3oFZ6oeS8ZvWxHuGkmFsTZvq3eKYLyMY3autqJK2ik7EzuMpaCRBJ5ojwB3Ffs9vSqhT67KxG3KbAiQgf12qNZDGYfVaAEtscbJXCcGvSDIWsbuZkmyH5NzlcsZsOR90HvGPZCD9OFcIYbksqfoDcLISB8kFaONi7TwYpofSuJLYGaJvDROyeG74kvJCriGBICGZE3MDVWej7pXaUtSnofre6GFJhUD673thInTvDLFiw7yS1GF}")
    public String apiGwPublicKey;

    /***
     * Unlike symmetric encryption algorithms, asymmetric encryption algorithms require two keys: public key and private key. The public key and private key are a pair. If the data is encrypted with the public key, only the corresponding private key can be decrypted; If the data is encrypted with a private key, it can only be decrypted with the corresponding public key. Because encryption and decryption use two different keys, this algorithm is called asymmetric encryption algorithm
     * Apply private key-key pair RSA asymmetric encryption authentication method
     * privateKey
     */
    @Value("${security.sign.privateKey:MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGMGB3OHGrOL51Lt2kj9LDObyvGHf906XUCmEyUZSGyZHStIgYrxsbgHRXIqJT6ERDmNulkNTfIyLns6ohU3qCV6Q5SNbUakuo0SdjrYgdlJiGL3KGk8aZfFX8ic6NL2D0YIote8Tj4elb+sk6Qx32TfDrnueh5VHQz2ZcuwU3QpZt3bGq2Ey8hKVkwo0oFV8YmteoImym3pN/3/KeS8PEW8ZCuG2F96QWkL2PIOI+oiVpeD+O/C3+lautQgwdh4lVGKynJCU+VcYxak48obHUAzUEtsgxDeIotLCOjSRocBzdkkezgr/Cch1wKKsr/DHfXN7Jv+0MRT6NoJ3pGHUlAgMBAAECggEARmPWFnPrXI/ViI7fAVXYj0QYMBk7yFBfqo0PU1/r85CFB+xff+NK6vBFFvg6Ap3SQUtL3NvOboZ15ukeE4FAwGLM6KfG2YIw0gt1CBqdTjcGk00MXH/kK9tHF8QKL5Fs4IRLt3Q3Yfq9eNxi9kcCzpMwD8k5ZHlwo4JIBdis8cEfsBocJYoovVX+80B/bJ3/PDp/4puGGDFYbgdFlmp9xqwoN2RbELbSjMAE3xEaq9+qhiVLgdO7pO6MLol8FaTVqVBVs8191yBR7iDgMjsuUy9yi+W5mnCl3RK1RV3aDW4yUtCyZgJ+BpVcrjCYyEhFXVallXLhU3xGB+pJfafawQKBgQC/PkN82XEr3f/viXmOQ+lFG52lV87z3THQnoWnC/z4tQARtBSuYBhmxAoxKRBYBsaNRy35DQLYekFH+1vPrOTvxBW8EOo1Da/w91cESVrxRD7BAMmwAC5LpynUh0Fmd3vnD6J5QSp3oOPraQBLRSS5CEtp+aWR+S4d9pgva5r0CQKBgQCzoGuS3xZj2Q/e6HCi7p3jEz3VFrVf8q4cpYONZuiFFTY2Xr0bQTO0YdTjxO9M1foT6cG4ykV8tgMYA6afVm5cCpXHla5n9PgnqFqUlkXPTZ0AwupEvEHlXiqtJN2Q6M40Vl29RP0ANB3zVKrkS89fa5VjgZjiDKEw+Jxx8iOXPQKBgHEMyG5ER68r04BxHucnY/zTP1SfXJox1qHBI7bE0mze6kSM1HDkI2iUWIYJBT1T/AlF23pO47C+TFrVzscsKEVH/6Ulxp1IEwp52/mPt2/J7OLAqXkeEj/seEDHF7UPyrGjP2M4T3daJ4Fgl6jnHbc3nQTva52n1EgoHnxhur6pAoGARIbMw0lMV2c1wMpYPjBeF4T9r89r/mCsv8z89PLvfeXruq5F4qHrQcOY3hQBXwS61OH2v7Ka88rUP1MVO/BTRdXb8jpOQOaZzTdS5yC1HTL01JeHnBVd5k0YZtmLlX7bO+g+9rfc9TjsvCnu0A2D3VlDsqR8WLCMxgvyUx4XKdkCgYEAqIHVg3mcs38E2vyELhKAsgq0MuNv7uIJg/WwX+f/usdAFeNkn2W/+bac/bpknXz5ZVYKQ3JXpWl2o6VuF9IfHO/El4wk4SrtWA0c3h11h4k4laj824KhhNRi+lZQ/5M2iDtp4uOGsxc7OFfgzBu3nYIbpUQ7SzC+AcwL0+1NanA=}")
    public String privateKey;

    /***
     * publicKey
     */
    @Value("${security.sign.publicKey:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhjBgdzhxqzi+dS7dpI/Swzm8rxh3/dOl1AphMlGUhsmR0rSIGK8bG4B0VyKiU+hEQ5jbpZDU3yMi57OqIVN6glekOUjW1GpLqNEnY62IHZSYhi9yhpPGmXxV/InOjS9g9GCKLXvE4+HpW/rJOkMd9k3w657noeVR0M9mXLsFN0KWbd2xqthMvISlZMKNKBVfGJrXqCJspt6Tf9/ynkvDxFvGQrhthfekFpC9jyDiPqIlaXg/jvwt/pWrrUIMHYeJVRispyQlPlXGMWpOPKGx1AM1BLbIMQ3iKLSwjo0kaHAc3ZJHs4K/wnIdcCirK/wx31zeyb/tDEU+jaCd6Rh1JQIDAQAB}")
    public String publicKey;

}
