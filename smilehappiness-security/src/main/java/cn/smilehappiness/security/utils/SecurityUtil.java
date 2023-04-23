package cn.smilehappiness.security.utils;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * <p>
 * Security tools
 * <p/>
 *
 * @author
 * @Date 2021/10/13 14:42
 */
public class SecurityUtil {

    /**
     * MAC The algorithm can be selected from the following algorithms: MD5, SHA, and HMAC. These three encryption algorithms can be described as non-reversible encryption and non-decryptible encryption methods. We call them one-way encryption algorithms. Simple encryption of the above three types is not very reliable, and can often be combined with multiple encryption methods
     * {@link SignatureAlgorithm}
     */
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    private static final Random RD = new SecureRandom();

    /**
     * <p>
     * Use MD5 for encryption
     * <p/>
     *
     * @param str
     * @return java.lang.String
     * @Date 2021/10/13 14:44
     */
    public static String encryptByMd5(String str) {
        try {
            // Determine the calculation method
            MessageDigest md = MessageDigest.getInstance(KEY_MD5);
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();

            int temp;
            StringBuilder sb = new StringBuilder();
            for (byte value : bytes) {
                temp = value;
                if (temp < 0) {
                    temp += 256;
                }
                if (temp < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(temp));
            }
            str = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * BASE64 decrypt
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) {
        return Base64.getDecoder().decode(key);
    }

    /**
     * BASE64 encryption
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) {
        return new String(Base64.getEncoder().encode(key));
    }

    /**
     * SHA encryption
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);

        return sha.digest();

    }

    /**
     * Initialize HMAC key
     *
     * @return
     * @throws Exception
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(SignatureAlgorithm.HS256.getJcaName());

        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC encryption
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {

        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), SignatureAlgorithm.HS256.getJcaName());
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);

        return mac.doFinal(data);
    }


    /**
     * <p>
     * HmacSHA256 Encryption base string generation
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/19 12:50
     */
    public static String hmacSHA256Key() throws Exception {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        return generateString(new Random(), str, 256);
    }

    /**
     * Generate a random string.
     *
     * @param random     the random number generator.
     * @param characters the characters for generating string.
     * @param length     the length of the generated string.
     * @return
     */
    public static String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }


    public static void main(String[] args) throws Exception {
        String inputStr = "smile@123";
        System.err.println(" original text:\n" + inputStr);

        //84f3966841a2dbc96629f2006f4d8192
        System.err.println("MD5 After encryption:\n" + encryptByMd5(inputStr));

        byte[] inputData = inputStr.getBytes();
        String encryptBASE64Str = SecurityUtil.encryptBASE64(inputData);
        System.err.println("BASE64 After encryption:\n" + encryptBASE64Str);
        byte[] decryptBASE64Str = SecurityUtil.decryptBASE64(encryptBASE64Str);
        System.err.println("BASE64 After decryption:\n" + new String(decryptBASE64Str));

        String key = SecurityUtil.initMacKey();
        System.err.println("Mac secret key:\n" + key);

        BigInteger sha = new BigInteger(SecurityUtil.encryptSHA(inputData));
        System.err.println("SHA:\n" + sha.toString(32));

        BigInteger mac = new BigInteger(SecurityUtil.encryptHMAC(inputData, inputStr));
        System.err.println("HMAC:\n" + mac.toString(16));

        String baseHmacKey = hmacSHA256Key();
        System.err.println("baseHmacKey:\n" + baseHmacKey);
        System.err.println("baseHmacKeyLength:" + baseHmacKey.length());
    }

}
