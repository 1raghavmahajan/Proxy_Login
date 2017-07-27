package com.BlackBox.Wifi_Login.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.BlackBox.Wifi_Login.Classes.Login_Task;
import com.BlackBox.Wifi_Login.Classes.User_Cred;
import com.android.volley.toolbox.Volley;

public class Login_Service extends IntentService {

    public static final String ACTION_LOGIN = "com.BlackBox.Wifi_Login.action.LOGIN";

    private Context context;

    public Login_Service() {
        super("Login_Service");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOGIN.equals(action)) {
                context = getApplicationContext();
                Login();
            }
        }
    }

    private void Login() {

//        Log.i("YOYO","Login initiated!");
        User_Cred user = new User_Cred();
        user.load_Cred(context);
        Login_Task login_task = new Login_Task(user, context,Volley.newRequestQueue(context));
        login_task.Login();
    }
}
