package cn.smilehappiness.cache.config;


import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * RedisCluster configuration 
 * <p/>
 *
 * @author
 * @Date 2021/10/5 17:01
 */
@Data
@ToString
public class RedisClusterProperties {

    /**
     * The interval time of cluster state scanning, in milliseconds 
     */
    private int scanInterval;

    /**
     * Cluster node 
     */
    private String nodes;

    /**
     * Default value: SLAVE (only read from the service node) sets the mode of reading operation to select the node. Available values are: SLAVE - only read from the service node 。
     * MASTER - Only read in the main service node. MASTER_ SLAVE - can be read in both master and slave service nodes 
     */
    private String readMode;
    /**
     * （From node connection pool size) default ：64
     */
    private int slaveConnectionPoolSize;
    /**
     * Primary node connection pool size) default value ：64
     */
    private int masterConnectionPoolSize;

    /**
     * （Command failure retry times) default value ：3
     */
    private int retryAttempts;

    /**
     * Command retry sending interval, unit: ms default value ：1500
     */
    private int retryInterval;

    /**
     * Default value of maximum number of execution failures ：3
     */
    private int failedAttempts;
}
