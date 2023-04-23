package cn.smilehappiness.exception.enums;

/**
 * <p>
 * Business exception enumeration class definition 
 * <p/>
 *
 * @author
 * @Date 2021/8/30 17:45
 */
public enum BusinessExceptionEnumExample implements BaseExceptionEnum<String> {

    /**
     * The user information obtained is empty 
     */
    USER_INFO_NULL("1", "#USER_INFO_NULL#", "The user information obtained is empty ");

    BusinessExceptionEnumExample(String code, String bizCode, String message) {
        this.code = code;
        this.bizCode = bizCode;
        this.message = message;
    }

    private final String code;
    private final String bizCode;
    private final String message;

    /**
     * <p>
     * Get code value 
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/2 16:42
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * <p>
     * Obtain the business code value, and then make information comparison based on the business code 
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/2 16:42
     */
    @Override
    public String getBizCode() {
        return bizCode;
    }

    /**
     * <p>
     * Get business description information 
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2021/10/2 16:43
     */
    @Override
    public String getMessage() {
        return message;
    }
}
