package com.smilehappiness.aspect.operate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 操作日志基础信息
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/28 14:48
 */
@Data
public class OperateLogBaseInfo implements Serializable {

    private static final long serialVersionUID = 43493492604994525L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求方法名
     */
    private String methodName;

    /**
     * 请求类名
     */
    private String className;

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 请求的业务模块名称
     */
    private String businessModuleName;

    /**
     * 访问ip
     */
    private String requestIp;

    /**
     * 请求操作描述
     */
    private String operationDescribe;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应结果
     */
    private String responseStr;

    /**
     * 接口调用耗时时间：单位毫秒
     */
    private Long operationTakeTime;

    /**
     * 记录时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 执行错误信息
     */
    private String errorMessage;

    /**
     * 保留字段
     */
    private String remark;

}
