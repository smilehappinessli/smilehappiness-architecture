package cn.smilehappiness.security.utils;

import cn.smilehappiness.security.constant.SmileConstants;
import cn.smilehappiness.security.utils.crypt.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * smile signature util
 * <p/>
 *
 * @author
 * @Date 2023/3/7 14:04
 */
public class SmileSignatureUtil {

    private static final Logger logger = LoggerFactory.getLogger(SmileSignatureUtil.class);
    public static final String SIGN_PREFIX = "VsMiLe6";

    private SmileSignatureUtil() {

    }

    public static String sign(String content, String signType, String privateKey, String charset) {
        return sign(content, signType, privateKey, charset, null);
    }

    /**
     * <p>
     * sign
     * <p/>
     *
     * @param content
     * @param signType
     * @param privateKey
     * @param charset
     * @param password
     * @return java.lang.String
     * @Date 2023/3/7 14:15
     */
    public static String sign(String content, String signType, String privateKey, String charset, String password) {
        try {
            byte[] contentBytes = content.getBytes(charset);
            if (signType.equals(SmileConstants.SIGN_TYPE_RSA)) {
                String signStr = RSAUtil.sign(contentBytes, Base64.decodeBase64(privateKey), SmileConstants.SIGN_SHA1RSA_ALGORITHMS);
                return StringUtils.join(SIGN_PREFIX, signStr);
            } else if (signType.equals(SmileConstants.SIGN_TYPE_RSA2)) {
                String signStr = RSAUtil.sign(contentBytes, Base64.decodeBase64(privateKey), SmileConstants.SIGN_SHA256RSA_ALGORITHMS);
                return StringUtils.join(SIGN_PREFIX, signStr);
            }

            logger.error("not support signType");
            throw new RuntimeException("not support signType.");
        } catch (UnsupportedEncodingException e) {
            logger.error("get content charset exception, content: {} , charset:{} ,error info: {}", content, charset, e);
            throw new RuntimeException("get content charset exception. content: " + content + " charset: " + charset + e);
        } catch (Exception e) {
            logger.error("sign exception.", e);
            throw new RuntimeException("sign exception." + e);
        }

    }

    /**
     * <p>
     * verify sign
     * <p/>
     *
     * @param content
     * @param signType
     * @param publicKey
     * @param charset
     * @param sign
     * @return boolean
     * @Date 2023/3/7 14:15
     */
    public static boolean verifySign(String content, String signType, String publicKey, String charset, String sign) {
        try {
            byte[] contentBytes = content.getBytes(charset);
            if (signType.equals(SmileConstants.SIGN_TYPE_RSA)) {
                return RSAUtil.verify(contentBytes, Base64.decodeBase64(publicKey), sign, SmileConstants.SIGN_SHA1RSA_ALGORITHMS);
            } else if (signType.equals(SmileConstants.SIGN_TYPE_RSA2)) {
                return RSAUtil.verify(contentBytes, Base64.decodeBase64(publicKey), sign, SmileConstants.SIGN_SHA256RSA_ALGORITHMS);
            }

            logger.error("not support signType.");
            throw new RuntimeException("not support signType.");
        } catch (UnsupportedEncodingException e) {
            logger.error("get content charset exception. content: " + content + " charset: " + charset, e);
            throw new RuntimeException("get content charset exception. content: " + content + " charset: " + charset + e);
        } catch (Exception e) {
            logger.error("sign verify exception.", e);
            throw new RuntimeException("sign verify exception." + e);
        }
    }

}
