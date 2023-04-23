package cn.smilehappiness.security.api.client;

import cn.smilehappiness.security.constant.SmileConstants;
import cn.smilehappiness.security.dto.BaseRequest;
import cn.smilehappiness.security.utils.*;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * smile client base deal
 * <p/>
 *
 * @author
 * @Date 2023/3/7 11:08
 */
public class SmileClient {

    private static final Logger logger = LoggerFactory.getLogger(SmileClient.class);

    protected String apiPublicKey;
    protected String merchantNo;
    protected String appId;
    protected String signType = SmileConstants.SIGN_TYPE_RSA;
    protected String privateKey;
    protected String publicKey;
    protected String charset = SmileConstants.CHARSET_UTF8;
    protected String format = SmileConstants.FORMAT_JSON;
    protected String encryptType;
    protected String encryptKey;

    public SmileClient() {

    }

    public SmileClient(String apiPublicKeyParam, String appId, String merchantNo, String signType, String privateKey, String publicKey, String charset, String format, String encryptType, String encryptKey) {
        this.apiPublicKey = apiPublicKeyParam;
        this.appId = appId;
        this.merchantNo = merchantNo;
        this.signType = signType;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.charset = charset;
        this.format = format;
        this.encryptType = encryptType;
        this.encryptKey = encryptKey;
    }

    public SmileClient(String apiPublicKeyParam, String appId, String merchantNo, String privateKey) {
        this(apiPublicKeyParam, appId, merchantNo, SmileConstants.SIGN_TYPE_RSA, privateKey, null, SmileConstants.CHARSET_UTF8, SmileConstants.FORMAT_JSON, null, null);
    }

    /**
     * <p>
     * rsa,rsa2，four params
     * <p/>
     *
     * @param apiPublicKeyParam
     * @param appId
     * @param signType
     * @param privateKey
     * @return
     * @Date 2023/3/7 11:08
     */
    public SmileClient(String apiPublicKeyParam, String appId, String merchantNo, String signType, String privateKey) {
        this(apiPublicKeyParam, appId, merchantNo, signType, privateKey, null, SmileConstants.CHARSET_UTF8, SmileConstants.FORMAT_JSON, null, null);
    }

    /**
     * <p>
     * check sign，five params
     * <p/>
     *
     * @param apiPublicKeyParam
     * @param appId
     * @param signType
     * @param privateKey
     * @return
     * @Date 2023/3/8 15:40
     */
    public SmileClient(String apiPublicKeyParam, String appId, String merchantNo, String signType, String privateKey, String publicKey) {
        this(apiPublicKeyParam, appId, merchantNo, signType, privateKey, publicKey, SmileConstants.CHARSET_UTF8, SmileConstants.FORMAT_JSON, null, null);
    }

    /**
     * <p>
     * six params -AES encrypt
     * <p/>
     *
     * @param apiPublicKeyParam
     * @param appId
     * @param signType
     * @param privateKey
     * @param encryptType
     * @param encryptKey
     * @return
     * @Date 2023/3/8 10:19
     */
    public SmileClient(String apiPublicKeyParam, String appId, String merchantNo, String signType, String privateKey, String encryptType, String encryptKey) {
        this(apiPublicKeyParam, appId, merchantNo, signType, privateKey, null, SmileConstants.CHARSET_UTF8, SmileConstants.FORMAT_JSON, encryptType, encryptKey);
    }

    public SmileClient(String apiPublicKeyParam, String appId, String merchantNo, String signType, String privateKey, String publicKey, String encryptType, String encryptKey) {
        this(apiPublicKeyParam, appId, merchantNo, signType, privateKey, publicKey, SmileConstants.CHARSET_UTF8, SmileConstants.FORMAT_JSON, encryptType, encryptKey);
    }

    /**
     * <p>
     * prepare params
     * <p/>
     *
     * @param request
     * @return cn.smilehappiness.security.utils.IcbcHashMap
     * @Date 2023/3/7 13:56
     */
    public SmileHashMap prepareParams(BaseRequest<?> request) {
        SmileHashMap params = new SmileHashMap();
        String strToSign = this.prepareParamStr(request, params);
        logger.info("addSign strToSign: {}", strToSign);

        if (signType.equals(SmileConstants.SIGN_TYPE_RSA) || signType.equals(SmileConstants.SIGN_TYPE_RSA2)) {
            String signedStr = SmileSignatureUtil.sign(strToSign, signType, privateKey, charset);
            params.put(SmileConstants.SIGN, signedStr);
        } else {
            // Other signature types
            logger.error("signType {} is not supported", signType);
            throw new RuntimeException("signType " + signType + " is not supported");
        }

        return params;
    }

