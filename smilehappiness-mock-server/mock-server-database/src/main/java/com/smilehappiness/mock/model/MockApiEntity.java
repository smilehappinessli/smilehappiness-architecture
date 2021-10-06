package com.smilehappiness.mock.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author smilehappiness
 * @date 2021-01-13 10:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mock_api")
public class MockApiEntity implements Serializable {

    /**
     * id
     **/
    private Integer id;


    /**
     * 请求url
     **/
    private String url;


    /**
     * 请求url
     **/
    private String desc;


    /**
     * 返回内容
     **/
    private String responseBody;


    /**
     * 变量集
     **/
    private String varList;


    /**
     * 规则集合
     **/
    private String ruleList;


    /**
     * sort
     **/
    private Integer sort;


    /**
     * remark
     **/
    private String remark;


    /**
     * createBy
     **/
    private String createBy;


    /**
     * createTime
     **/
    private Timestamp createTime;


    /**
     * modifyBy
     **/
    private String modifyBy;


    /**
     * modifyTime
     **/
    private Timestamp modifyTime;


    /**
     * isDelete
     **/
    private Integer isDelete;


    /**
     * deleteBy
     **/
    private String deleteBy;


    /**
     * deleteTime
     **/
    private Timestamp deleteTime;


    /**
     * isCancel
     **/
    private Integer isCancel;


    /**
     * version
     **/
    private Integer version;


    /**
     * udf1
     **/
    private String udf1;


    /**
     * udf2
     **/
    private String udf2;


    /**
     * udf3
     **/
    private String udf3;


    /**
     * udf4
     **/
    private String udf4;


    /**
     * udf5
     **/
    private String udf5;


    /**
     * udf6
     **/
    private String udf6;

    private Map<String, String> varMap;

}
