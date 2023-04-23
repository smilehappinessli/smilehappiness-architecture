package cn.smilehappiness.cache.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Distributed lock tool class 
 * <p/>
 *
 * @author
 * @Date 2021/10/4 15:43
 */
@Component
public class RedissonLockRedisUtil {

    @Resource
    private RedissonClient redissonClient;

    /**
     * Lock 
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * Locking, automatic release after expiration 
     *
     * @param lockKey
     * @param leaseTime Automatic lock release time 
     * @return
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * Locking, automatic release after expiration, time unit incoming 
     *
     * @param lockKey
     * @param unit      Time unit 
     * @param leaseTime Automatic release time after locking 
     * @return
     */
    public RLock lock(String lockKey, TimeUnit unit, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, unit);
        return lock;
    }

    /**
     * Attempt to acquire lock 
     *
     * @param lockKey
     * @param unit      Time unit 
     * @param waitTime  Maximum waiting time 
     * @param leaseTime Automatic release time after locking 
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * Attempt to acquire lock 
     *
     * @param lockKey
     * @param waitTime  Maximum waiting time 
     * @param leaseTime Automatic release time after locking 
     * @return
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;

        }
    }

    /**
     * Release lock 
     *
     * @param lockKey
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();

    }

    /**
     * Release lock 
     *
     * @param lock
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }
}
