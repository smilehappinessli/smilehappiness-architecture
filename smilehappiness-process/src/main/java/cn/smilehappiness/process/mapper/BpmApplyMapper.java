package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmApply;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper interface of biz application form 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Repository
public interface BpmApplyMapper extends BaseMapper<BpmApply> {

    /**
     * <p>
     * Get the application information at the time of incoming through bizId 
     * <p/>
     *
     * @param bizId
     * @return cn.smilehappiness.process.model.BpmApply
     * @Date 2021/11/4 12:01
     */
    BpmApply queryBpmApplyByBizId(@Param("bizId") String bizId);

    /**
     * <p>
     * Obtain the number of application records through bizId, generally one 
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/5 14:07
     */
    Integer countBpmApplyByBizId(@Param("bizId") String bizId);

    /**
     * <p>
     * Add process application record 
     * <p/>
     *
     * @param udf1
     * @param userId
     * @param bizId
     * @param bizCode
     * @param bizParam
     * @param processStatus
     * @param notifySuccess
     * @return void
     * @Date 2021/11/5 14:09
     */
    void addBpmApply(@Param("id") Long id, @Param("udf1") Long udf1, @Param("userId") Long userId, @Param("bizId") String bizId, @Param("bizCode") String bizCode, @Param("bizParam") String bizParam, @Param("processStatus") String processStatus, @Param("notifySuccess") String notifySuccess);

    /**
     * <p>
     * Get the data that needs to execute the policy 
     * <p/>
     *
     * @param
     * @return java.util.List<cn.smilehappiness.process.model.BpmApply>
     * @Date 2021/11/4 10:42
     */
    List<BpmApply> queryStrategyBpmApplyList();

    /**
     * <p>
     * Obtain the data to be processed by the process engine 
     * <p/>
     *
     * @param processStatus
     * @param bizCode
     * @return java.util.List<cn.smilehappiness.process.model.BpmApply>
     * @Date 2021/11/4 10:42
     */
    List<BpmApply> queryProcessingBpmApplyList(@Param("processStatus") String processStatus, @Param("bizCode") String bizCode);

    /**
     * <p>
     * Get data without callback 
     * <p/>
     *
     * @param
     * @return java.util.List<cn.smilehappiness.process.model.BpmApply>
     * @Date 2021/11/4 10:42
     */
    List<BpmApply> queryNotifyBpmApplyList();

    /**
     * <p>
     * Process status changed to - process template initialized  (1->2)
     * <p/>
     *
     * @param processStatus
     * @param bpmModel
     * @param bizId
     * @param bpmResult
     */
    void updateStrategyToProcess(@Param("processStatus") String processStatus, @Param("bpmModel") String bpmModel, @Param("bizId") String bizId, @Param("bpmResult") String bpmResult);

    /**
     * <p>
     * Update notification results 
     * <p/>
     *
     * @param notifySuccess
     * @param bizId
     * @return void
     * @Date 2021/11/4 10:32
     */
    void updateBpmApplyNotifyResult(@Param("notifySuccess") String notifySuccess, @Param("bizId") String bizId);

    /**
     * <p>
     * Process application passed 
     * <p/>
     *
     * @param processStatus
     * @param bpmResult
     * @param bizId
     * @param message
     * @return void
     * @Date 2021/11/4 14:34
     */
    void updateBpmApplyPass(@Param("processStatus") String processStatus, @Param("bpmResult") String bpmResult, @Param("bizId") String bizId, @Param("message") String message);

    /**
     * <p>
     * Process application rejected 
     * <p/>
     *
     * @param processStatus
     * @param bpmResult
     * @param bizId
     * @param message
     * @return void
     * @Date 2021/11/4 14:53
     */
    void updateBpmApplyReject(@Param("processStatus") String processStatus, @Param("bpmResult") String bpmResult, @Param("bizId") String bizId, @Param("message") String message);


    /**
     * <p>
     * Update process application status 
     * <p/>
     *
     * @param processStatus
     * @param bizId
     * @param message
     * @param bpmResult
     * @return void
     * @Date 2021/11/4 15:22
     */
    void updateBpmApplyProcessStatus(@Param("processStatus") String processStatus, @Param("bizId") String bizId, @Param("message") String message, @Param("bpmResult") String bpmResult);

    /**
     * <p>
     * Modify process status 
     * <p/>
     *
     * @param processStatus
     * @param bpmResult
     * @param bizIds
     * @param message
     * @param processStatusCondition
     * @return void
     * @Date 2022/5/28 16:51
     */
    void updateBpmApplyByStatus(@Param("processStatus") String processStatus, @Param("bpmResult") String bpmResult, @Param("bizIds") List<String> bizIds, @Param("message") String message, @Param("processStatusCondition") List<String> processStatusCondition);

    /**
     * <p>
     * Acquire lock - based on version
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/4 17:11
     */
    Integer applyLock(@Param("bizId") String bizId);

    /**
     * <p>
     * Release lock - based on version
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/4 17:12
     */
    Integer applyUnlock(@Param("bizId") String bizId);

}
