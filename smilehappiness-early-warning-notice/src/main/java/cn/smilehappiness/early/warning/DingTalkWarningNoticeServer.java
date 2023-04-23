package cn.smilehappiness.early.warning;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Nail warning notification service 
 * <p/>
 *
 * @author
 * @Date 2021/8/30 20:16
 */
@RefreshScope
@Component
@Slf4j
public class DingTalkWarningNoticeServer {

    private static ThreadPoolTaskExecutor TASK_EXECUTOR = null;

    @PostConstruct
    private void initConstruct() {
        if (dingTalkProperties != null && dingTalkProperties.getOpenSwitch() && (CollectionUtils.isNotEmpty(dingTalkProperties.getErrorDingNoticeUrl())
                                                                || CollectionUtils.isNotEmpty(dingTalkProperties.getWarnDingNoticeUrl())
                                                                || CollectionUtils.isNotEmpty(dingTalkProperties.getGeneralDingNoticeUrl()))
        ) {
            TASK_EXECUTOR = new ThreadPoolTaskExecutor();
            TASK_EXECUTOR.setCorePoolSize(1);
            TASK_EXECUTOR.setMaxPoolSize(1);
            TASK_EXECUTOR.setQueueCapacity(dingTalkProperties.getQueueCapacity());
            TASK_EXECUTOR.setThreadNamePrefix("dingTalkExecutor--");
            TASK_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
            TASK_EXECUTOR.initialize();
        }
    }

    private static final String ERROR_TYPE = "error";
    private static final String WARN_TYPE = "warn";

    private static final String LIMITER_PASS = "1";

    private static final String LIMITER_NOT_PASS = "0";

    private static final String DING_TALK_LIMITER_SCRIPT = "local nums = redis.call('ZCOUNT', KEYS[1], ARGV[1], ARGV[2]); " +
            "if nums < tonumber(ARGV[4]) then " +
            "    redis.call('ZADD', KEYS[1], ARGV[2], ARGV[3]); " +
            "    redis.call('ZREMRANGEBYSCORE', KEYS[1], 0, tonumber(ARGV[1])-1); " +
            "    return '" + LIMITER_PASS + "'; " +
            "else " +
            "    return '" + LIMITER_NOT_PASS + "'; " +
            "end";

    private static final String CURRENT_LIMITER_REDIS_KEY = ":dingtalk:message:limit:";

    public static final String PROD_ENV = "prod";

    private static final String SPLIT_FLAG = "access_token=";

    @Value("${spring.profiles.active:dev}")
    private String env;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private DingTalkProperties dingTalkProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * <p>
     * Send warn-level information. If it is a prod environment, the information will be notified to the prod warning group 
     * <p/>
     *
     * @param content
     * @return void
     * @Date 2022/5/8 19:00
     */
    public void sendWarningMessage(String content) {
        try {
            List<String> noticeUrl = dingTalkProperties.getWarnDingNoticeUrl();
            //General alert notification group (dev, test, sit, uat )
            if (!PROD_ENV.equals(env)) {
                noticeUrl = dingTalkProperties.getGeneralDingNoticeUrl();
            }

            //Nail
            submitMessage(noticeUrl, WARN_TYPE, null, content, null);
        } catch (DingTalkException e) {
            throw e;
        } catch (Exception e) {
            log.error("dingTalk sendWarningMessage error", e);
        }
    }

    public void sendWarningMessage(String title, String content) {
        try {
            List<String> noticeUrl = dingTalkProperties.getWarnDingNoticeUrl();
            //General alert notification group (dev, test, sit, uat )
            if (!PROD_ENV.equals(env)) {
                noticeUrl = dingTalkProperties.getGeneralDingNoticeUrl();
            }

            //Nail
            submitMessage(noticeUrl, WARN_TYPE, title, content, null);
        } catch (DingTalkException e) {
            throw e;
        }  catch (Exception e) {
            log.error("dingTalk sendWarningMessage error", e);
        }
    }

    /**
     * <p>
     * Send error level information. If it is a prod environment, the information will be notified to the prod warning group 
     * <p/>
     *
     * @param content
     * @return void
     * @Date 2022/5/8 19:00
     */
    public void sendErrorMessage(String content) {
        try {
            List<String> noticeUrl = dingTalkProperties.getErrorDingNoticeUrl();
            //General alert notification group (dev, test, sit, uat )
            if (!PROD_ENV.equals(env)) {
                noticeUrl = dingTalkProperties.getGeneralDingNoticeUrl();
            }

            //Nail
            submitMessage(noticeUrl, ERROR_TYPE, null, content, null);
        } catch (DingTalkException e) {
            throw e;
        }  catch (Exception e) {
            log.error("dingTalk sendErrorMessage error", e);
        }
    }