    /**
     * <p>
     * prepare param str
     * <p/>
     *
     * @param request
     * @param params
     * @return java.lang.String
     * @Date 2023/3/8 16:34
     */
    private String prepareParamStr(BaseRequest<?> request, SmileHashMap params) {
        Map<String, String> extraParams = request.getExtraParameters();
        if (extraParams != null) {
            params.putAll(extraParams);
        }

        //appId Is the public variable of the class
        params.put(SmileConstants.METHOD_NAME, request.getMethodName());
        params.put(SmileConstants.REQUEST_METHOD, StringUtils.lowerCase(request.getRequestMethod()));
        params.put(SmileConstants.MERCHANT_NO, merchantNo);
        params.put(SmileConstants.APP_ID, appId);
        params.put(SmileConstants.MSG_ID, request.getMsgId());
        params.put(SmileConstants.SIGN_TYPE, signType);
        params.put(SmileConstants.CHARSET, charset);
        params.put(SmileConstants.FORMAT, format);

        try {
            // Get the timestamp, where you can achieve a higher level of control over the time dimension
            long timestamp = System.currentTimeMillis();
            DateFormat df = new SimpleDateFormat(SmileConstants.YYYY_MM_DD);
            df.setTimeZone(TimeZone.getTimeZone(SmileConstants.DATE_TIMEZONE));
            //time error is within 3 minutes, gateway control,this only 1d validity
            params.put(SmileConstants.TIMESTAMP, df.format(new Date(timestamp)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Core content parameters need to be encrypted
        String bizContentStr = buildBizContentStr(request);
        if (request.isEncryptFlag()) {
            if (SecurityStringUtil.areEmpty(encryptType, encryptKey)) {
                logger.error("request need be encrypted, encrypt type and encrypt key can not be null");
                throw new RuntimeException("request need be encrypted, encrypt type and encrypt key can not be null");
            }

            if (bizContentStr != null) {
                params.put(SmileConstants.ENCRYPT_TYPE, encryptType);
                params.put(SmileConstants.BIZ_CONTENT_KEY, AesCryptUtil.encryptContent(bizContentStr, encryptType, encryptKey, charset));
            }

        } else {
            // Do not encrypt, fill in the requestParam field
            params.put(SmileConstants.BIZ_CONTENT_KEY, bizContentStr);
        }

        // Sort by rule
        return this.buildOrderedSignStr(request.getMethodName(), params);
    }

    /**
     * <p>
     * build biz content str
     * <p/>
     *
     * @param request
     * @return java.lang.String
     * @Date 2023/3/7 13:43
     */
    protected String buildBizContentStr(BaseRequest<?> request) {
        if (ObjectUtils.isEmpty(request.getBizContent())) {
            return null;
        }

        if (this.format.equals(SmileConstants.FORMAT_JSON)) {
            return JSON.toJSONString(request.getBizContent());
        } else {
            logger.error("only support json format, current format is not supported, format: {}", this.format);
            throw new RuntimeException("only support json format, current format is not supported, format: " + this.format);
        }
    }

    /**
     * <p>
     * build ordered sign str
     * <p/>
     *
     * @param methodName
     * @param params
     * @return java.lang.String
     * @Date 2023/3/7 11:25
     */
    private String buildOrderedSignStr(String methodName, Map<String, String> params) {
        Map<String, String> sortedMap = new TreeMap();
        sortedMap.putAll(params);
        Set<Map.Entry<String, String>> entries = sortedMap.entrySet();
        boolean hasParam = false;
        StringBuilder sb = new StringBuilder(methodName);
        sb.append("?");
        Iterator var6 = entries.iterator();

        while (var6.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var6.next();
            String name = entry.getKey();
            String value = entry.getValue();
            if (SecurityStringUtil.areNotEmpty(name, value)) {
                if (hasParam) {
                    sb.append("&");
                } else {
                    hasParam = true;
                }

                sb.append(name).append("=").append(value);
            }
        }

        return sb.toString();
    }

    /**
     * <p>
     * signCheck
     * <p/>
     *
     * @param request
     * @param signStr
     * @return T
     * @Date 2023/3/8 13:29
     */
    public <T> T signCheck(BaseRequest<T> request, String signStr) {
        if (StringUtils.isBlank(signStr)) {
            throw new RuntimeException("sign check fail, signStr param is null");
        }

        //check api public key legal
        CheckSignUtil.checkApiPublicKeyLegal(this.apiPublicKey, request.getApiGwPublicKey());

        SmileHashMap params = new SmileHashMap();
        String strToSign = this.prepareParamStr(request, params);
        logger.info("signCheck strToSign: {}", strToSign);

        int indexOfSignStart = SmileSignatureUtil.SIGN_PREFIX.length();
        String sign = signStr.substring(indexOfSignStart);
        boolean passed = SmileSignatureUtil.verifySign(strToSign, this.signType, this.publicKey, this.charset, sign);
        if (!passed) {
            logger.error("sign verify not passed, please check");
            throw new RuntimeException("sign verify not passed, please check");
        }

        if (!request.isEncryptFlag()) {
            return null;
        }

        // The signature is base64 encoded, and no comma will appear
        String startKey = StringUtils.join(SmileConstants.BIZ_CONTENT_KEY, "=");
        int indexOfStart = strToSign.lastIndexOf(startKey);
        int indexOfEnd = strToSign.lastIndexOf("&charset");
        String bizContentStr = strToSign.substring(indexOfStart, indexOfEnd).replace(startKey, "");
        String bizContentDecryptResult = AesCryptUtil.decryptContent(bizContentStr, this.encryptType, this.encryptKey, this.charset);
        return (T) JSON.parse(bizContentDecryptResult);
    }

}
