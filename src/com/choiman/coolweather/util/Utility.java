package com.choiman.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.choiman.coolweather.db.CoolWeatherDB;
import com.choiman.coolweather.model.City;
import com.choiman.coolweather.model.County;
import com.choiman.coolweather.model.Province;
import com.choiman.coolweather.model.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Utility {

  /**
   * 解析和处理服务器返回的省级数据
   */
  public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
    if (!TextUtils.isEmpty(response)) {
      String[] allProvices = response.split(",");
      if (allProvices != null && allProvices.length > 0) {
        for (String p:allProvices) {
          String[] array = p.split("\\|");
          Province province = new Province();
          province.setProvinceCode(array[0]);
          province.setProvinceName(array[1]);
          coolWeatherDB.saveProvince(province);
        }
        return true;
      }
    }
    return false;
  }

  /**
   * 解析和处理服务器返回的市级数据
   */
  public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response ,
      int provinceId) {
    if (!TextUtils.isEmpty(response)) {
      String[] allCities = response.split(",");
      if (allCities != null && allCities.length > 0) {
        for (String p:allCities) {
          String[] array = p.split("\\|");
          City city = new City();
          city.setCityCode(array[0]);
          city.setCityName(array[1]);
          city.setProvinceId(provinceId);
          coolWeatherDB.saveCity(city);
        }
        return true;
      }
    }
    return false;
  }

  /**
   * 解析和处理服务器返回的市级数据
   */
  public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB, String response ,
      int cityId) {
    if (!TextUtils.isEmpty(response)) {
      String[] allCounties = response.split(",");
      if (allCounties != null && allCounties.length > 0) {
        for (String c:allCounties) {
          String[] array = c.split("\\|");
          County county = new County();
          county.setCountyCode(array[0]);
          county.setCountyName(array[1]);
          county.setCityId(cityId);
          coolWeatherDB.saveCounty(county);
        }
        return true;
      }
    }
    return false;
  }

  /**
   * 解析服务器返回的JSON数据，并将解析出的数据存储在本地
   * @param context
   * @param response
   */
  public static void handleWeatherResponse(Context context, String response) {
    Gson gson = new Gson();
    try {
      JSONObject jsonObject = new JSONObject(response);
      Weather weather = gson.fromJson(jsonObject.getJSONObject("weatherinfo").toString(), Weather.class);
      saveWeatherInfo(context, weather.getCity(), weather.getCityid(), weather.getTemp1(),
          weather.getTemp2(), weather.getWeatherDesp(), weather.getPtime());
    } catch (JsonSyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * 将服务器返回的所有天气信息存储到SharePreferences文件中
   */
  
  public static void saveWeatherInfo(Context context, String cityName,
      String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日 ", Locale.CHINA);
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putBoolean("city_selected", true);
    editor.putString("city_name", cityName);
    editor.putString("weather_code", weatherCode);
    editor.putString("temp1", temp1);
    editor.putString("temp2", temp2);
    editor.putString("weather_desp", weatherDesp);
    editor.putString("publish_time", publishTime);
    editor.putString("current_date", simpleDateFormat.format(new Date()));
  }
}
