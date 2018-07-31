package com.BlackBox.Wifi_Login.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.BlackBox.Wifi_Login.R;

public class Splash_Screen extends AppCompatActivity {

  // Splash screen timer
  static final int SPLASH_TIME_OUT = 900;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        // This method will be executed once the timer is over
        // Start your app activity_main activity
        Intent i = new Intent(Splash_Screen.this, Main_Activity.class);
        startActivity(i);

        // close this activity
        finish();
      }
    }, SPLASH_TIME_OUT);
  }

}
