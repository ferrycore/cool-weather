package com.example.feng.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Feng on 2017/9/4.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public Carcash carcash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;

    }
public class Carcash{
    @SerializedName("txt")
    public String info;

}
public class Sport{
    @SerializedName("txt")
    public String info;

}

}
