package cn.smilehappiness.process.enums;

import cn.smilehappiness.exception.enums.BaseExceptionEnum;

/**
 * <p>
 * Business exception enumeration class definition 
 * <p/>
 *
 * @author
 * @Date 2021/10/23 16:30
 */
public enum ProcessExceptionEnum implements BaseExceptionEnum<String> {

    /**
     * Order number cannot be empty 
     */
    ORDER_NUMBER_NULL("66666666001", "66_ORDER_NUMBER_NULL", "The order number parameter cannot be empty"),
    PROCESS_RECORD_DATA_NULL("66666666002", "PROCESS_RECORD_DATA_NULL", "process record data is null"),
    ;

    private final String code;
    private final String bizCode;
    private final String message;

    ProcessExceptionEnum(String code, String bizCode, String message) {
        this.code = code;
        this.bizCode = bizCode;
        this.message = message;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getBizCode() {
        return bizCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
