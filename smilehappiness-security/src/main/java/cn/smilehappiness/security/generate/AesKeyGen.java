package cn.smilehappiness.security.generate;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * aes key gen
 * <p/>
 *
 * @author
 * @Date 2023/3/9 10:51
 */
public class AesKeyGen {

    public static void main(String[] args) {
        // AES  Key generation
        int keySize = 128;
        KeyGenerator keyGenerator;
        String projectPath = System.getProperty("user.dir");
        String basePath = projectPath + "/smilehappiness-security";
        String keyPath = basePath + "/src/test/java/cn/smilehappiness/security/generate/AesKey.txt";
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            if (args.length == 1) {
                keySize = Integer.parseInt(args[0]);
            }
            keyGenerator.init(keySize);
            byte[] encoded = keyGenerator.generateKey().getEncoded();
            String key = Base64.encodeBase64String(encoded);
            saveKey(keyPath, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * <p>
     * saveKey
     * <p/>
     *
     * @param KeyPath
     * @param key
     * @return void
     * @Date 2023/3/9 10:54
     */
    public static void saveKey(String KeyPath, String key) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(KeyPath));
        try {
            out.write(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
