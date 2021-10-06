package com.smilehappiness.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>
 * RedisConfig
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/27 15:34
 */
@EnableCaching
@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * 自定义springSessionDefaultRedisSerializer对象，将会替代默认的SESSION序列化对象。
     * 默认是JdkSerializationRedisSerializer，缺点是需要类实现Serializable接口。
     * 并且在反序列化时如果异常会抛出SerializationException异常，
     * 而SessionRepositoryFilter又没有处理异常，故如果序列化异常时就会导致请求异常
     */
    @Bean(name = "springSessionDefaultRedisSerializer")
    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * retemplate相关配置 TODO 后续考虑集群或者哨兵，Redisson自定义配置了，这里redis也要自定义配置，原始的factory不能加载了
     * 使redis支持插入对象
     * <p>
     * JacksonJsonRedisSerializer和GenericJackson2JsonRedisSerializer的区别：
     * GenericJackson2JsonRedisSerializer在json中加入@class属性，类的全路径包名，方便反系列化。
     * JacksonJsonRedisSerializer如果存放了List则在反系列化的时候，
     * 如果没指定TypeReference则会报错java.util.LinkedHashMap cannot be cast。
     *
     * @return 方法缓存 Methods the cache
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 创建Jedis连接工厂
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        connectionFactory.setHostName(redisProperties.getHost());
        connectionFactory.setPort(redisProperties.getPort());
        connectionFactory.setPassword(redisProperties.getPassword());

        // 定义并配置连接工厂
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会抛出异常
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 值采用json序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(stringRedisSerializer);

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.setEnableDefaultSerializer(true);
        // 调用后初始化方法，没有它将抛出异常
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 对hash类型的数据操作: 针对map类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作： 简单K-V操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作: 针对list类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作： set类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作 ：zset类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
