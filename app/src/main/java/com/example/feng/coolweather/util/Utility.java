package com.example.feng.coolweather.util;

import android.text.TextUtils;

import com.example.feng.coolweather.db.City;
import com.example.feng.coolweather.db.Country;
import com.example.feng.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Feng on 2017/9/3.
 */

public class Utility {
    public static boolean handleprovinceresponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allprovince =new JSONArray(response);
                for(int i=0;i<allprovince.length();i++){
                    JSONObject provinceobject =allprovince.getJSONObject(i);
                    Province province =new Province();
                    province.setProvinceName(provinceobject.getString("name"));
                    province.setProvinceCode(provinceobject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
      return false;
    }

    public static boolean handlecityresponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allcities =new JSONArray(response);
                for(int i=0;i<allcities.length();i++){
                   JSONObject cityobject =allcities.getJSONObject(i);
                    City city =new City();
                    city.setCityname(cityobject.getString("name"));
                    city.setCityCode(cityobject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean handleCountryResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for(int j=0;j<allCounties.length();j++) {
                    JSONObject countryobject = allCounties.getJSONObject(j);
                    Country country =new Country();
                    country.setCountryname(countryobject.getString("name"));
                    country.setWeatherid(countryobject.getString("weather_id"));
                    country.setCityid(cityId);
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
