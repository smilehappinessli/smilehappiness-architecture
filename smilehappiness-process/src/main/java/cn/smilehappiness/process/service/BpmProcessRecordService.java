package cn.smilehappiness.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smilehappiness.process.core.engine.IEngineLifeCycleService;
import cn.smilehappiness.process.core.process.INodeProcessService;
import cn.smilehappiness.process.dto.ProcessNodeResponse;
import cn.smilehappiness.process.model.BpmProcessRecord;

import java.util.List;

/**
 * <p>
 * Detailed record of service class in biz process node 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
public interface BpmProcessRecordService extends IService<BpmProcessRecord> {

    /**
     * <p>
     * Obtain the bpm process process list through bizId 
     * <p/>
     *
     * @param bizId
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessRecord>
     * @Date 2021/11/4 15:39
     */
    List<BpmProcessRecord> queryBpmProcessRecord(String bizId);

    /**
     * <p>
     * Preparation completed 
     * <p/>
     *
     * @param prepMessage
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 15:43
     */
    void nodePrepReady(String prepMessage, String processCode, String bizId);

    /**
     * <p>
     * Pre-preparation not completed 
     * <p/>
     *
     * @param prepMessage
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 15:46
     */
    void nodePrepUnready(String prepMessage, String processCode, String bizId);

    /**
     * <p>
     * Process node is being implemented 
     * <p/>
     *
     * @param processService
     * @param processNodeResponse
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 16:52
     */
    void nodeProcessing(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId);

    /**
     * <p>
     * Process node execution passed 
     * <p/>
     *
     * @param processService
     * @param processNodeResponse
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 16:53
     */
    void nodePass(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId);

    /**
     * <p>
     * Process node execution passed 
     * <p/>
     *
     * @param message
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/9 20:14
     */
    void nodePass(String message, String processCode, String bizId);

    /**
     * <p>
     * Process node execution rejection 
     * <p/>
     *
     * @param processService
     * @param processNodeResponse
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 16:53
     */
    void nodeReject(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId);

    /**
     * <p>
     * Process node execution rejection 
     * <p/>
     *
     * @param message
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/9 20:14
     */
    void nodeReject(String message, String processCode, String bizId);

    /**
     * <p>
     * Business exception  -break
     * <p/>
     *
     * @param processService
     * @param processNodeResponse
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 17:28
     */
    void nodeBizException(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId);

    /**
     * <p>
     * System-level exception  throws
     * <p/>
     *
     * @param engineLifeCycleService
     * @param e
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/4 17:29
     */
    void nodeException(IEngineLifeCycleService engineLifeCycleService, Exception e, String processCode, String bizId);

    /**
     * <p>
     * Process node processing (fallback, pass ）
     * <p/>
     *
     * @param bizId
     * @param processCode
     * @param processStatus
     * @param message
     * @return void
     * @Date 2022/12/13 10:30
     */
    void processNodeSkipOrBack(String bizId, String processCode, String processStatus, String message);

    /**
     * <p>
     * process node back(sourceSort >> targetSort  high->lower)
     * <p/>
     *
     * @param bizId
     * @param sourceSort
     * @param targetSort
     * @param processStatus
     * @param message
     * @return void
     * @Date 2022/12/15 11:18
     */
    void processNodeBack(String bizId, Integer sourceSort, Integer targetSort, String processStatus, String message);

}
