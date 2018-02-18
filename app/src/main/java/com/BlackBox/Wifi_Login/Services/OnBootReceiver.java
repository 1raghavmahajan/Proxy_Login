package com.BlackBox.Wifi_Login.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.BlackBox.Wifi_Login.Classes.User_Cred;

public class OnBootReceiver extends BroadcastReceiver {

    private static final String TAG = OnBootReceiver.class.getSimpleName()+" YOYO";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: starting service");
        if(intent.getAction()!=null) {
            String action = intent.getAction();
            Log.i(TAG, "action: "+action);
            if (action.equalsIgnoreCase("android.intent.action.BOOT_COMPLETED") ||
                    action.equalsIgnoreCase("android.intent.action.REBOOT") ||
                    action.equalsIgnoreCase("android.intent.action.QUICKBOOT_POWERON")||
                    action.equalsIgnoreCase("android.intent.action.LOCKED_BOOT_COMPLETED") ||
                    action.equalsIgnoreCase("android.intent.action.REBOOT") ||
                    action.equalsIgnoreCase("com.htc.intent.action.QUICKBOOT_POWERON")) {

                User_Cred user_cred = new User_Cred();
                if (user_cred.load_Cred(context)) {

                    Intent i = new Intent(context, BackgroundService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(i);
                    } else {
                        context.startService(i);
                    }
                }

            }
        }
    }
}
