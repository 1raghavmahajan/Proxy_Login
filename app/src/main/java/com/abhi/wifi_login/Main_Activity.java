package com.abhi.wifi_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main_Activity extends Activity {

    Button btn;
    EditText eT, eT2;
    CheckBox cB;
    String id, pwd;
    String pass1;
    User_Info user;

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

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
                    new HttpAsyncTask().execute("https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti");
                }
                else {
                    showAlertDialog(Main_Activity.this,
                            "No Internet Connection",
                            "No internet connection.", false);
                }

            }
        });

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
        editor.commit();
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
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

    public String POST(String url, User_Info user) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            //HttpClient httpclient = new DefaultHttpClient();
            DefaultHttpClient client = (DefaultHttpClient) WebClientDevWrapper.getNewHttpClient();

            // 2. make POST request to the given URL
            //HttpPost httpPost = new HttpPost(url);
            HttpPost post = new HttpPost(url);

            String json = "";


            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("auth_user", user.getId()));
            nameValuePairs.add(new BasicNameValuePair("auth_pass", user.getPwd()));
            //nameValuePairs.add(new BasicNameValuePair("fw_domain", "gpra.in"));
            nameValuePairs.add(new BasicNameValuePair("zone", "lan_iiti"));
            nameValuePairs.add(new BasicNameValuePair("redirurl", "http%3A%2F%2Fhanuman.iiti.ac.in%3A8003%2Findex.php%3Fzone%3Dlan_iiti"));
            nameValuePairs.add(new BasicNameValuePair("auth_voucher", ""));
            nameValuePairs.add(new BasicNameValuePair("accept", "Sign+In"));
            //nameValuePairs.add(new BasicNameValuePair("submit", "Login"));
            //nameValuePairs.add(new BasicNameValuePair("action", "fw_logon"));
            //nameValuePairs.add(new BasicNameValuePair("fw_logon_type", "logon"));
            //nameValuePairs.add(new BasicNameValuePair("redirect", ""));
            //nameValuePairs.add(new BasicNameValuePair("lang", "en-US"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = client.execute(post);

            // 9. receive response as inputStream
            inputStream = response.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                Log.i("result123",result);
            } else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            user = new User_Info();
            user.setId(id);
            user.setPwd(pwd);

            return POST(urls[0], user);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            //Log.i("result1234", result);
            String str2 = "bing";
            if(result.contains(str2)) {
            try {
                Toast.makeText(getBaseContext(), "Successfully Authenticated", Toast.LENGTH_LONG).show();
                JSONObject pass = new JSONObject();
                pass.accumulate("id", id);
                pass.accumulate("pwd", pwd);
                pass1 = pass.toString();
            } catch (Exception e) {
                Log.d("makepass", e.getLocalizedMessage());
            }

            // Intent i = new Intent(Main_Activity.this, Logged_In.class);
            // sending data to new activity
            //i.putExtra("data", pass1);
            //startActivity(i);
            //finish();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Not Authenticated, Please Check your Credentials!", Toast.LENGTH_LONG).show();
            }

        }
    }
}