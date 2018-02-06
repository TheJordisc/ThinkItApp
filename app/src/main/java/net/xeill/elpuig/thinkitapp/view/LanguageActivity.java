package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.view.manager.LocaleManager;

public class LanguageActivity extends AppCompatActivity {
    MediaPlayer playSoundPlayer;
    MediaPlayer musicPlayer;
    VideoView bgVideo;
    SharedPreferences settings;

    //TODO: play sound + setLocale
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        LocaleManager.setLocale(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        settings=getSharedPreferences("prefs", 0);

        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.bg_language));
        bgVideo.start();

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        musicPlayer = MediaPlayer.create(this, R.raw.bensound_summer);
        playSoundPlayer = MediaPlayer.create(this,R.raw.play);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();

        final ImageButton cat_flag = findViewById(R.id.catButton1);
        cat_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleManager.setNewLocale(getApplicationContext(),"ca");
                menuIntent();
            }
        });

        final ImageButton es_flag = findViewById(R.id.esButton2);
        es_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleManager.setNewLocale(getApplicationContext(),"es");
                menuIntent();
            }
        });

        final ImageButton it_flag = findViewById(R.id.itButton3);
        it_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleManager.setNewLocale(getApplicationContext(),"it");
                menuIntent();
            }
        });

        final ImageButton uk_flag = findViewById(R.id.ukButton4);
        uk_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleManager.setNewLocale(getApplicationContext(),"en");
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

        if(musicPlayer!=null && musicPlayer.isPlaying()){
            musicPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bgVideo!=null && !bgVideo.isPlaying()){
            bgVideo.start();
        }

        if(musicPlayer!=null && !musicPlayer.isPlaying()){
            musicPlayer.start();
        }
    }

    private void setUnmute() {
        musicPlayer.setVolume(1f,1f);
        playSoundPlayer.setVolume(1f,1f);
    }

    private void setMute() {
        musicPlayer.setVolume(0f,0f);
        playSoundPlayer.setVolume(1f,1f);
    }
}