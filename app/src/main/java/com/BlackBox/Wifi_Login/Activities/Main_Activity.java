package com.BlackBox.Wifi_Login.Activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.BlackBox.Wifi_Login.Classes.Connection_Detector;
import com.BlackBox.Wifi_Login.Classes.Login_Task;
import com.BlackBox.Wifi_Login.Classes.User_Cred;
import com.BlackBox.Wifi_Login.R;
import com.BlackBox.Wifi_Login.Services.BackgroundService;
import com.android.volley.toolbox.Volley;

//import android.util.Log;

@SuppressWarnings("deprecation")
public class Main_Activity extends AppCompatActivity {

    //final public String TAG = Main_Activity.class.getSimpleName() + " YOYO";

    private TextInputEditText eT_UserName, eT_Password;
    private CheckBox cB_startService;
    private Button btn_stopService;
    private User_Cred user;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_Login = findViewById(R.id.btn_Login);
        eT_UserName = findViewById(R.id.eT_UserName);
        eT_Password = findViewById(R.id.eT_Password);
        cB_startService = findViewById(R.id.cB_startService);
        btn_stopService = findViewById(R.id.btn_stopService);
        context = Main_Activity.this;

        user = new User_Cred();
        if (user.load_Cred(context)) //if cred are saved
        {
            eT_UserName.setText(user.getID());
            eT_Password.setText(user.getpwd());
            cB_startService.setChecked(true);
            if(isMyServiceRunning(BackgroundService.class))
                btn_stopService.setVisibility(View.VISIBLE);
            else
                btn_stopService.setVisibility(View.GONE);
        }

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        btn_stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyServiceRunning(BackgroundService.class)){
                    Intent i = new Intent(context, BackgroundService.class);
                    if(stopService(i)){
                        createSnackbar("Service Stopped!", Snackbar.LENGTH_SHORT);
                        btn_stopService.setVisibility(View.GONE);
                    }else {
                        if(!isMyServiceRunning(BackgroundService.class)){
                            createSnackbar("Service Stopped!", Snackbar.LENGTH_SHORT);
                            btn_stopService.setVisibility(View.GONE);
                        }else
                            createSnackbar("Some error occured, please try again.", Snackbar.LENGTH_SHORT);
                    }
                }
            }
        });

    }

    private void Login() {
        //Log.i(TAG, "Login");

        Connection_Detector cd = new Connection_Detector(context);

        user.setID(eT_UserName.getText().toString().trim());
        user.setpwd(eT_Password.getText().toString().trim());

        if (user.getID().equals(""))
        {
            eT_UserName.setError("Field Empty");
        }
        else if (user.getpwd().equals(""))
        {
            eT_Password.setError("Field Empty");
        }
        else
        {
            int Con_Status = cd.isConnectedToWifi();
            String Alert_Status = "No connectivity";
            switch (Con_Status) {
                case 1:
                    Alert_Status = "Please turn on Wifi.";
                    break;
                case 2:
                    Alert_Status = "Please turn off Mobile data.";
                    break;
                case 3:
                    Alert_Status = "You are not on an IITI network.";
                    break;
            }
            if(Con_Status != 4) {
                showAlertDialog("Error",
                        Alert_Status);
            }
            else
            {
                User_Cred cred = new User_Cred();
                if(cred.load_Cred(context))
                    User_Cred.clear_cred(context);

                user.save_cred(context);

                onTaskCompleteListener listener = new onTaskCompleteListener() {
                    @Override
                    public void onSuccess(Boolean alreadyLoggedIn) {
                        progressDialog.cancel();
                        if (cB_startService.isChecked()) {

                            // Log.i(TAG, "Starting Service..");
                            Intent i = new Intent(context, BackgroundService.class);
                            if (!isMyServiceRunning(BackgroundService.class)) {
                                createSnackbar("Logged In and Service Started!",Snackbar.LENGTH_SHORT);
                                startService(i);
                                btn_stopService.setVisibility(View.VISIBLE);
                            }
                            else
                                createSnackbar("Logged in!",Snackbar.LENGTH_SHORT);

                        }else {
                            createSnackbar("Logged in!",Snackbar.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        createSnackbar(error, Snackbar.LENGTH_LONG);
                    }
                };
                Login_Task login_task = new Login_Task(user, Volley.newRequestQueue(context), listener);

                progressDialog = ProgressDialog.show(context, "Logging in", "Please wait", true, false);

                login_task.Login();

//                IntentFilter intentFilter = new IntentFilter(Login_Task.ACTION_RESULT);
//                registerReceiver(new MyOtherBroadcastReceiver(), intentFilter);

            }
        }
    }

    @Override
    protected void onResume() {
        if(isMyServiceRunning(BackgroundService.class))
            btn_stopService.setVisibility(View.VISIBLE);
        else
            btn_stopService.setVisibility(View.GONE);
        super.onResume();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //alertDialog.setIcon(R.mipmap.ic_app);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void createSnackbar(String message, int length) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.relative), message, length);
        //noinspection deprecation
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorSnack));
        snackbar.show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public interface onTaskCompleteListener {
        void onSuccess(Boolean alreadyLoggedIn);
        void onFailure(String error);
    }

}