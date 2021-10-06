package com.smilehappiness.aspect.operate;

import java.util.List;

/**
 * <p>
 * 日志存储器
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/28 13:48
 */
public interface IOperateLogStore {

    /**
     * <p>
     * 存储操作日志信息
     * <p/>
     *
     * @param logList 日志集合
     * @param logList
     * @return void
     * @Date 2021/8/28 13:10
     */
    void store(List<OperateLogBaseInfo> logList);
}
