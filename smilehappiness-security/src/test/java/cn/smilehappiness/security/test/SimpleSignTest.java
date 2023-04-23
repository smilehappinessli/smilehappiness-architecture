package cn.smilehappiness.security.test;

import cn.smilehappiness.security.config.SimpleSignComponent;
import cn.smilehappiness.security.dto.SimpleSignRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * <p>
 * simple sign test
 * <p/>
 *
 * @author
 * @Date 2023/3/10 10:52
 */
public class SimpleSignTest {

    @Autowired
    public SimpleSignComponent simpleSignUtil;

    /**
     * <p>
     * test simple sign, need spring boot start class
     * <p/>
     *
     * @param
     * @return void
     * @Date 2023/3/10 11:02
     */
    @Test
    public void testSimpleSign() {
        SimpleSignRequest<Map<String, Object>> request = new SimpleSignRequest<>();
        //sign param
        request.setMethodName("getUserInfo");
        request.setRequestMethod("get");
        request.setMerchantNo("6666XF20230306001");
        request.setAppId("10000000006666001");
        request.setMsgId("666");
        request.setSignTimestamp(System.currentTimeMillis());
        request.setApiGwPublicKey("YyQyCaWdilrUL3oFZ6oeS8ZvWxHuGkmFsTZvq3eKYLyMY3autqJK2ik7EzuMpaCRBJ5ojwB3Ffs9vSqhT67KxG3KbAiQgf12qNZDGYfVaAEtscbJXCcGvSDIWsbuZkmyH5NzlcsZsOR90HvGPZCD9OFcIYbksqfoDcLISB8kFaONi7TwYpofSuJLYGaJvDROyeG74kvJCriGBICGZE3MDVWej7pXaUtSnofre6GFJhUD673thInTvDLFiw7yS1GF");
        simpleSignUtil.addSign(request);
    }


}
