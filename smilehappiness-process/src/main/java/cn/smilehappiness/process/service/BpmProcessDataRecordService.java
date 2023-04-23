package cn.smilehappiness.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.smilehappiness.process.model.BpmProcessDataRecord;

import java.util.List;

/**
 * <p>
 * Process record service class 
 * </p>
 *
 * @author
 * @since 2021-11-14
 */
public interface BpmProcessDataRecordService extends IService<BpmProcessDataRecord> {

    /**
     * <p>
     * Obtain the number of data to be cleaned through the business ID (incoming ID) 
     * <p/>
     *
     * @param bizId
     * @return java.lang.Integer
     * @Date 2021/11/14 13:59
     */
    Integer countProcessDataRecordByBizId(String bizId);

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
    void initProcessDataTemplate(String bizId, String bpmModel);

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
    List<BpmProcessDataRecord> queryBpmProcessDataRecordSingle(String bizId, List<String> fieldNameList);

}
