package cn.smilehappiness.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import cn.smilehappiness.process.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * biz process node template 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BpmProcessTemplateObject ", description=" biz process node template ")
public class BpmProcessTemplate extends BaseVo<BpmProcessTemplate> {

    private static final long serialVersionUID = -6857395626033261206L;

    @ApiModelProperty(value = "Process model ")
    private String bpmModel;

    @ApiModelProperty(value = "Process code ")
    private String processCode;

    @ApiModelProperty(value = "technological process bean")
    private String processBean;

    @ApiModelProperty(value = "Preparation (Y/N)")
    private String isPrep;

    @ApiModelProperty(value = "configuration information ")
    private String optionConfig;

    @ApiModelProperty(value = "Node execution order ")
    private Integer sort;

    @ApiModelProperty(value = "Effective or not (Y/N)")
    private String isValid;

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
