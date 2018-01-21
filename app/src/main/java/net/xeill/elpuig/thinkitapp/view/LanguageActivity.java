package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;

public class LanguageActivity extends AppCompatActivity {
    MediaPlayer playSoundPlayer;
    VideoView bgVideo;
    //TODO: play sound + setLocale
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.bg_language));
        bgVideo.start();

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        playSoundPlayer = MediaPlayer.create(this,R.raw.play);
        playSoundPlayer.setVolume(1f,1f);

        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(LanguageActivity.this,MainActivity.class);
                startActivity(homeIntent);
                LanguageActivity.this.finish();
            }
        });

        final ImageButton cat_flag = findViewById(R.id.catButton1);
        cat_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuIntent();
            }
        });

        final ImageButton es_flag = findViewById(R.id.esButton2);
        es_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuIntent();
            }
        });

        final ImageButton it_flag = findViewById(R.id.itButton3);
        it_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuIntent();
            }
        });

        final ImageButton uk_flag = findViewById(R.id.ukButton4);
        uk_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuIntent();
            }
        });
    }
    public void menuIntent(){
        Intent menuIntent = new Intent(LanguageActivity.this,MainActivity.class);
        playSoundPlayer.start();
        startActivity(menuIntent);
        LanguageActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(LanguageActivity.this,MainActivity.class);
        startActivity(mainIntent);
        LanguageActivity.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(bgVideo!=null && bgVideo.isPlaying()){
            bgVideo.stopPlayback();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bgVideo!=null && !bgVideo.isPlaying()){
            bgVideo.start();
        }
    }
}