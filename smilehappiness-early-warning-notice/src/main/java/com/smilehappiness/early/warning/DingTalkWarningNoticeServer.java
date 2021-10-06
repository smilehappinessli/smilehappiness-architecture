package com.smilehappiness.early.warning;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.smilehappiness.utils.DingTalkUtil;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 钉钉预警通知服务
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 20:16
 */
@Component
public class DingTalkWarningNoticeServer {

    @Value("${spring.profiles.active:dev}")
    private String env;

    /**
     * 一般预警通知群
     */
    @Value("${earlyWarning.notice.generalDingNoticeUrl}")
    private String generalDingNoticeUrl;

    /**
     * 高频异常通知预警
     */
    @Value("${earlyWarning.notice.errorDingNoticeUrl}")
    private String errorDingNoticeUrl;

    /**
     * <p>
     * 通知预警
     * <p/>
     *
     * @param message
     * @param exception
     * @return void
     * @Date 2020/12/15 18:16
     */
    public void sendMsgToDingTalk(String message, Exception exception) {
        //通知预警
        DingTalkUtil.sendMsgToDingTalk(errorDingNoticeUrl, org.apache.commons.lang3.StringUtils.join("【" + env + "】,", "risk:", message), exception);
    }

    /**
     * <p>
     * 通知预警
     * <p/>
     *
     * @param message
     * @return void
     * @Date 2020/12/15 18:16
     */
    public void sendTextToDingTalk(String message) {
        //通知预警
        DingTalkUtil.sendTextToDingTalk(errorDingNoticeUrl, org.apache.commons.lang3.StringUtils.join("【" + env + "】,", "risk:", message), null);
    }

    public void sendTextToDingTalk(String url, String title, String content) {
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(title + "\n" + content);
        request.setText(text);

        try {
            client.execute(request);
        } catch (ApiException var7) {
            var7.printStackTrace();
        }
    }

    public void sendWarningMessage(String content) {
        //钉钉
        sendTextToDingTalk(generalDingNoticeUrl, "【" + env + " - warning】", content);
    }

    public void sendWarningMessage(String title, String content) {
        //钉钉
        sendTextToDingTalk(generalDingNoticeUrl, "【" + env + " - warning - " + title + "】", content);
    }

    public void sendErrorMessage(String content) {
        //钉钉
        sendTextToDingTalk(errorDingNoticeUrl, "【" + env + " - error】", content);
    }

    public void sendErrorMessage(String title, String content) {
        //钉钉
        sendTextToDingTalk(errorDingNoticeUrl, "【" + env + " - error - " + title + "】", content);
    }

}
