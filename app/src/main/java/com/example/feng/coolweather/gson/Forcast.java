package com.example.feng.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Feng on 2017/9/4.
 */

public class Forcast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
