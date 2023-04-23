package cn.smilehappiness.cache.interceptor;

import cn.smilehappiness.cache.util.RedisUtil;
import cn.smilehappiness.cache.util.RedissonLockRedisUtil;
import cn.smilehappiness.common.enums.ResultCodeEnum;
import cn.smilehappiness.exception.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Idempotent interceptor processing 
 * <p/>
 *
 * @author
 * @Date 2021/10/14 15:34
 */
@Slf4j
@Component
public class IdempotencyInterceptor extends HandlerInterceptorAdapter {

    private static final String IDEMPOTENCY_KEY = "idempotencyKey";
    private static final String LOCK_OBJ = "lockObj";
    private static final String IDEMPOTENCY_PREFIX = "interfaceOperation:idempotency:";
    public static final String HEAD_AUTHORIZATION = "Authorization";
    /**
     * TokenPrompt message on failure 
     */
    private static final String ERROR_MESSAGE = "This operation has expired. Please refresh and try again ";

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedissonLockRedisUtil redissonLockRedisUtil;
    @Resource
    private RedissonClient redissonClient;


    /**
     * <p>
     * Idempotency interceptor preprocessing, note: if token is empty, idempotency verification is not performed 
     * <p/>
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @Date 2021/10/14 15:56
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        InterfaceIdempotency idempotency = handlerMethod.getMethodAnnotation(InterfaceIdempotency.class);
        if (idempotency == null) {
            return true;
        }

        if (redisUtil == null) {
            log.warn("RedisUtil injection failed during interface idempotence verification ");
            return true;
        }

        String token = request.getHeader(HEAD_AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            if (idempotency.validateToken()) {
                log.warn("Idempotency check interface: {}, token in request header is empty .", request.getRequestURI());
                //response.sendError(Integer.parseInt(ResultCodeEnum.UNAUTHORIZED.getCode()), ERROR_MESSAGE);
                throw new BusinessException(ResultCodeEnum.UNAUTHORIZED.getCode(), "@@IDEMPOTENCY_TOKEN_EXPIRE##", ERROR_MESSAGE);
            }
            return true;
        }

        String idempotencyKey = StringUtils.join(IDEMPOTENCY_PREFIX, idempotency.formUniqueKey(), ":", token);

        //It supports the expiration unlocking function. It will be automatically unlocked after 90 seconds without calling the unlock method. Of course, in order not to occupy resources and use the lock to handle the business, it is generally recommended to release the lock manually 
        //RLock lock = redissonLockRedisUtil.lock(idempotencyKey, idempotency.operationTokenExpire());

        RLock lock = redissonClient.getLock(idempotencyKey);

        request.setAttribute(IDEMPOTENCY_KEY, idempotencyKey);
        request.setAttribute(LOCK_OBJ, lock);

        //1.0.7Release the lock manually (improve performance ）
        if (lock.tryLock()) {
            //execute biz method
            log.info("During interface idempotence verification, key: [{}], [{}] verification passed ", idempotencyKey, idempotency.formUniqueKey());
            return true;
        } else {
            log.warn("During interface idempotency verification, key: [{}], [{}] forms are submitted repeatedly ", idempotencyKey, idempotency.formUniqueKey());
            throw new BusinessException(ResultCodeEnum.SERVICE_UNAVAILABLE.getCode(), "@@FORM_REPEAT_COMMIT##", idempotency.errorMessage());
        }
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        if (handler instanceof HandlerMethod && request.getAttribute(LOCK_OBJ) != null) {
            RLock lock = (RLock) request.getAttribute(LOCK_OBJ);
            //If a lock exists, release the lock 
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

}
