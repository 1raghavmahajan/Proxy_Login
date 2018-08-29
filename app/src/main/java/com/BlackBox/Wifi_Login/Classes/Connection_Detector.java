package com.BlackBox.Wifi_Login.Classes;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/*
0 : No connectivity
1 : No wifi OR Just data
2 : Wifi + Data
3 : Not IIT wifi
4 : All fine
*/


public class Connection_Detector {

  private final Context _context;
  @SuppressWarnings("FieldCanBeLocal")
  private ConnectivityManager connectivityManager;
  private final String TAG = Connection_Detector.class.getSimpleName() + " YOYO";

  public Connection_Detector(Context context) {
    this._context = context;
  }

  //checks if connected to IITI network (Returns 4 if true)
  @SuppressLint("StaticFieldLeak")
  public int isConnectedToWifi() {

    connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {

      NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

      boolean f = true;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        if (connectivityManager.getRestrictBackgroundStatus() == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED) {
          f = false;
        }
      }

      if (activeNetworkInfo != null && f) {
        switch (activeNetworkInfo.getType()) {
          case TYPE_WIFI:

            if (activeNetworkInfo.getExtraInfo() != null) {

              if (activeNetworkInfo.getExtraInfo().contains("IIT") || activeNetworkInfo.getExtraInfo().contains("captive")) {
                return 4; // all fine
              } else {
                return 3; // other wifi
              }

            } else {
              return 0;
            }

          case TYPE_MOBILE:
            return 2;
          default:
            return 1;
        }
      } else {
        return 0;
      }
    }
    return 0;
  }

}
