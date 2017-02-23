package com.abhi.wifi_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

    Button btn;
    EditText eT, eT2;
    CheckBox cB;
    String id, pwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        eT = (EditText) findViewById(R.id.editText);
        eT2 = (EditText) findViewById(R.id.editText2);
        cB = (CheckBox) findViewById(R.id.checkBox);

        loadSavedPreferences();
        // show location button click event
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Connection_Detector cd = new Connection_Detector(getApplicationContext());
                id = eT.getText().toString().trim();
                pwd = eT2.getText().toString().trim();
                if (id.equals("")) {
                    Toast.makeText(Main_Activity.this,
                            "Name Field is empty", Toast.LENGTH_SHORT).show();
                } else if (pwd.equals("")) {
                    Toast.makeText(Main_Activity.this,
                            "Contact Field is empty", Toast.LENGTH_SHORT).show();
                }
                else if (cd.isConnectingToInternet())
                // true or false
                {
                    savePreferences("CheckBox_Value", cB.isChecked());
                    if (cB.isChecked()) {
                        savePreferences("saved_id", eT.getText().toString());
                        savePreferences("saved_pwd", eT2.getText().toString());
                    }
                    net_vol();
                    //new HttpAsyncTask().execute("https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti");
                }
                else {
                    showAlertDialog(Main_Activity.this,
                            "No Internet Connection",
                            "No internet connection.", false);
                }

            }
        });

    }

    private void net_vol()
    {

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
                params.put("auth_user", id);
                params.put("auth_pass", pwd);
                params.put("zone", "lan_iiti");
                params.put("redirurl", "http%3A%2F%2Fhanuman.iiti.ac.in%3A8003%2Findex.php%3Fzone%3Dlan_iiti");
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

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value", false);
        String saved_id = sharedPreferences.getString("saved_id", "");
        String saved_pwd = sharedPreferences.getString("saved_pwd", "");
        if (checkBoxValue) {
            cB.setChecked(true);
        } else {
            cB.setChecked(false);
        }

        eT.setText(saved_id);
        eT2.setText(saved_pwd);
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
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