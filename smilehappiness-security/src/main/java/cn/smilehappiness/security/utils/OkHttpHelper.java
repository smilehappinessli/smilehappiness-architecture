package cn.smilehappiness.security.utils;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OkHttpHelper
 * <p/>
 *
 * @author
 * @Date 2023/3/10 10:50
 */
public class OkHttpHelper {

    public static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);
    private static final OkHttpClient CLIENT;

    private OkHttpHelper() {
    }

    public static OkHttpClient client() {
        return CLIENT;
    }

    public static String request(Request request) {
        try {
            Response response = CLIENT.newCall(request).execute();
            return response.body().string();
        } catch (Exception var2) {
            logger.error(" request was aborted", var2);
            return null;
        }
    }

    public static String request(Request request, OkHttpClient httpClient) {
        try {
            Response response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception var2) {
            logger.error(" request was aborted", var2);
            return null;
        }
    }

    public static <T> T request(Request request, Class<T> clazz) {
        String result = request(request);
        return JSON.parseObject(result, clazz);
    }

    public static void requestAsync(Request request, Callback callback) {
        CLIENT.newCall(request).enqueue(callback);
    }

    public static String get(String url, Map<String, String> headers) {
        Builder builder = getBuilder(url, headers);
        Request request = builder.build();
        return request(request);
    }

    public static <T> T get(String url, Map<String, String> headers, Class<T> clazz) {
        String result = get(url, headers);
        return JSON.parseObject(result, clazz);
    }

    public static void getAsync(String url, Map<String, String> headers, Callback callback) {
        Builder builder = getBuilder(url, headers);
        Request request = builder.build();
        requestAsync(request, callback);
    }

    public static String postJson(String url, Map<String, String> headers, Object body) {
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, JSON.toJSONString(body));
        return post(url, headers, requestBody);
    }

    /**
     * <p>
     *  Synchronize post request
     * <p/>
     *
     * @param url
     * @param headers
     * @param body
     * @param httpClient  Use separate client calls
     * @return java.lang.String
     * @Date 2022/5/16 13:16
     */
    public static String postJson(String url, Map<String, String> headers, Object body, OkHttpClient httpClient) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        Builder builder = getBuilder(url, headers);
        RequestBody requestBody = RequestBody.create(OkHttpHelper.MEDIA_JSON, JSON.toJSONString(body));
        Request request = builder.post(requestBody).build();
        return request(request, httpClient);
    }

    public static <T> T postJson(String url, Map<String, String> headers, Object body, Class<T> clazz) {
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, JSON.toJSONString(body));
        return post(url, headers, requestBody, clazz);
    }

    public static void postJsonAsync(String url, Map<String, String> headers, Object body, Callback callback) {
        RequestBody requestBody = RequestBody.create(MEDIA_JSON, JSON.toJSONString(body));
        postAsync(url, headers, requestBody, callback);
    }

    public static String post(String url, Map<String, String> headers, RequestBody body) {
        Builder builder = getBuilder(url, headers);
        Request request = builder.post(body).build();
        return request(request);
    }

    public static <T> T post(String url, Map<String, String> headers, RequestBody body, Class<T> clazz) {
        String result = post(url, headers, body);
        return JSON.parseObject(result, clazz);
    }

    public static void postAsync(String url, Map<String, String> headers, RequestBody body, Callback callback) {
        Builder builder = getBuilder(url, headers);
        Request request = builder.post(body).build();
        requestAsync(request, callback);
    }

    private static Builder getBuilder(String url, Map<String, String> headers) {
        Builder builder = (new Builder()).url(url);
        if (Objects.nonNull(headers) && headers.size() > 0) {
            Iterator var3 = headers.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, String> header = (Map.Entry) var3.next();
                builder.header((String) header.getKey(), (String) header.getValue());
            }
        }

        return builder;
    }

    static {
        CLIENT = (new OkHttpClient.Builder()).connectTimeout(15L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS).build();
    }
}
