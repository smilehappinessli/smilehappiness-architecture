package cn.smilehappiness.process.core.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import cn.smilehappiness.process.mapper.BpmApplyMapper;
import cn.smilehappiness.process.mapper.BpmProcessDataRecordMapper;
import cn.smilehappiness.process.model.BpmApply;
import cn.smilehappiness.process.model.BpmProcessDataRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * In-process data processing 
 * <p/>
 *
 * @author
 * @Date 2021/11/6 10:45
 */
@Slf4j
@Service
public class ProcessDataSupport {

    @Resource
    public BpmApplyMapper bpmApplyMapper;
    @Resource
    private BpmProcessDataRecordMapper bpmProcessDataRecordMapper;

    /**
     * <p>
     * The data bound by the process is converted into map form 
     * <p/>
     *
     * @param bizId
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Date 2021/11/14 10:34
     */
    public Map<String, Object> getProcessData(String bizId) {
        Map<String, Object> processDataMap = new HashMap<>(16);
        List<BpmProcessDataRecord> bpmProcessData = bpmProcessDataRecordMapper.queryBpmProcessDataRecord(bizId);
        if (CollectionUtils.isEmpty(bpmProcessData)) {
            return processDataMap;
        }

        for (BpmProcessDataRecord bpmProcessDataRecord : bpmProcessData) {
            String filedName = bpmProcessDataRecord.getFieldName();
            String value = bpmProcessDataRecord.getFieldValue();
            processDataMap.put(filedName, value);
        }
        return processDataMap;
    }

    /**
     * <p>
     * Get all data in the process 
     * <p/>
     *
     * @param bizId
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/14 14:58
     */
    public JSONObject getAllProcessData(String bizId) {
        Map<String, Object> processData = this.getProcessData(bizId);
        processData.put("bizId", bizId);

        JSONObject allDataRecord = JSON.parseObject("{}");
        allDataRecord.put("data", JSON.parseObject(JSON.toJSONString(processData)));

        BpmApply bpmApply = bpmApplyMapper.queryBpmApplyByBizId(bizId);
        if (bpmApply == null || StringUtils.isEmpty(bpmApply.getBizParam())) {
            log.error("Process application record result is empty, please check ");
            throw new SystemInternalException("Procedure application record result is empty!");
        }

        JSONObject bizParam = JSON.parseObject(bpmApply.getBizParam());
        allDataRecord.put("bizParam", bizParam);
        return allDataRecord;
    }

    /**
     * <p>
     * The data in the process is transferred from map to list and saved 
     * <p/>
     *
     * @param bizId
     * @param processDataMap
     * @return void
     * @Date 2021/11/14 11:14
     */
    public void saveProcessData(String bizId, Map<String, Object> processDataMap) {
        List<BpmProcessDataRecord> recordList = new ArrayList<>();
        for (Map.Entry<String, Object> key : processDataMap.entrySet()) {
            BpmProcessDataRecord tempDataRecord = new BpmProcessDataRecord();
            tempDataRecord.setBizId(bizId);
            tempDataRecord.setFieldName(key.getKey());
            tempDataRecord.setFieldValue(key.getValue() == null ? null : key.getValue().toString());
            recordList.add(tempDataRecord);
        }

        if (CollectionUtils.isNotEmpty(recordList)) {
            bpmProcessDataRecordMapper.updateBpmProcessDataRecordBatch(recordList);
        }
    }

}
