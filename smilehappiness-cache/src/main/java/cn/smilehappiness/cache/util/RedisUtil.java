package cn.smilehappiness.cache.util;

import cn.smilehappiness.cache.enums.CoreEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * RedisUse tool class 
 * <p/>
 *
 * @author
 * @Date 2021/10/4 15:43
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Specify cache expiration time 
     *
     * @param key  key 
     * @param time Time (seconds )
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Specify cache expiration time 
     *
     * @param key      key 
     * @param time     Time (seconds )
     * @param timeUnit Company 
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Obtain the expiration time according to the key 
     *
     * @param key Key cannot be null
     * @return Time (seconds) returns 0 to represent permanent validity 
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * Find matches key
     *
     * @param pattern key
     * @return /
     */
    public List<String> scan(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(new String(cursor.next()));
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Pagination query  key
     *
     * @param patternKey key
     * @param page       Page 
     * @param size       Number of pages 
     * @return /
     */
    public List<String> findKeysForPage(String patternKey, int page, int size) {
        ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<>(size);
        int tmpIndex = 0;
        int fromIndex = page * size;
        int toIndex = page * size + size;
        while (cursor.hasNext()) {
            if (tmpIndex >= fromIndex && tmpIndex < toIndex) {
                result.add(new String(cursor.next()));
                tmpIndex++;
                continue;
            }
            // After obtaining the data that meets the conditions, you can exit 
            if (tmpIndex >= toIndex) {
                break;
            }
            tmpIndex++;
            cursor.next();
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Determine whether the key exists 
     *
     * @param key key 
     * @return true Exists false does not exist 
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete cache 
     *
     * @param key One value or multiple values can be passed 
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /*============================String=============================*/

    /**
     * Normal cache acquisition 
     *
     * @param key key 
     * @return value 
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * Normal cache put 
     *
     * @param key   key 
     * @param value value 
     * @return trueSuccess false Failure 
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Normal cache put and set time 
     *
     * @param key   key 
     * @param value value 
     * @param time  Time (seconds) time should be greater than 0. If time is less than or equal to 0, infinite period will be set 
     * @return trueSuccess false Failure 
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Normal cache put and set time 
     *
     * @param key      key 
     * @param value    value 
     * @param time     time 
     * @param timeUnit type 
     * @return trueSuccess false Failure 
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Incremental 
     *
     * @param key   key 
     * @param delta How many (greater than 0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("Increment factor must be greater than 0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Decrement 
     *
     * @param key   key 
     * @param delta How many (less than 0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("Decline factor must be greater than 0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }
    /*============================String end=============================*/

    /*================================Map==============================*/

    /**
     * HashGet
     *
     * @param key  Key cannot be null
     * @param item Item cannot be null
     * @return value 
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * Get all key values corresponding to hashKey 
     *
     * @param key key 
     * @return Corresponding multiple key values 
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * HashSet
     *
     * @param key key 
     * @param map Corresponding to multiple key values 
     * @return true Success false Failure 
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet And set the time 
     *
     * @param key  key 
     * @param map  Corresponding to multiple key values 
     * @param time Time (seconds )
     * @return trueSuccess false Failure 
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put data into a hash table. If it does not exist, it will be created 
     *
     * @param key   key 
     * @param item  term 
     * @param value value 
     * @return true Success false Failure 
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put data into a hash table. If it does not exist, it will be created 
     *
     * @param key   key 
     * @param item  term 
     * @param value value 
     * @param time  Time (seconds) Note: If the existing hash table has time, the original time will be replaced here 
     * @return true Success false Failure 
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete the value in the hash table 
     *
     * @param key  Key cannot be null
     * @param item Item can make multiple cannot be null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * Judge whether there is a value for this item in the hash table. It is not recommended to use it. It is recommended to use the hasKey() method to judge 
     *
     * @param key  Key cannot be null
     * @param item Item cannot be null
     * @return true Yes, false, no, if the key exists and the value does not match, an error may be reported 
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hashIf the increment does not exist, it will create one and return the added value 
     *
     * @param key  key 
     * @param item term 
     * @param by   How many (greater than 0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hashDecrement 
     *
     * @param key  key 
     * @param item term 
     * @param by   To reduce the score (less than 0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }
    /*================================Map  end====================*/

    /*============================set=============================*/

    /**
     * Get all the values in the Set according to the key 
     *
     * @param key key 
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query from a set according to value to see if it exists 
     *
     * @param key   key 
     * @param value value 
     * @return true Exists false does not exist 
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put data into set cache 
     *
     * @param key    key 
     * @param values Values can be multiple 
     * @return Number of successes 
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Put the set data into the cache 
     *
     * @param key    key 
     * @param time   Time (seconds )
     * @param values Values can be multiple 
     * @return Number of successes 
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get the length of the set cache 
     *
     * @param key key 
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Remove with value 
     *
     * @param key    key 
     * @param values Values can be multiple 
     * @return Number of removed 
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /*============================set end=============================*/

    /*===============================list=============================*/

    /**
     * Get the contents of the list cache 
     *
     * @param key   key 
     * @param start start 
     * @param end   End 0 to - 1 represents all values 
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the length of the list cache 
     *
     * @param key key 
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get the value in the list through the index 
     *
     * @param key   key 
     * @param index When index>=0, 0 header, 1 second element, and so on; When index<0, - 1, footer, - 2, the penultimate element, and so on 
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Put the list into the cache 
     *
     * @param key   key 
     * @param value value 
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put the list into the cache 
     *
     * @param key   key 
     * @param value value 
     * @param time  Time (seconds )
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put the list into the cache 
     *
     * @param key   key 
     * @param value value 
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Put the list into the cache 
     *
     * @param key   key 
     * @param value value 
     * @param time  Time (seconds )
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Modify a piece of data in the list according to the index 
     *
     * @param key   key 
     * @param index Indexes 
     * @param value value 
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove N values as value
     *
     * @param key   key 
     * @param count How many are removed 
     * @param value value 
     * @return Number of removed 
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /*===============================list  end=============================*/

    /**
     * Get key value by fuzzy query 
     *
     * @param pattern "*"Query all key
     * @return
     */
    public Set keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * Message queue using Redis 
     *
     * @param channel
     * @param message Message content 
     */
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }


    //=========BoundListOperations usage  start============

    /**
     * Add data to the Redis list (from the right ）
     *
     * @param listKey
     * @param expireEnum Enumeration class of validity period 
     * @param values     Data to be added 
     */
    public void addToListRight(String listKey, CoreEnum.ExpireEnum expireEnum, Object... values) {
        //Binding operation 
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //insert data 
        boundValueOperations.rightPushAll(values);
        //Set expiration time 
        boundValueOperations.expire(expireEnum.getTime(), expireEnum.getTimeUnit());
    }

    /**
     * Traverse the list
     *
     * @param listKey
     * @param start   Start sequence number 
     * @param end     End sequence number 
     * @return
     */
    public List<Object> rangeList(String listKey, long start, long end) {
        //Binding operation 
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        //Query data 
        return boundValueOperations.range(start, end);
    }

    /**
     * Pop up the value on the right -- and remove this value 
     *
     * @param listKey
     */
    public Object rifhtPop(String listKey) {
        //Binding operation 
        BoundListOperations<String, Object> boundValueOperations = redisTemplate.boundListOps(listKey);
        return boundValueOperations.rightPop();
    }

    //=========BoundListOperations usage  End============

    /**
     * Save Entity Class 
     *
     * @param key
     * @param entity
     */
    public void setEntity(String key, Object entity) {
        redisTemplate.opsForValue().set(key, JsonUtil.entity2Json(entity));
    }

    /**
     * Get the entity class according to the specified type 
     *
     * @param key   rediskey
     * @param clazz Specify type 
     * @return Cache value 
     */
    public <T> T getEntity(String key, Class<T> clazz) {
        Object v = redisTemplate.opsForValue().get(key);
        if (v != null) {
            return JsonUtil.json2Entity((String) v, clazz);
        }
        return null;
    }
}
