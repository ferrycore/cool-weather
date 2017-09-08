package com.example.feng.coolweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.coolweather.gson.Forcast;
import com.example.feng.coolweather.gson.Weather;
import com.example.feng.coolweather.util.HttpUtil;
import com.example.feng.coolweather.util.MainActivity;
import com.example.feng.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherlayout;
    private TextView titlecity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    public SwipeRefreshLayout swipeRefreshLayout;
    private String mWeatherid;
    public DrawerLayout drawerLayout;
    public Button newbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherlayout =(ScrollView)findViewById(R.id.weather_layout);
        titlecity =(TextView)findViewById(R.id.title_city);
        titleUpdateTime =(TextView)findViewById(R.id.title_update_time);
        degreeText =(TextView)findViewById(R.id.degree_text);
        weatherInfoText =(TextView)findViewById(R.id.weather_info_text);
        forecastLayout =(LinearLayout)findViewById(R.id.forecast_layout);
        aqiText =(TextView)findViewById(R.id.aqi_text);
        pm25Text =(TextView)findViewById(R.id.pm25_text);
        comfortText =(TextView)findViewById(R.id.comfort_text);
        carWashText =(TextView)findViewById(R.id.car_wash_text);
        sportText =(TextView)findViewById(R.id.sport_text);
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.wipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        newbutton =(Button)findViewById(R.id.nav_button);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString =prefs.getString("weather",null);
        if(weatherString!=null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
            mWeatherid =weather.basic.weatherId;

        }
        else {

            mWeatherid =getIntent().getStringExtra("weather_id");
            weatherlayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherid);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherid);
            }
        });
    }
    public void requestWeather(final String weatherId){
        String weatherUrl ="http://guolin.tech/api/weather?cityid="+weatherId+"&key=ffd46fb244474d8ebb87d3cc9821280d";
        HttpUtil.sendOkHttprequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_LONG).show();
             swipeRefreshLayout.setRefreshing(false);
                }
            });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            final String responseText =response.body().string();
            final Weather weather =Utility.handleWeatherResponse(responseText);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(weather!=null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor =PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                        showWeatherInfo(weather);
                    }
                    else {
                        Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_LONG).show();

                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            }
        });
    }

    private void showWeatherInfo(Weather weather){
        String cityname =weather.basic.cityname;
        String updatename =weather.basic.update.updatename.split(" ")[1];
        String degeree =weather.now.tempername+"C";
        String weatherInfo =weather.now.more.info;
        titlecity.setText(cityname);
        titleUpdateTime.setText(updatename);
        degreeText.setText(degeree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forcast forcast:weather.forcastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forcast_item,forecastLayout,false);
            TextView datetext =(TextView)view.findViewById(R.id.data_text);
            TextView infotext =(TextView)view.findViewById(R.id.info_text);
            TextView maxText =(TextView)view.findViewById(R.id.max_text);
            TextView minText =(TextView)view.findViewById(R.id.min_text);
            datetext.setText(forcast.date);
            infotext.setText(forcast.more.info);
            maxText.setText(forcast.temperature.max);
            minText.setText(forcast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
          aqiText.setText(weather.aqi.city.aqi);
          pm25Text.setText(weather.aqi.city.pm25);

        }
        String comfort ="舒适度："+weather.suggestion.comfort.info;
        String carWash ="洗车指数： "+weather.suggestion.carcash.info;
        String sport ="运动建议："+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherlayout.setVisibility(View.VISIBLE);

    }
}
