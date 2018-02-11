package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;

import java.util.ArrayList;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnCompleteListener;
import me.toptas.fancyshowcase.OnViewInflateListener;

public class MathsTutorialActivity extends AppCompatActivity {
    //TODO: Indicadores cuando se espera acción de usuario

    SharedPreferences settings;

    MediaPlayer musicPlayer;
    MediaPlayer correctPlayer;
    MediaPlayer levelUpPlayer;
    MediaPlayer mLifelinePlayer;
    VideoView bgVideo;

    ImageView mLifeline5050Cross;
    AppCompatImageButton lifeline;
    Button mNextButton;
    LinearLayout keyboard;
    LinearLayout lifelineDrawer;

    LinearLayout op1;
    LinearLayout op2;

    TextView op1OpType;
    TextView op2OpType;

    TextView op1Op1TV;
    TextView op1Op2TV;
    TextView op1ResTV;

    TextView op2Op1TV;
    TextView op2Op2TV;
    TextView op2ResTV;

    TextView mScoreText;
    TextView mLevelText;
    TextView mLivesContainer;

    TextView bonusScore;
    TextView bonusTime;

    TextView mLevelBox;

    AppCompatTextView mTimer;

    FloatingActionButton stopFAB;

    FancyShowCaseQueue mQueue,mQueue2,mQueue3, mQueue4,mQueue5;


