package com.smilehappiness.limit.aspect;

import com.smilehappiness.exception.exceptions.SystemInternalException;
import com.smilehappiness.limit.annotation.ApiLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * Api Limit切面类
 *
 * @author smilehappiness
 * @Date 2020/7/5 19:55
 */
@Aspect
@Component
public class ApiLimitAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @Autowired默认按照类型装配的。也就是说，想要获取RedisTemplate< String, Object>的Bean，要根据名字装配。那么自然想到使用@Resource，它默认按照名字装配
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * <p>
     * 定义切面表达式（类的维度拦截，可以拦截更多的方法）
     * <p/>
     *
     * @param
     * @return void
     * @Date 2020/7/5 20:07
     */
    @Pointcut("execution(* com.smilehappiness..service..*Impl.*(..))")
    private void myPointCut() {
    }

    /**
     * <p>
     * 实现对添加ApiLimit注解的方法进行拦截，Limit处理（方式一：切面类拦截）
     * <p/>
     *
     * @param joinPoint
     * @return void
     * @Date 2020/7/5 20:10
     */
    //@Around("myPointCut()")
    public void requestLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        ApiLimit apiLimit = this.getAnnotation(joinPoint);
        //对业务方法进行全局限流
        if (apiLimit != null) {
            dealLimit(apiLimit, joinPoint, false);
        }
    }

    /**
     * <p>
     * 针对业务方法进行Limit限流处理，如果第一次请求被限制了，等待10秒后重试，如果再次失败，则抛出异常
     * <p/>
     *
     * @param apiLimit
     * @param joinPoint
     * @param flag      判断是否进行过一次重试了，如果重试过一次还是被限制，就抛异常
     * @return void
     * @Date 2020/7/5 20:30
     */
    private void dealLimit(ApiLimit apiLimit, ProceedingJoinPoint joinPoint, Boolean flag) {
        try {
            //业务方法中，参数唯一key
            String cacheKey = "apiLimit:" + apiLimit.limitApiName();
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            long methodCounts = valueOperations.increment(cacheKey, 1);
            // 如果该key不存在，则从0开始计算，并且当count为1的时候，设置过期时间
            if (methodCounts == 1) {
                redisTemplate.expire(cacheKey, apiLimit.timeSecond(), TimeUnit.SECONDS);
            }

            // 如果redis中的count大于限制的次数，则等待10秒重试
            if (methodCounts > apiLimit.limitCounts()) {
                if (!flag) {
                    //等待10秒后，第一次重试
                    Thread.sleep(10 * 1000);
                    logger.warn("等待10秒后，第一次重试...");

                    // 递归，再次请求业务方法
                    dealLimit(apiLimit, joinPoint, true);
                } else {
                    //如果第一次请求被限制了，等待10秒后重试，如果再次失败，则抛出异常，当然，如果不需要重试，直接抛异常或者逻辑处理即可
                    throw new SystemInternalException("三方Api接口超限，请30秒后再试！");
                }
            } else {
                //正常执行业务方法
                joinPoint.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("针对业务方法进行Limit限流处理时异常，异常原因：{}", e.getMessage());
            throw new SystemInternalException("针对业务方法进行Limit限流处理时异常，异常原因：" + e.getMessage());
        }

    }

    /**
     * <p>
     * 环绕通知，对使用ApiLimit注解的方法进行拦截，限流处理（方式二：直接对注解进行拦截）
     * <p/>
     *
     * @param joinPoint
     * @return void
     * @Date 2020/7/5 21:03
     */
    @Around("@annotation(com.smilehappiness.limit.annotation.ApiLimit)")
    public void requestLimitByAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method == null) {
            return;
        }

        if (method.isAnnotationPresent(ApiLimit.class)) {
            dealLimit(method.getAnnotation(ApiLimit.class), joinPoint, false);
        }
    }

    /**
     * <p>
     * 获取注解
     * <p/>
     *
     * @param joinPoint
     * @return com.smilehappiness.limit.annotation.ApiLimit
     * @Date 2020/7/5 21:45
     */
    private ApiLimit getAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method == null) {
            return null;
        }

        return method.getAnnotation(ApiLimit.class);
    }
}