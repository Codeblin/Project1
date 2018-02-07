package com.jiannapohotmail.com.project1.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jiannapohotmail.com.project1.R;

public class HelpActivity extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button btn = findViewById(R.id.btnNext);
        mp = MediaPlayer.create(this, R.raw.voice);
        mp.start();

        btn.setVisibility(getIntent().getIntExtra("visibility_flag", 0));

        checkLocationPermission();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Χρήση τοποθεσίας")
                        .setMessage("Για να μπορέσει η εφαρμογή να λειτουργήσει σωστά, θα πρέπει να επιτρέψεις την εύρεση τοποθεσίας.")
                        .setPositiveButton("Να επιτραπεί", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HelpActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).setCancelable(false)
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void GotoMap(View view){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        if (mp != null){
            mp.stop();
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        if (mp != null){
            if (!mp.isPlaying()){
                mp = MediaPlayer.create(getApplicationContext(), R.raw.voice);
                mp.start();
            }
        }

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        clearMediaPlayer();
        super.onDestroy();
    }

    private void clearMediaPlayer(){
        if (mp != null) {
            mp.reset();
            mp.release();
            mp = null;
        }
    }
}
