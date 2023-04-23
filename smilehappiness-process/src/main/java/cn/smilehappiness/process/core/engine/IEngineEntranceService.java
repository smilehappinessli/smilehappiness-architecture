package cn.smilehappiness.process.core.engine;

import com.alibaba.fastjson.JSONObject;
import cn.smilehappiness.process.dto.EngineRequestDto;

/**
 * <p>
 * BPMProcess entry service, call and query (top-level interface ）
 * <p>
 * <p/>
 *
 * @author
 * @Date 2021/11/3 17:57
 */
public interface IEngineEntranceService {

    /**
     * <p>
     * Initiate process call 
     * <p/>
     *
     * @param request
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/3 18:08
     */
    JSONObject engineInvoke(EngineRequestDto request);

    /**
     * <p>
     * Initiate process call - manually trigger compensation 
     * <p/>
     *
     * @param bizId
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/3 18:09
     */
    JSONObject engineInvoke(String bizId);

    /**
     * <p>
     * Process execution result query 
     * <p/>
     *
     * @param bizId
     * @return com.alibaba.fastjson.JSONObject
     * @Date 2021/11/3 18:08
     */
    JSONObject queryEngineResult(String bizId);

}
