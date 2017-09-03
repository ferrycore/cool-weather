package com.example.feng.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Feng on 2017/9/3.
 */

public class HttpUtil {
    public static void sendOkHttprequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request =new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
