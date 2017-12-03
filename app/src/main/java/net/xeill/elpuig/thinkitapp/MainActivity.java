package net.xeill.elpuig.thinkitapp;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer musicPlayer;
    MediaController.MediaPlayerControl videoPlayer;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicPlayer = MediaPlayer.create(this,  R.raw.theme);
        musicPlayer.start();
        musicPlayer.setLooping(true); // Set looping
        musicPlayer.setVolume(100,100);

        VideoView bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.background));
        bgVideo.start();

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        musicPlayer.release();
        //videoPlayer.stop();
        //videoPlayer.release();
    }
}
