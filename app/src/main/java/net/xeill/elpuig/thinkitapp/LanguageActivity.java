package net.xeill.elpuig.thinkitapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class LanguageActivity extends AppCompatActivity {
    MediaPlayer playSoundPlayer;
    //TODO: play sound + setLocale
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        playSoundPlayer = MediaPlayer.create(this,R.raw.play);
        playSoundPlayer.setVolume(1f,1f);

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
}