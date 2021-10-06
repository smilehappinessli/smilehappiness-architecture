package com.smilehappiness.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * <p>
 * 钉钉预警通知工具类
 * <p/>
 *
 * @author smilehappiness
 * @Date 2021/8/30 19:08
 */
public class DingTalkUtil {

    public static void sendMsgToDingTalk(String url, String content, Exception e1) {
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content + "：：：" + getExceptionDetail(e1));
        request.setText(text);
        try {
            client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendTextToDingTalk(String url, String content, String msg) {
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content + "：：：" + msg);
        request.setText(text);
        try {
            client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }


    public static String getExceptionDetail(Exception ex) {
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
