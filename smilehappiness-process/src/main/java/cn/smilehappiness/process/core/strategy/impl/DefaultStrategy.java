package cn.smilehappiness.process.core.strategy.impl;

import cn.smilehappiness.process.core.strategy.AbstractStrategyServer;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * DefaultPolicy implementation, which can be implemented on the business side 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 11:59
 */
@Scope("prototype")
@Service("DefaultStrategy")
public class DefaultStrategy extends AbstractStrategyServer {

    @Resource
    private BpmApplyMapper bpmApplyMapper;

    /**
     * <p>
     * Return the policy weight score 
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @Date 2021/11/4 12:00
     */
    @Override
    public Integer score() {
        if ("defaultStrategy".equalsIgnoreCase(bpmApplyMapper.queryBpmApplyByBizId(bizId).getBizCode())) {
            return 100;
        }

        return 0;
    }
}