    public void sendErrorMessage(String title, String content) {
        try {
            List<String> noticeUrl = dingTalkProperties.getErrorDingNoticeUrl();
            //General alert notification group (dev, test, sit, uat )
            if (!PROD_ENV.equals(env)) {
                noticeUrl = dingTalkProperties.getGeneralDingNoticeUrl();
            }

            //Nail
            submitMessage(noticeUrl, ERROR_TYPE, title, content, null);
        } catch (DingTalkException e) {
            throw e;
        }  catch (Exception e) {
            log.error("dingTalk sendErrorMessage error", e);
        }
    }

    /**
     * <p>
     * Notification alert 
     * <p/>
     *
     * @param message
     * @param exception
     * @return void
     * @Date 2020/12/15 18:16
     */
    public void sendErrorMsgToDingTalk(String message, Exception exception) {
        try {
            List<String> noticeUrl = dingTalkProperties.getWarnDingNoticeUrl();
            //General alert notification group (dev, test, sit, uat )
            if (!PROD_ENV.equals(env)) {
                noticeUrl = dingTalkProperties.getGeneralDingNoticeUrl();
            }

            //Notification alert
            submitMessage(noticeUrl, ERROR_TYPE, null, message, exception);
        } catch (DingTalkException e) {
            throw e;
        }  catch (Exception e) {
            log.error("dingTalk sendErrorMsgToDingTalk error", e);
        }
    }

    /**
     * <p>
     * Notification alert 
     * <p/>
     *
     * @param message
     * @return void
     * @Date 2020/12/15 18:16
     */
    public void sendTextToDingTalk(String message) {
        try {
            List<String> noticeUrl = dingTalkProperties.getWarnDingNoticeUrl();
            //General alert notification group (dev, test, sit, uat )
            if (!PROD_ENV.equals(env)) {
                noticeUrl = dingTalkProperties.getGeneralDingNoticeUrl();
            }

            //Notification alert
            submitMessage(noticeUrl, null, null, message, null);
        } catch (DingTalkException e) {
            throw e;
        } catch (Exception e) {
            log.error("dingTalk sendTextToDingTalk error", e);
        }
    }

    private void submitMessage(List<String> urls, String type, String title, String content, Exception e) {
        if (StringUtils.isBlank(content)) {
            log.error("DingTalk message content cannot be empty!");
            return;
        }
        if (CollectionUtils.isEmpty(urls)) {
            log.error(type + " type dingTalk url not configure!");
            return;
        }
        if (TASK_EXECUTOR == null) {
            throw new DingTalkException("Please configure dingTalk information!");
        }

        String msgBody = wrapMsgBody(type, title, content, e);
        TASK_EXECUTOR.execute(() -> sendMsgToDingTalk(urls, msgBody, 1));
    }

    private void sendMsgToDingTalk(List<String> urls, String msgBody, int retryTimes) {

        for (String url : urls) {
            try {
                String[] split = url.split(SPLIT_FLAG);
                if (split.length != 2) {
                    continue;
                }
                String redisKey = applicationName + CURRENT_LIMITER_REDIS_KEY + split[1];
                long now = System.currentTimeMillis();
                long limitTimeAgo = now - dingTalkProperties.getLimitTimeSecond() * 1000;
                String result = redisTemplate.execute(RedisScript.of(DING_TALK_LIMITER_SCRIPT, String.class), Collections.singletonList(redisKey), Long.toString(limitTimeAgo), Long.toString(now), UUID.randomUUID().toString(), dingTalkProperties.getLimitNumber().toString());

                if (LIMITER_PASS.equals(result)) {
                    callDingTalkClient(msgBody, url);
                    return;
                }
            } catch (Exception e) {
                log.error("DingTalk sendMsgToDingTalk error, url={}", url, e);
            }
        }

        if (retryTimes <= (dingTalkProperties.getLimitTimeSecond() / dingTalkProperties.getSleepSeconds() + 1)) {
            try {
                log.info("dingTalk turn into sleep");
                TimeUnit.SECONDS.sleep(dingTalkProperties.getSleepSeconds());
            } catch (InterruptedException e){}
            // Throw back to queue and queue again
            TASK_EXECUTOR.execute(() -> sendMsgToDingTalk(urls, msgBody, retryTimes + 1));
        } else {
            log.error("Drop dingTalk message; msgBody: {}", msgBody);
        }
    }

    private void callDingTalkClient(String msgBody, String url) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(msgBody);
        request.setText(text);
        client.execute(request);
    }

    private String wrapMsgBody(String type, String title, String content, Exception e1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("env: ").append(env).append("\n");
        if (StringUtils.isNotBlank(type)) {
            stringBuilder.append("type: ").append(type).append("\n");
        }
        if (StringUtils.isNotBlank(title)) {
            stringBuilder.append("[").append(title).append("] \n");
        }
        if (StringUtils.isNotBlank(content)) {
            stringBuilder.append(e1 == null? content : (content + ":::" + getExceptionDetail(e1)));
        } else if (e1 != null){
            stringBuilder.append(getExceptionDetail(e1));
        }
        return stringBuilder.toString();
    }


    private String getExceptionDetail(Exception ex) {
        String ret = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            pout.close();
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
}
