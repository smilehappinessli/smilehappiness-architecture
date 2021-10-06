package com.smilehappiness.cache.config;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * Redis 池配置
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
public class RedisPoolProperties {

    /**
     * 连接池中的最大空闲连接数
     */
    private int maxIdle;
    /**
     * 连接池中的最小空闲连接数
     */
    private int minIdle;

    private int maxActive;
    /**
     * 连接池最大阻塞等待时间（使用负值表示没有限制）
     */
    private int maxWait;
    /**
     * 连接超时时间（毫秒）
     */
    private int connTimeout;

    private int soTimeout;
    /**
     * 池大小
     */
    private int size;

}