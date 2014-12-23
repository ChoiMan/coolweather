package com.choiman.coolweather.receiver;

import com.choiman.coolweather.service.AutoUpdateServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver{

  @Override
  public void onReceive(Context context, Intent intent) {
    Intent i = new Intent(context, AutoUpdateServer.class);
    context.startService(i);
  }
  
}
