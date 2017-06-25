package com.BlackBox.Wifi_Login.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.BlackBox.Wifi_Login.R;
import com.BlackBox.Wifi_Login.Services.BackgroundService;

public class StopServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_service);
    }

    public void stopService(View view) {
        Intent i = new Intent(getApplicationContext(), BackgroundService.class);
        stopService(i);
    }
}
