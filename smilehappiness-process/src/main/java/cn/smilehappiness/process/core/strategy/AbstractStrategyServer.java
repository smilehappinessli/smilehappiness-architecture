package cn.smilehappiness.process.core.strategy;

import cn.smilehappiness.early.warning.DingTalkWarningNoticeServer;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import cn.smilehappiness.distribute.service.impl.CachedUidGenerator;
import cn.smilehappiness.process.constants.ProcessConstants;
import cn.smilehappiness.process.mapper.BpmProcessRecordMapper;
import cn.smilehappiness.process.mapper.BpmProcessTemplateMapper;
import cn.smilehappiness.process.model.BpmProcessRecord;
import cn.smilehappiness.process.model.BpmProcessTemplate;
import cn.smilehappiness.process.service.BpmProcessDataRecordService;
import cn.smilehappiness.process.utils.SpringTransactionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Policy Service Abstract Class 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 11:56
 */
public abstract class AbstractStrategyServer implements IStrategyService {

    private String bpmModel;
    private Integer weight;
    protected String bizId;
    protected String bizCode;
    protected String strategyOptionConfig;

    @Resource
    private CachedUidGenerator cachedUidGenerator;
    @Resource
    private BpmProcessRecordMapper bpmProcessRecordMapper;
    @Resource
    private BpmProcessTemplateMapper bpmProcessTemplateMapper;
    @Autowired
    private BpmProcessDataRecordService bpmProcessDataRecordService;
    @Autowired
    private SpringTransactionUtil springTransactionUtil;
    @Autowired
    protected DingTalkWarningNoticeServer dingTalkWarningNoticeServer;

    /**
     * <p>
     * Initialization Policy 
     * <p/>
     *
     * @param bizId
     * @param initialWeight        Initial weight value 
     * @param bpmModel             Model 
     * @param bizCode              business code
     * @param strategyOptionConfig Policy configuration 
     * @return void
     * @Date 2021/11/4 11:47
     */
    @Override
    public void initStrategy(String bizId, Integer initialWeight, String bpmModel, String bizCode, String strategyOptionConfig) {
        this.bizId = bizId;
        this.weight = initialWeight;
        this.bpmModel = bpmModel;
        this.bizCode = bizCode;
        this.strategyOptionConfig = strategyOptionConfig;
    }

    /**
     * <p>
     * Strategy weight=score weight+initial weight 
     * The one with the highest strategic weight score wins 
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @Date 2021/11/4 11:46
     */
    @Override
    public Integer getStrategyWeight() {
        return this.score() + weight;
    }

    /**
     * <p>
     * Get different strategy weight scores 
     * <p/>
     *
     * @param
     * @return java.lang.Integer
     * @Date 2021/11/4 11:50
     */
    public abstract Integer score();

    /**
     * <p>
     * Initialize process engine template 
     * <p/>
     *
     * @param userId
     * @return void
     * @Date 2020/9/9 13:51
     */
    @Override
    public void initStrategyTemplate(Long userId) {
        try {
            springTransactionUtil.transactionalExecute(item -> {
                // Initialize process template 
                this.initBpmProcessRecordInfo(userId);

                // Initialize data template 
                if (bpmProcessDataRecordService.countProcessDataRecordByBizId(bizId) < 1) {
                    bpmProcessDataRecordService.initProcessDataTemplate(bizId, bpmModel);
                }
            });
        } catch (Exception e) {
            dingTalkWarningNoticeServer.sendWarningMessage(StringUtils.join(ProcessConstants.DING_CORE_KEY_BILL, "--", ProcessConstants.DING_CORE_KEY_USER, "【", bizId, "】Initialization process engine template exception, exception reason ：" + e));
            throw new SystemInternalException("【" + bizId + "】Error initializing process engine template ：" + e.getMessage());
        }
    }

    /**
     * <p>
     * Initialize process template 
     * <p/>
     *
     * @param userId
     * @return void
     * @Date 2022/1/6 11:14
     */
    private void initBpmProcessRecordInfo(Long userId) {
        if (bpmProcessRecordMapper.countProcessRecordByBizId(bizId) >= 1) {
            return;
        }

        List<BpmProcessTemplate> bpmProcessTemplateList = bpmProcessTemplateMapper.getBpmProcessTemplate(bizId, bpmModel);
        if (CollectionUtils.isEmpty(bpmProcessTemplateList)) {
            return;
        }

        /*ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes != null ? servletRequestAttributes.getRequest() : null;
        String userId = "-1";
        if (httpServletRequest != null) {
            userId = httpServletRequest.getHeader("userId");
        }*/

        List<BpmProcessRecord> bpmProcessRecordList = new ArrayList<>();
        for (BpmProcessTemplate bpmProcessTemplate : bpmProcessTemplateList) {
            BpmProcessRecord bpmProcessRecord = new BpmProcessRecord();
            //Assignment distribution id
            Long id = cachedUidGenerator.getUID();
            bpmProcessRecord.setId(id);
            bpmProcessRecord.setBizId(bizId);

            bpmProcessRecord.setUserId(userId);
            bpmProcessRecord.setIsPrep(bpmProcessTemplate.getIsPrep());
            bpmProcessRecord.setProcessCode(bpmProcessTemplate.getProcessCode());
            bpmProcessRecord.setProcessBean(bpmProcessTemplate.getProcessBean());
            bpmProcessRecord.setOptionConfig(bpmProcessTemplate.getOptionConfig());
            bpmProcessRecord.setSort(bpmProcessTemplate.getSort());
            bpmProcessRecord.setCreatedBy(bpmProcessTemplate.getCreatedBy());
            //Process status (init,process,pass,reject)
            bpmProcessRecord.setProcessStatus(ProcessConstants.BPM_RESULT_INIT);
            bpmProcessRecord.setRemark(bpmProcessTemplate.getRemark());

            bpmProcessRecordList.add(bpmProcessRecord);
        }

        //Initialize process data and save in batch 
        bpmProcessRecordMapper.initProcessTemplate(bpmProcessRecordList);
    }

}
