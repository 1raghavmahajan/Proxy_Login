package com.abhi.wifi_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Main_Activity extends Activity {

    Button btn_Login;
    EditText editText_ID, editText_pwd;
    CheckBox checkBox;
    User_Info user1 ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Login = (Button) findViewById(R.id.button);
        editText_ID = (EditText) findViewById(R.id.editText);
        editText_pwd = (EditText) findViewById(R.id.editText2);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        user1 = new User_Info();
        if(user1.load_Cred(getApplicationContext())) //if cred are saved
        {
            editText_ID.setText(user1.getID());
            editText_pwd.setText(user1.getpwd());
            checkBox.setChecked(true);
        }

        btn_Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Connection_Detector cd = new Connection_Detector(getApplicationContext());

                user1.setID(editText_ID.getText().toString().trim());
                user1.setpwd(editText_pwd.getText().toString().trim());

                if (user1.getID().equals(""))
                {
                    Toast.makeText(Main_Activity.this,"Name Field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (user1.getpwd().equals(""))
                {
                    Toast.makeText(Main_Activity.this,"Contact Field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (cd.isConnectedtoWifi())
                {
                    if (checkBox.isChecked()) {
                        user1.save_cred(getApplicationContext());
                    }
                    Login_task();
                }
                else
                {
                    showAlertDialog(Main_Activity.this,
                            "No Internet Connection",
                            "No internet connection.");
                }

            }
        });

    }

    private void Login_task()    {

        // Tag used to cancel the request
        String req_tag = "POST_REQUEST";

        String url = "https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, response.toString());
                        pDialog.hide();
                        if(response.toString().contains("Invalid"))
                        {
                            Toast.makeText(Main_Activity.this, "Invalid Credentials Provided.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "pottyError: " + error.getMessage());
                        pDialog.hide();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("auth_user", user1.getID());
                params.put("auth_pass", user1.getpwd());
                params.put("zone", "lan_iiti");
                params.put("redirurl", "https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti");
                params.put("auth_voucher", "");
                params.put("accept", "Sign+In");
                return params;
            }

            @Override
            public void deliverError(final VolleyError error) {

                //Log.d(TAG, "Redirect");
                final int status = error.networkResponse.statusCode;
                String mess_str = "Unknown Error";
                // Handle 30x
                if (HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    final String location = error.networkResponse.headers.get("Location");

                    if(location.contains("bing"))
                    {
                        mess_str = "Successfully Authenticated!";
                    }
                    else
                    {
                        mess_str = "Invalid Credentials Provided.";
                    }
                }
                pDialog.hide();
                Toast.makeText(Main_Activity.this, mess_str, Toast.LENGTH_SHORT).show();
            }

        };

        // Adding request to request queue
        RequestQueue mRequestQueue;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        strReq.setTag(req_tag);
        mRequestQueue.add(strReq);

    }

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}