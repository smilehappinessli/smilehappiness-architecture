package cn.smilehappiness.process.vo;


import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Base class 
 * <p/>
 *
 * @author
 * @Date 2021/9/27 10:10
 */
public class BaseVo<T> implements Serializable {

    //Specify the primary key generation strategy to use the snowflake algorithm (default strategy ）
    //The snowflake algorithm (snowflake) is a distributed ID generation algorithm open source on Weibo. Its core idea is to use a 64-bit long number as the global unique ID. It is widely used in distributed systems, and the ID introduces a timestamp, which basically keeps self-increasing 。
    @TableId(type = IdType.ASSIGN_ID)
//    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Creation time 
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * Creator format account name/login user name 
     */
    private String createdBy;

    /**
     * Modification time 
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedTime;

    /**
     * Modifier format account name/login user name 
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;

    private Date deleteTime;

    /**
     * Deletor format account name/login user name 
     */
    private String deleteBy;

    @TableLogic
    private boolean isDelete;

    /**
     * Optimistic lock version number 
     */
    private String version;

    /**
     * remarks 
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
