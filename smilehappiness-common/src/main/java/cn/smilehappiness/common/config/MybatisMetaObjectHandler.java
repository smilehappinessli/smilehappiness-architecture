package cn.smilehappiness.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * Fill creation time and modification time 
 * <p/>
 *
 * @author
 * @Date 2021/9/27 11:59
 */
@Component
public class MybatisMetaObjectHandler implements MetaObjectHandler {


    /**
     * Population policy on insertion 
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdTime", new Date(), metaObject);
    }

    /**
     * Population policy when updating 
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedTime", new Date(), metaObject);
    }
}
