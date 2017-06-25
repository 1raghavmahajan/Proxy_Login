package com.BlackBox.Wifi_Login.Classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/*
0 : No connectivity
1 : No wifi OR Just data
2 : Wifi + Data
3 : Not IIT wifi
4 : All fine

*/


public class Connection_Detector {

     private final Context _context;

    public Connection_Detector(Context context) {
        this._context = context;
    }

    //checks if connected to IITI network (Returns 4 if true)
    public int isConnectedToWifi() {

        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {

            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo != null) {
                switch (activeNetworkInfo.getType()) {
                    case TYPE_WIFI:
                        if (activeNetworkInfo.getExtraInfo().contains("IIT") || activeNetworkInfo.getExtraInfo().contains("captive")) {
                            return 4; // all fine
                        } else {
                            return 3;
                        }
                    case TYPE_MOBILE:
                        return 2;
                    default:
                        return 1;
                }
            } else {
                return 1;
            }
        }
        return 0;
    }
}
