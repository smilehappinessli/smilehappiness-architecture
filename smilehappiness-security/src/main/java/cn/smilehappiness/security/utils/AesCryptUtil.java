package cn.smilehappiness.security.utils;

import cn.smilehappiness.security.constant.SmileConstants;
import cn.smilehappiness.security.utils.crypt.AesUtil;

/**
 * <p>
 * smile encrypt util
 * <p/>
 *
 * @author
 * @Date 2023/3/7 11:32
 */
public class AesCryptUtil {

    private static final String AES_TYPE = SmileConstants.ENCRYPT_TYPE_AES;

    /**
     * <p>
     * encrypt content
     * <p/>
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return java.lang.String
     * @Date 2023/3/7 11:42
     */
    public static String encryptContent(String content, String encryptType, String encryptKey, String charset) {
        if (AES_TYPE.equals(encryptType)) {
            return AesUtil.aesEncrypt(content, encryptKey, charset);
        } else {
            throw new RuntimeException(" The algorithm type is not currently supported：encrypeType=" + encryptType);
        }
    }

    /**
     * <p>
     * decrypt content
     * <p/>
     *
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return java.lang.String
     * @Date 2023/3/7 11:42
     */
    public static String decryptContent(String content, String encryptType, String encryptKey, String charset) {
        if (AES_TYPE.equals(encryptType)) {
            return AesUtil.aesDecrypt(content, encryptKey, charset);
        } else {
            throw new RuntimeException(" The algorithm type is not currently supported：encrypeType=" + encryptType);
        }
    }

}
