package cn.smilehappiness.security.generate;

import cn.smilehappiness.security.utils.crypt.RSAUtil;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * <p>
 * KeyGen
 * <p/>
 *
 * @author
 * @Date 2023/3/9 10:52
 */
public class KeyGenRsa {

    /**
     *  Specify the public key to store the file
     */
    private static final String PUBLIC_KEY = "yourname.pub";
    /**
     *  Specify the private key to store the file
     */
    private static final String PRIVATE_KEY = "yourname.pri";

    public static void main(String[] args) throws Exception {
        try {
            String projectPath = System.getProperty("user.dir");
            String basePath = projectPath + "/smilehappiness-security";
            String privateKeyPath = basePath + "/src/test/java/cn/smilehappiness/security/generate/" + PRIVATE_KEY;
            String publicKeyPath = basePath + "/src/test/java/cn/smilehappiness/security/generate/" + PUBLIC_KEY;

            OutputStream priOs = new FileOutputStream(privateKeyPath);
            OutputStream pubOs = new FileOutputStream(publicKeyPath);
            if (args.length == 1) {
                int keySize = Integer.parseInt(args[0]);
                RSAUtil.genRsaWithoutNewline(keySize, priOs, pubOs);
            } else {
                RSAUtil.genRsaWithoutNewline(2048, priOs, pubOs);
            }
        } catch (Exception e) {
            throw new Exception("failed to generate key");
        }
    }

}
