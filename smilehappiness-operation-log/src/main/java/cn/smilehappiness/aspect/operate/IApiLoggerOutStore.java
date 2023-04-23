package cn.smilehappiness.aspect.operate;

import java.util.List;

/**
 * <p>
 * Log storage -ApiLoggerOut
 * <p/>
 *
 * @author
 * @Date 2022/1/3 18:02
 */
public interface IApiLoggerOutStore {

    /**
     * <p>
     * Store operation log information 
     * <p/>
     *
     * @param logList Log collection 
     * @param logList
     * @return void
     * @Date 2022/1/3 18:03
     */
    void store(List<OperateLogBaseInfo> logList);

}
