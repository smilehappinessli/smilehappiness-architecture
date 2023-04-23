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

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;

/**
 * <p>
 * Digest
 * <p/>
 *
 * @author
 * @Date 2023/3/7 11:39
 */
public class DigestUtil {

    public static String sha256(String data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA256");
        sha.update(data.getBytes());
        byte[] digest = sha.digest();
        return toHax(digest);
    }

    public static String hmac(String data, String password) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] digestBytes = mac.doFinal(data.getBytes());
        return toHax(digestBytes);
    }

    public static String toHax(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String out = Integer.toHexString(digest[i] & 0xFF);
            if (out.length() == 1) {
                sb.append('0');
            }
            sb.append(out);
        }
        return sb.toString();
    }

}
