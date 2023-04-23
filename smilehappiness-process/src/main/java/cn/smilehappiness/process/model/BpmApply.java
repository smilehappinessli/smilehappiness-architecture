package cn.smilehappiness.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import cn.smilehappiness.process.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * biz Application Form 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BpmApplyObject ", description=" biz Application Form ")
public class BpmApply extends BaseVo<BpmApply> {

    private static final long serialVersionUID = 6806211099100200357L;

    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "Business ID (unique )")
    private String bizId;

    @ApiModelProperty(value = "Business type ")
    private String bizCode;

    @ApiModelProperty(value = "Original business parameters ")
    private String bizParam;

    @ApiModelProperty(value = "Process model ")
    private String bpmModel;

    @ApiModelProperty(value = "Submission time ")
    private Date bpmApplyTime;

    @ApiModelProperty(value = "Completion time ")
    private Date bpmCommitTime;

    @ApiModelProperty(value = "Process status (1,2,3,4)")
    private String processStatus;

    @ApiModelProperty(value = "Original process results (pass,reject)")
    private String oriBpmResult;

    @ApiModelProperty(value = "External process results (pass,reject)")
    private String outBpmResult;

    @ApiModelProperty(value = "Recall the status, and notify the business system after the application is passed, such as sending text messages and notifying clearing settlement  (Y/N)")
    private String notifyStatus;

    @ApiModelProperty(value = "Business information ")
    private String message;

    @TableField("udf_1")
    @ApiModelProperty(value = "Extension field. The first phase is assigned the orderApply order id")
    private String udf1;

    @TableField("udf_2")
    @ApiModelProperty(value = "Extended field ")
    private String udf2;

    @TableField("udf_3")
    @ApiModelProperty(value = "Extended field ")
    private String udf3;

}
