package com.choiman.coolweather.service;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.choiman.coolweather.receiver.AutoUpdateReceiver;
import com.choiman.coolweather.util.HttpCallbackListener;
import com.choiman.coolweather.util.HttpUtil;
import com.choiman.coolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class AutoUpdateServer extends Service{

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    new Thread(new Runnable() {
      
      @Override
      public void run() {
        updateWeather();
      }
    }).start();
    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
    int anHour = 8 * 60 * 60 * 1000;//8小时的毫秒数
    long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
    Intent i = new Intent(this, AutoUpdateReceiver.class);
    PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    return super.onStartCommand(intent, flags, startId);
  }

  private void updateWeather() {
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
    String weatherCode = pref.getString("weather_code", "");
    String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
    HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
      
      @Override
      public void onFinish(String response) {
        Utility.handleWeatherResponse(AutoUpdateServer.this, response);
      }
      
      @Override
      public void onError(Exception e) {
        e.printStackTrace();
      }
    });
  }

}
