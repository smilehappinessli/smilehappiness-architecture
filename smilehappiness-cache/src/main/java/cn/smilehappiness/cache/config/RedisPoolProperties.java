package cn.smilehappiness.cache.config;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * Redis Pool configuration 
 * <p/>
 *
 * @author
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
public class RedisPoolProperties {

    /**
     * Maximum number of free connections in the connection pool 
     */
    private int maxIdle;
    /**
     * Minimum number of free connections in the connection pool 
     */
    private int minIdle;

    private int maxActive;
    /**
     * Maximum blocking waiting time of connection pool (use negative value to indicate no limit ）
     */
    private int maxWait;
    /**
     * Connection timeout (ms ）
     */
    private int connTimeout;

    private int soTimeout;
    /**
     * Pool size 
     */
    private int size;

}
