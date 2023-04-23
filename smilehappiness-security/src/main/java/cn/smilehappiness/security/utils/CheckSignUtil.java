package cn.smilehappiness.security.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * CheckSignUtil
 * <p/>
 *
 * @author
 * @Date 2023/3/15 15:23
 */
public class CheckSignUtil {

    public CheckSignUtil() {
    }

    /**
     * <p>
     * check api public key legal
     * <p/>
     *
     * @param apiPublicKeyConfig
     * @param apiPublicKeyParam
     * @return boolean
     * @Date 2023/3/15 15:24
     */
    public static boolean checkApiPublicKeyLegal(String apiPublicKeyConfig, String apiPublicKeyParam) {
        if (StringUtils.isBlank(apiPublicKeyParam) || !apiPublicKeyConfig.equals(apiPublicKeyParam)) {
            throw new RuntimeException("apiPublicKey param [" + apiPublicKeyParam + "] is unLegal");
        }

        return true;
    }

}
