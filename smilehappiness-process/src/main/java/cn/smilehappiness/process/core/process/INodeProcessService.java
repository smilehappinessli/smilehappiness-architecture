package cn.smilehappiness.process.core.process;

import cn.smilehappiness.process.dto.PrepResponse;
import cn.smilehappiness.process.dto.ProcessNodeResponse;

import java.util.Map;

/**
 * <p>
 * Process node call 
 * - Front 
 * - call 
 * - Callback 
 * <p/>
 *
 * @author
 * @Date 2021/11/4 16:26
 */
public interface INodeProcessService {

    /**
     * <p>
     * Process initialization, initializing all required basic parameters 
     * <p>
     * <p/>
     *
     * @param bizId       business id
     * @param processCode Code defined by process node 
     * @param processData Data in process 
     * @param options     json Configuration item, used to expand customized business scenarios 
     * @return void
     * @Date 2021/11/4 16:27
     */
    void initProcess(String bizId, String processCode, Map<String, Object> processData, String options);

    /**
     * <p>
     * When executing invoke, the node is prepared in advance, which can clean data, obtain data sources, etc., and can be expanded later 
     * <p/>
     *
     * @param
     * @return cn.smilehappiness.process.dto.PrepResponse
     * @Date 2021/11/4 16:30
     */
    PrepResponse prep();

    /**
     * <p>
     * Execute process logic 
     * <p>
     * code - Process response status 
     * 200 adopt 
     * 203 Processing 
     * 300 refuse 
     * 500 Business exception 
     * <p>
     * isPass -    By default false
     * isProcess - Default in processing false
     * isReject -  Rejection defaults to false
     * isBizException - Business exception defaults to false
     * bizData    - Business data is used to transfer business values during node callback 
     * <p/>
     *
     * @param
     * @return cn.smilehappiness.process.dto.ProcessNodeResponse
     * @Date 2021/11/4 16:31
     */
    ProcessNodeResponse invoke();

    /**
     * <p>
     * Callback during business processing 
     * ProcessNodeResponse.code= 203
     * <p/>
     *
     * @param response
     * @return boolean
     * @Date 2021/11/4 16:31
     */
    boolean nodeProcessingCall(ProcessNodeResponse response);

    /**
     * <p>
     * When the node passes, the callback is executed 
     * ProcessNodeResponse.code= 200
     * <p/>
     *
     * @param response
     * @return boolean
     * @Date 2021/11/4 16:31
     */
    boolean nodePassCall(ProcessNodeResponse response);

    /**
     * <p>
     * Callback on node rejection 
     * ProcessNodeResponse.code= 300
     * <p/>
     *
     * @param response
     * @return boolean
     * @Date 2021/11/4 16:32
     */
    boolean nodeRejectCall(ProcessNodeResponse response);

    /**
     * <p>
     * Callback in case of business exception 
     * ProcessNodeResponse.code= 500
     * <p/>
     *
     * @param response
     * @return boolean
     * @Date 2021/11/4 16:33
     */
    boolean nodeBizExceptionCall(ProcessNodeResponse response);

}

