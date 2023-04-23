package cn.smilehappiness.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smilehappiness.process.model.BpmApply;

/**
 * <p>
 * biz application form service 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
public interface BpmApplyService extends IService<BpmApply> {

    /**
     * <p>
     * Get application records 
     * <p/>
     *
     * @param orderNumber
     * @return cn.smilehappiness.process.model.BpmApply
     * @Date 2021/12/17 17:21
     */
    BpmApply queryBpmApplyByBizId(String orderNumber);

}
