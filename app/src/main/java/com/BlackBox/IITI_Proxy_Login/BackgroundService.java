package com.BlackBox.IITI_Proxy_Login;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BackgroundService extends Service {

    private MyBroadcastReceiver br;

    final public String TAG = BackgroundService.class.getSimpleName() + " YOYO";
    public static final String ACTION_LOGIN = "com.BlackBox.IITI_Proxy_Login.action.LOGIN";
    public static final String EXTRA_URL = "com.BlackBox.IITI_Proxy_Login.extra.URL";
    RequestQueue requestQueue;
    Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand  " + Thread.currentThread().getName());
        context = getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        br = new MyBroadcastReceiver();

        Intent notificationIntent = new Intent(this, StopServiceActivity.class);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                //.setCategory(Notification.CATEGORY_SERVICE)
                .setContentTitle("Login Service")
                .setSmallIcon(R.mipmap.ic_error)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        startForeground(1459, notification);

        registerReceiver(br, intentFilter);

        return super.onStartCommand(intent, flags, startId);
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
                i.setAction(ACTION_LOGIN);
                i.putExtra(EXTRA_URL, "https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti");
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
