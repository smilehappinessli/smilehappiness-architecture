package cn.smilehappiness.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.smilehappiness.distribute.service.impl.CachedUidGenerator;
import cn.smilehappiness.process.mapper.BpmProcessDataRecordMapper;
import cn.smilehappiness.process.mapper.BpmProcessDataTemplateMapper;
import cn.smilehappiness.process.model.BpmProcessDataRecord;
import cn.smilehappiness.process.model.BpmProcessDataTemplate;
import cn.smilehappiness.process.service.BpmProcessDataRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Service implementation class 
 * </p>
 *
 * @author
 * @since 2021-11-14
 */
@Service
public class BpmProcessDataRecordServiceImpl extends ServiceImpl<BpmProcessDataRecordMapper, BpmProcessDataRecord> implements BpmProcessDataRecordService {

    @Resource
    private BpmProcessDataTemplateMapper bpmProcessDataTemplateMapper;
    @Resource
    private CachedUidGenerator cachedUidGenerator;

    /**
     * <p>
     * Obtain the number of data to be cleaned through the business ID (incoming ID) 
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/14 13:59
     */
    @Override
    public Integer countProcessDataRecordByBizId(String bizId) {
        QueryWrapper<BpmProcessDataRecord> ew = new QueryWrapper<>();
        ew.eq("biz_id", bizId);
        return baseMapper.selectCount(ew);
    }

    /**
     * <p>
     * Initialize data template 
     * <p/>
     *
     * @param bizId
     * @param bpmModel
     * @return void
     * @Date 2021/11/14 13:59
     */
    @Override
    public void initProcessDataTemplate(String bizId, String bpmModel) {
        List<BpmProcessDataTemplate> bpmProcessDataTemplateList = bpmProcessDataTemplateMapper.getBpmProcessDataTemplate(bizId, bpmModel);
        if (CollectionUtils.isEmpty(bpmProcessDataTemplateList)) {
            return;
        }

        List<BpmProcessDataRecord> bpmProcessDataRecordList = new ArrayList<>();
        for (BpmProcessDataTemplate bpmProcessDataTemplate : bpmProcessDataTemplateList) {
            BpmProcessDataRecord bpmProcessDataRecord = new BpmProcessDataRecord();
            //Assignment distribution id
            Long id = cachedUidGenerator.getUID();
            bpmProcessDataRecord.setId(id);
            bpmProcessDataRecord.setBizId(bizId);
            bpmProcessDataRecord.setFieldName(bpmProcessDataTemplate.getFieldName());
            bpmProcessDataRecord.setFieldType(bpmProcessDataTemplate.getFieldType());
            bpmProcessDataRecord.setFieldValue(bpmProcessDataTemplate.getDefaultValue());
            bpmProcessDataRecord.setRemark(bpmProcessDataTemplate.getRemark());
            bpmProcessDataRecord.setCreatedBy(bpmProcessDataTemplate.getCreatedBy());

            bpmProcessDataRecordList.add(bpmProcessDataRecord);
        }

        //Initialize data template -- batch operation 
        baseMapper.initProcessDataTemplateBatch(bpmProcessDataRecordList);
    }

    /**
     * <p>
     * Obtain process data record information through bizId and fieldName 
     * <p/>
     *
     * @param bizId
     * @param fieldNameList
     * @return java.util.List<cn.smilehappiness.process.model.BpmProcessDataRecord>
     * @Date 2021/12/28 14:54
     */
    @Override
    public List<BpmProcessDataRecord> queryBpmProcessDataRecordSingle(String bizId, List<String> fieldNameList) {
        return baseMapper.queryBpmProcessDataRecordSingle(bizId, fieldNameList);
    }

}
