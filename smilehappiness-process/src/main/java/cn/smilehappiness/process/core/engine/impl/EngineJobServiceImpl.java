package cn.smilehappiness.process.core.engine.impl;

import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.core.component.DoProcessServer;
import cn.smilehappiness.process.core.component.ModelConfig;
import cn.smilehappiness.process.core.engine.IEngineJobService;
import cn.smilehappiness.process.core.engine.IEngineStrategyService;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import cn.smilehappiness.process.model.BpmApply;
import cn.smilehappiness.process.utils.JSONUtils;
import cn.smilehappiness.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * Policy, process, callback job service class 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 11:37
 */
@Slf4j
@Service
public class EngineJobServiceImpl implements IEngineJobService {

    @Resource
    private BpmApplyMapper bpmApplyMapper;
    @Resource
    private DoProcessServer doProcessServer;
    @Resource
    private ModelConfig modelConfig;

    /**
     * <p>
     * Batch execution of policies job
     * <p/>
     *
     * @param strategyBeanName
     * @return void
     * @Date 2021/11/4 11:36
     */
    @Override
    public void batchStrategyJob(String strategyBeanName) {
        List<BpmApply> strategyList = bpmApplyMapper.queryStrategyBpmApplyList();
        if (CollectionUtils.isEmpty(strategyList)) {
            log.info("Policy batch execution job, no data to be processed temporarily 。。。");
            return;
        }

        log.info("{} pieces of data to be processed for batch execution of jobs 。。。 ", strategyList.size());

        for (BpmApply strategy : strategyList) {
            SpringUtil.getBean(strategyBeanName, IEngineStrategyService.class).executeStrategy(strategy.getBizId(), -1L);
        }
    }

    /**
     * <p>
     * Process batch execution job Note: the data in the process to be optimized can be processed in multiple threads to improve the lending speed 
     * <p/>
     *
     * @param processStatus If there are more than one status, comma separation 
     * @param bizCode
     * @return void
     * @Date 2021/11/4 11:36
     */
    @Override
    public void batchInvokeEngineJob(String processStatus, String bizCode) {
        List<BpmApply> bpmApplyList = bpmApplyMapper.queryProcessingBpmApplyList(processStatus, bizCode);
        if (CollectionUtils.isEmpty(bpmApplyList)) {
            log.info("The process executes jobs in batches, and there is no data to be processed temporarily 。。。");
            return;
        }

        log.info("{} pieces of data to be processed for batch execution of jobs 。。。 ", bpmApplyList.size());

        for (BpmApply process : bpmApplyList) {
            // Synchronous/asynchronous  modelConfig
            JSONObject modelConfigJsonObj = modelConfig.getModelConfig(process.getBpmModel());
            String model = JSONUtils.parseValue("R#processConfig.executeModel#", "sync", modelConfigJsonObj);
            if (ProcessConstants.ASYNC_EXECUTE.equals(model)) {
                doProcessServer.doProcessAsync(process.getBizId());
            } else {
                doProcessServer.doProcessByLock(process.getBizId());
            }
        }
    }

    /**
     * <p>
     * Trigger process through bizId -- single process processing 
     * <p/>
     *
     * @param bizId
     * @return void
     * @Date 2021/11/17 17:10
     */
    @Override
    public void singleInvokeEngineJob(String bizId) {
        BpmApply bpmApply = bpmApplyMapper.queryBpmApplyByBizId(bizId);
        if (bpmApply == null) {
            log.info("When the process is triggered through bizId, there is no data to be processed temporarily 。。。");
            return;
        }

        doProcessServer.doProcessByLock(bpmApply.getBizId());
    }

    /**
     * <p>
     * Notification callback batch execution job
     * <p/>
     *
     * @param
     * @return void
     * @Date 2021/11/3 18:10
     */
    @Override
    public void batchEngineNotifyJob() {
        List<BpmApply> notifyList = bpmApplyMapper.queryNotifyBpmApplyList();
        for (BpmApply notify : notifyList) {
            if (modelConfig.getEngineLifeCycle(notify.getBizId()).processBizNotify()) {
                //Update callback results 
                bpmApplyMapper.updateBpmApplyNotifyResult(ProcessConstants.BPM_RESULT_NOTIFY_SUCCESS, notify.getBizId());
            }
        }
    }
}
