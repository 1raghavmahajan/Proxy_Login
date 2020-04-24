package com.BlackBox.Wifi_Login.Services;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.BlackBox.Wifi_Login.R;

public class BackgroundService extends Service {

  private static final int Notification_ID = 5151;
  final public String TAG = BackgroundService.class.getSimpleName() + " YOYO";
  
  private static final String TITLE = "Login Service";
  private static final String TEXT = "Click here to Login"; //"Service to automate Wifi Login"

  private ConChangeReceiver br;
  private Context context;


  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    Log.i(TAG, "onStartCommand  " + Thread.currentThread().getName());
    context = getApplicationContext();

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    intentFilter.addAction(Intent.ACTION_USER_PRESENT);
    if (VERSION.SDK_INT >= VERSION_CODES.N) {
      intentFilter.addAction(Intent.ACTION_USER_UNLOCKED);
    }
    intentFilter.addAction(Intent.ACTION_SCREEN_ON);
    br = new ConChangeReceiver();

//    Intent notificationIntent = new Intent(this, Main_Activity.class);
//    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    Intent loginIntent = new Intent(context, Login_Service.class);
    loginIntent.setAction(Login_Service.ACTION_LOGIN2);

//    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    PendingIntent pendingIntent = PendingIntent.getService(this, 0, loginIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Builder builder;
    if (notificationManager != null) {
      if (VERSION.SDK_INT >= VERSION_CODES.O) {
        String id = "WifiLogin_Service_01";
        CharSequence name = "Wifi_Login_Service";
        String description = "Service to automate Wifi Login";
//        String description = "Click here to Login";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
        notificationChannel.setDescription(description);
        notificationManager.createNotificationChannel(notificationChannel);

        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        notificationChannel.setSound(null, null);

        builder = new Builder(context, id)
            .setContentTitle(TITLE)
            .setContentText(TEXT)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setVisibility(Notification.VISIBILITY_SECRET)
            .setCategory(Notification.CATEGORY_SERVICE);

      } else {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
          builder = new Builder(context)
              .setContentTitle(TITLE)
              .setContentText(TEXT)
              .setSmallIcon(R.drawable.ic_icon)
              .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon))
              .setContentIntent(pendingIntent)
              .setOngoing(true)
              .setVisibility(Notification.VISIBILITY_SECRET)
              .setPriority(Notification.PRIORITY_MIN)
              .setCategory(Notification.CATEGORY_SERVICE);
        } else {
          builder = new Builder(context)
              .setContentTitle(TITLE)
              .setContentText(TEXT)
              .setSmallIcon(R.drawable.ic_icon)
              .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_icon))
              .setContentIntent(pendingIntent)
              .setPriority(Notification.PRIORITY_MIN)
              .setOngoing(true);
        }
      }

      startForeground(Notification_ID, builder.build());

      registerReceiver(br, intentFilter);

      return START_STICKY;
    } else {

      stopSelf();
      return START_REDELIVER_INTENT;

    }

//        Todo:
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            new JobInfo.Builder(125,new ComponentName(context,Login_Service.class))
//                    .setPersisted(true)
//            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//        }
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {

    Log.i(TAG, "onTaskRemoved: ");

//    Intent myIntent = new Intent(getApplicationContext(), BackgroundService.class);
//
//    PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, myIntent, 0);
//
//    AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//    Calendar calendar = Calendar.getInstance();
//
//    calendar.setTimeInMillis(System.currentTimeMillis());
//
//    calendar.add(Calendar.SECOND, 10);
//
//    assert alarmManager1 != null;
//    alarmManager1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//    Toast.makeText(getApplicationContext(), "Start Alarm", Toast.LENGTH_SHORT).show();

    super.onTaskRemoved(rootIntent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "onDestroy");
    try {
      if (br != null) {
        unregisterReceiver(br);
      }
    } catch (Exception e) {
      // already unregistered
      Log.e(TAG, "onDestroy: br", e);
    }
    stopForeground(true);
  }

  @Override
  public void onLowMemory() {
    Log.i(TAG, "onLowMemory: ");
    Toast.makeText(context, "Low Memory", Toast.LENGTH_SHORT).show();
    Log.i(TAG, "onDestroy");
    try {
      if (br != null) {
        unregisterReceiver(br);
      }
    } catch (Exception e) {
      // already unregistered
      Log.e(TAG, "onDestroy: br", e);
    }
    stopForeground(true);
    super.onLowMemory();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}
