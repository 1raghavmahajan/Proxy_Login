package com.BlackBox.Wifi_Login.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.BlackBox.Wifi_Login.Activities.Main_Activity;
import com.BlackBox.Wifi_Login.Classes.Login_Task;
import com.BlackBox.Wifi_Login.Classes.User_Cred;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Login_Service extends IntentService {

    public static final String ACTION_LOGIN = "com.BlackBox.Wifi_Login.action.LOGIN";
    private static final String TAG = Login_Service.class.getSimpleName()+" YOYO";

    private Context context;
    RequestQueue requestQueue;

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

        Log.i("YOYO","Login initiated!");
        User_Cred user = new User_Cred();
        user.load_Cred(context);
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context);
        }
        Login_Task login_task = new Login_Task(user,requestQueue , new Main_Activity.onTaskCompleteListener() {
            @Override
            public void onSuccess(Boolean alreadyLoggedIn) {
                if(!alreadyLoggedIn)
                    Toast.makeText(context, "Logged In!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Login Failure: "+error);
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            }

        });
        login_task.Login();
    }
}
