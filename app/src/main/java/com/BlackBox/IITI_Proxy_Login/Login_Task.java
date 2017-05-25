package com.BlackBox.IITI_Proxy_Login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

//import static com.android.volley.VolleyLog.TAG;

class Login_Task {

    private User_Info user;
    private static final String TAG = Login_Task.class.getSimpleName() + " YOYO";

    Login_Task(User_Info user_info) {
        user = user_info;
    }

    void Login(String url, final Context context, RequestQueue requestQueue) {

        // Tag used to cancel the request
        String request_Tag = "POST_REQUEST";
        final String ACTION_RESULT = "com.BlackBox.app.ACTION_RESULT";

        StringRequest strReq = new StringRequest
                (
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Intent i = new Intent(ACTION_RESULT);
                                i.putExtra("resultStatus", false);
                                if (response.toString().contains("Invalid")) {
                                    Toast.makeText(context, "Invalid Credentials Provided.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Unknown error.", Toast.LENGTH_SHORT).show();
                                }
                                context.sendBroadcast(i);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "onErrorResponse: " + error.getMessage());
                                Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ACTION_RESULT);
                                i.putExtra("resultStatus", false);
                                context.sendBroadcast(i);
                            }
                        }
                ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("auth_user", user.getID());
                params.put("auth_pass", user.getpwd());
                params.put("zone", "lan_iiti");
                params.put("redirurl", "https://hanuman.iiti.ac.in:8003/index.php?zone=lan_iiti");
                params.put("auth_voucher", "");
                params.put("accept", "Sign+In");
                return params;
            }

            @Override
            public void deliverError(final VolleyError error) {

                Intent i = new Intent(ACTION_RESULT);

                String mess_str = "Unknown deliveryError";
                if (error != null) {
                    Log.i(TAG, "Error details: " + error.toString());
                    if (error.toString().contains("Timeout")) {
                        mess_str = "Authentication server not reachable. Please try after some time.";
                    } else if (error.toString().contains("NoConnectionError")) {
                        mess_str = "No Connection. Please try after some time.";
                    } else {
                        if (error.networkResponse != null) //network response
                        {
                            final int status = error.networkResponse.statusCode;
                            Log.i(TAG, "Status : " + status);
                            // Handle 30x
                            if (HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                                final String location = error.networkResponse.headers.get("Location");

                                if (location.contains("bing")) {
                                    mess_str = "Successfully Authenticated!";
                                    i.putExtra("resultStatus", true);
                                } else {
                                    mess_str = "Invalid Credentials Provided.";
                                }
                            }
                        } else {
                            mess_str = "No Network response";
                        }
                    }
                }
                Log.i("YOYO", "Message for noobs: " + mess_str);
                Toast.makeText(context, mess_str, Toast.LENGTH_SHORT).show();
                context.sendBroadcast(i);
            }
        };

        // Adding request to request queue
        strReq.setTag(request_Tag);
        requestQueue.add(strReq);
    }

}
