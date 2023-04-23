package cn.smilehappiness.process.model;

import cn.smilehappiness.process.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2021-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BpmProcessDataTemplateobject ", description = "")
public class BpmProcessDataTemplate extends BaseVo<BpmProcessDataTemplate> {

    private static final long serialVersionUID = 927014226268146077L;

    @ApiModelProperty(value = "Process model ")
    private String bpmModel;

    @ApiModelProperty(value = "Field name ")
    private String fieldName;

    @ApiModelProperty(value = "Field type (String,Integer,Double)")
    private String fieldType;

    @ApiModelProperty(value = "Default ")
    private String defaultValue;

    private String udf1;

    private String udf2;

    private String udf3;

}
