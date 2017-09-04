package com.example.feng.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Feng on 2017/9/4.
 */

public class Now {
    @SerializedName("tmp")
    public String tempername;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;

    }

}
