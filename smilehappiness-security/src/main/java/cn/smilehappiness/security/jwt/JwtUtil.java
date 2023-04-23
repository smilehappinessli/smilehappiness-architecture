//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.smilehappiness.security.jwt;

import cn.smilehappiness.security.dto.UserTokenInfo;
import cn.smilehappiness.security.utils.SecurityUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * Jwt Tools
 * <p/>
 *
 * @author
 * @Date 2021/10/13 18:57
 */
public class JwtUtil {

    private static final String AUTHOR = "smile";
    private static final String KEY_CLAIMS = "key_claims";
    private static final String SUBJECT = "key_subject";

    public JwtUtil() {
    }

    /**
     * <p>
     * Parse token information
     * setAllowedClockSkewSeconds()  Set the allowed time offset (seconds). If a negative value is passed in, it will be set to 0, which is equivalent to not set. This method is related to setNotBefore() in the builder. When setNotBefore() is set in the builder, then the Token must be available after this time, while setting the allowed time offset in the parser is equivalent to turning this time point into a time range。
     * For example, if setNotBefore is set to 5 seconds after the current time when generating a JWT token, then immediately parsing the token will throw a PredictionJwtException exception, indicating that the JWT's receiving time has not yet arrived. So if I set the allowable time offset to 5 seconds or more when parsing, I can receive the parsing when judging the JWT。
     * In addition, this offset will also affect the expiration time of the JWT. For example, the JWT has already expired for 5 seconds. If I set the offset to 5 seconds or more, it will still be used as a valid JWT credential
     * <p/>
     *
     * @param authenticationToken
     * @param tokenSecret
     * @return io.jsonwebtoken.Claims
     * @Date 2021/10/13 21:28
     */
    public static <T> T parserToken(Class<T> clazz, String authenticationToken, long tokenActiveTime, String tokenSecret) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(generalKey(tokenSecret))
                .setAllowedClockSkewSeconds(tokenActiveTime)
                .build()
                .parseClaimsJws(authenticationToken);

        //Claims Is aMap
        Claims claims = jws.getBody();
        //map To entity
        return JSON.parseObject(JSON.toJSONString(claims.get(KEY_CLAIMS)), clazz);
    }

    /**
     * <p>
     * generatetoken
     * <p/>
     *
     * @param userInfo
     * @param tokenSecret
     * @param tokenExpireTime
     * @return java.lang.String
     * @Date 2021/10/13 21:18
     */
    public static String generateToken(UserTokenInfo userInfo, String tokenSecret, long tokenExpireTime) {
        //  Create the private declaration of payload (added according to the specific business needs. If you want to verify this, you need to communicate with the recipient of jwt in advance）
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(KEY_CLAIMS, userInfo);

        //  Time of generating JWT
        long nowTime = System.currentTimeMillis();
        Date issuedAt = new Date(nowTime);

        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                // jti(JWT ID)：jwt The unique ID of can be set as a non-repeating value according to business needs, mainly used as a one-time token to avoid replay attacks
                .setId(UUID.randomUUID().toString())
                //jwt Issued on
                .setIssuedAt(issuedAt)
                //jwt Issuer
                .setIssuer(AUTHOR)
                //jwt For the user to be targeted, put the login user name, a string in json format, which can store userid, rolid, and so on, as the unique identifier of the user
                .setSubject(SUBJECT)
                // The signature algorithm used in the signature process uses HAMC to salt (secret key) hash the SHA digest algorithm. jjwt has encapsulated this part
                .signWith(generalKey(tokenSecret));

        if (tokenExpireTime >= 0L) {
            long expMillis = nowMillis + tokenExpireTime * 1000;
            Date exp = new Date(expMillis);
            // Set token expiration time
            builder.setExpiration(exp);
        }

        return builder.compact();
    }


    /**
     * <p>
     * Generate encryption from stringkey
     * The secret key used when generating the signature. Remember that the secret key cannot be exposed. It is the private key of your server. It should not be exposed in any scenario. Once the client knows the secret, it means that the client can sign the jwt itself
     * <p/>
     *
     * @param tokenSecret
     * @return javax.crypto.SecretKey
     * @Date 2021/10/18 21:00
     */
    public static SecretKey generalKey(String tokenSecret) {
        try {
            byte[] baseKey = tokenSecret.getBytes(StandardCharsets.UTF_8);
            //  Use AES encryption algorithm to construct a key according to the given byte array
            return new SecretKeySpec(baseKey, SignatureAlgorithm.HS256.getJcaName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        // Get HmacSHA256 encryption base string generation
        System.err.println("hmacSHA256: " + SecurityUtil.hmacSHA256Key());

        // generatetoken
        UserTokenInfo userTokenInfo = new UserTokenInfo();
        userTokenInfo.setUserId(1L);
        userTokenInfo.setUserName("18266666666");
        userTokenInfo.setAppMerchant("appMerchant");
        userTokenInfo.setMobile("18266666666");
        String tokenSecret = "LIo9Zkl3MHhRfUpWxjb05PjPqvIBrpW5oomDSl2crpSdxEe2GIKVbJ0Q7Zw2v0mv5oS0eRVrmpSVkEKkji0M4sGrhalr2B7JbhHxlMhj6BBAwSaPeckb3xH16rulIW8gtKokILPUhiMLo8LWdVtivowJCjnDSyeSrxJJzMzBXIZx56eMCitRFa4OE8nuDNmQy2tzyh10fWKLoujxzkcDMRO9wkqMs2wVLfscWBwoQS2KF7bseAEVg7DPxhcRmk79";
        String baseToken = generateToken(userTokenInfo, tokenSecret, 2592000);
        String token = StringUtils.join("Bearer " + baseToken);
        System.err.println("token: " + token);

        // analysistoken
        UserTokenInfo userTokenInfoParse = parserToken(UserTokenInfo.class, baseToken, 60, tokenSecret);
        System.out.println(JSON.toJSONString(userTokenInfoParse));
    }


}
