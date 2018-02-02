package com.jiannapohotmail.com.project1.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jiannapohotmail.com.project1.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
