package cn.smilehappiness.process.core.component;

import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.cache.util.RedissonLockRedisUtil;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.core.engine.IEngineLifeCycleService;
import cn.smilehappiness.process.core.process.INodeProcessService;
import cn.smilehappiness.process.dto.PrepResponse;
import cn.smilehappiness.process.dto.ProcessNodeResponse;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import cn.smilehappiness.process.model.BpmProcessRecord;
import cn.smilehappiness.process.service.BpmProcessRecordService;
import cn.smilehappiness.process.utils.JSONUtils;
import cn.smilehappiness.utils.SpringUtil;
import cn.smilehappiness.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Process processing core services 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 10:18
 */
@Slf4j
@Component
@RefreshScope
public class DoProcessServer {

    /**
     * Process switch 
     */
    @Value("${bpm.process.canProcess:true}")
    private boolean canProcess;

    @Autowired
    private BpmProcessRecordService bpmProcessRecordService;

    @Resource
    private BpmApplyMapper bpmApplyMapper;

    @Resource
    private ModelConfig modelConfig;

    @Autowired
    private ProcessDataSupport processDataSupport;

    @Resource
    private DoNotifyServer doNotifyServer;

    @Autowired
    private RedissonLockRedisUtil redissonLockRedisUtil;

    /**
     * <p>
     * Asynchronous call 
     * <p/>
     *
     * @param bizId
     * @return void
     * @Date 2021/11/4 14:03
     */
    @Async
    public void doProcessAsync(String bizId) {
        ThreadUtil.execute(() -> this.doProcessByLock(bizId));
    }

    /**
     * <p>
     * Process execution (it is recommended to use optimistic lock+distributed lock to control uniqueness, and use idempotent processing+distributed lock+bpmApply unique index processing in Phase I) -- synchronous execution 
     * <p/>
     *
     * @param bizId
     * @return void
     * @Date 2021/11/4 17:15
     */
    public void doProcessByLock(String bizId) {
        String bizLockKey = StringUtils.join("biz:process:doProcess:", bizId);
        //It supports the expiration unlocking function. It will be automatically unlocked after 180 seconds without calling the unlock method. Of course, in order not to occupy resources, it is generally recommended to release the lock manually after handling the business with the lock 
        boolean lockFlag = redissonLockRedisUtil.tryLock(bizLockKey, 1L, 180L);
        if (lockFlag) {
            try {
                this.doProcess(bizId);
            } catch (Exception e) {
                log.error(bizId + "-Process execution exception ", e);
                throw new SystemInternalException(bizId + "Process execution failed, failure reason ：" + e.getMessage());
            } finally {
                redissonLockRedisUtil.unlock(bizLockKey);
            }
        } else {
            log.error(bizId + "-Process execution - failed to acquire distributed lock - process execution exception, please try again later ");
            throw new SystemInternalException(bizId + "-Process execution - failed to acquire distributed lock - process execution exception, please try again later ");
        }
    }

    /**
     * <p>
     * Process switch control 
     * <p/>
     *
     * @param bizId
     * @return boolean
     * @Date 2021/11/4 16:49
     */
    private boolean canProcess(String bizId) {
        if (!canProcess) {
            log.info("【{}】The process switch is not turned on ...", bizId);
            return false;
        }

        //Template initialization or processing can be repeated 
        String processStatus = bpmApplyMapper.queryBpmApplyByBizId(bizId).getProcessStatus();

        // The process template initialization strategy has just been implemented or is in process 
        if (ProcessConstants.PROCESS_STATUS_TEMPLATE.equals(processStatus)) {
            return true;
        }

        return ProcessConstants.PROCESS_STATUS_PROCESS.equals(processStatus);
    }

