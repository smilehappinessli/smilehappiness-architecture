package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmProcessRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Detailed record mapper interface of biz process node 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Repository
public interface BpmProcessRecordMapper extends BaseMapper<BpmProcessRecord> {

    /**
     * <p>
     * Obtain the bpm process process list through bizId 
     * <p/>
     *
     * @param bizId
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessRecord>
     * @Date 2021/11/4 15:39
     */
    List<BpmProcessRecord> queryBpmProcessRecord(@Param("bizId") String bizId);

    /**
     * <p>
     * Obtain the number of process records through the business ID (incoming ID) 
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/14 14:10
     */
    Integer countProcessRecordByBizId(@Param("bizId") String bizId);

    /**
     * <p>
     * Initialize process data through process template 
     * <p/>
     *
     * @param bpmProcessRecordList
     * @return void
     * @Date 2021/11/14 14:10
     */
    void initProcessTemplate(@Param("bpmProcessRecordList") List<BpmProcessRecord> bpmProcessRecordList);

    /**
     * <p>
     * Update front node status 
     * <p/>
     *
     * @param isPrep
     * @param processStatus
     * @param message
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 15:48
     */
    void updatePrepStatus(@Param("isPrep") String isPrep, @Param("processStatus") String processStatus, @Param("message") String message, @Param("processCode") String processCode, @Param("bizId") String bizId);

    /**
     * <p>
     * Update process status 
     * <p/>
     *
     * @param processStatus
     * @param message
     * @param invokeRecord
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 16:15
     */
    void updateProcessNormalStatus(@Param("processStatus") String processStatus, @Param("message") String message, @Param("invokeRecord") String invokeRecord, @Param("processCode") String processCode, @Param("bizId") String bizId);

    /**
     * <p>
     * Update process status --pass
     * <p/>
     *
     * @param processStatus
     * @param message
     * @param invokeRecord
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 16:15
     */
    void updateProcessPassStatus(@Param("processStatus") String processStatus, @Param("message") String message, @Param("invokeRecord") String invokeRecord, @Param("processCode") String processCode, @Param("bizId") String bizId);

    /**
     * <p>
     * Update exception information 
     * <p/>
     *
     * @param processStatus
     * @param errorMessage
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 16:21
     */
    void updateProcessErrorStatus(@Param("processStatus") String processStatus, @Param("errorMessage") String errorMessage, @Param("processCode") String processCode, @Param("bizId") String bizId);

}
