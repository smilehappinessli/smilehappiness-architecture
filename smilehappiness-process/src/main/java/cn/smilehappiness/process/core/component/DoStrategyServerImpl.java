package cn.smilehappiness.process.core.component;

import com.alibaba.fastjson.JSON;
import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.core.engine.IEngineStrategyService;
import cn.smilehappiness.process.core.strategy.IStrategyService;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import cn.smilehappiness.process.mapper.BpmStrategyMapper;
import cn.smilehappiness.process.model.BpmApply;
import cn.smilehappiness.process.model.BpmStrategy;
import cn.smilehappiness.utils.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * Policy service 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 13:51
 */
@Slf4j
@Service("DoStrategyServer")
@RefreshScope
public class DoStrategyServerImpl implements IEngineStrategyService {

    /**
     * Policy switch 
     */
    @Value("${bpm.process.canStrategy:true}")
    private boolean canStrategy;

    @Resource
    public BpmStrategyMapper strategyMapper;

    @Resource
    public BpmApplyMapper bpmApplyMapper;

    /**
     * <p>
     * Strategy switch control 
     * <p/>
     *
     * @param bizId
     * @return boolean
     * @Date 2021/11/4 13:47
     */
    private boolean canStrategy(String bizId) {
        // Policy off 
        if (!canStrategy) {
            return false;
        }

        BpmApply bpmApply = bpmApplyMapper.queryBpmApplyByBizId(bizId);
        if (bpmApply == null) {
            return false;
        }

        // The policy needs to be executed only when the state 1 is initialized 
        return ProcessConstants.PROCESS_STATUS_INIT.equals(bpmApply.getProcessStatus());
    }

    /**
     * <p>
     * Policy scheduling execution 
     * <p/>
     *
     * @param bizId
     * @param userId
     * @return java.lang.String
     * @Date 2021/11/4 13:13
     */
    @Override
    public String executeStrategy(String bizId, Long userId) {
        log.info("bizId[{}] - executeStrategy，isStrategySwitch[{}]", bizId, canStrategy);
        // Verification strategy 
        if (!canStrategy(bizId)) {
            return null;
        }

        /** Select Model  **/
        ChoiceStrategyModel choiceModel = choiceStrategy(bizId);
        if (choiceModel == null) {
            return null;
        }

        /** Initialize the highest policy engine template  */
        choiceModel.getStrategyService().initStrategyTemplate(userId);

        /**  Process status changed to - template initialized  (1->2) **/
        bpmApplyMapper.updateStrategyToProcess(ProcessConstants.PROCESS_STATUS_TEMPLATE, choiceModel.getModel(), bizId, ProcessConstants.BPM_RESULT_PROCESS);
        return choiceModel.getModel();
    }

    /**
     * <p>
     * Decision strategy, select the model 
     * <p/>
     *
     * @param bizId
     * @return cn.smilehappiness.process.component.ChoiceModel
     * @Date 2021/11/4 13:43
     */
    public ChoiceStrategyModel choiceStrategy(String bizId) {
        /** Query policy list  **/
        List<BpmStrategy> strategyList = queryStrategyList(null);
        if (CollectionUtils.isEmpty(strategyList)) {
            return null;
        }

        // The final strategy is the first one by default (here, each time you request to obtain a service instance, instead of using @ Autowired injection ）
        BpmStrategy defaultStrategy = strategyList.get(0);
        IStrategyService highestStrategyService = SpringUtil.getBean(defaultStrategy.getStrategyBean(), IStrategyService.class);
        //Initialization Policy Required Parameters 
        highestStrategyService.initStrategy(bizId, defaultStrategy.getWeight(), defaultStrategy.getBpmModel(), defaultStrategy.getBizCode(), defaultStrategy.getOptionConfig());

        // Model 
        String bpmModel = defaultStrategy.getBpmModel();

        // Compare the remaining weight scores from the second 
        for (int i = 1; i < strategyList.size(); i++) {
            BpmStrategy bpmStrategy = strategyList.get(i);
            IStrategyService iStrategyService = SpringUtil.getBean(bpmStrategy.getStrategyBean(), IStrategyService.class);
            // Initialize policy template 
            iStrategyService.initStrategy(bizId, bpmStrategy.getWeight(), bpmStrategy.getBpmModel(), bpmStrategy.getBizCode(), bpmStrategy.getOptionConfig());
            // Get weight 
            if (iStrategyService.getStrategyWeight() > highestStrategyService.getStrategyWeight()) {
                //Compare weights and replace the strategy with the highest weight 
                highestStrategyService = iStrategyService;
                bpmModel = bpmStrategy.getBpmModel();
            }
        }

        log.info("bizId[{}] - doStrategy, Strategy wins  highestStrategyService[{}]", bizId, highestStrategyService);
        return new ChoiceStrategyModel(bpmModel, highestStrategyService);
    }

    /**
     * <p>
     * Query policy range - subclasses can be overridden as their own query methods 
     * <p/>
     *
     * @param apply
     * @return java.util.List<cn.smilehappiness.process.model.BpmStrategy>
     * @Date 2021/11/4 13:45
     */
    public List<BpmStrategy> queryStrategyList(BpmApply apply) {
        if (apply != null) {
            log.info("queryStrategyList:{}", JSON.toJSONString(apply));
        }
        return strategyMapper.queryStrategyList();
    }

}

/**
 * <p>
 * Model selection class 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 13:49
 */
@Data
class ChoiceStrategyModel {

    /**
     * Model name 
     */
    private String model;
    private IStrategyService strategyService;

    public ChoiceStrategyModel(String model, IStrategyService strategyService) {
        this.model = model;
        this.strategyService = strategyService;
    }
}
