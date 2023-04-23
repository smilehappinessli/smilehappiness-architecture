package cn.smilehappiness.limit.aspect;

import cn.smilehappiness.exception.exceptions.SystemInternalException;
import cn.smilehappiness.limit.annotation.ApiLimit;
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
 * Api LimitSection class 
 *
 * @author
 * @Date 2020/7/5 19:55
 */
@Aspect
@Component
public class ApiLimitAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @AutowiredAssembled by type by default. In other words, if you want to get the bean of RedisTemplate<String, Object>, you need to assemble it according to the name. Then naturally think of using @ Resource, which is assembled by name by default 
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * <p>
     * Define facet expressions (dimension interception of classes, which can intercept more methods ）
     * <p/>
     *
     * @param
     * @return void
     * @Date 2020/7/5 20:07
     */
    @Pointcut("execution(* cn.smilehappiness..service..*Impl.*(..))")
    private void myPointCut() {
    }

    /**
     * <p>
     * Implement the interception of the method of adding ApiLimit annotation, and the Limit processing (method 1: interception of facet class ）
     * <p/>
     *
     * @param joinPoint
     * @return void
     * @Date 2020/7/5 20:10
     */
    //@Around("myPointCut()")
    public void requestLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        ApiLimit apiLimit = this.getAnnotation(joinPoint);
        //Global flow restriction on business methods 
        if (apiLimit != null) {
            dealLimit(apiLimit, joinPoint, false);
        }
    }

    /**
     * <p>
     * Limit the flow for the business method. If the first request is restricted, wait for 10 seconds and try again. If it fails again, throw an exception 
     * <p/>
     *
     * @param apiLimit
     * @param joinPoint
     * @param flag      Judge whether a retry has been performed. If the retry has been performed once or is restricted, throw an exception 
     * @return void
     * @Date 2020/7/5 20:30
     */
    private void dealLimit(ApiLimit apiLimit, ProceedingJoinPoint joinPoint, Boolean flag) {
        try {
            //In the business method, the parameter is unique key
            String cacheKey = "apiLimit:" + apiLimit.limitApiName();
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            long methodCounts = valueOperations.increment(cacheKey, 1);
            // If the key does not exist, it is calculated from 0, and when the count is 1, the expiration time is set 
            if (methodCounts == 1) {
                redisTemplate.expire(cacheKey, apiLimit.timeSecond(), TimeUnit.SECONDS);
            }

            // If the count in Redis is greater than the limit, wait 10 seconds to retry 
            if (methodCounts > apiLimit.limitCounts()) {
                if (!flag) {
                    //Wait 10 seconds and try again for the first time 
                    Thread.sleep(10 * 1000);
                    logger.warn("Wait 10 seconds and try again for the first time ...");

                    // Recursive, request business method again 
                    dealLimit(apiLimit, joinPoint, true);
                } else {
                    //If the first request is restricted, wait for 10 seconds and try again. If it fails again, throw an exception. Of course, if you do not need to retry, throw an exception or logical processing directly 
                    throw new SystemInternalException("Three-party Api interface exceeds the limit. Please try again in 30 seconds ！");
                }
            } else {
                //Normal execution of business methods 
                joinPoint.proceed();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("Exception during limit flow restriction processing for business method, reason for exception ：【{}】", e.getMessage());
            throw new SystemInternalException("Exception during limit flow restriction processing for business method, reason for exception ：" + e.getMessage());
        }

    }

    /**
     * <p>
     * Surround the notification, intercept the method using ApiLimit annotation, and limit the flow (method 2: directly intercept the annotation ）
     * <p/>
     *
     * @param joinPoint
     * @return void
     * @Date 2020/7/5 21:03
     */
    @Around("@annotation(cn.smilehappiness.limit.annotation.ApiLimit)")
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
     * Get annotation 
     * <p/>
     *
     * @param joinPoint
     * @return cn.smilehappiness.limit.annotation.ApiLimit
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
