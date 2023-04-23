package cn.smilehappiness.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * <p>
 * RedisConfig
 * <p/>
 *
 * @author
 * @Date 2021/8/27 15:34
 */
@EnableCaching
@Configuration
public class RedisConfiguration {

    @Resource
    private RedisBaseProperties redisBaseProperties;

    /**
     * Customizing the springSessionDefaultRedisSerializer object will replace the default SESSION serialization object 。
     * The default is JdkSerializationRedisSerializer. The disadvantage is that the Serializable interface needs to be implemented by the class 。
     * In addition, SerializationException will be thrown if the exception occurs during deserialization ，
     * The SessionRepositoryFilter does not handle the exception, so if the exception is serialized, it will cause the request exception 
     */
    @Bean(name = "springSessionDefaultRedisSerializer")
    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * retemplateThe relevant configuration TODO will consider the cluster or sentry. Redison has customized the configuration. Here Redis also needs to customize the configuration. The original factory cannot be loaded 
     * Enable Redis to support inserting objects 
     * <p>
     * JacksonJsonRedisSerializerDifference from GenericJackson2JsonRedisSerializer ：
     * GenericJackson2JsonRedisSerializerAdd the @ class attribute and the full path package name of the class in json to facilitate deserialization 。
     * JacksonJsonRedisSerializerIf a List is stored, it will be de-serialized ，
     * If no TypeReference is specified, an error will be reported java.util.LinkedHashMap cannot be cast。
     *
     * @return Method cache  Methods the cache
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Create a Jedis connection factory 
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        connectionFactory.setHostName(redisBaseProperties.getHost());
        connectionFactory.setPort(redisBaseProperties.getPort());
        connectionFactory.setPassword(redisBaseProperties.getPassword());

        // Define and configure connection factories 
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //Use Jackson2JsonRedisSerializer to serialize and deserialize the value of Redis (JDK serialization method is used by default ）
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        // Specify the fields to serialize, field, get, set, and modifier range. Any includes private and public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // Specify the type of serialized input. The class must be non-final modified. The final modified class, such as String and Integer, will throw an exception 
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // Values are serialized using json 
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //Use StringRedisSerializer to serialize and deserialize the key value of Redis 
        template.setKeySerializer(stringRedisSerializer);

        // Set hash key and value serialization mode 
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.setEnableDefaultSerializer(true);
        // Initialize the method after calling. Without it, an exception will be thrown 
        template.afterPropertiesSet();

        return template;
    }

    /**
     * Data operation on hash type: data operation on map type 
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * Operation on Redis string type data: simple K-V operation 
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * Data operation on linked list type: data operation on list type 
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * Data operation on unordered collection type: set type data operation 
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * Data operation on ordered set type: zset type data operation 
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
