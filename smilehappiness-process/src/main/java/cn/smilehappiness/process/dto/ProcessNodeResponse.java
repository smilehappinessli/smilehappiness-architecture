package cn.smilehappiness.process.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Process return results 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 14:18
 */
@Data
public class ProcessNodeResponse<T> implements Serializable {

    private static final long serialVersionUID = -4417205750532597176L;

    /**
     * Process response status - passed  200
     */
    private static final String CODE_PASS = "200";
    /**
     * Process response status - in process  203
     */
    private static final String CODE_PROCESSING = "203";
    /**
     * Process Response Status - Rejected  300
     */
    private static final String CODE_REJECT = "300";
    /**
     * Process response status - business exception  500
     */
    private static final String CODE_BIZ_ERROR = "500";

    /**
     * Returned process response status value 
     */
    private String code;

    /**
     * Process information 
     */
    private String message;

    /**
     * Implementation information 
     */
    private String invokeRecord;

    /**
     * adopt 
     */
    private boolean isPass = false;

    /**
     * Processing 
     */
    private boolean isProcess = false;

    /**
     * refuse 
     */
    private boolean isReject = false;

    /**
     * Business exception 
     */
    private boolean isBizException = false;

    /**
     * Business data 
     */
    private T bizData;


    public ProcessNodeResponse<T> nodePass(T bizData) {
        this.code = CODE_PASS;
        this.isPass = true;
        this.message = "adopt ";
        this.bizData = bizData;
        return this;
    }

    public ProcessNodeResponse<T> nodePass(String message, T bizData) {
        this.code = CODE_PASS;
        this.isPass = true;
        this.message = "adopt ";
        this.bizData = bizData;
        return this;
    }

    public ProcessNodeResponse<T> nodeProcessing(String message, T bizData) {
        this.code = CODE_PROCESSING;
        this.isProcess = true;
        this.message = message;
        return this;
    }

    public ProcessNodeResponse<T> nodeReject(String message, T bizData) {
        this.message = message;
        this.isReject = true;
        return this;
    }

    public ProcessNodeResponse<T> nodeBizError(String message, T bizData) {
        this.isBizException = true;
        this.message = message;
        return this;
    }


}
