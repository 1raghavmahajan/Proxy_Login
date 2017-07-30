package com.BlackBox.Wifi_Login.Classes;

import android.content.Context;

import com.BlackBox.Wifi_Login.Activities.Main_Activity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class Login_Task {

    //private static final String TAG = Login_Task.class.getSimpleName() + " YOYO";
    public static final String ACTION_RESULT = "com.BlackBox.app.ACTION_RESULT";

    private User_Cred user;
    private Context context;
    private RequestQueue requestQueue;
    private Main_Activity.onTaskCompleteListener listener;

    public Login_Task(User_Cred user_cred, Context context, RequestQueue requestQueue, Main_Activity.onTaskCompleteListener listener) {
        user = user_cred;
        this.context = context;
        this.requestQueue = requestQueue;
        this.listener = listener;
    }

    public void Login(){

        String request_Tag = "URL_REQUEST";
        final String url = "http://www.bing.com";

        StringRequest strReq = new StringRequest
                (
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                listener.onSuccess();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.onFailure(error.getMessage());
                            }
                        }
                ) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }

            @Override
            public void deliverError(final VolleyError error) {

                boolean f = true;

                String error_str = "Unknown deliveryError";
                if (error != null) {

                    //Log.i(TAG, "Error details: " + error.toString());
                    if (error.toString().contains("Timeout")) {
                        error_str = "Authentication server not reachable. Please try after some time";
                    } else if (error.toString().contains("NoConnectionError")) {
                        error_str = "No Connection. Please try after some time";
                    } else {
                        if (error.networkResponse != null) //network response
                        {
                            final int status = error.networkResponse.statusCode;
                            if (status == HttpURLConnection.HTTP_MOVED_PERM ||
                                    status == HttpURLConnection.HTTP_MOVED_TEMP ||
                                    status == HttpURLConnection.HTTP_SEE_OTHER)
                            {
                                f = false;
                                String location = error.networkResponse.headers.get("Location");
                                sendPost(location);
                            }
                            else
                                error_str = "Unknown Response";
                        } else
                            error_str = "No Network response";
                    }
                }
                if(f) {
                    listener.onFailure(error_str);
                }
            }

        };

        strReq.setTag(request_Tag);
        requestQueue.add(strReq);

    }

    private void sendPost(String url) {

        // Tag used to cancel the request
        String request_Tag = "POST_REQUEST";

        StringRequest strReq = new StringRequest
                (
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("Invalid")) {
                                    listener.onFailure("Invalid Credentials Provided!");
                                } else {
                                    listener.onFailure("Unknown error");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                VolleyLog.d(TAG, "onErrorResponse: " + error.getMessage());
                                listener.onFailure(error.getMessage());
                            }
                        }
                ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("auth_user", user.getID());
                params.put("auth_pass", user.getpwd());
//                params.put("zone", "iiti_auth");
//                params.put("redirurl", "http://iiti.ac.in");
//                params.put("auth_voucher", "");
                params.put("accept", "Sign In");
                return params;
            }

            @Override
            public void deliverError(final VolleyError error) {

                String error_str = "Unknown deliveryError";
                if (error != null) {

                    if (error.toString().contains("Timeout")) {
                        error_str = "Authentication server not reachable. Please try after some time";
                    } else if (error.toString().contains("NoConnectionError")) {
                        error_str = "No Connection. Please try after some time";
                    } else {
                        if (error.networkResponse != null) //network response
                        {
                            final int status = error.networkResponse.statusCode;
                            //Log.i(TAG, "Status : " + status);
                            // Handle 30x
                            if (HttpURLConnection.HTTP_MOVED_PERM == status || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                                final String location = error.networkResponse.headers.get("Location");

                                if (location.contains("bing")) {
                                    listener.onSuccess();
                                } else {
                                    error_str = "Invalid Credentials Provided";
                                }

                            }
                        } else {
                            error_str = "No Network response";
                        }
                    }
                }
//                Log.i("YOYO", "Message for noobs: " + error_str);

                listener.onFailure(error_str);

            }

        };

        strReq.setTag(request_Tag);
        requestQueue.add(strReq);
    }

}
