package com.BlackBox.IITI_Proxy_Login;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    boolean Login(String url, final Context context, RequestQueue requestQueue) {

        class ResultStatus {
            private boolean f;

            private ResultStatus() {
                f = false;
            }

            void set() {
                f = true;
            }

            private boolean getStatus() {
                return f;
            }
        }
        final ResultStatus resultStatus = new ResultStatus();

        // Tag used to cancel the request
        String request_Tag = "POST_REQUEST";

        StringRequest strReq = new StringRequest
                (
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.toString().contains("Invalid"))
                                    Toast.makeText(context, "Invalid Credentials Provided.", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, "Error: " + error.getMessage());
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

                String mess_str = "Unknown deliveryError";
                if (error != null) {
                    Log.i(TAG, "deliveryError : " + error.toString());
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
                                    resultStatus.set();
                                } else {
                                    mess_str = "Invalid Credentials Provided.";
                                }
                            }
                        } else {
                            mess_str = "No Network response";
                        }
                    }
                }
                Log.i("YOYO", "Delivery Error: " + mess_str);
                Toast.makeText(context, mess_str, Toast.LENGTH_LONG).show();
            }
        };

        // Adding request to request queue
        strReq.setTag(request_Tag);
        requestQueue.add(strReq);

        return (resultStatus.getStatus());
    }

}
