package cn.smilehappiness.security.test;

import cn.smilehappiness.security.api.client.SmileClient;
import cn.smilehappiness.security.config.SmileSecurityConfig;
import cn.smilehappiness.security.constant.SmileConstants;
import cn.smilehappiness.security.dto.BaseRequest;
import cn.smilehappiness.security.utils.SmileHashMap;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * <p>
 * add sign test
 * <p/>
 *
 * @author
 * @Date 2023/3/8 18:11
 */
public class AddSignTest {

    @Autowired
    private SmileSecurityConfig smileSecurityConfig;

    public AddSignTest() {
        // There is no Spring startup class, so you need to configure parameters manually
        if (ObjectUtils.isEmpty(smileSecurityConfig)) {
            smileSecurityConfig = new SmileSecurityConfig();
            smileSecurityConfig.setMerchantNo("6666XF20230306001");
            smileSecurityConfig.setAppId("10000000006666001");
            smileSecurityConfig.setAesKey("5KF4efouyBWmx643Hps1uw==");
            smileSecurityConfig.setApiGwPublicKey("YyQyCaWdilrUL3oFZ6oeS8ZvWxHuGkmFsTZvq3eKYLyMY3autqJK2ik7EzuMpaCRBJ5ojwB3Ffs9vSqhT67KxG3KbAiQgf12qNZDGYfVaAEtscbJXCcGvSDIWsbuZkmyH5NzlcsZsOR90HvGPZCD9OFcIYbksqfoDcLISB8kFaONi7TwYpofSuJLYGaJvDROyeG74kvJCriGBICGZE3MDVWej7pXaUtSnofre6GFJhUD673thInTvDLFiw7yS1GF");
            smileSecurityConfig.setPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCGMGB3OHGrOL51Lt2kj9LDObyvGHf906XUCmEyUZSGyZHStIgYrxsbgHRXIqJT6ERDmNulkNTfIyLns6ohU3qCV6Q5SNbUakuo0SdjrYgdlJiGL3KGk8aZfFX8ic6NL2D0YIote8Tj4elb+sk6Qx32TfDrnueh5VHQz2ZcuwU3QpZt3bGq2Ey8hKVkwo0oFV8YmteoImym3pN/3/KeS8PEW8ZCuG2F96QWkL2PIOI+oiVpeD+O/C3+lautQgwdh4lVGKynJCU+VcYxak48obHUAzUEtsgxDeIotLCOjSRocBzdkkezgr/Cch1wKKsr/DHfXN7Jv+0MRT6NoJ3pGHUlAgMBAAECggEARmPWFnPrXI/ViI7fAVXYj0QYMBk7yFBfqo0PU1/r85CFB+xff+NK6vBFFvg6Ap3SQUtL3NvOboZ15ukeE4FAwGLM6KfG2YIw0gt1CBqdTjcGk00MXH/kK9tHF8QKL5Fs4IRLt3Q3Yfq9eNxi9kcCzpMwD8k5ZHlwo4JIBdis8cEfsBocJYoovVX+80B/bJ3/PDp/4puGGDFYbgdFlmp9xqwoN2RbELbSjMAE3xEaq9+qhiVLgdO7pO6MLol8FaTVqVBVs8191yBR7iDgMjsuUy9yi+W5mnCl3RK1RV3aDW4yUtCyZgJ+BpVcrjCYyEhFXVallXLhU3xGB+pJfafawQKBgQC/PkN82XEr3f/viXmOQ+lFG52lV87z3THQnoWnC/z4tQARtBSuYBhmxAoxKRBYBsaNRy35DQLYekFH+1vPrOTvxBW8EOo1Da/w91cESVrxRD7BAMmwAC5LpynUh0Fmd3vnD6J5QSp3oOPraQBLRSS5CEtp+aWR+S4d9pgva5r0CQKBgQCzoGuS3xZj2Q/e6HCi7p3jEz3VFrVf8q4cpYONZuiFFTY2Xr0bQTO0YdTjxO9M1foT6cG4ykV8tgMYA6afVm5cCpXHla5n9PgnqFqUlkXPTZ0AwupEvEHlXiqtJN2Q6M40Vl29RP0ANB3zVKrkS89fa5VjgZjiDKEw+Jxx8iOXPQKBgHEMyG5ER68r04BxHucnY/zTP1SfXJox1qHBI7bE0mze6kSM1HDkI2iUWIYJBT1T/AlF23pO47C+TFrVzscsKEVH/6Ulxp1IEwp52/mPt2/J7OLAqXkeEj/seEDHF7UPyrGjP2M4T3daJ4Fgl6jnHbc3nQTva52n1EgoHnxhur6pAoGARIbMw0lMV2c1wMpYPjBeF4T9r89r/mCsv8z89PLvfeXruq5F4qHrQcOY3hQBXwS61OH2v7Ka88rUP1MVO/BTRdXb8jpOQOaZzTdS5yC1HTL01JeHnBVd5k0YZtmLlX7bO+g+9rfc9TjsvCnu0A2D3VlDsqR8WLCMxgvyUx4XKdkCgYEAqIHVg3mcs38E2vyELhKAsgq0MuNv7uIJg/WwX+f/usdAFeNkn2W/+bac/bpknXz5ZVYKQ3JXpWl2o6VuF9IfHO/El4wk4SrtWA0c3h11h4k4laj824KhhNRi+lZQ/5M2iDtp4uOGsxc7OFfgzBu3nYIbpUQ7SzC+AcwL0+1NanA=");
            smileSecurityConfig.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhjBgdzhxqzi+dS7dpI/Swzm8rxh3/dOl1AphMlGUhsmR0rSIGK8bG4B0VyKiU+hEQ5jbpZDU3yMi57OqIVN6glekOUjW1GpLqNEnY62IHZSYhi9yhpPGmXxV/InOjS9g9GCKLXvE4+HpW/rJOkMd9k3w657noeVR0M9mXLsFN0KWbd2xqthMvISlZMKNKBVfGJrXqCJspt6Tf9/ynkvDxFvGQrhthfekFpC9jyDiPqIlaXg/jvwt/pWrrUIMHYeJVRispyQlPlXGMWpOPKGx1AM1BLbIMQ3iKLSwjo0kaHAc3ZJHs4K/wnIdcCirK/wx31zeyb/tDEU+jaCd6Rh1JQIDAQAB");
        }
    }

