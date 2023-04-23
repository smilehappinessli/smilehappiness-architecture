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
@ApiModel(value = "BpmProcessDataRecordobject ", description = "")
public class BpmProcessDataRecord extends BaseVo<BpmProcessDataRecord> {

    private static final long serialVersionUID = 1007959644011450121L;

    @ApiModelProperty(value = "business id")
    private String bizId;

    @ApiModelProperty(value = "Field name ")
    private String fieldName;

    @ApiModelProperty(value = "Field type (String,Integer,Double)")
    private String fieldType;

    @ApiModelProperty(value = "field value ")
    private String fieldValue;

    private String udf1;

    private String udf2;

    private String udf3;


}
