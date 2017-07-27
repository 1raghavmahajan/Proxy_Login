package com.BlackBox.Wifi_Login.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.BlackBox.Wifi_Login.Activities.StopServiceActivity;
import com.BlackBox.Wifi_Login.Classes.Connection_Detector;
import com.BlackBox.Wifi_Login.R;

public class BackgroundService extends Service {

    private MyBroadcastReceiver br;

    final public String TAG = BackgroundService.class.getSimpleName() + " YOYO";
    private static final int Notification_ID = 1459;
    private Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand  " + Thread.currentThread().getName());
        context = getApplicationContext();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        br = new MyBroadcastReceiver();

        Intent notificationIntent = new Intent(this, StopServiceActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID,getPackageName()+"_CHANNEL",NotificationManager.IMPORTANCE_MIN);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setContentTitle("Login Service")
                    .setSmallIcon(R.drawable.ic_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .setCategory(Notification.CATEGORY_SERVICE);

        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //noinspection deprecation
                builder = new Notification.Builder(context)
                        .setContentTitle("Login Service")
                        .setSmallIcon(R.drawable.ic_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .setVisibility(Notification.VISIBILITY_SECRET)
                        .setCategory(Notification.CATEGORY_SERVICE);
            }else{
                //noinspection deprecation
                builder = new Notification.Builder(context)
                        .setContentTitle("Login Service")
                        .setSmallIcon(R.drawable.ic_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon))
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);
            }
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            new JobInfo.Builder(125,new ComponentName(context,Login_Service.class))
////                    .setPersisted(true)
//            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//        }

        startForeground(Notification_ID, builder.build());

        registerReceiver(br, intentFilter);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        unregisterReceiver(br);
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "received " + Thread.currentThread().getName());

            Connection_Detector connection_detector = new Connection_Detector(context);
            int connection_Status = connection_detector.isConnectedToWifi();
            Log.i(TAG, "connection_Status: " + connection_Status);
            if (connection_Status == 4) {
                Intent i = new Intent(context, Login_Service.class);
                i.setAction(Login_Service.ACTION_LOGIN);
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

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(context, "onTask Removed", Toast.LENGTH_SHORT).show();
//        unregisterReceiver(br);
//        stopForeground(true);
//        stopSelf();
        super.onTaskRemoved(rootIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
