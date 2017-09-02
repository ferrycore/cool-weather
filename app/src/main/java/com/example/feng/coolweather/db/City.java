package com.example.feng.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Feng on 2017/9/2.
 */

public class City extends DataSupport {
    private int id;
    private String cityname;
    private int cityCode;
    private int provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getId() {
        return id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getCityname() {
        return cityname;
    }
}
