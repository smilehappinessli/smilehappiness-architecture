package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmProcessDataRecord;
import org.apache.ibatis.annotations.Param;
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
public interface BpmProcessDataRecordMapper extends BaseMapper<BpmProcessDataRecord> {


    /**
     * <p>
     * Query the data required by the process 
     * <p/>
     *
     * @param bizId
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessDataRecord>
     * @Date 2021/11/14 11:16
     */
    List<BpmProcessDataRecord> queryBpmProcessDataRecord(@Param("bizId") String bizId);

    /**
     * <p>
     * Obtain process data record information through bizId and fieldName 
     * <p/>
     *
     * @param bizId
     * @param fieldNameList
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessDataRecord>
     * @Date 2021/12/28 14:53
     */
    List<BpmProcessDataRecord> queryBpmProcessDataRecordSingle(@Param("bizId") String bizId, @Param("fieldNameList") List<String> fieldNameList);

    /**
     * <p>
     * Batch update data in the process 
     * <p/>
     *
     * @param dataRecordList
     * @return void
     * @Date 2021/11/14 11:16
     */
    void updateBpmProcessDataRecordBatch(List<BpmProcessDataRecord> dataRecordList);

    /**
     * <p>
     * Initialize data template 
     * <p/>
     *
     * @param bizId
     * @param bpmModel
     * @return void
     * @Date 2021/11/14 14:06
     */
    void initProcessDataTemplate(@Param("bizId") String bizId, @Param("bpmModel") String bpmModel);

    /**
     * <p>
     * Initialize data template -- batch operation 
     * <p/>
     *
     * @param bpmProcessDataRecordList
     * @return void
     * @Date 2021/12/20 10:55
     */
    void initProcessDataTemplateBatch(@Param("bpmProcessDataRecordList") List<BpmProcessDataRecord> bpmProcessDataRecordList);

}
