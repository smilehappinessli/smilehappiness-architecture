/**
 * icbc.com.cn Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package cn.smilehappiness.security.constant;

/**
 * <p>
 * <p>
 * <p/>
 *
 * @author
 * @Date 2023/3/6 19:30
 */
public class SmileConstants {

    public static final String SIGN_TYPE = "sign_type";

    public static final String SIGN_TYPE_MD5 = "MD5";

    public static final String SIGN_TYPE_RSA = "RSA";

    public static final String SIGN_TYPE_RSA2 = "RSA2";

    public static final String SIGN_TYPE_SM2 = "SM2";

    public static final String SIGN_TYPE_CA = "CA";

    public static final String SIGN_TYPE_SM = "SM";

    public static final String SIGN_TYPE_EM = "EM";

    public static final String SIGN_TYPE_EM_SM = "EM-SM";

    public static final String SIGN_SHA1RSA_ALGORITHMS = "SHA1WithRSA";

    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    public static final String ENCRYPT_TYPE_AES = "AES";

    public static final String METHOD_NAME = "method_name";

    public static final String REQUEST_METHOD = "request_method";

    public static final String MERCHANT_NO = "merchant_no";

    public static final String APP_ID = "app_id";

    public static final String SIGN_TIMESTAMP = "sign_timestamp";

    public static final String API_GW_PUBLIC_KEY = "api_gw_public_key";

    public static final String REQUEST_SIGN = "request_sign";

    public static final String FORMAT = "format";

    public static final String TIMESTAMP = "timestamp";

    public static final String SIGN = "sign";

    public static final String APP_AUTH_TOKEN = "app_auth_token";

    public static final String CHARSET = "charset";

    public static final String NOTIFY_URL = "notify_url";

    public static final String RETURN_URL = "return_url";

    public static final String ENCRYPT_TYPE = "encrypt_type";

    public static final String BIZ_CONTENT_KEY = "biz_content";

    /**
     * Default time format
     **/
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyyMMdd";

    /**
     * Date Default time zone
     **/
    public static final String DATE_TIMEZONE = "GMT+8";

    /**
     * UTF-8 character set
     **/
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * GBK character set
     **/
    public static final String CHARSET_GBK = "GBK";

    /**
     * JSON  Format
     */
    public static final String FORMAT_JSON = "json";

    /**
     * XML  Format
     */
    public static final String FORMAT_XML = "xml";

    public static final String CA = "ca";

    public static final String PASSWORD = "password";

    public static final String RESPONSE_BIZ_CONTENT = "response_biz_content";

    /**
     * Message unique number
     **/
    public static final String MSG_ID = "msg_id";

    /**
     * sdk The version number in the headerkey
     */
    public static final String VERSION_HEADER_NAME = "APIGW-VERSION";

    /**
     * sdk Region number, for overseas institutions
     */
    public static final String ZONE_NO = "Zone-No";

    /**
     * Request type
     */
    public static final String REQUEST_Type = "Request-Type";

    /**
     * For em-type signatures, send a request to CICC Cryptography
     */
    public static final String EM_CFCA = "CFCA";

    /**
     * For em-type signature, send a request to the NC client
     */
    public static final String EM_NC = "NC";

    /**
     * Refined information
     */
    public static final String REFINE_INFO = "Apirefined-Info";

}
