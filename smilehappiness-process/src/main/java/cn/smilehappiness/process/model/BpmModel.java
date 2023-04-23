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
 * biz process model table 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BpmModelObject ", description=" biz process model table ")
public class BpmModel extends BaseVo<BpmModel> {

    private static final long serialVersionUID = -2696002483439288399L;

    @ApiModelProperty(value = "Process model ")
    private String bpmModel;

    @ApiModelProperty(value = "Online time ")
    private Date onlineTime;

    @ApiModelProperty(value = "Process configuration (synchronous/asynchronous/callback  )")
    private String bpmConfig;

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
