package cn.smilehappiness.security.config;

import cn.smilehappiness.security.constant.SmileConstants;
import cn.smilehappiness.security.dto.SimpleSignRequest;
import cn.smilehappiness.security.utils.CheckSignUtil;
import cn.smilehappiness.security.utils.OkHttpHelper;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Three-party interactive signature tool class
 * <p/>
 *
 * @author
 * @Date 2023/3/10 10:31
 */
@Component
public class SimpleSignComponent {

    @Autowired
    private SmileSimpleSecurityConfig smileSimpleSecurityConfig;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSignComponent.class);

    /**
     * <p>
     * sign Signature generation - time error is within 3 minutes
     * <p/>
     *
     * @param simpleSignRequest
     * @return java.util.SortedMap<java.lang.String, java.lang.String>
     * @Date 2023/3/15 15:09
     */
    public SortedMap<String, String> addSign(SimpleSignRequest<Map<String, Object>> simpleSignRequest) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put(SmileConstants.METHOD_NAME, simpleSignRequest.getMethodName());
        params.put(SmileConstants.REQUEST_METHOD, StringUtils.lowerCase(simpleSignRequest.getRequestMethod()));
        params.put(SmileConstants.MERCHANT_NO, simpleSignRequest.getMerchantNo());

        try {
            // Get the timestamp, where you can achieve a higher level of control over the time dimension
            DateFormat df = new SimpleDateFormat(SmileConstants.YYYY_MM_DD);
            df.setTimeZone(TimeZone.getTimeZone(SmileConstants.DATE_TIMEZONE));
            //time error is within 3 minutes, gateway control,this only 1d validity
            params.put(SmileConstants.TIMESTAMP, df.format(new Date(simpleSignRequest.getSignTimestamp())));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        params.put(SmileConstants.APP_ID, simpleSignRequest.getAppId());
        params.put(SmileConstants.MSG_ID, simpleSignRequest.getMsgId());
        params.put(SmileConstants.API_GW_PUBLIC_KEY, simpleSignRequest.getApiGwPublicKey());

        StringBuilder sb = new StringBuilder();
        sb.append("secret").append(smileSimpleSecurityConfig.getAesKey());
        params.forEach((key, value) -> sb.append(key).append(value));
        sb.append(smileSimpleSecurityConfig.getAesKey()).append("secret");

        LOG.info(" Interface request header parameter information before：{} ", JSON.toJSONString(sb));
        String encodeSignStr = DigestUtils.md5Hex(sb.toString().toUpperCase());
        //summary 32 length
        params.put(SmileConstants.REQUEST_SIGN, encodeSignStr);
        LOG.info(" Interface request header parameter information：{} ", JSON.toJSONString(params));
        return params;
    }

    /**
     * <p>
     * sign check
     * <p/>
     *
     * @param request
     * @param generateSign
     * @return T
     * @Date 2023/3/15 15:34
     */
    public <T> T signCheck(SimpleSignRequest<T> request, String generateSign) {
        if (StringUtils.isBlank(request.getRequestSign()) || StringUtils.isBlank(generateSign)) {
            throw new RuntimeException("simple sign check fail, requestSign or generateSign param is null");
        }

        //check api public key legal
        CheckSignUtil.checkApiPublicKeyLegal(smileSimpleSecurityConfig.getApiGwPublicKey(), request.getApiGwPublicKey());

        LOG.info("simple signCheck, methodName:{}, requestSign:{}, generateSign:{}", request.getMethodName(), request.getRequestSign(), generateSign);
        boolean passed = StringUtils.equals(request.getRequestSign(), generateSign);
        if (!passed) {
            LOG.error("simple sign verify not passed, please check");
            throw new RuntimeException("simple sign verify not passed, please check");
        }

        return null;
    }

    /**
     * <p>
     * getRequestResult
     * <p/>
     *
     * @param simpleSignRequest
     * @param param
     * @param url
     * @param returnResult
     * @return V
     * @Date 2023/3/15 15:11
     */
    public <K, V> V getRequestResult(SimpleSignRequest<Map<String, Object>> simpleSignRequest, K param, String url, V returnResult) {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException(" Request address cannot be empty！");
        }

        LOG.info(" Interface request parameter information：{} ", JSON.toJSONString(param));

        Map<String, String> serveHeadersInfo = addSign(simpleSignRequest);

        String result = OkHttpHelper.postJson(url, serveHeadersInfo, param);
        return (V) JSON.parseObject(result, returnResult.getClass());
    }

}
