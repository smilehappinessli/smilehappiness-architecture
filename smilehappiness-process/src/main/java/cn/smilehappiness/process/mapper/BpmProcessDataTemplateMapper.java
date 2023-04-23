package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmProcessDataTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper Interface 
 * </p>
 *
 * @author
 * @since 2021-11-14
 */
@Repository
public interface BpmProcessDataTemplateMapper extends BaseMapper<BpmProcessDataTemplate> {

    /**
     * <p>
     * Get the list of process data templates 
     * <p/>
     *
     * @param bizId
     * @param bpmModel
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessDataTemplate>
     * @Date 2021/12/19 18:45
     */
    @Select("SELECT field_name,field_type,default_value,remark,created_by from bpm_process_data_template where is_delete = 0 and bpm_model = #{bpmModel} and #{bizId} not in (select r.biz_id from bpm_process_data_record r where r.biz_id = #{bizId} and r.is_delete = 0)")
    List<BpmProcessDataTemplate> getBpmProcessDataTemplate(@Param("bizId") String bizId, @Param("bpmModel") String bpmModel);
}
