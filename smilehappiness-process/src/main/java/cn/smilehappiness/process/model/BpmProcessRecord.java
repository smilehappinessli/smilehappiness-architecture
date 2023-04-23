package cn.smilehappiness.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import cn.smilehappiness.process.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Detailed record of biz process node 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BpmProcessRecordObject ", description=" Detailed record of biz process node ")
public class BpmProcessRecord extends BaseVo<BpmProcessRecord> {

    private static final long serialVersionUID = 955641808161963511L;

    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "business id ")
    private String bizId;

    @ApiModelProperty(value = "Preparation (Y/N)")
    private String isPrep;

    @ApiModelProperty(value = "Process code ")
    private String processCode;

    @ApiModelProperty(value = "technological process bean")
    private String processBean;

    @ApiModelProperty(value = "to configure ")
    private String optionConfig;

    @ApiModelProperty(value = "Process status (init,process,pass,reject)")
    private String processStatus;

    @ApiModelProperty(value = "Process record ")
    private String invokeRecord;

    @ApiModelProperty(value = "Supplementary information ")
    private String message;

    @ApiModelProperty(value = "Capture exception information ")
    private String errorMessage;

    @ApiModelProperty(value = "Node execution order ")
    private Integer sort;

    @TableField("udf_1")
    @ApiModelProperty(value = "Extended field ")
    private String udf1;

    @TableField("udf_2")
    @ApiModelProperty(value = "Extended field ")
    private String udf2;

    @TableField("udf_3")
    @ApiModelProperty(value = "Extended field ")
    private String udf3;

}
