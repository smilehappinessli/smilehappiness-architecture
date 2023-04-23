package cn.smilehappiness.process.core.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import cn.smilehappiness.process.core.engine.IEngineLifeCycleService;
import cn.smilehappiness.process.mapper.BpmModelMapper;
import cn.smilehappiness.process.model.BpmModel;
import cn.smilehappiness.process.utils.JSONUtils;
import cn.smilehappiness.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * Model configuration class 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 10:49
 */
@Slf4j
@Component
public class ModelConfig {

    @Resource
    private BpmModelMapper bpmModelMapper;

    /**
     * <p>
     *   Get ModelConfig configuration information 
     *   Turn formatting on and off  @formatter:off
     *   Reopen formatting  @formatter:on
     * @formatter:off
     *  {
     *      engineLifeCycle:{
     *          lifeBean:"defaultEngineLifeCycleBean", //Life cycle management 
     *          options:{
     *              // Customize various parameters - they will be injected into the life cycle 
     *
     *
     *          }
     *      },
     *      processConfig:{
     *          executeModel:"async/sync"  // Asynchronous - synchronous 
     *      }
     *   }
     * <p/>
     * @formatter:on
     *
     * @param bpmModel
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/4 11:18
     */
    public JSONObject getModelConfig(String bpmModel) {
        return JSON.parseObject(bpmModelMapper.queryBpmModelByBpmModel(bpmModel).getBpmConfig());
    }

    /**
     * <p>
     * Obtain model data through business ID 
     * <p/>
     *
     * @param bizId
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/4 11:18
     */
    public JSONObject getModelConfigByBizId(String bizId) {
        return JSON.parseObject(bpmModelMapper.queryBpmModelByBizId(bizId).getBpmConfig());
    }

    /**
     * <p>
     * Judge whether the model exists 
     * <p/>
     *
     * @param bizId
     * @return boolean
     * @Date 2021/11/4 11:18
     */
    public boolean engineModelConfigExits(String bizId) {
        return bpmModelMapper.countBpmModelByBizId(bizId) > 0;
    }


    /**
     * <p>
     * Get lifecycle services 
     * <p/>
     *
     * @param bizId
     * @return IEngineLifeCycleService
     * @Date 2021/11/4 11:20
     */
    public IEngineLifeCycleService getEngineLifeCycle(String bizId) {
        if (!engineModelConfigExits(bizId)) {
            log.error("The model data does not exist, please contact the administrator ");
            throw new SystemInternalException("Model data does not exist!");
        }

        //Obtain model data through business ID 
        BpmModel bpmModelEntity = bpmModelMapper.queryBpmModelByBizId(bizId);
        // Get request parameter information, callback, etc. through bpmModel configuration 
        JSONObject modelConfig = JSON.parseObject(bpmModelEntity.getBpmConfig());

        IEngineLifeCycleService engineLifeCycleService = SpringUtil.getBean(JSONUtils.parseValue("R#engineLifeCycle.lifeBean#", "", modelConfig), IEngineLifeCycleService.class);

        // Inject the parameters bizId, model and options 
        engineLifeCycleService.initProcessCycle(bizId, bpmModelEntity.getBpmModel(), modelConfig.toJSONString());
        return engineLifeCycleService;
    }

}
