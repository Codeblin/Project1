package com.jiannapohotmail.com.project1.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.jiannapohotmail.com.project1.R;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Get video from Raw
        VideoView videoView = findViewById(R.id.video_view);
        String path = "android.resource://" + getPackageName() + "/" + getIntent().getExtras().getInt("video_resource");
        videoView.setVideoURI(Uri.parse(path));

        // Set video control buttons
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set video view fullscreen
        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);

        videoView.start();
    }
}
