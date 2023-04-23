package cn.smilehappiness.security.dto;

/**
 * <p>
 * SimpleSignRequest
 * <p/>
 *
 * @author
 * @Date 2023/3/15 13:49
 */
public class SimpleSignRequest<T> extends BaseResponse {

    private String methodName;
    private String requestMethod;
    private String merchantNo;
    private String appId;
    /**
     * check validity，time error is within 3 minutes, gateway control
     */
    private long signTimestamp;
    private String requestSign;
    private String apiGwPublicKey;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getSignTimestamp() {
        return signTimestamp;
    }

    public void setSignTimestamp(long signTimestamp) {
        this.signTimestamp = signTimestamp;
    }

    public String getRequestSign() {
        return requestSign;
    }

    public void setRequestSign(String requestSign) {
        this.requestSign = requestSign;
    }

    public String getApiGwPublicKey() {
        return apiGwPublicKey;
    }

    public void setApiGwPublicKey(String apiGwPublicKey) {
        this.apiGwPublicKey = apiGwPublicKey;
    }
}
