package cn.smilehappiness.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smilehappiness.exception.exceptions.BusinessException;
import cn.smilehappiness.process.enums.ProcessExceptionEnum;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import cn.smilehappiness.process.model.BpmApply;
import cn.smilehappiness.process.service.BpmApplyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * biz application form service realization class 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Service
public class BpmApplyServiceImpl extends ServiceImpl<BpmApplyMapper, BpmApply> implements BpmApplyService {

    /**
     * <p>
     * Get application records 
     * <p/>
     *
     * @param orderNumber
     * @return cn.smilehappiness.process.model.BpmApply
     * @Date 2021/12/17 17:21
     */
    @Override
    public BpmApply queryBpmApplyByBizId(String orderNumber) {
        if (StringUtils.isBlank(orderNumber)) {
            throw new BusinessException(ProcessExceptionEnum.ORDER_NUMBER_NULL);
        }

        QueryWrapper<BpmApply> ew = new QueryWrapper<>();
        ew.eq("biz_id", orderNumber);
        ew.orderByDesc("created_time");
        ew.last("limit 1");
        return baseMapper.selectOne(ew);
    }

}
