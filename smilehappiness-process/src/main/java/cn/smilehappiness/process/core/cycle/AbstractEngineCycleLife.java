package cn.smilehappiness.process.core.cycle;

import cn.smilehappiness.process.core.engine.IEngineLifeCycleService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * EngineCycleLifeAbstract class, which can be extended later 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 15:34
 */
@Slf4j
public abstract class AbstractEngineCycleLife implements IEngineLifeCycleService {

    protected String bizId;
    protected String bpmModel;
    protected String options;

    @Override
    public void initProcessCycle(String bizId, String bpmModel, String options) {
        this.bizId = bizId;
        this.bpmModel = bpmModel;
        this.options = options;
    }

}
