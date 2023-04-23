package cn.smilehappiness.aspect.operate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Basic information of operation log 
 * <p/>
 *
 * @author
 * @Date 2021/8/28 14:48
 */
@Data
public class OperateLogBaseInfo implements Serializable {

    private static final long serialVersionUID = 43493492604994525L;

    /**
     * user id
     */
    private String userId;

    /**
     * user name 
     */
    private String userName;

    /**
     * request url
     */
    private String requestUrl;

    /**
     * Request method name 
     */
    private String methodName;

    /**
     * Request class name 
     */
    private String className;

    /**
     * Request method 
     */
    private String requestType;

    /**
     * business id
     */
    private String bizId;

    /**
     * Requested business module name 
     */
    private String businessModuleName;

    /**
     * visit ip
     */
    private String requestIp;

    /**
     * Request operation description 
     */
    private String operationDescribe;

    /**
     * Request parameters 
     */
    private String requestParams;

    /**
     * Response results 
     */
    private String responseStr;

    /**
     * Interface call time: unit: ms 
     */
    private Long operationTakeTime;

    /**
     * Record time 
     */
    private Date createTime;

    /**
     * Modification time 
     */
    private Date modifyTime;

    /**
     * Execution error message 
     */
    private String errorMessage;

    /**
     * Reserved field 
     */
    private String remark;

}
