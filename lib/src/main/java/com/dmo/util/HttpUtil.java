package com.dmo.util;


import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Http client util
 */
public class HttpUtil {

    private HttpUtil () {}

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient CLIENT;

    static {
        CLIENT = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /*
     * @Description : 常规的POST请求
     * @author      : yaoyuan
     * @date        : 2025/6/25 15:46
     */
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return getResponseBody(request);
    }

    private static String getResponseBody(Request request) throws IOException {
        Response response = CLIENT.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return StringUtils.EMPTY;
        }
        String result = responseBody.string();
        response.close();
        return result;
    }
}
