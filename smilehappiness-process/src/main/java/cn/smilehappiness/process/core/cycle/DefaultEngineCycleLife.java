package cn.smilehappiness.process.core.cycle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.process.core.component.ProcessDataSupport;
import cn.smilehappiness.process.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Default EngineCycle engine lifecycle implementation 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 15:37
 */
@Slf4j
@Scope("prototype")
@Service("DefaultEngineCycleLife")
public class DefaultEngineCycleLife extends AbstractEngineCycleLife {

    @Autowired
    private ProcessDataSupport processDataSupport;

    @Override
    public boolean beforeProcessCallBack() {
        log.info("Life cycle management  - beforeProcessCallBack ......");
        return true;
    }

    /**
     * <p>
     * Processing when the process is first executed (processing of data in the basic request parameters ）
     * <p/>
     *
     * @param
     * @return boolean
     * @Date 2021/11/14 14:36
     */
    @Override
    public boolean firstProcessCallBack() {
        log.info("Life cycle management  - firstProcessCallBack ......");

        Map<String, Object> processData = new HashMap<>(16);
        // Processing data cleaning 
        JSONObject allDataRecord = processDataSupport.getAllProcessData(bizId);
        log.info(" bizId-{} - Data to be cleaned allData {}", bizId, allDataRecord);

        // Business data, cleaning into record 
        JSONObject paramConfig = JSON.parseObject(options).getJSONObject("engineLifeCycle").getJSONObject("param_options");
        log.info(" bizId-{} - Cleaning rules dataRule {}", bizId, paramConfig);
        for (String key : paramConfig.keySet()) {
            String express = paramConfig.getString(key);
            Object value = JSONUtils.parseValue(express, "", allDataRecord);
            processData.put(key, value);
        }

        log.info(" bizId-{} - Results after cleaning  processData {}", bizId, processData);

        // The previous node updates the value to the library - convenient for subsequent process nodes 
        processDataSupport.saveProcessData(bizId, processData);
        return true;
    }

    @Override
    public boolean processProcessingCallBack() {
        log.info("Life cycle management  - processProcessingCallBack ......");

        //Callback can be added here 
        return true;
    }

    @Override
    public boolean processPassCallBack() {
        log.info("Life cycle management  - processPassCallBack ......");
        return true;
    }

    @Override
    public boolean processRejectCallBack() {
        log.info("Life cycle management  - processRejectCallBack ......");
        return true;
    }

    @Override
    public boolean processBreakCallBack(String message) {
        log.info("Life cycle management  - processBreakCallBack ......");
        log.info("processBreakCallBack message: {} ", message);
        return true;
    }

    @Override
    public boolean processExceptionCallBack(String message) {
        log.info("Life cycle management  - processExceptionCallBack ......");
        log.info("processExceptionCallBack message: {} ", message);
        return true;
    }

    @Override
    public boolean processExceptionCallBack(Exception e, String message) {
        log.info("Life cycle management  - processExceptionCallBack ......");
        log.info("processExceptionCallBack message: {} ", message);
        return true;
    }

    @Override
    public boolean processBizNotify() {
        log.info("Life cycle management - business callback  processBizNotify ......");
        return true;
    }

}
