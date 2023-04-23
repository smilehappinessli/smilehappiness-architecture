package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmProcessTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * biz process node template Mapper interface 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Repository
public interface BpmProcessTemplateMapper extends BaseMapper<BpmProcessTemplate> {

    /**
     * <p>
     * Get process template data 
     * <p/>
     *
     * @param bizId
     * @param bpmModel
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessTemplate>
     * @Date 2021/12/19 18:34
     */
    @Select("SELECT process_code,is_prep,sort,process_bean,option_config,created_by from bpm_process_template where bpm_model = #{bpmModel} and is_delete = 0 and is_valid = 'Y' and #{bizId} not in (select r.biz_id from bpm_process_record r where r.biz_id = #{bizId} and r.is_delete = 0)")
    List<BpmProcessTemplate> getBpmProcessTemplate(@Param("bizId") String bizId, @Param("bpmModel") String bpmModel);

}
