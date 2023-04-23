package cn.smilehappiness.cache.config;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * RedisSentry configuration 
 * <p/>
 *
 * @author
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
public class RedisSentinelProperties {

    /**
     * Sentry master name 
     */
    private String master;

    /**
     * Sentinel Nodes  
     */
    private String nodes;

    /**
     * Sentry configuration 
     */
    private boolean masterOnlyWrite;

    /**
     *
     */
    private int failMax;
}
