package com.example.feng.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Feng on 2017/9/2.
 */

public class Country extends DataSupport {
    private int id;
    private String countryname;
    private String weatherid;
    private int cityid;

    public void setcityid(int id) {
        this.id = id;
    }

    public int getid() {
        return id;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getCountryname() {
        return countryname;
    }

    public String getWeatherid() {
        return weatherid;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public void setWeatherid(String weatherid) {
        this.weatherid = weatherid;
    }


}
