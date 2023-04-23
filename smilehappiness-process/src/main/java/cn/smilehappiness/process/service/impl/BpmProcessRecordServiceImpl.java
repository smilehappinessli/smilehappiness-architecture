package cn.smilehappiness.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smilehappiness.exception.exceptions.BusinessException;
import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.core.engine.IEngineLifeCycleService;
import cn.smilehappiness.process.core.process.INodeProcessService;
import cn.smilehappiness.process.dto.ProcessNodeResponse;
import cn.smilehappiness.process.enums.ProcessExceptionEnum;
import cn.smilehappiness.process.mapper.BpmProcessRecordMapper;
import cn.smilehappiness.process.model.BpmProcessRecord;
import cn.smilehappiness.process.service.BpmProcessRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Detailed record service implementation class of biz process node 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Slf4j
@Service
public class BpmProcessRecordServiceImpl extends ServiceImpl<BpmProcessRecordMapper, BpmProcessRecord> implements BpmProcessRecordService {

    /**
     * <p>
     * Obtain the bpm process process list through bizId 
     * <p/>
     *
     * @param bizId
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessRecord>
     * @Date 2021/11/4 15:39
     */
    @Override
    public List<BpmProcessRecord> queryBpmProcessRecord(String bizId) {
        return baseMapper.queryBpmProcessRecord(bizId);
    }

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
    @Override
    public void nodePrepReady(String prepMessage, String processCode, String bizId) {
        baseMapper.updatePrepStatus(ProcessConstants.PREP_READY, ProcessConstants.BPM_RESULT_PROCESS, prepMessage, processCode, bizId);
    }

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
    @Override
    public void nodePrepUnready(String prepMessage, String processCode, String bizId) {
        baseMapper.updatePrepStatus(ProcessConstants.PREP_UNREADY, ProcessConstants.BPM_RESULT_PROCESS, prepMessage, processCode, bizId);
    }

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
    @Override
    public void nodeProcessing(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId) {
        String message = processNodeResponse.getMessage();
        processService.nodeProcessingCall(processNodeResponse);
        baseMapper.updateProcessNormalStatus(ProcessConstants.BPM_RESULT_PROCESS, message, processNodeResponse.getInvokeRecord(), processCode, bizId);
    }

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
    @Override
    public void nodePass(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId) {
        String message = processNodeResponse.getMessage();
        processService.nodePassCall(processNodeResponse);
        baseMapper.updateProcessPassStatus(ProcessConstants.BPM_RESULT_PASS, message, processNodeResponse.getInvokeRecord(), processCode, bizId);
    }

    /**
     * <p>
     * Process node execution passed 
     * <p/>
     *
     * @param message
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/9 20:16
     */
    @Override
    public void nodePass(String message, String processCode, String bizId) {
        baseMapper.updateProcessPassStatus(ProcessConstants.BPM_RESULT_PASS, message, null, processCode, bizId);
    }

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
    @Override
    public void nodeReject(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId) {
        String message = processNodeResponse.getMessage();
        // Node rejected callback 
        processService.nodeRejectCall(processNodeResponse);
        baseMapper.updateProcessNormalStatus(ProcessConstants.BPM_RESULT_REJECT, message, processNodeResponse.getInvokeRecord(), processCode, bizId);
    }

    /**
     * <p>
     * Process node execution rejection 
     * <p/>
     *
     * @param message
     * @param processCode
     * @param bizId
     * @return void
     * @Date 2021/11/9 20:17
     */
    @Override
    public void nodeReject(String message, String processCode, String bizId) {
        baseMapper.updateProcessNormalStatus(ProcessConstants.BPM_RESULT_REJECT, message, null, processCode, bizId);
    }

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
    @Override
    public void nodeBizException(INodeProcessService processService, ProcessNodeResponse processNodeResponse, String processCode, String bizId) {
        String message = processNodeResponse.getMessage();
        /** Node business exception callback  **/
        processService.nodeBizExceptionCall(processNodeResponse);
        baseMapper.updateProcessNormalStatus(ProcessConstants.BPM_RESULT_BREAK, message, processNodeResponse.getInvokeRecord(), processCode, bizId);
    }

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
    @Override
    public void nodeException(IEngineLifeCycleService engineLifeCycleService, Exception e, String processCode, String bizId) {
        log.error("Node exception - bizId - {} processCode - {}, exception reason ：", bizId, processCode, e);
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(e.getMessage()).append("\n");
        for (StackTraceElement err : e.getStackTrace()) {
            errorMessage.append("\tat " + err.toString() + "\n");
        }

        // Engine and exception callback 
        engineLifeCycleService.processExceptionCallBack(e, ProcessConstants.SYS_EXCEPTION);

        /** Unknown exception , process */
        baseMapper.updateProcessErrorStatus(ProcessConstants.BPM_RESULT_PROCESS, errorMessage.toString(), processCode, bizId);
    }

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
    @Override
    public void processNodeSkipOrBack(String bizId, String processCode, String processStatus, String message) {
        List<BpmProcessRecord> bpmProcessList = this.queryBpmProcessRecord(bizId);
        if (CollectionUtils.isEmpty(bpmProcessList)) {
            throw new BusinessException(ProcessExceptionEnum.PROCESS_RECORD_DATA_NULL);
        }

        bpmProcessList = bpmProcessList.stream().filter(item -> processCode.equals(item.getProcessCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bpmProcessList)) {
            throw new BusinessException(ProcessExceptionEnum.PROCESS_RECORD_DATA_NULL);
        }

        BpmProcessRecord bpmProcessRecord = bpmProcessList.get(0);
        bpmProcessRecord.setMessage(message);
        bpmProcessRecord.setProcessStatus(processStatus);
        log.info("processNodeSkipOrBack update option params:{}", JSON.toJSONString(bpmProcessRecord));
        baseMapper.updateById(bpmProcessRecord);
    }

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
    @Override
    public void processNodeBack(String bizId, Integer sourceSort, Integer targetSort, String processStatus, String message) {
        List<BpmProcessRecord> bpmProcessList = this.queryBpmProcessRecord(bizId);
        if (CollectionUtils.isEmpty(bpmProcessList)) {
            throw new BusinessException(ProcessExceptionEnum.PROCESS_RECORD_DATA_NULL);
        }

        bpmProcessList = bpmProcessList.stream().filter(item -> item.getSort() >= targetSort && item.getSort() <= sourceSort).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bpmProcessList)) {
            throw new BusinessException(ProcessExceptionEnum.PROCESS_RECORD_DATA_NULL);
        }

        for (BpmProcessRecord bpmProcessRecord : bpmProcessList) {
            bpmProcessRecord.setMessage(message);
            bpmProcessRecord.setProcessStatus(processStatus);
        }

        log.info("processNodeBack update option params:{}", JSON.toJSONString(bpmProcessList));
        this.updateBatchById(bpmProcessList);
    }

}