    @Test
    public void testAddSign() {
        BaseRequest<Map<String, Object>> request = new BaseRequest<>();
        //sign param
        request.setMethodName("getUserInfo");
        request.setRequestMethod("get");
        request.setMerchantNo("6666XF20230306001");
        request.setAppId("10000000006666001");
        request.setMsgId("666");
        request.setSignTimestamp(System.currentTimeMillis());
        request.setApiGwPublicKey("YyQyCaWdilrUL3oFZ6oeS8ZvWxHuGkmFsTZvq3eKYLyMY3autqJK2ik7EzuMpaCRBJ5ojwB3Ffs9vSqhT67KxG3KbAiQgf12qNZDGYfVaAEtscbJXCcGvSDIWsbuZkmyH5NzlcsZsOR90HvGPZCD9OFcIYbksqfoDcLISB8kFaONi7TwYpofSuJLYGaJvDROyeG74kvJCriGBICGZE3MDVWej7pXaUtSnofre6GFJhUD673thInTvDLFiw7yS1GF");

        /*Map<String, Object> map = new HashMap<>(16);
        map.put("testParam", "hello");
        request.setBizContent(map);*/

        SmileClient client = new SmileClient(smileSecurityConfig.getApiGwPublicKey(), smileSecurityConfig.getAppId(), smileSecurityConfig.getMerchantNo(), SmileConstants.SIGN_TYPE_RSA2, smileSecurityConfig.getPrivateKey());
        SmileHashMap smileHashMap = client.prepareParams(request);
        System.out.println(JSON.toJSONString(smileHashMap));
    }

}
