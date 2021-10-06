package com.smilehappiness.cache.config;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Redis配置文件读取
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = false)
public class RedisProperties {

    private int database;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;
    /**
     * redis连接ip
     */
    private String host;
    /**
     * redis连接端口
     */
    private Integer port;
    /**
     * redis连接密码
     */
    private String password;
    /**
     * Redis配置模式：单机模式、集群模式、哨兵模式
     */
    private String mode;

    /**
     * 池配置
     */
    private RedisPoolProperties pool;

    /**
     * 单机信息配置
     */
    private RedisSingleProperties single;

    /**
     * 集群信息配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;
}

