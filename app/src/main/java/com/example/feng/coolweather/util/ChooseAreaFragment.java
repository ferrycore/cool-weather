package com.example.feng.coolweather.util;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.coolweather.R;
import com.example.feng.coolweather.WeatherActivity;
import com.example.feng.coolweather.db.City;
import com.example.feng.coolweather.db.Country;
import com.example.feng.coolweather.db.Province;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Feng on 2017/9/3.
 */

public class ChooseAreaFragment extends Fragment{
    public static final int LEVAL_PROVINCE =0;
    public static final int LEVAL_CITY =1;
    public static final int LEVAL_COUNTRY =2;
    private ProgressDialog progressDialog;
    private TextView titletext;
    private Button backbutton;
    private ListView listView;
    private ArrayAdapter<String>adapter;
    private List<String>dataList =new ArrayList<>();
    /*
    省列表
    * */
    private List<Province> provinceList;

    private List<City> cityList;

    private List<Country>countryList;

    private Province selectedProvince;

    private City selectedCity;

    private int currentLeval;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.choose_area,container,false);
        titletext =(TextView)view.findViewById(R.id.title_text);
        backbutton =(Button)view.findViewById(R.id.back_button);
        listView =(ListView)view.findViewById(R.id.list_view);
        adapter =new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
             if(currentLeval ==LEVAL_PROVINCE){
                 selectedProvince =provinceList.get(position);
                 queryCities();
             }
             else if(currentLeval ==LEVAL_CITY){
                 selectedCity =cityList.get(position);
                 queryCountries();
             }
             else if(currentLeval ==LEVAL_COUNTRY){
                 String weatherId =countryList.get(position).getWeatherid();
                 if(getActivity() instanceof MainActivity){
                 Intent intent  =new Intent(getActivity(), WeatherActivity.class);
                 intent.putExtra("weather_id",weatherId);
                 startActivity(intent);
                 getActivity().finish();}
                 else if(getActivity() instanceof WeatherActivity){
                     WeatherActivity activity =(WeatherActivity)getActivity();
                     activity.drawerLayout.closeDrawers();
                     activity.swipeRefreshLayout.setRefreshing(true);
                     activity.requestWeather(weatherId);
                 }
             }
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(currentLeval ==LEVAL_COUNTRY){
                    queryCities();
                }
                else if(currentLeval ==LEVAL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
    /*查询所有的省，优先从数据库查询，如果没有查询再去服务器查询*/

    private void queryProvinces(){
        titletext.setText("中国");
        backbutton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeval=LEVAL_PROVINCE;

        }
        else{
            String address ="http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }
    private void queryCities(){
        titletext.setText(selectedProvince.getProvinceName());
        backbutton.setVisibility(View.VISIBLE);
        cityList =DataSupport.where("provinceid =?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityname());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeval =LEVAL_CITY;
        }
        else {
            int provinceCode =selectedProvince.getProvinceCode();
            String address ="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }
    private void queryCountries(){
        titletext.setText(selectedCity.getCityname());
        backbutton.setVisibility(View.VISIBLE);
        countryList =DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(Country.class);
        if(countryList.size()>0){
            dataList.clear();
            for(Country country :countryList){
                dataList.add(country.getCountryname());

            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLeval =LEVAL_COUNTRY;
        }
        else{
            int provinceCode =selectedProvince.getProvinceCode();
            int cityCode =selectedCity.getCityCode();
            String address ="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"country");
        }
    }

    private void queryFromServer(String address,final String type){
       showProgressDialog();
        HttpUtil.sendOkHttprequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
             getActivity().runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     closeProgressDialog();
                     Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                 }
             });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responsetext =response.body().string();
                boolean result =false;
                if("province".equals(type)){
                    result =Utility.handleprovinceresponse(responsetext);
                }
                else if("city".equals(type)){
                    result =Utility.handlecityresponse(responsetext,selectedProvince.getId());
                }
                else if("country".equals(type)){
                    result =Utility.handleCountryResponse(responsetext,selectedCity.getId());

                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }

                            else if("city".equals(type)){
                                queryCities();
                            }
                            else if("country".equals(type)){
                               queryCountries();
                            }
                        }
                    });
                }
            }

        });
    }
    private void showProgressDialog(){
        if(progressDialog ==null){
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载....");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
      if(progressDialog!=null) {
          progressDialog.dismiss();
      }
    }
}
