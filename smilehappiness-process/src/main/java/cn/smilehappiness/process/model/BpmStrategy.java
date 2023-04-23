package cn.smilehappiness.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import cn.smilehappiness.process.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * biz process strategy table 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BpmStrategyObject ", description=" biz process strategy table ")
public class BpmStrategy extends BaseVo<BpmStrategy> {

    private static final long serialVersionUID = 7219881958687867260L;

    @ApiModelProperty(value = "Business type ")
    private String bizCode;

    @ApiModelProperty(value = "Policy name ")
    private String strategyName;

    @ApiModelProperty(value = "strategy bean")
    private String strategyBean;

    @ApiModelProperty(value = "Policy configuration ")
    private String optionConfig;

    @ApiModelProperty(value = "Weight value, different weights take different strategies ")
    private Integer weight;

    @ApiModelProperty(value = "Process model ")
    private String bpmModel;

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