    List<AppCompatButton> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths);

        settings=getSharedPreferences("prefs", 0);

        musicPlayer = MediaPlayer.create(this,  R.raw.bensound_summer);
        musicPlayer.setLooping(true);

        correctPlayer = MediaPlayer.create(MathsTutorialActivity.this,R.raw.correct);
        levelUpPlayer = MediaPlayer.create(MathsTutorialActivity.this,R.raw.levelup);
        mLifelinePlayer = MediaPlayer.create(MathsTutorialActivity.this,R.raw.lifeline);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();

        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.bg_maths));

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });

        lifelineDrawer = findViewById(R.id.lifeline_drawer);
        lifeline = findViewById(R.id.lifeline_50_50);
        mLifeline5050Cross = findViewById(R.id.lifeline_50_50_x);

        mNextButton = findViewById(R.id.button_next_tutorial);

        op1Op1TV = findViewById(R.id.oper1_op1);
        op1Op2TV = findViewById(R.id.oper1_op2);
        op1ResTV = findViewById(R.id.oper1_res);

        op2Op1TV = findViewById(R.id.oper2_op1);
        op2Op2TV = findViewById(R.id.oper2_op2);
        op2ResTV = findViewById(R.id.oper2_res);

        op1OpType = findViewById(R.id.oper1_opType);
        op2OpType = findViewById(R.id.oper2_opType);

        op1 = findViewById(R.id.op1_layout);
        op2 = findViewById(R.id.op2_layout);

        keyboard = findViewById(R.id.keyboard_line2);

        mScoreText = findViewById(R.id.score);
        mLevelText = findViewById(R.id.level);

        mTimer=findViewById(R.id.timer);

        stopFAB = findViewById(R.id.fab_stop);

        //TODO: REVISAR BONUS!
        bonusScore = findViewById(R.id.added_score);
        bonusScore.setText("+100\n+1000");
        bonusTime = findViewById(R.id.added_time);
        bonusTime.setText("+00:05");

        mLivesContainer = findViewById(R.id.lives_fake_container);
        mLivesContainer.setVisibility(View.VISIBLE);
        mLevelBox = findViewById(R.id.level_up);

        buttons.add((AppCompatButton) findViewById(R.id.answer1));
        buttons.add((AppCompatButton) findViewById(R.id.answer2));
        buttons.add((AppCompatButton) findViewById(R.id.answer3));
        buttons.add((AppCompatButton) findViewById(R.id.answer4));
        buttons.add((AppCompatButton) findViewById(R.id.answer5));
        buttons.add((AppCompatButton) findViewById(R.id.answer6));
        buttons.add((AppCompatButton) findViewById(R.id.answer7));
        buttons.add((AppCompatButton) findViewById(R.id.answer8));

        for (int i = 4; i < 8; i++) {
            buttons.get(i).setVisibility(View.GONE);
        }


        op1Op1TV.setText("10");
        op1OpType.setText("+");
        op1Op2TV.setText("?");
        setQuestion(op1Op2TV);
        op1ResTV.setText("12");

        op2Op1TV.setText("5");
        op2OpType.setText("-");
        op2Op2TV.setText("1");
        op2ResTV.setText("?");
        setQuestionNext(op2ResTV);

        mScoreText.setText("1500");
        mLevelText.setText(getString(R.string.level) + " 1");

        mLevelBox.setText(getString(R.string.level) + " 2");

        mTimer.setText("00:10");

        buttons.get(0).setText("1");
        buttons.get(1).setText("8");
        buttons.get(2).setText("2");
        buttons.get(3).setText("10");
        buttons.get(4).setText("4");
        buttons.get(5).setText("6");
        buttons.get(6).setText("11");
        buttons.get(7).setText("3");

        Animation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(250);

        final FancyShowCaseView fancyShowCaseViewWelcome = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_welcome1))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewWelcome2 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_welcome2))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewMainOperation = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_main_operation))
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .focusOn(op1)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewNextOperation = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_next_operation))
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(op2)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewAnswers = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_answers))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(keyboard)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewTimer = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_timer))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mTimer)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewScore = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_score))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mScoreText)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewScore2 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_score2))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mScoreText)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewLives = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.lives))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mLivesContainer)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewLevels = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_levels))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mLevelText)
                .enterAnimation(animation)
                .build();

        mQueue = new FancyShowCaseQueue()
                .add(fancyShowCaseViewWelcome)
                .add(fancyShowCaseViewWelcome2)
                .add(fancyShowCaseViewMainOperation)
                .add(fancyShowCaseViewNextOperation)
                .add(fancyShowCaseViewAnswers)
                .add(fancyShowCaseViewTimer)
                .add(fancyShowCaseViewScore)
                .add(fancyShowCaseViewScore2)
                .add(fancyShowCaseViewLives)
                .add(fancyShowCaseViewLevels);
        mQueue.show();

        final FancyShowCaseView fancyShowCaseViewHardness = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.hardness))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(keyboard)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewPress = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_try_answer))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewComplete = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_complete_operation1))
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(op1)
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewComplete2 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_complete_operation2))
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .focusOn(op2)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewBonus1 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_bonus1))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(bonusTime)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewBonus2 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_bonus2))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(bonusScore)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewLifeLines = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_lifelines1))
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .focusOn(lifelineDrawer)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewLifeLines2 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_lifelines2))
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .focusOn(lifelineDrawer)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewLifeLines3 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_lifelines3))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewLifeLines4 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_lifelines4))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(keyboard)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewGameOver = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_gameover))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(stopFAB)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewGameOver2 = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_gameover2))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        final FancyShowCaseView fancyShowCaseViewEnd = new FancyShowCaseView.Builder(this)
                .title(getResources().getString(R.string.tutorial_end))
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .enterAnimation(animation)
                .build();

        mQueue.setCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                mLevelBox.setVisibility(View.VISIBLE);
                levelUpPlayer.start();
                mLevelText.setText(getString(R.string.level) + " 2");
                buttons.get(4).setVisibility(View.VISIBLE);
                buttons.get(5).setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLevelBox.setVisibility(View.GONE);
                        mQueue2 = new FancyShowCaseQueue().add(fancyShowCaseViewHardness).add(fancyShowCaseViewPress);

                        mQueue2.show();

                        buttons.get(2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(getResources().getColor(R.color.color_green_correct)));
                                correctPlayer.start();

                                for (Button b : buttons) {
                                    b.setEnabled(false);
                                }
                                buttons.get(2).setOnClickListener(null);

                                op1Op2TV.setTextColor(getResources().getColor(R.color.color_green_correct));
                                op1Op2TV.setTextSize(40f);
                                op1Op2TV.setText("2");
                                ViewCompat.setBackground(op1Op2TV,getResources().getDrawable(R.drawable.square_back_correct));
                                bonusScore.setVisibility(View.VISIBLE);
                                bonusTime.setVisibility(View.VISIBLE);
                                mTimer.setText("00:15");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mQueue3=new FancyShowCaseQueue()
                                                .add(fancyShowCaseViewComplete)
                                                .add(fancyShowCaseViewComplete2)
                                                .add(fancyShowCaseViewBonus1)
                                                .add(fancyShowCaseViewBonus2);

                                        mQueue3.show();

                                        mQueue3.setCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete() {
                                                bonusScore.setVisibility(View.GONE);
                                                bonusTime.setVisibility(View.GONE);
                                                op1Op1TV.setText(op2Op1TV.getText());
                                                op1OpType.setText(op2OpType.getText());
                                                op1Op2TV.setText(op2Op2TV.getText());
                                                op1ResTV.setText(op2ResTV.getText());

                                                op2Op1TV.setText("9");
                                                op2OpType.setText("x");
                                                op2Op2TV.setText("2");
                                                op2ResTV.setText("?");

                                                //TODO: Poner estilo pregunta y estilos normales
                                                //rellenar teclado de nuevo
                                                //quitar verde de opcion y de operacion
                                                op1ResTV.setTextSize(60);
                                                op1ResTV.setTextColor(getResources().getColor(R.color.color_question));
                                                op1ResTV.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_question));

                                                op1Op2TV.setTextColor(getResources().getColor(R.color.color_black));
                                                op1Op2TV.setTextSize(40f);
                                                op1Op2TV.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_normal));

                                                buttons.get(0).setText("8");
                                                buttons.get(1).setText("1");
                                                buttons.get(2).setText("9");
                                                buttons.get(3).setText("11");
                                                buttons.get(4).setText("4");
                                                buttons.get(5).setText("6");

                                                ViewCompat.setBackgroundTintList(buttons.get(2),ViewCompat.getBackgroundTintList(buttons.get(0)));

                                                for (Button button: buttons) {
                                                    button.setEnabled(true);
                                                }

                                                mQueue4 = new FancyShowCaseQueue()
                                                        .add(fancyShowCaseViewLifeLines)
                                                        .add(fancyShowCaseViewLifeLines2)
                                                        .add(fancyShowCaseViewLifeLines3);

                                                new Handler().postDelayed(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              mQueue4.show();

                                                              mQueue4.setCompleteListener(new OnCompleteListener() {
                                                                  @Override
                                                                  public void onComplete() {
                                                                      lifeline.setOnClickListener(new View.OnClickListener() {
                                                                          @Override
                                                                          public void onClick(View view) {
                                                                              lifeline.setOnClickListener(null);
                                                                              mLifeline5050Cross.setVisibility(View.VISIBLE);
                                                                              view.setEnabled(false);
                                                                              ViewCompat.setBackgroundTintList(view,ColorStateList.valueOf(Color.GRAY));
                                                                              mLifelinePlayer.start();

                                                                              buttons.get(0).setText("");
                                                                              buttons.get(2).setText("");
                                                                              buttons.get(5).setText("");

                                                                              mQueue5 = new FancyShowCaseQueue()
                                                                                      .add(fancyShowCaseViewLifeLines4)
                                                                                      .add(fancyShowCaseViewGameOver)
                                                                                      .add(fancyShowCaseViewGameOver2);

                                                                              new Handler().postDelayed(new Runnable() {
                                                                                                            @Override
                                                                                                            public void run() {
                                                                                                                mQueue5.show();
                                                                                                                mQueue5.setCompleteListener(new OnCompleteListener() {
                                                                                                                    @Override
                                                                                                                    public void onComplete() {
                                                                                                                        //TODO: stop listener
                                                                                                                        //gameover sound, cartel
                                                                                                                        //2seg -> ultimo showcase
                                                                                                                        //on complete -> handler + Intent
                                                                                                                        stopFAB.setOnClickListener(new View.OnClickListener() {
                                                                                                                            @Override
                                                                                                                            public void onClick(View view) {
                                                                                                                                Intent playIntent = new Intent(MathsTutorialActivity.this,MathsActivity.class);
                                                                                                                                startActivity(playIntent);
                                                                                                                            }
                                                                                                                        });
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        },1000);


                                                                          }
                                                                      });
                                                                  }
                                                              });
                                                          }
                                                      },1000);


                                            }
                                        });
                                    }
                                },1000);

                            }
                        });

                    }
                },2000);
            }
        });


    }

    private void setQuestionNext(TextView op) {
        op.setTextSize(40);
        op.setTextColor(getResources().getColor(R.color.color_question));
        op.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_question));
    }

    private void setQuestion(TextView op) {
        op.setTextSize(60);
        op.setTextColor(getResources().getColor(R.color.color_question));
        op.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_question));
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
        musicPlayer.setVolume(0.7f,0.7f);
        correctPlayer.setVolume(1f,1f);
        levelUpPlayer.setVolume(1f,1f);
        mLifelinePlayer.setVolume(1f,1f);
    }

    private void setMute() {
        musicPlayer.setVolume(0f,0f);
        correctPlayer.setVolume(0f,0f);
        levelUpPlayer.setVolume(0f,0f);
        mLifelinePlayer.setVolume(0f,0f);
    }
}
