package cn.smilehappiness.distribute.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import cn.smilehappiness.distribute.service.impl.CachedUidGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;


/**
 * <p>
 * Integrated distributed id
 * The numeric type does not support automatic conversion, and needs precise matching. For example, if you return Long, the entity primary key cannot be defined as Integer
 * <p/>
 *
 * @author
 * @Date 2021/12/8 21:11
 */
@Configuration
public class DistributeIdGeneratorConfig implements IdentifierGenerator {

    @Lazy
    @Resource
    private CachedUidGenerator cachedUidGenerator;

    @Override
    public Long nextId(Object entity) {
        //Call the distributed ID service, generate the distributed ID, and return the generated ID value 
        return cachedUidGenerator.getUID();
    }

}
