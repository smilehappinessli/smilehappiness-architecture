package com.smilehappiness.aspect.operate;

import com.alibaba.fastjson.JSON;
import com.smilehappiness.enums.ResultCodeEnum;
import com.smilehappiness.exception.exceptions.AbstractBizException;
import com.smilehappiness.exception.exceptions.BusinessException;
import com.smilehappiness.exception.exceptions.SystemInternalException;
import com.smilehappiness.result.CommonResult;
import com.smilehappiness.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
 * 定义日志切面类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/28 13:15
 */
@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    private final static String lineSplit = "\n|\t\t\t";
    private OperateLogBuffer logBuffer = new OperateLogBuffer();

    /**
     * <p>
     * 使用@Pointcut定义切点，在加注解的位置切入代码
     * <p/>
     *
     * @param
     * @return void
     * @Date 2021/8/28 15:16
     */
    @Pointcut("@annotation(com.smilehappiness.aspect.operate.OperateLog)")
    public void logPointCut() {

    }

    /**
     * <p>
     * try{
     *      try{
     *          //@Before
     *          method.invoke(..);
     *      }finally{
     *          //@After
     *      }
     *      //@AfterReturning
     * }catch(){
     *      //@AfterThrowing
     * }
     * <p/>
     *
     * @param joinPoint
     * @return java.lang.Object
     * @Date 2021/8/28 16:18
     */
    @Around("logPointCut()")
    public Object aroundHandler(ProceedingJoinPoint joinPoint) {
        LocalDateTime startTime = LocalDateTime.now();
        OperateLogBaseInfo logBaseInfo = null;
        Object proceedObj = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            printStart(stringBuilder);
            stringBuilder.append(lineSplit);
            stringBuilder.append("执行方法:");

            //赋值bizId
            /*if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] instanceof EngineRequest) {
                EngineRequest requestDto = JSON.parseObject(JSON.toJSONString(joinPoint.getArgs()[0]), EngineRequest.class);
                if (requestDto != null) {
                    logBaseInfo.setBizId(requestDto.getBizId());
                }
            }*/

            logBaseInfo = fillBaseLogInfo(joinPoint, stringBuilder);
            stringBuilder.append(lineSplit);

            //正常执行业务方法
            //LocalDateTime bizTimeStart = LocalDateTime.now();
            proceedObj = joinPoint.proceed();
            //LocalDateTime bizTimeEnd = LocalDateTime.now();
            //log.info("业务方法执行耗时（毫秒）：{}", getTakeTime(bizTimeStart, bizTimeEnd, TimeUnit.MILLISECONDS));

            stringBuilder.append("返回值:").append(JSON.toJSONString(proceedObj));
            return proceedObj;
        } catch (Exception exception) {
            //exception.printStackTrace();

            log.error("记录请求数据出现异常，异常信息：【{}】", exception.getMessage());
            stringBuilder.append("记录请求数据出现异常，异常信息：【").append(exception.getMessage()).append("】");
            if (logBaseInfo != null) {
                logBaseInfo.setErrorMessage("记录请求数据出现异常，异常信息：【" + exception.getMessage() + "】");
            }

            if (exception instanceof AbstractBizException) {
                //抛出业务异常
                AbstractBizException baseException = (AbstractBizException) exception;
                if (StringUtils.isNotBlank(baseException.getCode())) {
                    throw new BusinessException(baseException.getCode(), baseException.getBizCode(), exception.getLocalizedMessage());
                } else {
                    //如果没有设置状态码，默认返回500
                    throw new BusinessException(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), baseException.getBizCode(), exception.getLocalizedMessage());
                }
            } else {
                //抛出系统异常
                throw new SystemInternalException(exception.getMessage());
            }
        } catch (Throwable throwable) {
            //throwable.printStackTrace();

            log.error("Throwable-记录请求数据出现异常，异常信息：【{}】", throwable.getMessage());
            stringBuilder.append("记录请求数据出现异常，异常信息：【").append(throwable.getMessage()).append("】");
            if (logBaseInfo != null) {
                logBaseInfo.setErrorMessage("Throwable-记录请求数据出现异常，异常信息：【" + throwable.getMessage() + "】");
            }

            throw new SystemInternalException(throwable.getMessage());
        } finally {
            //获取耗时时间
            long takeTime = DateUtil.getTakeTime(startTime, LocalDateTime.now(), TimeUnit.MILLISECONDS);
            if (logBaseInfo != null) {
                logBaseInfo.setOperationTakeTime(takeTime);
                logBaseInfo.setModifyTime(new Date());
                //响应结果
                if (proceedObj != null) {
                    logBaseInfo.setResponseStr(JSON.toJSONString(proceedObj));
                }

                //异步存储修改结果和更新修改时间
                logBuffer.enqueue(logBaseInfo);
            }

            //调用
            stringBuilder.append(lineSplit);
            stringBuilder.append("耗时（毫秒）：");
            stringBuilder.append(takeTime);
            printEnd(stringBuilder);
            log.info(stringBuilder.toString());
        }
    }

    /**
     * <p>
     * 填充基础操作日志信息
     * <p/>
     *
     * @param joinPoint
     * @param stringBuilder
     * @return com.smilehappiness.aspect.operate.OperateLogBaseInfo
     * @Date 2021/8/28 16:56
     */
    private OperateLogBaseInfo fillBaseLogInfo(ProceedingJoinPoint joinPoint, StringBuilder stringBuilder) throws NoSuchMethodException {
        OperateLogBaseInfo logInfo = new OperateLogBaseInfo();
        ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = servletRequestAttributes != null ? servletRequestAttributes.getRequest() : null;
        if (request == null) {
            throw new SystemInternalException("ServletRequestAttributes参数为空！");
        }

        //请求地址
        String requestUrl = request.getRequestURI();
        //请求方式
        String requestType = request.getMethod();
        //ip
        String requestIp = getIPAddress(request);

        //header
        String businessModuleName = request.getHeader("businessModuleName");
        String userId = request.getHeader("userId");
        String userName = request.getHeader("userName");

        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        //获取类的字节码对象，通过字节码对象获取方法信息
        Class<?> classTarget = joinPoint.getTarget().getClass();
        //String className = classTarget.getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, parameterTypes);
        //String methodName = objMethod.getName();
        OperateLog operateLog = objMethod.getAnnotation(OperateLog.class);
        //操作描述
        String operationDescribe = operateLog != null ? operateLog.value() : StringUtils.EMPTY;

        //请求参数
        String reqParam = "";
        if ("GET".equals(requestType)) {
            reqParam = getGetParams(joinPoint, request);
        }
        if ("POST".equals(requestType)) {
            reqParam = getPostParams(joinPoint);
        }

        //用户id
        logInfo.setUserId(userId);
        //用户名称
        logInfo.setUserName(userName);
        //请求url
        logInfo.setRequestUrl(requestUrl);
        //方法名
        logInfo.setMethodName(methodName);
        //类名
        logInfo.setClassName(className);
        //请求方式
        logInfo.setRequestType(requestType);
        //请求的业务模块名称
        logInfo.setBusinessModuleName(businessModuleName);
        //访问ip
        logInfo.setRequestIp(requestIp);
        //操作描述
        logInfo.setOperationDescribe(operationDescribe);
        //请求参数
        logInfo.setRequestParams(JSON.toJSONString(reqParam));
        //记录时间
        logInfo.setCreateTime(new Date());
        //修改时间
        logInfo.setModifyTime(new Date());

        log.info("基础日志操作信息：{}", JSON.toJSONString(logInfo));

        //以下方法拼接打印的日志信息
        appendLogInfo(stringBuilder, objMethod, joinPoint);

        return logInfo;
    }

    /**
     * 以下方法拼接打印的日志信息
     *
     * @param stringBuilder
     * @param objMethod
     * @param joinPoint
     */
    private void appendLogInfo(StringBuilder stringBuilder, Method objMethod, ProceedingJoinPoint joinPoint) {
        stringBuilder.append(objMethod.toGenericString());
        stringBuilder.append(lineSplit);
        stringBuilder.append("请求参数依次为:");
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            stringBuilder.append(lineSplit);
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
     * 获取get请求参数
     * <p/>
     *
     * @param request
     * @param joinPoint
     * @return java.lang.String
     * @Date 2021/8/28 16:10
     */
    private String getGetParams(ProceedingJoinPoint joinPoint, HttpServletRequest request) {
//        String params = request.getQueryString();//返回请求行中的参数部分
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
     * 获取post请求参数
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

    /**
     * <p>
     * 获取请求ip信息
     * <p/>
     *
     * @param request
     * @return java.lang.String
     * @Date 2021/8/28 15:20
     */
    public String getIPAddress(HttpServletRequest request) {

        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
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
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        //如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，X-Forwarded-For中第一个非unknown的有效IP字符串才是真实ip
        if (ipAddresses != null && ipAddresses.split(",").length > 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr()获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private void printStart(StringBuilder stringBuilder) {
        stringBuilder.append("\n/--------------------------------------------------------------------");
    }

    private void printEnd(StringBuilder stringBuilder) {
        stringBuilder.append("\n\\--------------------------------------------------------------------");
    }
}
