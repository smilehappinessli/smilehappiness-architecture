package cn.smilehappiness.process.core.process;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.process.dto.PrepResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * <p>
 * Process node abstract service class 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 16:38
 */
public abstract class AbstractNodeProcessCleanDataSource implements INodeProcessService {

    /**
     * Business parameters 
     **/
    protected String bizId;
    protected String processCode;
    protected Map<String, Object> processData;
    protected JSONObject optionsJson;

    /**
     * <p>
     * Process initialization, initializing all required basic parameters 
     * <p/>
     *
     * @param bizId
     * @param processCode
     * @param processData
     * @param options
     * @return void
     * @Date 2021/11/4 16:35
     */
    @Override
    public void initProcess(String bizId, String processCode, Map<String, Object> processData, String options) {
        this.bizId = bizId;
        this.processCode = processCode;
        this.processData = processData;
        this.optionsJson = StringUtils.isBlank(options) ? JSON.parseObject("{}") : JSON.parseObject(options);
    }

    /**
     * <p>
     * When executing invoke, the node is prepared in advance, which can clean data, obtain data sources, etc., and can be expanded later 
     * <p/>
     *
     * @param
     * @return cn.smilehappiness.process.dto.PrepResponse
     * @Date 2021/11/4 16:34
     */
    @Override
    public PrepResponse prep() {
        return new PrepResponse().prepSuccess();
    }

    /**
     * <p>
     * Get business id
     * <p/>
     *
     * @param
     * @return java.lang.String
     * @Date 2022/2/16 11:02
     */
    public String getBizId() {
        return bizId;
    }

}
