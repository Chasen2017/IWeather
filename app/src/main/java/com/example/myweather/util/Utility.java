package com.example.myweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.myweather.db.MyWeatherDB;
import com.example.myweather.model.City;
import com.example.myweather.model.County;
import com.example.myweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 处理数据的工具类
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvinceResponse(MyWeatherDB myWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    myWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public synchronized  static boolean hanleCitiesResponse(MyWeatherDB myWeatherDB, String response,
                                                          int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    myWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return  false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public synchronized static boolean handleCountiesResponse(MyWeatherDB myWeatherDB, String response,
                                                              int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    myWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 处理服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("result");
            JSONObject today = weatherInfo.getJSONObject("today");
            String temperature = today.getString("temperature");    //温度
            String weatherDesp = today.getString("weather");    //天气描述
            String cityName = today.getString("city");  //城市名
            String date_y = today.getString("date_y");  //日期
            String dressingAdvice = today.getString("dressing_advice");//穿衣建议
            saveWeatherInfo(context, cityName, date_y, weatherDesp, temperature, dressingAdvice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将服务器返回的所有天气信息存储到SharePreference文件中
     */
    public static void saveWeatherInfo(Context context, String cityName, String date_y, String weatherDesp, String temperature, String dressingAdvice) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("current_date", date_y);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("temp", temperature);
        editor.putString("dressing_advice", dressingAdvice);
        editor.commit();
    }

}
