package cn.smilehappiness.aspect.operate;

import java.util.List;

/**
 * <p>
 * Log storage 
 * <p/>
 *
 * @author
 * @Date 2021/8/28 13:48
 */
public interface IOperateLogStore {

    /**
     * <p>
     * Store operation log information 
     * <p/>
     *
     * @param logList Log collection 
     * @param logList
     * @return void
     * @Date 2021/8/28 13:10
     */
    void store(List<OperateLogBaseInfo> logList);

}
