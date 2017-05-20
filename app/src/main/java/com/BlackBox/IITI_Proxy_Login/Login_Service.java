package com.BlackBox.IITI_Proxy_Login;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Login_Service extends IntentService {

    public static final String ACTION_LOGIN = "com.BlackBox.IITI_Proxy_Login.action.LOGIN";
    public static final String EXTRA_URL = "com.BlackBox.IITI_Proxy_Login.extra.URL";
    RequestQueue requestQueue;

    Context context;

    public Login_Service() {
        super("Login_Service");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOGIN.equals(action)) {
                context = getApplicationContext();
                final String URL = intent.getStringExtra(EXTRA_URL);
                Login(URL);
            }
        }
    }

    private void Login(String URL) {

        User_Info user = new User_Info();
        user.load_Cred(context);
        Login_Task login_task = new Login_Task(user);
        requestQueue = Volley.newRequestQueue(context);
        login_task.Login(URL, context, requestQueue);

    }
}
