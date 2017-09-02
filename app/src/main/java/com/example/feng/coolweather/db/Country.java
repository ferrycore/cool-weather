package com.example.feng.coolweather.db;

/**
 * Created by Feng on 2017/9/2.
 */

public class Country {
    private int id;
    private String countryname;
    private String weatherid;
    private int cityid;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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
