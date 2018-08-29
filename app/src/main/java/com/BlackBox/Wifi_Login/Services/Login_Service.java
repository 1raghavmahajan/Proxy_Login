package com.BlackBox.Wifi_Login.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.BlackBox.Wifi_Login.Activities.Main_Activity;
import com.BlackBox.Wifi_Login.Classes.Login_Task;
import com.BlackBox.Wifi_Login.Classes.User_Cred;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class Login_Service extends IntentService {

  public static final String ACTION_LOGIN = "com.BlackBox.Wifi_Login.action.LOGIN";
  public static final String ACTION_LOGIN2 = "com.BlackBox.Wifi_Login.action.LOGIN2";

  private static final String TAG = Login_Service.class.getSimpleName() + " YOYO";
  RequestQueue requestQueue;
  private Context context;
  boolean show = false;

  public Login_Service() {
    super("Login_Service");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      Log.i(TAG, "onHandleIntent: "+action);
      if (ACTION_LOGIN.equals(action)) {
        context = getApplicationContext();
        show = false;
        Login();
      }else if(ACTION_LOGIN2.equals(action)){
        context = getApplicationContext();
        show = true;
        Login();
      }
    }
  }

  private void Login() {
    Log.i("YOYO", "Login initiated!");
    User_Cred user = new User_Cred();
    user.load_Cred(context);
    if (requestQueue == null) {
      requestQueue = Volley.newRequestQueue(context);
    }
    Login_Task login_task = new Login_Task(user, requestQueue, new Main_Activity.onTaskCompleteListener() {
      @Override
      public void onSuccess(Boolean alreadyLoggedIn) {
        if(!show) {
          if (!alreadyLoggedIn) {
            new Handler().post(new Runnable() {
              @Override
              public void run() {
                Toast.makeText(getApplicationContext(), "Logged In!", Toast.LENGTH_SHORT).show();
              }
            });
          }
        }else {
          new Handler().post(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(getApplicationContext(), "Logged In!", Toast.LENGTH_SHORT).show();
            }
          });
        }
      }

      @Override
      public void onFailure(String error) {
        Log.e(TAG, "Login Failure: " + error);
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
      }

    });
    login_task.Login();
  }

}
