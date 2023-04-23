package cn.smilehappiness.early.warning;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @date 2023/3/23 14:01
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "earlywarning.notice")
public class DingTalkProperties {

    /**
     * Current limiting number
     */
//    @Value("${earlywarning.notice.oneMinuteLimitNumber:20}")
    private Integer limitNumber = 18;

    /**
     * Current limiting time
     */
    private Integer limitTimeSecond = 60;

    /**
     * on-off
     */
    private Boolean openSwitch = true;

    /**
     * sleep time(second)
     */
    private Integer sleepSeconds = 5;

    /**
     * ThreadPool queue capacity
     */
    private Integer queueCapacity = 500;

    /**
     * General alert notification group (dev, test, sit, uat) )
     */
//    @Value("${earlyWarning.notice.generalDingNoticeUrl:xxx}")
    private List<String> generalDingNoticeUrl;

    /**
     * prod-warnLevel exception notification alert
     */
//    @Value("${earlyWarning.notice.warnDingNoticeUrl:xxx}")
    private List<String> warnDingNoticeUrl;

    /**
     * prod-errorLevel exception notification alert
     */
//    @Value("${earlyWarning.notice.errorDingNoticeUrl:xxx}")
    private List<String> errorDingNoticeUrl;

}
