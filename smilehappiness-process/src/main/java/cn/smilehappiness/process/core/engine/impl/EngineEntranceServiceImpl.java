package cn.smilehappiness.process.core.engine.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.distribute.service.impl.CachedUidGenerator;
import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.core.component.DoProcessServer;
import cn.smilehappiness.process.core.component.ModelConfig;
import cn.smilehappiness.process.core.engine.IEngineEntranceService;
import cn.smilehappiness.process.core.engine.IEngineStrategyService;
import cn.smilehappiness.process.dto.EngineRequestDto;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import cn.smilehappiness.process.utils.JSONUtils;
import cn.smilehappiness.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * Process entry service, call and query implementation 
 * <p/>
 *
 * @author
 * @Date 2021/11/5 13:44
 */
@Service
@RefreshScope
public class EngineEntranceServiceImpl implements IEngineEntranceService {

    private static final Logger log = LoggerFactory.getLogger(EngineEntranceServiceImpl.class);

    @Resource
    private CachedUidGenerator cachedUidGenerator;

    @Value("${bpm.process.canQuery:true}")
    private boolean canQuery;
    @Value("${bpm.doStrategy:DoStrategyServer}")
    private String strategyName;

    @Resource
    private ModelConfig modelConfig;
    @Resource
    private BpmApplyMapper bpmApplyMapper;
    @Resource
    private DoProcessServer doProcessServer;

    /**
     * <p>
     * Initiate process call 
     * <p/>
     *
     * @param request
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/5 13:56
     */
    @Override
    public JSONObject engineInvoke(EngineRequestDto request) {
        log.info("bpmRequest parameters ：{}", JSON.toJSONString(request));

        Long udf1 = request.getId();
        Long userId = request.getUserId();
        String bizId = request.getBizId();
        String bizCode = request.getBizCode();
        //Judge whether the application has been initiated 
        if (this.bpmApplyMapper.countBpmApplyByBizId(bizId) < 1) {
            //Add application record 
            Long id = cachedUidGenerator.getUID();
            this.bpmApplyMapper.addBpmApply(id, udf1, userId, bizId, bizCode, JSON.toJSONString(request.getRequestParam()), ProcessConstants.PROCESS_STATUS_INIT, ProcessConstants.BPM_RESULT_NOTIFY_INIT);
            //Policy scheduling execution 
            String bpmModel = SpringUtil.getBean(this.strategyName, IEngineStrategyService.class).executeStrategy(bizId, userId);
            if (StringUtils.isNotBlank(bpmModel)) {
                JSONObject modelConfigDeal = this.modelConfig.getModelConfig(bpmModel);
                String executeModel = JSONUtils.parseValue("R#processConfig.executeModel#", "sync", modelConfigDeal);
                //Process execution 
                if (StringUtils.equalsIgnoreCase(ProcessConstants.ASYNC_EXECUTE, executeModel)) {
                    doProcessServer.doProcessAsync(bizId);
                } else {
                    doProcessServer.doProcessByLock(bizId);
                }
            }
        } else {
            //If a process has been initiated, it can be executed directly. The unfinished process can be re-executed (in extreme cases with high concurrency, it is necessary to consider the scenario that the process in execution triggers the process again ）
            doProcessServer.doProcessByLock(bizId);
        }

        //Process execution result query (asynchronous push to business end, active query ）
        return this.queryEngineResult(bizId);
    }

    /**
     * <p>
     * Initiate process call - manually trigger compensation 
     * <p/>
     *
     * @param bizId
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/5 14:00
     */
    @Override
    public JSONObject engineInvoke(String bizId) {
        if (this.bpmApplyMapper.countBpmApplyByBizId(bizId) > 0) {
            this.doProcessServer.doProcess(bizId);
        }

        return this.queryEngineResult(bizId);
    }

    /**
     * <p>
     * Process execution result query 
     * <p/>
     *
     * @param bizId
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/5 13:47
     */
    @Override
    public JSONObject queryEngineResult(String bizId) {
        JSONObject jsonObject = new JSONObject();
        if (!this.canQuery) {
            return jsonObject;
        }

        return jsonObject;
    }

}
