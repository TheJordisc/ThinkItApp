package net.xeill.elpuig.thinkitapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;

public class MainActivity extends AppCompatActivity {
    MediaPlayer musicPlayer;
    MediaPlayer playSoundPlayer;
    VideoView bgVideo;
    MediaController.MediaPlayerControl videoPlayer;
    MediaController mediaController;
    Handler mSplashHandler;
    SharedPreferences settings;
    FloatingActionButton volumeFAB;

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
        musicPlayer.setLooping(true); // Set looping
        playSoundPlayer = MediaPlayer.create(this,R.raw.play);

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

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();

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


//                        if (settings.getBoolean("isFirstPlay", true)) {
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
        musicPlayer.setVolume(0.7f,0.7f);
        playSoundPlayer.setVolume(1f,1f);

        volumeFAB.setActivated(true);
        ViewCompat.setBackgroundTintList(volumeFAB, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        settings.edit().putBoolean("mute",false).apply();
    }

    private void setMute() {
        musicPlayer.setVolume(0f,0f);
        playSoundPlayer.setVolume(0f,0f);

        volumeFAB.setActivated(false);
        //TODO: NO VA EN 4.4
//        ViewCompat.setBackground(volumeFAB,getResources().getDrawable(R.drawable.fab_volume));
        ViewCompat.setBackgroundTintList(volumeFAB, ColorStateList.valueOf(getResources().getColor(R.color.color_grey_disabled)));
        settings.edit().putBoolean("mute",true).apply();
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
