package net.xeill.elpuig.thinkitapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;

public class MainActivity extends AppCompatActivity {


    //TODO: Silenciar TODA la aplicacion, no solo la main
    MediaPlayer musicPlayer;
    MediaPlayer playSoundPlayer;
    VideoView bgVideo;
    MediaController.MediaPlayerControl videoPlayer;
    MediaController mediaController;
    Handler mSplashHandler;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings=getSharedPreferences("prefs", 0);

        if (settings.getBoolean("isFirstRun",true)) {
            settings.edit().putBoolean("mute",false).apply();
            settings.edit().putBoolean("isFirstRun",false).apply();
        }

        mSplashHandler = new Handler();

        musicPlayer = MediaPlayer.create(this,  R.raw.theme);
        musicPlayer.start();
        musicPlayer.setLooping(true); // Set looping
        playSoundPlayer = MediaPlayer.create(this,R.raw.play);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.background));
        bgVideo.start();

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });



        final ImageView playButton = findViewById(R.id.play_button_image);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.setActivated(true);
                playSoundPlayer.start();

                mSplashHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if (settings.getBoolean("isFirstPlay", true)) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(R.string.tutorial_msg)
                                    .setPositiveButton(R.string.tutorial_yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            musicPlayer.stop();
                                            Intent playIntent = new Intent(MainActivity.this,MathsTutorialActivity.class);
                                            startActivity(playIntent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.tutorial_no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            musicPlayer.stop();
                                            Intent playIntent = new Intent(MainActivity.this,MathsActivity.class);
                                            startActivity(playIntent);
                                            finish();
                                        }
                                    })
                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            playButton.setActivated(false);
                                        }
                                    })
                                    .create().show();
                            //TODO:DEBUG ONLY. UNCOMMENT ON RELEASE
                            settings.edit().putBoolean("isFirstPlay",false).apply();
                        } else {
                            musicPlayer.stop();
                            Intent playIntent = new Intent(MainActivity.this,MathsActivity.class);
                            startActivity(playIntent);
                            finish();
                        }

                    }
                }, 1000L);
            }
        });

        final FloatingActionButton volumeFAB = findViewById(R.id.volume_fab);
        if (settings.getBoolean("mute",true)) {
            volumeFAB.setActivated(false);
        } else {
            volumeFAB.setActivated(true);
        }


        volumeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volumeFAB.isActivated()) {
                    volumeFAB.setActivated(false);
                    settings.edit().putBoolean("mute",true).apply();
                    setMute();
                } else {
                    volumeFAB.setActivated(true);
                    settings.edit().putBoolean("mute",false).apply();
                    setUnmute();
                }

            }
        });

        FloatingActionButton languageFAB = findViewById(R.id.language_fab);
        languageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent languageIntent = new Intent(MainActivity.this,LanguageActivity.class);
                startActivity(languageIntent);
                MainActivity.this.finish();
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
                                MainActivity.this.finish();
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
        musicPlayer.setVolume(0.5f,0.5f);
        playSoundPlayer.setVolume(1f,1f);
    }

    private void setMute() {
        musicPlayer.setVolume(0f,0f);
        playSoundPlayer.setVolume(0f,0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(musicPlayer!=null && musicPlayer.isPlaying()){
            musicPlayer.pause();
        }

        if(bgVideo!=null && bgVideo.isPlaying()){
            bgVideo.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(musicPlayer!=null && !musicPlayer.isPlaying()){
            musicPlayer.start();
        }

        if(bgVideo!=null && !bgVideo.isPlaying()){
            bgVideo.start();
        }
    }
}
