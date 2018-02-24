package net.xeill.elpuig.thinkitapp.view;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
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

        if (settings.getBoolean("isFirstRun",true)) {
            ab.setDisplayHomeAsUpEnabled(false);
        } else {
            musicPlayer.start();
        }

        final ImageButton cat_flag = findViewById(R.id.catButton1);
        cat_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LocaleManager.setNewLocale(getApplicationContext(),"ca");
                LocaleManager.setLocale(LanguageActivity.this,"ca");
                menuIntent(getResources().getString(R.string.ca_flag));
            }
        });

        final ImageButton es_flag = findViewById(R.id.esButton2);
        es_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LocaleManager.setNewLocale(getApplicationContext(),"es");
                LocaleManager.setLocale(LanguageActivity.this,"es");
                menuIntent(getResources().getString(R.string.es_flag));
            }
        });

        final ImageButton it_flag = findViewById(R.id.itButton3);
        it_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LocaleManager.setNewLocale(getApplicationContext(),"it");
                LocaleManager.setLocale(LanguageActivity.this,"it");
                menuIntent(getResources().getString(R.string.it_flag));
            }
        });

        final ImageButton uk_flag = findViewById(R.id.ukButton4);
        uk_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LocaleManager.setNewLocale(getApplicationContext(),"en");
                LocaleManager.setLocale(LanguageActivity.this,"en");
                menuIntent(getResources().getString(R.string.uk_flag));
            }
        });
    }
    public void menuIntent(String language){
        //TODO: Convertir a resource después de importar strings traducidos
        Toast.makeText(this,getResources().getString(R.string.language_change_toast) + language, Toast.LENGTH_SHORT).show();

        if (settings.getBoolean("isFirstRun",true)) {
            settings.edit().putBoolean("isFirstRun",false).apply();
        }

        playSoundPlayer.start();
        LanguageActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        if (settings.getBoolean("isFirstRun",true)) {
            finishAffinity(); //this method finalize all activities
            System.exit(0);
        } else {
            finish();
        }
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

        if(musicPlayer!=null && !musicPlayer.isPlaying() && settings.getBoolean("isFirstRun",false)){
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