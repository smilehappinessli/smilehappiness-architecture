package cn.smilehappiness.security.utils.crypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>
 * AES algorithm
 * <p/>
 *
 * @author
 * @Date 2023/3/7 11:35
 */
public class AesUtil {

    private static final String AES_ALG = "AES";
    /**
     * AES algorithm
     */
    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";

    private static final byte[] AES_IV = initIv(AES_CBC_PCK_ALG);

    private static final String CHARSET_UTF8 = "UTF-8";

    public static String aesEncrypt(String content, String aesKey) {
        return aesEncrypt(content, aesKey, CHARSET_UTF8);
    }


    /**
     * AES encryption
     */
    public static String aesEncrypt(String content, String aesKey, String charset) {
        try {
            return new String(aesEncrypt(content.getBytes(charset), aesKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] aesEncrypt(byte[] content, String aesKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(aesKey), AES_ALG), iv);

            byte[] encryptBytes = cipher.doFinal(content);
            return Base64.encodeBase64(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String aesDecrypt(String content, String aesKey) {
        return aesDecrypt(content, aesKey, CHARSET_UTF8);
    }

    public static byte[] aesDecrypt(byte[] content, String aesKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(aesKey), AES_ALG), iv);

            return cipher.doFinal(Base64.decodeBase64(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES decrypt
     */
    public static String aesDecrypt(String content, String aesKey, String charset) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(aesKey), AES_ALG), iv);

            byte[] cleanBytes = cipher.doFinal(Base64.decodeBase64(content));
            return new String(cleanBytes, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The method of initial vector is all 0 The writing method here is suitable for other algorithms. For AES algorithm, the IV value must be 128 bits (16 bytes)
     */
    private static byte[] initIv(String fullAlg) {

        try {
            Cipher cipher = Cipher.getInstance(fullAlg);
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        } catch (Exception e) {
            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }

}
