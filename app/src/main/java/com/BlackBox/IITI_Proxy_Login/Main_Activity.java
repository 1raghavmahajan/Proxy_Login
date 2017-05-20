package com.BlackBox.IITI_Proxy_Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;

public class Main_Activity extends Activity {

    final public String TAG = Main_Activity.class.getSimpleName();
    final private String URL = "https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti";

    Button btn_Login;
    EditText eT_UserName, eT_Password;
    CheckBox cB_saveCred;
    CheckBox cB_startService;
    User_Info user;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        btn_Login = (Button) findViewById(R.id.btn_Login);
        eT_UserName = (EditText) findViewById(R.id.eT_UserName);
        eT_Password = (EditText) findViewById(R.id.eT_Password);
        cB_saveCred = (CheckBox) findViewById(R.id.cB_saveCred);
        cB_startService = (CheckBox) findViewById(R.id.cB_startService);
        context = getApplicationContext();

        user = new User_Info();
        if (user.load_Cred(context)) //if cred are saved
        {
            eT_UserName.setText(user.getID());
            eT_Password.setText(user.getpwd());
            cB_saveCred.setChecked(true);
        }
    }

    public void Login() {
        Log.i(TAG, "Login");

        Connection_Detector cd = new Connection_Detector(context);

        user.setID(eT_UserName.getText().toString().trim());
        user.setpwd(eT_Password.getText().toString().trim());

        if (user.getID().equals(""))
        {
            Toast.makeText(Main_Activity.this,"Name Field is empty", Toast.LENGTH_SHORT).show();
        }
        else if (user.getpwd().equals(""))
        {
            Toast.makeText(Main_Activity.this,"Contact Field is empty", Toast.LENGTH_SHORT).show();
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
                showAlertDialog(Main_Activity.this,
                        "Error",
                        Alert_Status);
            }
            else
            {
                if (cB_saveCred.isChecked()) {
                    user.save_cred(context);
                }

                Login_Task login_task = new Login_Task(user);

                boolean allGood = login_task.Login(URL, context, Volley.newRequestQueue(context));

                Log.i(TAG, "All Good: " + String.valueOf(allGood));

                if (cB_startService.isChecked())
                {
                    Log.i(TAG, "Starting Service..");
                    Intent i = new Intent(context, BackgroundService.class);
                    startService(i);
                }
            }
        }
    }

    public void showAlertDialog(Context context, String title, String message) {
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

    public void Onclick_Login(View view) {
        Log.i("YOYO", "OnCLick");
        Login();
    }
}