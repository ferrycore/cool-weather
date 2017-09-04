package com.example.feng.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Feng on 2017/9/4.
 */

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;

    }
}
