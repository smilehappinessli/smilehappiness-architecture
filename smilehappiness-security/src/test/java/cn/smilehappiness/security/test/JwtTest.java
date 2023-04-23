//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.smilehappiness.security.test;

import cn.smilehappiness.security.dto.UserTokenInfo;
import cn.smilehappiness.security.jwt.JwtUtil;
import cn.smilehappiness.security.utils.SecurityUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * <p>
 * JWT test
 * <p/>
 *
 * @author
 * @Date 2023/3/8 18:05
 */
public class JwtTest {

    @Test
    public void testJwt() throws Exception {
        // Get HmacSHA256 encryption base string generation
        System.err.println("hmacSHA256: " + SecurityUtil.hmacSHA256Key());

        // generatetoken
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        userTokenInfo.setUserId(1L);
        userTokenInfo.setUserName("18266666666");
        userTokenInfo.setAppMerchant("appMerchant");
        userTokenInfo.setMobile("18266666666");
        String tokenSecret = "LIo9Zkl3MHhRfUpWxjb05PjPqvIBrpW5oomDSl2crpSdxEe2GIKVbJ0Q7Zw2v0mv5oS0eRVrmpSVkEKkji0M4sGrhalr2B7JbhHxlMhj6BBAwSaPeckb3xH16rulIW8gtKokILPUhiMLo8LWdVtivowJCjnDSyeSrxJJzMzBXIZx56eMCitRFa4OE8nuDNmQy2tzyh10fWKLoujxzkcDMRO9wkqMs2wVLfscWBwoQS2KF7bseAEVg7DPxhcRmk79";
        String baseToken = JwtUtil.generateToken(userTokenInfo, tokenSecret, 2592000);
        //Bearer
        String token = StringUtils.join("JWT " + baseToken);
        System.err.println("token: " + token);

        // analysistoken
        UserTokenInfo userTokenInfoParse = JwtUtil.parserToken(UserTokenInfo.class, baseToken, 60, tokenSecret);
        System.out.println(JSON.toJSONString(userTokenInfoParse));
    }


}
