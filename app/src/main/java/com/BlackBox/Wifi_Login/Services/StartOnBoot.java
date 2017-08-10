package com.BlackBox.Wifi_Login.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.BlackBox.Wifi_Login.Classes.User_Cred;

public class StartOnBoot extends BroadcastReceiver {

    private static final String TAG = StartOnBoot.class.getSimpleName()+" YOYO";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        User_Cred user_cred = new User_Cred();
        boolean b = user_cred.load_Cred(context);
        if(b) {
            Log.i(TAG, "onReceive: starting service");
            Intent i = new Intent(context, BackgroundService.class);
            context.startService(i);
        }
    }
}
