package com.BlackBox.IITI_Proxy_Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WifiStateMonitor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Connection_Detector connection_detector = new Connection_Detector(context);

        if(connection_detector.isConnectedToWifi() == 4)
        {

                Intent i = new Intent(context, Main_Activity.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
        }

    }
}
