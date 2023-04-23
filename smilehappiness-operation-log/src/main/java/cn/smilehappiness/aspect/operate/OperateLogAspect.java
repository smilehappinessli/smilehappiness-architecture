package cn.smilehappiness.aspect.operate;

import com.alibaba.fastjson.JSON;
import cn.smilehappiness.common.enums.ResultCodeEnum;
import cn.smilehappiness.exception.exceptions.AbstractBizException;
import cn.smilehappiness.exception.exceptions.BusinessException;
import cn.smilehappiness.exception.exceptions.SystemInternalException;
import cn.smilehappiness.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * Define log facet processing class 
 * <p/>
 *
 * @author
 * @Date 2022/3/14 20:11
 */
@Aspect
@RefreshScope
@Component
public class OperateLogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(OperateLogAspect.class);

    private static final ThreadLocal<LocalDateTime> START_TIME_THREAD_LOCAL = new NamedThreadLocal<>("biz deal startTime");
    private static final ThreadLocal<OperateLogBaseInfo> LOG_THREAD_LOCAL = new NamedThreadLocal<>("operateLogBaseInfo");
    private static final ThreadLocal<StringBuilder> STRING_BUILDER = new NamedThreadLocal<>("stringBuilder line split");

    private static final String LINE_SPLIT = "\n|\t\t\t";
    private final OperateLogBuffer logBuffer = new OperateLogBuffer();

    @Value("${logger.apiLogger.saveSwitchFlag:true}")
    private boolean loggerSaveSwitchFlag;
    @Value("${logger.apiLogger.stackTraceFlag:true}")
    private boolean stackTraceFlag;

    @Autowired
    private HttpServletRequest request;

    /**
     * <p>
     * Use @ Pointcut to define the pointcut and cut in the code at the annotated position 
     * Turn formatting on and off  @formatter:off
     * try {
     * try {
     *
     * @param
     * @return void
     * @Before method.invoke(..);
     * } finally {
     * @After //Post notification, executed after method execution 
     * }
     * @AfterReturning //Return the notification and execute it after the method returns the result 
     * } catch () {
     * @AfterThrowing }
     * Reopen formatting  @formatter:on
     * <p/>
     * @Date 2022/3/10 11:22
     */
    @Pointcut("@annotation(cn.smilehappiness.aspect.operate.OperateLog)")
    public void logPointCut() {

    }

    /**
     * <p>
     * The pre-notification is used to intercept the start time of the user's operation recorded by the Controller layer and the core request parameter information 
     * <p/>
     *
     * @param joinPoint
     * @return void
     * @Date 2022/3/14 17:28
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (!loggerSaveSwitchFlag) {
            return;
        }

        START_TIME_THREAD_LOCAL.remove();
        LOG_THREAD_LOCAL.remove();
        STRING_BUILDER.remove();

        LocalDateTime startTime = LocalDateTime.now();
        //Thread binding variable (this data is only visible to the currently requested thread ）
        START_TIME_THREAD_LOCAL.set(startTime);

        StringBuilder stringBuilder = new StringBuilder();
        this.printStart(stringBuilder);
        stringBuilder.append(LINE_SPLIT);
        stringBuilder.append("Implementation method ：");

        OperateLogBaseInfo logBaseInfo = this.fillBaseLogInfo(joinPoint, stringBuilder);
        LOG_THREAD_LOCAL.set(logBaseInfo);
        stringBuilder.append(LINE_SPLIT);
        STRING_BUILDER.set(stringBuilder);
    }

    /**
     * <p>
     * Post-notification is used to intercept the time-consuming payment request of the user's business operation recorded by the Controller layer 
     * <p/>
     *
     * @param joinPoint
     * @return void
     * @Date 2022/3/14 17:28
     */
    @After("logPointCut()")
    public void doAfter(JoinPoint joinPoint) throws Throwable {
        if (!loggerSaveSwitchFlag) {
            return;
        }

        OperateLogBaseInfo logBaseInfo = LOG_THREAD_LOCAL.get();
        StringBuilder stringBuilder = STRING_BUILDER.get();
        if (logBaseInfo == null || stringBuilder == null) {
            return;
        }

        LocalDateTime startTime = START_TIME_THREAD_LOCAL.get();
        //Acquisition time 
        long takeTime = DateUtil.getTakeTime(startTime, LocalDateTime.now(), TimeUnit.MILLISECONDS);
        logBaseInfo.setOperationTakeTime(takeTime);
        logBaseInfo.setModifyTime(new Date());

        //Call time 
        stringBuilder.append(LINE_SPLIT);
        stringBuilder.append("Time (ms ）：");
        stringBuilder.append(takeTime);

        //Asynchronous storage of modification results and update of modification time 
        logBuffer.enqueue(logBaseInfo);

        printEnd(stringBuilder);
        LOG.info("{}", stringBuilder);

        LOG_THREAD_LOCAL.set(logBaseInfo);
        STRING_BUILDER.set(stringBuilder);
    }

    /**
     * <p>
     * Result set processing 
     * <p/>
     *
     * @param result
     * @return void
     * @Date 2022/3/14 16:03
     */
    @AfterReturning(returning = "result", pointcut = "logPointCut()")
    public void doAfterReturning(Object result) throws Throwable {
        if (!loggerSaveSwitchFlag) {
            return;
        }

        OperateLogBaseInfo logBaseInfo = LOG_THREAD_LOCAL.get();
        StringBuilder stringBuilder = STRING_BUILDER.get();
        if (logBaseInfo == null || stringBuilder == null) {
            return;
        }

        //Response results 
        if (result == null) {
            LOG.warn("doAfterReturning result is null");
        }

        stringBuilder.append("【ApiLogger】Return results ：").append(JSON.toJSONString(result));
        logBaseInfo.setResponseStr(JSON.toJSONString(result));

        LOG_THREAD_LOCAL.set(logBaseInfo);
        STRING_BUILDER.set(stringBuilder);
    }

    /**
     * <p>
     * Exception notification records error log information 
     * <p/>
     *
     * @param joinPoint
     * @param throwable
     * @return void
     * @Date 2022/3/14 18:02
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        if (!loggerSaveSwitchFlag) {
            return;
        }

        OperateLogBaseInfo logBaseInfo = LOG_THREAD_LOCAL.get();
        StringBuilder stringBuilder = STRING_BUILDER.get();

        if (throwable instanceof Exception) {
            Exception exception = (Exception) throwable;
            if (stringBuilder != null) {
                if (stackTraceFlag) {
                    exception.printStackTrace();
                }

                LOG.error("【{}】Exception occurred during execution, exception information ：【{}】", logBaseInfo.getRequestUrl(), exception.getMessage());
                stringBuilder.append("【").append(logBaseInfo.getRequestUrl()).append("】Exception occurred during execution, exception information ：【").append(exception.getMessage()).append("】");
                logBaseInfo.setErrorMessage(StringUtils.join("【", logBaseInfo.getRequestUrl(), "】Exception occurred during execution, exception information ：【", exception.getMessage(), "】"));

                if (exception instanceof AbstractBizException) {
                    //Throw business exception 
                    AbstractBizException baseException = (AbstractBizException) exception;
                    if (StringUtils.isNotBlank(baseException.getCode())) {
                        throw new BusinessException(baseException.getCode(), baseException.getBizCode(), exception.getLocalizedMessage());
                    } else {
                        //If no status code is set, return by default 500
                        throw new BusinessException(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), baseException.getBizCode(), exception.getLocalizedMessage());
                    }
                } else {
                    //Throw system exception 
                    throw new SystemInternalException(exception.getMessage());
                }
            }
        }

        if (throwable != null && stringBuilder != null) {
            if (stackTraceFlag) {
                throwable.printStackTrace();
            }

            LOG.error("Throwable-Exception in recording request data, exception information ：【{}】", throwable.getMessage());
            stringBuilder.append("Exception in recording request data, exception information ：【").append(throwable.getMessage()).append("】");
            logBaseInfo.setErrorMessage("Throwable-Exception in recording request data, exception information ：【" + throwable.getMessage() + "】");

            throw new SystemInternalException(throwable.getMessage());
        }
    }

    /**
     * <p>
     * Fill in basic operation log information 
     * <p/>
     *
     * @param joinPoint
     * @param stringBuilder
     * @return cn.smilehappiness.aspect.operate.OperateLogBaseInfo
     * @Date 2022/3/10 11:47
     */
    private OperateLogBaseInfo fillBaseLogInfo(JoinPoint joinPoint, StringBuilder stringBuilder) {
        OperateLogBaseInfo logInfo = new OperateLogBaseInfo();

        //Request address 
        String requestUrl = request.getRequestURI();
        //Requested method type (post/get)
        String requestType = request.getMethod();
        //request ip
        String requestIp = getIPAddress(request);

        //header
        String businessModuleName = request.getHeader("bizName");
        String userId = request.getHeader("userId");
        String userName = request.getHeader("userName");

        //user id
        logInfo.setUserId(userId);
        //User name 
        logInfo.setUserName(userName);
        //request url
        logInfo.setRequestUrl(requestUrl);
        //Request method 
        logInfo.setRequestType(requestType);
        //Requested business module name 
        logInfo.setBusinessModuleName(businessModuleName);
        //visit ip
        logInfo.setRequestIp(requestIp);
        //Record time 
        logInfo.setCreateTime(new Date());

        //Get the description information of the method and class attribute information in the annotation 
        this.initMethodDescription(joinPoint, logInfo);

        //Request parameters 
        String reqParam = "";
        if ("GET".equals(requestType)) {
            reqParam = getGetParams(joinPoint, request);
        }
        if ("POST".equals(requestType)) {
            reqParam = getPostParams(joinPoint);
        }
        //Request parameters 
        logInfo.setRequestParams(reqParam);

        //The following methods are used to splice the printed log information 
        this.appendLogInfo(stringBuilder, joinPoint);

        return logInfo;
    }

    /**
     * <p>
     * Get the description information of the method and class attribute information in the annotation 
     * <p/>
     *
     * @param joinPoint
     * @param logInfo
     * @return void
     * @Date 2022/3/14 17:15
     */
    public void initMethodDescription(JoinPoint joinPoint, OperateLogBaseInfo logInfo) {
        //Obtain method signature (obtain target method information through this signature )
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        /*//Get the bytecode object of the class, and obtain the method information through the bytecode object 
        Class<?> classTarget = joinPoint.getTarget().getClass();
        if (StringUtils.isBlank(logInfo.getBusinessModuleName())) {
            FeignClient feignClient = classTarget.getAnnotation(FeignClient.class);
            //Requested business module name 
            if (feignClient != null) {
                logInfo.setBusinessModuleName(feignClient.name());
            }
        }*/

        /*String className = classTarget.getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, parameterTypes);
        String methodName = objMethod.getName();*/

        OperateLog operateLog = method.getAnnotation(OperateLog.class);
        //pedagogical operation 
        String operationDescribe = operateLog != null ? operateLog.value() : StringUtils.EMPTY;

        //Method name 
        logInfo.setMethodName(methodName);
        //Class name 
        logInfo.setClassName(className);
        //pedagogical operation 
        logInfo.setOperationDescribe(operationDescribe);
    }

    /**
     * <p>
     * Splice printed log information 
     * <p/>
     *
     * @param stringBuilder
     * @param joinPoint
     * @return void
     * @Date 2022/3/10 13:05
     */
    private void appendLogInfo(StringBuilder stringBuilder, JoinPoint joinPoint) {
        //Obtain method signature (obtain target method information through this signature )
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        stringBuilder.append(method.toGenericString());
        stringBuilder.append(LINE_SPLIT);
        stringBuilder.append("The request parameters are ：");
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            stringBuilder.append(LINE_SPLIT);
            stringBuilder.append("\t\t");
            stringBuilder.append(i);
            stringBuilder.append(":");
            Object arg = joinPoint.getArgs()[i];
            if (arg instanceof ServletResponse || arg instanceof ServletRequest) {
                continue;
            }
            stringBuilder.append(JSON.toJSONString(arg));
        }
    }

    /**
     * <p>
     * Get request ip information 
     * <p/>
     *
     * @param request
     * @return java.lang.String
     * @Date 2022/3/10 11:48
     */
    public String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid Service Agent 
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache Service Agent 
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic Service Agent 
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：Some proxy servers 
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_X_FORWARDED");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_FORWARDED");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_VIA");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("REMOTE_ADDR");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginxService Agent 
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //Some networks use multi-layer proxy, so there will be multiple IPs obtained, which are generally separated by commas (,), and the first IP is the real IP of the client IP
        //If multi-level reverse proxy is passed, the value of X-Forwarded-For is not one, but a string of IP values. The first non-unknown valid IP string in X-Forwarded-For is true ip
        if (ipAddresses != null && ipAddresses.split(",").length > 0) {
            ip = ipAddresses.split(",")[0];
        }

        //It still cannot be obtained. Finally, it can be obtained through request. getRemoteAddr() 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * <p>
     * Get request parameters 
     * <p/>
     *
     * @param request
     * @param joinPoint
     * @return java.lang.String
     * @Date 2021/8/28 16:10
     */
    private String getGetParams(JoinPoint joinPoint, HttpServletRequest request) {
        // Return the parameter part in the request line 
        // String params = request.getQueryString();
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String[]> map = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entrySet = map.entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();
            String value = ((String[]) entry.getValue())[0];
            stringBuilder.append(key).append("=").append(value).append("&");
        }

        String params = stringBuilder.toString();
        params = StringUtils.isEmpty(params) ? "" : params.substring(0, params.length() - 1);

        if (StringUtils.isNotBlank(params)) {
            return params;
        }

        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            stringBuilder.append(i);
            stringBuilder.append(":");
            Object arg = joinPoint.getArgs()[i];
            if (arg instanceof ServletResponse || arg instanceof ServletRequest) {
                continue;
            }
            stringBuilder.append(arg);

            if (i != joinPoint.getArgs().length - 1) {
                stringBuilder.append(" ,");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * <p>
     * Get post request parameters 
     * <p/>
     *
     * @param joinPoint
     * @return java.lang.String
     * @Date 2021/8/28 15:40
     */
    private String getPostParams(JoinPoint joinPoint) {
        if (joinPoint.getArgs() == null) {
            return StringUtils.EMPTY;
        }

        return JSON.toJSONString(joinPoint.getArgs());
    }

    private void printStart(StringBuilder stringBuilder) {
        stringBuilder.append("\n/--------------------------------------------------------------------");
    }

    private void printEnd(StringBuilder stringBuilder) {
        stringBuilder.append("\n\\--------------------------------------------------------------------");
    }

}