    /**
     * <p>
     * Process execution 
     * <p/>
     *
     * @param bizId
     * @return void
     * @Date 2021/11/5 15:50
     */
    @SuppressWarnings("all")
    public void doProcess(String bizId) {
        log.info("bizId[{}] - doEngineProcess, isProcessSwitch[{}]", bizId, canProcess);

        // Judge whether the process can be executed 
        if (!canProcess(bizId)) {
            return;
        }

        /**Get process lifecycle management **/
        IEngineLifeCycleService engineLifeCycleService = modelConfig.getEngineLifeCycle(bizId);

        /*** Check before executing the process  ***/
        if (!engineLifeCycleService.beforeProcessCallBack()) {
            /**
             *
             * engineLifeCycleServiceProcessing - Terminated before running the process - it will still be scheduled later 
             *
             * 1.There are some switches in the process - such as time period 
             *
             * 2.Due to some business reasons - temporarily terminated 
             *
             */
            engineLifeCycleServiceProcessing(bizId);
            return;
        }

        // Node end representation 
        boolean isEndpoint = false;
        // Process Rejection Status 
        boolean isReject = false;
        // Process abnormal state 
        boolean isBizException = false;

        // Process node 
        List<BpmProcessRecord> bpmProcessList = bpmProcessRecordService.queryBpmProcessRecord(bizId);
        if (CollectionUtils.isEmpty(bpmProcessList)) {
            log.warn("When executing the process template and running the process, the obtained process record is empty ！");
            engineLifeCycleService.processExceptionCallBack(ProcessConstants.BIZ_EXCEPTION);
            return;
        }

        /************Initial process *************/
        if (isFirstEndPoint(bpmProcessList)) {
            log.info("bizId[{}] - doEngineProcess - isFirstEndPoint", bizId);
            engineLifeCycleService.firstProcessCallBack();
        }

        /***********Execute process flow **********/
        for (int i = 0; i < bpmProcessList.size(); i++) {
            // Process node record 
            BpmProcessRecord processNode = bpmProcessList.get(i);
            //Process code 
            String processCode = processNode.getProcessCode();
            //technological process bean
            String processBean = processNode.getProcessBean();

            try {
                log.info("bizId【{}】 - doEngineProcess - Execute process flow, processCode [{}], current thread name ：【{}】 ", bizId, processCode, Thread.currentThread().getName());

                // As long as one node refuses, it ends directly 
                if (isProcessReject(processNode)) {
                    isEndpoint = true;
                    isReject = true;
                    break;
                }

                // Skip the node that passed the execution 
                if (isProcessFinish(processNode)) {
                    if (i == bpmProcessList.size() - 1) {
                        isEndpoint = true;
                    }
                    continue;
                }

                /**
                 *
                 * Core logic 
                 * >> Preconditions met 
                 * >> Execute process rule logic 
                 * >> Carry out relevant processing according to the results (pass/reject/in execution/business exception) 
                 *
                 */
                INodeProcessService processService = SpringUtil.getBean(processBean, INodeProcessService.class);
                //Get request parameter data and initialize process parameters 
                //Transfer the data bound in the process into the Map and transfer the parameters needed in the whole process, so as to realize data decoupling across nodes 
                Map<String, Object> processDataMap = processDataSupport.getProcessData(bizId);
                //Process initialization, initializing all required basic parameters 
                processService.initProcess(bizId, processNode.getProcessCode(), processDataMap, processNode.getOptionConfig());

                // If it is Y, it should be skipped. Do not execute it again (precondition  (2->3)）
                boolean preFlag = ProcessConstants.BPM_RESULT_NOTIFY_SUCCESS.equals(processNode.getIsPrep());
                PrepResponse prepResponse = preFlag ? new PrepResponse().prepSuccess() : processService.prep();

                // Update data - After the preparation of the front data is completed, it will be updated uniformly once (at present, the front data has not been processed, so it can not be processed here to save one IO operation ）
                //processDataSupport.saveProcessData(bizId,processDataMap);

                if (prepResponse.isPrep()) {
                    log.info("bizId[{}] - doEngineProcess - prep - {} Front satisfaction ", bizId, processCode);
                    // Preparation 
                    bpmProcessRecordService.nodePrepReady(prepResponse.getMessage(), processCode, bizId);

                    // Node execution (process )
                    ProcessNodeResponse processNodeResponse = processService.invoke();

                    // After the node is executed, update it uniformly once 
                    processDataSupport.saveProcessData(bizId, processDataMap);

                    /******** Status inspection of each node  ********/
                    // Processing (may occur in manual process scenario ）
                    if (processNodeResponse.isProcess()) {
                        // Processing 
                        bpmProcessRecordService.nodeProcessing(processService, processNodeResponse, processCode, bizId);
                        break;
                    }

                    // adopt 
                    if (processNodeResponse.isPass()) {
                        bpmProcessRecordService.nodePass(processService, processNodeResponse, processCode, bizId);
                        // The last node starts the process end identifier 
                        if (i == bpmProcessList.size() - 1) {
                            isEndpoint = true;
                        }
                        continue;
                    }

                    // Reject (terminate process )
                    if (processNodeResponse.isReject()) {
                        bpmProcessRecordService.nodeReject(processService, processNodeResponse, processCode, bizId);
                        isEndpoint = true;
                        // Reject the whole process at once Reject all 
                        isReject = true;
                        break;
                    }

                    // Business exception (termination process )
                    if (processNodeResponse.isBizException()) {
                        bpmProcessRecordService.nodeBizException(processService, processNodeResponse, processCode, bizId);
                        isEndpoint = true;
                        // Reject the whole process at once Reject all 
                        isBizException = true;
                        break;
                    }
                } else {
                    log.info("bizId[{}] - doEngineProcess - prep - {} Front unsatisfied ", bizId, processCode);
                    // Preconditions are not valid 
                    bpmProcessRecordService.nodePrepUnready(prepResponse.getMessage(), processCode, bizId);
                    break;
                }
            } catch (Exception e) {
                // Node exception 
                bpmProcessRecordService.nodeException(engineLifeCycleService, e, processCode, bizId);
                log.error(e.getMessage());
                return;
            }
        }

        try {
            /************* Terminate the process  *************/
            if (isEndpoint) {
                // Process Reject 
                if (isReject) {
                    processApplyReject(engineLifeCycleService, bizId);
                    return;
                }

                //Abnormal termination of business 
                if (isBizException) {
                    engineLifeCycleServiceBizException(engineLifeCycleService, bizId, "Business exception, please check processRecord");
                    return;
                }

                // Validity of validation process 
                if (checkBpmApplyPassValid(bizId)) {
                    // Process passed 
                    processApplyPass(engineLifeCycleService, bizId);
                } else {
                    /** Unknown exception in the process  **/
                    engineLifeCycleServiceApplyException(engineLifeCycleService, bizId, "Process passed, verification failed ");
                }
            }

        } catch (Exception e) {
            log.error("Process termination exception ", e);
            // Process exception - caused by callback operation, etc 
            engineLifeCycleServiceApplyException(engineLifeCycleService, bizId, "Process termination exception :" + e.getMessage());
        }

    }

