package net.xeill.elpuig.thinkitapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.view.manager.LocaleManager;

public class MainActivity extends AppCompatActivity {
    MediaPlayer playSoundPlayer;
    MediaPlayer musicPlayer;
    VideoView bgVideo;
    Handler mSplashHandler;
    SharedPreferences settings;
    FloatingActionButton volumeFAB;

    private SoundPool soundPool;
    private int soundIds[] = new int[2];

    private int volume = 0;

    boolean musicPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings=getSharedPreferences("prefs", 0);


        if (settings.getBoolean("isFirstRun",true)) {
            settings.edit().putBoolean("mute",false).apply();

            Intent firstTime = new Intent(MainActivity.this, LanguageActivity.class);
            finish();
            startActivity(firstTime);
        } else {
            String lang = settings.getString("language","en");
            LocaleManager.setLocale(this,lang);
        }

        mSplashHandler = new Handler();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(2)
                    .build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //Unused music from soundpool, using MediaPlayer
//        soundIds[0] = soundPool.load(this, R.raw.modern_theme_nicolai_heidlas,1);
        soundIds[1] = soundPool.load(this, R.raw.play,1);

//        playSoundPlayer = MediaPlayer.create(this,R.raw.play);

        volumeFAB = findViewById(R.id.volume_fab);

        volumeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volumeFAB.isActivated()) {
                    setMute();
                } else {
                    setUnmute();
                }

            }
        });

//        soundPool.play(soundIds[0], volume, volume, 1, 1, 1);


        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.background2));

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        bgVideo.start();



        final ImageView playButton = findViewById(R.id.play_button_image);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setActivated(true);
                playButton.setEnabled(false);
//                playSoundPlayer.start();
                soundPool.play(soundIds[1], volume, volume, 1, 0, 1);

                mSplashHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


//                        if (settings.getBoolean("isFirstPlay", true)) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(R.string.tutorial_msg)
                                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                            soundPool.stop(soundIds[0]);
                                            musicPlayer.stop();
                                            Intent playIntent = new Intent(MainActivity.this,MathsTutorialActivity.class);
                                            startActivity(playIntent);
                                        }
                                    })
                                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                            soundPool.stop(soundIds[0]);
                                            musicPlayer.stop();
                                            Intent playIntent = new Intent(MainActivity.this,MathsActivity.class);
                                            startActivity(playIntent);
                                        }
                                    })
                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            playButton.setActivated(false);
                                            playButton.setEnabled(true);

                                        }
                                    })
                                    .create().show();
                            //TODO:DEBUG ONLY. UNCOMMENT ON RELEASE
                            settings.edit().putBoolean("isFirstPlay",false).apply();
//                        } else {
//                            musicPlayer.stop();
//                            Intent playIntent = new Intent(MainActivity.this,MathsActivity.class);
//                            startActivity(playIntent);
//                            finish();
//                        }

                    }
                }, 1000L);
            }
        });



        FloatingActionButton languageFAB = findViewById(R.id.language_fab);
        languageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageIntent = new Intent(MainActivity.this,LanguageActivity.class);
                startActivity(languageIntent);
            }
        });

        FloatingActionButton helpFAB = findViewById(R.id.help_fab);
        helpFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpIntent = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(helpIntent);
            }
        });

        FloatingActionButton closeFAB = findViewById(R.id.close_fab);
        closeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.exit_sure)
                        .setPositiveButton(R.string.exit_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                MainActivity.this.finish();
                                finishAffinity(); //this method finalize all activities
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(R.string.exit_back, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // NOTHING
                            }
                        })
                        .create().show();
            }
        });
        FloatingActionButton scoreFAB = findViewById(R.id.score_fab);
        scoreFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scoreIntent = new Intent(MainActivity.this,ScoreActivity.class);
                startActivity(scoreIntent);
            }
        });
    }

    private void setUnmute() {
        //TODO: Controlar volumen across the activities
//        soundPool.setVolume(soundIds[0],0.8f,0.8f);
//        soundPool.setVolume(soundIds[1],1f,1f);
//        playSoundPlayer.setVolume(1f,1f);

        musicPlayer.setVolume(0.8f,0.8f);
        volume=1;
        volumeFAB.setActivated(true);
//        ViewCompat.setBackgroundTintList(volumeFAB, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        volumeFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

        settings.edit().putBoolean("mute",false).apply();
    }

    private void setMute() {
        volume=0;
        musicPlayer.setVolume(0,0);
        volumeFAB.setActivated(false);
//        ViewCompat.setBackground(volumeFAB,getResources().getDrawable(R.drawable.fab_volume));
//        ViewCompat.setBackgroundTintList(volumeFAB, ColorStateList.valueOf(getResources().getColor(R.color.color_grey_disabled)));
        volumeFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_grey_disabled)));

        settings.edit().putBoolean("mute",true).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();


        if(!musicPaused){
            musicPlayer.pause();
        }

        if(bgVideo!=null && bgVideo.isPlaying()){
            bgVideo.pause();
        }

        musicPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();


        if(musicPaused){
            musicPlayer.start();
        }

        if(bgVideo!=null && !bgVideo.isPlaying()){
            bgVideo.start();
        }

        musicPaused = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        musicPlayer = MediaPlayer.create(this,  R.raw.modern_theme_nicolai_heidlas);
        musicPlayer.setLooping(true);
        musicPlayer.start();

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }
    }
}
