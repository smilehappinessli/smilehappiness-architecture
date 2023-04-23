package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * biz process model table Mapper interface 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Repository
public interface BpmModelMapper extends BaseMapper<BpmModel> {

    /**
     * <p>
     * Obtain model data through model name 
     * <p/>
     *
     * @param bpmModel
     * @return cn.smilehappiness.process.model.BpmModel
     * @Date 2021/11/4 11:15
     */
    BpmModel queryBpmModelByBpmModel(@Param("bpmModel") String bpmModel);

    /**
     * <p>
     * Obtain the number of models through the business ID 
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/4 11:15
     */
    Integer countBpmModelByBizId(@Param("bizId") String bizId);

    /**
     * <p>
     * Obtain model data through business ID 
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/4 11:15
     */
    BpmModel queryBpmModelByBizId(@Param("bizId") String bizId);

}
