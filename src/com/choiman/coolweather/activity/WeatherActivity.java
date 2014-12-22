package com.choiman.coolweather.activity;

import com.choiman.coolweather.R;
import com.choiman.coolweather.util.HttpCallbackListener;
import com.choiman.coolweather.util.HttpUtil;
import com.choiman.coolweather.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{

  private LinearLayout weatherInfoLayout;

  /**
   * 城市名
   */
  private TextView cityNameText;

  /**
   * 发布时间
   */
  private TextView publishTextView;

  /**
   * 天气描述信息
   */
  private TextView weatherDespText;

  /**
   * 显示温度1
   */
  private TextView temp1Text;

  /**
   * 温度2
   */
  private TextView temp2Text;

  /**
   * 当前日期
   */
  private TextView currentDateText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.weather_layout);
    weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
    cityNameText = (TextView)findViewById(R.id.city_name);
    publishTextView = (TextView)findViewById(R.id.publish_text);
    weatherDespText = (TextView)findViewById(R.id.weather_desp);
    temp1Text = (TextView)findViewById(R.id.temp1);
    temp2Text = (TextView)findViewById(R.id.temp2);
    currentDateText = (TextView)findViewById(R.id.current_date);
    String countyCode = getIntent().getStringExtra("county_code");
    if (!TextUtils.isEmpty(countyCode)) {
      publishTextView.setText("同步中...");
      weatherInfoLayout.setVisibility(View.INVISIBLE);
      cityNameText.setVisibility(View.INVISIBLE);
      queryWeatherCode(countyCode);
    } else {
      showWeather();
    }
  }

  /**
   * 查询县级代号所对应的天气代号
   * @param countyCode
   */
  private void queryWeatherCode(String countyCode) {
    String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
    queryFromServer(address, "countyCode");
  }

  /**
   * 查询天气代号所对应的天气
   */
  private void queryWeatherInfo(String weatherCode) {
    String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
    queryFromServer(address, "weatherCode");
  }

  /**
   * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
   */
  private void queryFromServer(final String address, final String type) {
    HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
      
      @Override
      public void onFinish(String response) {
        if ("countyCode".equals(type)) {
          if (!TextUtils.isEmpty(response)) {
            //从服务器返回的数据中解析出天气代号
            String[] array = response.split("\\|");
            if (array != null && array.length == 2) {
              String weatherCode = array[1];
              queryWeatherInfo(weatherCode);
            }
          }
        } else if ("weatherCode".equals(type)) {
          //处理服务器返回的天气信息
          Utility.handleWeatherResponse(WeatherActivity.this, response);
          runOnUiThread(new Runnable() {
            public void run() {
              showWeather();
            }
          });
        }
      }
      
      @Override
      public void onError(Exception e) {
        runOnUiThread(new Runnable() {
          public void run() {
            publishTextView.setText("同步失败");
          }
        });
      }
    });
  }

  private void showWeather() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    cityNameText.setText(prefs.getString("city_name", ""));
    publishTextView.setText(prefs.getString("public_time", "") + "发布");
    weatherDespText.setText(prefs.getString("weather_desp", ""));
    temp1Text.setText(prefs.getString("temp1", ""));
    temp2Text.setText(prefs.getString("temp2", ""));
    currentDateText.setText(prefs.getString("current_date", ""));
    weatherInfoLayout.setVisibility(View.VISIBLE);
    cityNameText.setVisibility(View.VISIBLE);
  }

}
