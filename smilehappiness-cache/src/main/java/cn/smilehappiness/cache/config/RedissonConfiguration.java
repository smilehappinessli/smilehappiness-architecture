package cn.smilehappiness.cache.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * RedisCluster configuration 
 * <p/>
 *
 * @author
 * @Date 2021/10/5 17:01
 */
@Configuration
@EnableConfigurationProperties(RedisBaseProperties.class)
public class RedissonConfiguration {

    @Resource
    private RedisBaseProperties redisBaseProperties;


    @Configuration
    @ConditionalOnClass({Redisson.class})
    @ConditionalOnExpression("'${smilehappiness.redis.mode}'=='single' or '${smilehappiness.redis.mode}'=='cluster' or '${smilehappiness.redis.mode}'=='sentinel'")
    protected class RedissonSingleClientConfiguration {

        /**
         * <p>
         * Stand-alone mode redisson client 
         * <p/>
         *
         * @param
         * @return org.redisson.api.RedissonClient
         * @Date 2021/10/5 17:10
         */
        @Bean
        @ConditionalOnProperty(name = "smilehappiness.redis.mode", havingValue = "single")
        RedissonClient redissonSingle() {
            Config config = new Config();
            String node = redisBaseProperties.getSingle().getAddress();
            node = node.startsWith("redis://") ? node : "redis://" + node;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setTimeout(redisBaseProperties.getPool().getConnTimeout())
                    .setConnectionPoolSize(redisBaseProperties.getPool().getSize())
                    .setConnectionMinimumIdleSize(redisBaseProperties.getPool().getMinIdle());
            if (StringUtils.isNotBlank(redisBaseProperties.getPassword())) {
                serverConfig.setPassword(redisBaseProperties.getPassword());
            }
            return Redisson.create(config);
        }


        /**
         * <p>
         * Redisson client in cluster mode 
         * <p/>
         *
         * @param
         * @return org.redisson.api.RedissonClient
         * @Date 2021/10/5 17:11
         */
        @Bean
        @ConditionalOnProperty(name = "smilehappiness.redis.mode", havingValue = "cluster")
        RedissonClient redissonCluster() {
            System.out.println("cluster redisProperties:" + redisBaseProperties.getCluster());

            Config config = new Config();
            String[] nodes = redisBaseProperties.getCluster().getNodes().split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setScanInterval(redisBaseProperties.getCluster().getScanInterval())
                    .setIdleConnectionTimeout(redisBaseProperties.getPool().getSoTimeout())
                    .setConnectTimeout(redisBaseProperties.getPool().getConnTimeout())
                    .setRetryAttempts(redisBaseProperties.getCluster().getRetryAttempts())
                    .setRetryInterval(redisBaseProperties.getCluster().getRetryInterval())
                    .setMasterConnectionPoolSize(redisBaseProperties.getCluster().getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redisBaseProperties.getCluster().getSlaveConnectionPoolSize())
                    .setTimeout(redisBaseProperties.getTimeout());
            if (StringUtils.isNotBlank(redisBaseProperties.getPassword())) {
                serverConfig.setPassword(redisBaseProperties.getPassword());
            }
            return Redisson.create(config);
        }

        /**
         * <p>
         * Sentinel mode redisson client 
         * <p/>
         *
         * @param
         * @return org.redisson.api.RedissonClient
         * @Date 2021/10/5 17:11
         */
        @Bean
        @ConditionalOnProperty(name = "smilehappiness.redis.mode", havingValue = "sentinel")
        RedissonClient redissonSentinel() {
            System.out.println("sentinel redisProperties:" + redisBaseProperties.getSentinel());
            Config config = new Config();
            String[] nodes = redisBaseProperties.getSentinel().getNodes().split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((index) -> newNodes.add(
                    index.startsWith("redis://") ? index : "redis://" + index));

            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .addSentinelAddress(newNodes.toArray(new String[0]))
                    .setMasterName(redisBaseProperties.getSentinel().getMaster())
                    .setReadMode(ReadMode.SLAVE)
                    .setTimeout(redisBaseProperties.getTimeout())
                    .setMasterConnectionPoolSize(redisBaseProperties.getPool().getSize())
                    .setSlaveConnectionPoolSize(redisBaseProperties.getPool().getSize());

            if (StringUtils.isNotBlank(redisBaseProperties.getPassword())) {
                serverConfig.setPassword(redisBaseProperties.getPassword());
            }

            return Redisson.create(config);
        }
    }
}
