package cn.smilehappiness.cache.config;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RedisProfile reading 
 * <p/>
 *
 * @author
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
@Configuration
@ConfigurationProperties(prefix = "smilehappiness.redis", ignoreUnknownFields = false)
public class RedisBaseProperties {

    private int database;

    /**
     * Time to wait for the node to reply to the command. The time starts when the command is sent successfully 
     */
    private int timeout;
    /**
     * redisconnect ip
     */
    private String host;
    /**
     * redisConnection port 
     */
    private Integer port;
    /**
     * redisConnection password 
     */
    private String password;
    /**
     * RedisConfiguration mode: stand-alone mode, cluster mode, sentry mode 
     */
    private String mode;

    /**
     * Pool configuration 
     */
    private RedisPoolProperties pool;

    /**
     * Stand-alone information configuration 
     */
    private RedisSingleProperties single;

    /**
     * Cluster information configuration 
     */
    private RedisClusterProperties cluster;

    /**
     * Sentry configuration 
     */
    private RedisSentinelProperties sentinel;
}