    /**
     * <p>
     * When a process passes, verify the validity of all process nodes under the list. As long as one process fails, the list cannot be counted as passed 
     * <p/>
     *
     * @param bizId
     * @return boolean
     * @Date 2021/11/4 15:12
     */
    private boolean checkBpmApplyPassValid(String bizId) {
        List<BpmProcessRecord> bpmProcessRecord = bpmProcessRecordService.queryBpmProcessRecord(bizId);
        if (CollectionUtils.isEmpty(bpmProcessRecord)) {
            return false;
        }

        for (BpmProcessRecord processNode : bpmProcessRecord) {
            if (!ProcessConstants.BPM_RESULT_PASS.equals(processNode.getProcessStatus())) {
                /***
                 *
                 * Abnormal scenario 
                 * When this code is executed, it indicates that there is a node that has failed 
                 * The reason should be that the node status has been manually modified 
                 * Or it may be caused by concurrent access 
                 *
                 */
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Judge whether it is rejected 
     * <p/>
     *
     * @param processRecord
     * @return boolean
     * @Date 2021/11/4 14:28
     */
    private boolean isProcessReject(BpmProcessRecord processRecord) {
        return ProcessConstants.BPM_RESULT_REJECT.equals(processRecord.getProcessStatus());
    }

    /**
     * <p>
     * Judge whether the current node has completed 
     * <p/>
     *
     * @param processRecord
     * @return boolean
     * @Date 2021/11/4 14:28
     */
    private boolean isProcessFinish(BpmProcessRecord processRecord) {
        return ProcessConstants.BPM_RESULT_PASS.equals(processRecord.getProcessStatus());
    }

    /**
     * <p>
     * Judge whether it is the first execution of the process 
     * <p/>
     *
     * @param bpmProcessList
     * @return boolean
     * @Date 2021/11/4 14:30
     */
    private boolean isFirstEndPoint(List<BpmProcessRecord> bpmProcessList) {
        // In order to prevent parallel running in the future, check all nodes here. If any node is not in the initialization state, it means that the process has been executed 
        for (BpmProcessRecord processRecord : bpmProcessList) {
            if (!ProcessConstants.BPM_RESULT_INIT.equals(processRecord.getProcessStatus())) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Business process application passed 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @return void
     * @Date 2021/11/4 14:31
     */
    private void processApplyPass(IEngineLifeCycleService engineLifeCycleService, String bizId) {
        if (engineLifeCycleService.processPassCallBack()) {
            // Update process results 
            bpmApplyMapper.updateBpmApplyPass(ProcessConstants.PROCESS_STATUS_DONE, ProcessConstants.BPM_RESULT_PASS, bizId, ProcessConstants.BPM_RESULT_PASS_MSG);
        }

        //Execute callback 
        processBizNotify(engineLifeCycleService, bizId);
    }

    /**
     * <p>
     * Business process application rejected 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @return void
     * @Date 2021/11/4 14:40
     */
    private void processApplyReject(IEngineLifeCycleService engineLifeCycleService, String bizId) {
        if (engineLifeCycleService.processRejectCallBack()) {
            // Update business process application form status 
            bpmApplyMapper.updateBpmApplyPass(ProcessConstants.PROCESS_STATUS_DONE, ProcessConstants.BPM_RESULT_REJECT, bizId, ProcessConstants.BPM_RESULT_REJECT_MSG);
        }

        //Execute callback 
        processBizNotify(engineLifeCycleService, bizId);
    }

    /**
     * <p>
     * Execute callback 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @return void
     * @Date 2021/11/4 15:00
     */
    private void processBizNotify(IEngineLifeCycleService engineLifeCycleService, String bizId) {
        /**Default synchronization. Configure whether the callback is asynchronous in the model  **/
        JSONObject model = modelConfig.getModelConfigByBizId(bizId);
        String executeModel = JSONUtils.parseValue("R#notifyConfig.executeModel#", "sync", model);

        /**Asynchronous callback method */
        if (ProcessConstants.ASYNC_EXECUTE.equalsIgnoreCase(executeModel)) {
            /** Call callback asynchronously  */
            ThreadUtil.execute(() -> doNotifyServer.processBizNotifyAsync(engineLifeCycleService, bizId));
            return;
        }

        /** Synchronization return method  **/
        doNotifyServer.processBizNotify(engineLifeCycleService, bizId);
    }

    /**
     * <p>
     * Business Process Application - Processing 
     * <p/>
     *
     * @param bizId
     * @return void
     * @Date 2021/11/4 15:13
     */
    private void engineLifeCycleServiceProcessing(String bizId) {
        // Business exception callback 
        bpmApplyMapper.updateBpmApplyProcessStatus(ProcessConstants.PROCESS_STATUS_PROCESS, bizId, ProcessConstants.BPM_RESULT_CYCLE_PROCESS_MSG, ProcessConstants.BPM_RESULT_PROCESS);
    }


    /**
     * <p>
     * Business process application exception - business exception 
     * Business exceptions will directly suspend the process - break mode 
     * Specific relevant personnel need to be informed 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @param message
     * @return void
     * @Date 2021/11/4 15:19
     */
    private void engineLifeCycleServiceBizException(IEngineLifeCycleService engineLifeCycleService, String bizId, String message) {
        engineLifeCycleService.processBreakCallBack(message);
        bpmApplyMapper.updateBpmApplyProcessStatus(ProcessConstants.PROCESS_STATUS_EXCEPTION, bizId, message, ProcessConstants.BPM_RESULT_EXCEPTION);
    }

    /**
     * <p>
     * Business process application exception - termination exception 
     * Specific relevant personnel need to be informed 
     * <p/>
     *
     * @param engineLifeCycleService
     * @param bizId
     * @param message
     * @return void
     * @Date 2021/11/4 15:21
     */
    private void engineLifeCycleServiceApplyException(IEngineLifeCycleService engineLifeCycleService, String bizId, String message) {
        engineLifeCycleService.processExceptionCallBack(message);
        bpmApplyMapper.updateBpmApplyProcessStatus(ProcessConstants.PROCESS_STATUS_EXCEPTION, bizId, message, ProcessConstants.BPM_RESULT_EXCEPTION);
    }


}
