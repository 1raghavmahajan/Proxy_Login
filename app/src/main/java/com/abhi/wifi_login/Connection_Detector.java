package com.abhi.wifi_login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
0 : No connectivity
1 : No wifi OR Just data
2 : Wifi + Data
3 : Not IIT wifi
4 : All fine

*/


 class Connection_Detector {

    private Context _context;

    Connection_Detector(Context context) {
        this._context = context;
    }

    int isConnectedtoWifi() {

        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
            {
                if(info[0].isConnected()) // if data on
                {
                    return 2;
                }
                else
                {
                    if (info[1].isConnected()) // if wifi connected
                    {
                        if (info[1].getExtraInfo().contains("IIT"))
                            return 4;
                        else
                            return 3;
                    }
                    else
                    {
                        return 1;
                    }
                }

            }
        }
        return 0;
    }
}
