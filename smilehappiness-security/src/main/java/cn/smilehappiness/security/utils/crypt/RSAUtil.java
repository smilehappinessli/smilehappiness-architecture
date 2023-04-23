/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.smilehappiness.security.utils.crypt;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

/**
 * <p>
 * RSA
 * <p/>
 *
 * @author
 * @Date 2023/3/7 11:40
 */
public class RSAUtil {

    public static KeyPair generateKeyPair() throws Exception {
        return generateKeyPair(1024);
    }

    public static KeyPair generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize, new SecureRandom(UUID.randomUUID().toString().getBytes()));
        return keyPairGenerator.generateKeyPair();
    }

    public static String sign(byte[] data, byte[] privateKey, String password, String algorithm) throws Exception {
        return sign(data, AesUtil.aesDecrypt(privateKey, password), algorithm);
    }

    public static String sign(byte[] data, byte[] privateKey, String algorithm) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");
        PrivateKey pk = keyfactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(pk);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    public static boolean verify(byte[] data, byte[] publicKey, String sign, String algorithm) throws Exception {
        X509EncodedKeySpec xksc = new X509EncodedKeySpec(publicKey);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");
        PublicKey pk = keyfactory.generatePublic(xksc);
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(pk);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    public static void generateRsa(OutputStream priKeyOutputStream, OutputStream pubKeyOutputStream) throws Exception {
        generateRsa(priKeyOutputStream, pubKeyOutputStream, null);
    }

    public static void generateRsa(OutputStream priKeyOutputStream, OutputStream pubKeyOutputStream, String password)
            throws Exception {
        saveKeys(priKeyOutputStream, pubKeyOutputStream, generateKeyPair(), password);
    }

    public static void genRsaWithoutNewline(int keysize, OutputStream priKeyOutputStream, OutputStream pubKeyOutputStream)
            throws Exception {
        genRsaWithoutNewline(keysize, priKeyOutputStream, pubKeyOutputStream, null);
    }

    public static void genRsaWithoutNewline(int keysize, OutputStream priKeyOutputStream, OutputStream pubKeyOutputStream,
                                            String password) throws Exception {
        KeyPair kp = generateKeyPair(keysize);
        savePrivateKeyWithoutNewline(priKeyOutputStream, kp.getPrivate().getEncoded(), password);
        savePublicKeyWithoutNewline(pubKeyOutputStream, kp.getPublic().getEncoded());
    }


    /**
     * @param filePath Public key file path
     * @return Public key file string
     * @throws Exception
     */
    public static String loadPublicKeyFromStream(String filePath) throws Exception {
        return loadApiKeyFromStream(new FileInputStream(filePath), KeyType.PUBLIC_KEY);
    }

    protected static String loadApiKeyFromStream(InputStream in, KeyType keyType) throws Exception {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader rb = new BufferedReader(isr);
        StringBuffer privateKey = new StringBuffer();
        String line = null;
        boolean firstLine = true;
        boolean containsDesc = false;
        boolean start = false;
        while ((line = rb.readLine()) != null) {
            if (firstLine) {
                if (line.startsWith(GENERATR_ALG)) {
                    containsDesc = true;
                }
                firstLine = false;
            }
            if (containsDesc && !start) {
                if (keyType.equals(KeyType.PRIVATE_KEY) && line.equals(new String(START_PRIVATE_KEY_BYTES))) {
                    start = true;
                } else if (keyType.equals(KeyType.PUBLIC_KEY) && line.equals(new String(START_PUBLIC_KEY_BYTES))) {
                    start = true;
                }
                continue;
            }
            if (start) {
                if (keyType.equals(KeyType.PRIVATE_KEY) && line.equals(new String(END_PRIVATE_KEY_BYTES))) {
                    break;
                } else if (keyType.equals(KeyType.PUBLIC_KEY) && line.equals(new String(END_PUBLIC_KEY_BYTES))) {
                    break;
                }
            }
            privateKey.append(line);
        }
        return privateKey.toString().trim();

    }

    public static byte[] loadPublicKeyFromStream(InputStream in) throws Exception {
        return loadKeyFromStream(in, KeyType.PUBLIC_KEY);
    }

    public static byte[] loadPrivateKeyFromStream(InputStream in, String password) throws Exception {
        return AesUtil.aesDecrypt(loadKeyFromStream(in, KeyType.PRIVATE_KEY), password);
    }

    public static byte[] loadPrivateKeyFromStream(InputStream in) throws Exception {
        return loadKeyFromStream(in, KeyType.PRIVATE_KEY);
    }

    // ######################################################### Storage and acquisition#############################################################################
    private static final String CHARSET_UTF8 = "UTF-8";
    /**
     * The maximum length of the data in the last line of the generated file
     */
    private static final int LINE_SIZE = 65;

    private static final String NEW_LINE_STR = System.getProperty("line.separator");
    /**
     * Used to generate file line breaks
     */
    private static final byte[] NEW_LINE_BYTES = NEW_LINE_STR.getBytes();
    /**
     * Used to mark the beginning of the public key in the generated file
     */
    private static final byte[] START_PUBLIC_KEY_BYTES = "##############start public key##############".getBytes();
    /**
     * Used to mark the end of the public key in the generated file
     */
    private static final byte[] END_PUBLIC_KEY_BYTES = "##############end public key##############".getBytes();
    /**
     * Used to mark the beginning of the private key in the generation file
     */
    private static final byte[] START_PRIVATE_KEY_BYTES = "##############start private key##############".getBytes();
    /**
     * Used to mark the end of the private key in the generated file
     */
    private static final byte[] END_PRIVATE_KEY_BYTES = "##############end private key##############".getBytes();

    private static final String GENERATR_ALG = "key generate algorithm: ";

    protected static void saveKeys(OutputStream priKeyOutputStream, OutputStream pubKeyOutputStream, KeyPair kp)
            throws Exception {
        saveKeys(priKeyOutputStream, pubKeyOutputStream, kp, null);
    }

    protected static void saveKeys(OutputStream priKeyOutputStream, OutputStream pubKeyOutputStream, KeyPair kp,
                                   String password) throws Exception {
        savePrivateKey(priKeyOutputStream, kp.getPrivate().getEncoded(), password);
        savePublicKey(pubKeyOutputStream, kp.getPublic().getEncoded());
    }

    protected static void savePublicKey(OutputStream pubKeyOutputStream, byte[] pubKeyBytes) throws Exception {
        try {
            pubKeyOutputStream.write(getTitle().getBytes(CHARSET_UTF8));
            pubKeyOutputStream.write(NEW_LINE_BYTES);
            pubKeyOutputStream.write(START_PUBLIC_KEY_BYTES);
            pubKeyOutputStream.write(NEW_LINE_BYTES);
            saveBase64KeyToStream(Base64.encodeBase64(pubKeyBytes), pubKeyOutputStream);
            pubKeyOutputStream.write(END_PUBLIC_KEY_BYTES);
        } finally {
            pubKeyOutputStream.flush();
            pubKeyOutputStream.close();
        }
    }

    protected static void savePrivateKey(OutputStream priKeyOutputStream, byte[] priKeyBytes, String password)
            throws Exception {
        try {
            if (password != null) {
                priKeyBytes = AesUtil.aesEncrypt(priKeyBytes, password);
            }
            priKeyOutputStream.write(getTitle().getBytes(CHARSET_UTF8));
            priKeyOutputStream.write(NEW_LINE_BYTES);
            priKeyOutputStream.write(START_PRIVATE_KEY_BYTES);
            priKeyOutputStream.write(NEW_LINE_BYTES);
            saveBase64KeyToStream(Base64.encodeBase64(priKeyBytes), priKeyOutputStream);
            priKeyOutputStream.write(END_PRIVATE_KEY_BYTES);
        } finally {
            priKeyOutputStream.flush();
            priKeyOutputStream.close();
        }
    }

    protected static void savePublicKeyWithoutNewline(OutputStream pubKeyOutputStream, byte[] pubKeyBytes)
            throws Exception {
        try {
            pubKeyOutputStream.write(Base64.encodeBase64(pubKeyBytes));
        } finally {
            pubKeyOutputStream.flush();
            pubKeyOutputStream.close();
        }
    }

    protected static void savePrivateKeyWithoutNewline(OutputStream priKeyOutputStream, byte[] priKeyBytes,
                                                       String password) throws Exception {
        try {
            if (password != null) {
                priKeyBytes = AesUtil.aesEncrypt(priKeyBytes, password);
            }
            priKeyOutputStream.write(Base64.encodeBase64(priKeyBytes));
        } finally {
            priKeyOutputStream.flush();
            priKeyOutputStream.close();
        }
    }

    protected static byte[] loadKeyFromStream(InputStream in, KeyType keyType) throws Exception {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader rb = new BufferedReader(isr);
        StringBuffer privateKey = new StringBuffer();
        String line = null;
        boolean firstLine = true;
        boolean containsDesc = false;
        boolean start = false;
        while ((line = rb.readLine()) != null) {
            if (firstLine) {
                if (line.startsWith(GENERATR_ALG)) {
                    containsDesc = true;
                }
                firstLine = false;
            }
            if (containsDesc && !start) {
                if (keyType.equals(KeyType.PRIVATE_KEY) && line.equals(new String(START_PRIVATE_KEY_BYTES))) {
                    start = true;
                } else if (keyType.equals(KeyType.PUBLIC_KEY) && line.equals(new String(START_PUBLIC_KEY_BYTES))) {
                    start = true;
                }
                continue;
            }
            if (start) {
                if (keyType.equals(KeyType.PRIVATE_KEY) && line.equals(new String(END_PRIVATE_KEY_BYTES))) {
                    break;
                } else if (keyType.equals(KeyType.PUBLIC_KEY) && line.equals(new String(END_PUBLIC_KEY_BYTES))) {
                    break;
                }
            }
            privateKey.append(line);
        }
        return Base64.decodeBase64(privateKey.toString());

    }

    protected static void saveBase64KeyToStream(byte[] base64Bytes, OutputStream out) throws IOException {
        int start = 0, len = 0;
        while (start < base64Bytes.length) {
            len = Math.min(LINE_SIZE, base64Bytes.length - start);
            out.write(base64Bytes, start, len);
            out.write(NEW_LINE_BYTES);
            start += len;
        }
    }

    protected static String getTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(GENERATR_ALG);
        sb.append("RSA");
        sb.append(NEW_LINE_STR);
        sb.append("key size: ");
        sb.append(1024);
        return sb.toString();
    }

    public enum KeyType {

        PUBLIC_KEY("public key"), PRIVATE_KEY("private key");

        String type;

        private KeyType(String type) {
            this.type = type;
        }

    }

}
