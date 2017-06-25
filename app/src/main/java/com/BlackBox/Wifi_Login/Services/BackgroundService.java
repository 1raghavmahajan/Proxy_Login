package com.BlackBox.Wifi_Login.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.BlackBox.Wifi_Login.Activities.Main_Activity;
import com.BlackBox.Wifi_Login.Activities.StopServiceActivity;
import com.BlackBox.Wifi_Login.Classes.Connection_Detector;
import com.BlackBox.Wifi_Login.R;

//import android.util.Log;

public class BackgroundService extends Service {

    private MyBroadcastReceiver br;

    //final public String TAG = BackgroundService.class.getSimpleName() + " YOYO";
    private static final String EXTRA_URL = "com.BlackBox.Wifi_Login.extra.URL";
    private static final int Notification_ID = 1459;
    private Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Log.i(TAG, "onStartCommand  " + Thread.currentThread().getName());
        context = getApplicationContext();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        br = new MyBroadcastReceiver();

        Intent notificationIntent = new Intent(this, StopServiceActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                //.setCategory(Notification.CATEGORY_SERVICE)
                .setContentTitle("Login Service")
                .setSmallIcon(R.mipmap.ic_error)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        startForeground(Notification_ID, notification);

        registerReceiver(br, intentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //Log.i(TAG, "onDestroy");
        unregisterReceiver(br);
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.i(TAG, "received " + Thread.currentThread().getName());

            Connection_Detector connection_detector = new Connection_Detector(context);
            int connection_Status = connection_detector.isConnectedToWifi();
            //Log.i(TAG, "connection_Status: " + connection_Status);
            if (connection_Status == 4) {
                Intent i = new Intent(context, Login_Service.class);
                i.setAction(Login_Service.ACTION_LOGIN);
                i.putExtra(EXTRA_URL, Main_Activity.URL);
                startService(i);
            }
        }
    }

    @Override
    public void onLowMemory() {
        Toast.makeText(context, "Low Memory", Toast.LENGTH_SHORT).show();
        unregisterReceiver(br);
        stopForeground(true);
        stopSelf();
        super.onLowMemory();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
