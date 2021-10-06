package com.smilehappiness.cache.config;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * Redis哨兵配置
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
public class RedisSentinelProperties {

    /**
     * 哨兵master 名称
     */
    private String master;

    /**
     * 哨兵节点
     */
    private String nodes;

    /**
     * 哨兵配置
     */
    private boolean masterOnlyWrite;

    /**
     *
     */
    private int failMax;
}