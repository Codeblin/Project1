package com.jiannapohotmail.com.project1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_identification", MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        if (isFirstTime){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();

            startMapsActivity(1, HelpActivity.class);
        }else {
            startMapsActivity(1, MapActivity.class);
        }
    }

    private void startMapsActivity(int secondsDelayed, Class<?> targetAcitvityClass){
        final Intent intent = new Intent(this, targetAcitvityClass);
        intent.putExtra("visibility_flag", View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
                finish();
            }
        }, secondsDelayed * 1000);
    }
}
