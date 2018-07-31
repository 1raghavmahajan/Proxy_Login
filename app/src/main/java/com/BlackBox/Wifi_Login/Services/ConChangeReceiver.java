package com.BlackBox.Wifi_Login.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.BlackBox.Wifi_Login.Classes.Connection_Detector;


class ConChangeReceiver extends BroadcastReceiver {

  private static final String TAG = ConChangeReceiver.class.getSimpleName() + " YOYO";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.i(TAG, "received " + intent);

    Connection_Detector connection_detector = new Connection_Detector(context);
    int connection_Status = connection_detector.isConnectedToWifi();
    Log.i(TAG, "connection_Status: " + connection_Status);
    if (connection_Status == 4) {
      Intent i = new Intent(context, Login_Service.class);
      i.setAction(Login_Service.ACTION_LOGIN);
//            Login_Service.enqueueWork(context,Login_Service.class,5980,new Intent(Login_Service.ACTION_LOGIN));
      context.startService(i);
    }
  }

}