package cn.smilehappiness.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.smilehappiness.process.model.BpmStrategy;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * biz process strategy table Mapper interface 
 * </p>
 *
 * @author
 * @since 2021-11-03
 */
@Repository
public interface BpmStrategyMapper extends BaseMapper<BpmStrategy> {

    /**
     * <p>
     * Obtain all effective process policies 
     * <p/>
     *
     * @param
     * @return java.util.List<cn.smilehappiness.process.model.BpmStrategy>
     * @Date 2021/11/4 13:40
     */
    List<BpmStrategy> queryStrategyList();

}
