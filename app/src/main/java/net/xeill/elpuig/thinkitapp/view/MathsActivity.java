package net.xeill.elpuig.thinkitapp.view;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Operation;

import java.util.ArrayList;
import java.util.List;

public class MathsActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences settings;
    MediaPlayer mMusicPlayer;
    MediaPlayer mCountdownPlayer;
    MediaPlayer mLifelinePlayer;
    MediaPlayer mLevelUpPlayer;
    MediaPlayer mLastLifePlayer;
    MediaPlayer mIncorrectPlayer;
    MediaPlayer mCorrectPlayer;
    VideoView bgVideo;

    int mCorrectAnswers =0;
    List<AppCompatButton> answerButtons;
    int mEnabledButtons=4;
    int mCorrectButtonIndex;
    boolean firstTime = true;

    ColorStateList defColor = ColorStateList.valueOf(Color.GRAY);
    ColorStateList defButtonColor;
    Drawable defTimerColor;

    float defOp1Size = 40;
    float defOp2Size = 24;
    Operation op1=null;
    Operation op2=null;

    int mLives=3;

    AppCompatTextView mTimer;
    CountDownTimer mCountdownTimer;

    boolean mCountdownPlayed = false;
    int mScore=0;

    TextView op1OpType;
    TextView op2OpType;

    TextView op1Op1TV;
    TextView op1Op2TV;
    TextView op1ResTV;

    TextView op2Op1TV;
    TextView op2Op2TV;
    TextView op2ResTV;

    TextView mScoreText;
    TextView mAddedScoreText;
    TextView mAddedTimeText;
    TextView mLevelText;
    TextView mLastLife;
    TextView mLevelUp;

    TextView mLifelineHint;

    long mBonusTime=0;
    boolean mHasBonus=true;
    long mInitialMillis=0;
    long mMillisLeft =0;

    boolean mAnswerWasCorrect=false;
    boolean isFirstAnswer=true;

    boolean mLifeline5050Used = false;
    boolean mLifelinePassoverUsed = false;

    boolean mLifelineHintShown = false;

    ImageButton mLifeline5050;
    ImageButton mLifelinePassover;

    //TODO: FOR DEBUG ONLY
    //int level=5;
    int level = 1;
    int scalableLevelsStartValueOp1 = 50;
    int scalableLevelsStartValueOp2 = 50;
    int scalableLevelsStartValueOp1Bis = 40; //Bis is for multiplications and divisions
    int scalableLevelsStartValueOp2Bis = 20;

    boolean mPaused;
    boolean mTimeoutOnPause = false;
    Drawable defQuestionBackground;
    MediaPlayer mFastMusicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maths);

        //TODO: Añadir créditos bensound.com en help/about
        mMusicPlayer = MediaPlayer.create(this,R.raw.bensound_jazzyfrenchy);
        mFastMusicPlayer = MediaPlayer.create(this,R.raw.theme1_fast);
        mMusicPlayer.setLooping(true);
        mFastMusicPlayer.setLooping(true);

        mLifelinePlayer = MediaPlayer.create(MathsActivity.this,R.raw.lifeline);
        mLevelUpPlayer = MediaPlayer.create(MathsActivity.this,R.raw.levelup);
        mCountdownPlayer = MediaPlayer.create(MathsActivity.this,R.raw.countdown);
        mLastLifePlayer = MediaPlayer.create(MathsActivity.this,R.raw.lastlife);
        mIncorrectPlayer = MediaPlayer.create(MathsActivity.this,R.raw.incorrect);
        mCorrectPlayer = MediaPlayer.create(MathsActivity.this,R.raw.correct);

        settings=getSharedPreferences("prefs", 0);
        if (settings.getBoolean("mute",true)) {
            mMusicPlayer.setVolume(0f,0f);
            mFastMusicPlayer.setVolume(0f,0f);
            mLifelinePlayer.setVolume(0f,0f);
            mLevelUpPlayer.setVolume(0f,0f);
            mCountdownPlayer.setVolume(0f,0f);
            mLastLifePlayer.setVolume(0f,0f);
            mIncorrectPlayer.setVolume(0f,0f);
            mCorrectPlayer.setVolume(0f,0f);
        } else {
            mMusicPlayer.setVolume(0.7f,0.7f);
            mFastMusicPlayer.setVolume(0.7f,0.7f);
            mLifelinePlayer.setVolume(1f,1f);
            mLevelUpPlayer.setVolume(1f,1f);
            mCountdownPlayer.setVolume(1f,1f);
            mLastLifePlayer.setVolume(1f,1f);
            mIncorrectPlayer.setVolume(1f,1f);
            mCorrectPlayer.setVolume(1f,1f);
        }

        mMusicPlayer.start();

        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.bg_maths));
        bgVideo.start();

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MathsActivity.this)
                        .setMessage(R.string.home_sure)
                        .setPositiveButton(R.string.exit_menu, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent homeIntent = new Intent(MathsActivity.this,MainActivity.class);
                                startActivity(homeIntent);
                                MathsActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.home_resume, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // NOTHING
                            }
                        })
                        .create().show();
            }
        });

        answerButtons = new ArrayList<>();
        answerButtons.add((AppCompatButton) findViewById(R.id.answer1));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer2));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer3));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer4));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer5));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer6));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer7));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer8));


        for (AppCompatButton b : answerButtons) {
            b.setVisibility(View.GONE);
        }

        mScoreText = findViewById(R.id.score);
        mScoreText.setText(mScore+"");
        mAddedScoreText = findViewById(R.id.added_score);
        mAddedTimeText = findViewById(R.id.added_time);
        mTimer=findViewById(R.id.timer);
        //TODO: si no hay comodines que se puedan utilizar no mostrar
        mLifelineHint = findViewById(R.id.hint_lifelines_text);

        mLevelText = findViewById(R.id.level);
        mLevelText.setText(getString(R.string.level) + " " + level);

        mLastLife = findViewById(R.id.last_life);
        mLevelUp = findViewById(R.id.level_up);

        op1Op1TV = findViewById(R.id.oper1_op1);
        op1Op2TV = findViewById(R.id.oper1_op2);
        op1ResTV = findViewById(R.id.oper1_res);

        op2Op1TV = findViewById(R.id.oper2_op1);
        op2Op2TV = findViewById(R.id.oper2_op2);
        op2ResTV = findViewById(R.id.oper2_res);

        op1OpType = findViewById(R.id.oper1_opType);
        op2OpType = findViewById(R.id.oper2_opType);

        defButtonColor = ViewCompat.getBackgroundTintList(answerButtons.get(0));
        defQuestionBackground = op1Op1TV.getBackground();

        loadOperation();

        //COMODINES
        mLifelinePassover=findViewById(R.id.lifeline_passover);
        mLifeline5050=findViewById(R.id.lifeline_50_50);
        mLifelinePassover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLifelinePassoverUsed=true;
                view.setEnabled(false);
                ViewCompat.setBackgroundTintList(view,ColorStateList.valueOf(Color.GRAY));
                mLifelineHint.setVisibility(View.GONE);

                mCountdownTimer.cancel();

                //TODO: STOP COUNTDOWN ONPAUSE ONSTOP
                if (mCountdownPlayer != null && mCountdownPlayer.isPlaying()) {
                    mCountdownPlayer.stop();
                }

                for (AppCompatButton b : answerButtons) {
                    b.setEnabled(false);
                }

                correctAnswer();
            }
        });

        mLifeline5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLifeline5050Used=true;
                view.setEnabled(false);
                ViewCompat.setBackgroundTintList(view,ColorStateList.valueOf(Color.GRAY));
                mLifelinePlayer.start();

                mLifelineHint.setVisibility(View.GONE);

                List<Integer> toHide = new ArrayList<>();
                for (int i = 0; i < mEnabledButtons/2; i++) {
                    int hiddenButtonIndex;
                    boolean alreadyHidden;

                    do {
                        hiddenButtonIndex = ((int)(Math.random() * (((mEnabledButtons-1) - 0) + 1)) + 0);
                        alreadyHidden = false;
                        for (Integer j : toHide) {
                            if (j == hiddenButtonIndex) {
                                alreadyHidden=true;
                                break;
                            }
                        }
                    } while (hiddenButtonIndex == mCorrectButtonIndex || alreadyHidden);


                    toHide.add(hiddenButtonIndex);
                }

                for (int i : toHide) {
                    answerButtons.get(i).setText("");
                    answerButtons.get(i).setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMusicPlayer !=null && mMusicPlayer.isPlaying()){
            mMusicPlayer.stop();
        }
        if (mFastMusicPlayer != null && mFastMusicPlayer.isPlaying()) {
            mFastMusicPlayer.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mMusicPlayer !=null && mMusicPlayer.isPlaying()){
            mMusicPlayer.pause();
        }

        if(bgVideo!=null && bgVideo.isPlaying()){
            bgVideo.stopPlayback();
        }

        //mCountdownTimer.cancel();
        mPaused=true;

        if (mCountdownPlayer != null && mCountdownPlayer.isPlaying()) {
            mCountdownPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mMusicPlayer !=null && !mMusicPlayer.isPlaying()){
            mMusicPlayer.start();
        }

        if(bgVideo!=null && !bgVideo.isPlaying()){
            bgVideo.start();
        }

        //TODO: Recuperar estado de countdown, inciar como tal (como?)
        mPaused=false;
        if (mTimeoutOnPause) {
            new AlertDialog.Builder(MathsActivity.this)
                    .setMessage(R.string.dialog_timeout_on_pause)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            incorrectAnswerOrTimeOut();
                        }
                    })
                    .create().show();
        }
    }

    @Override
    public void onClick(final View view) {
        mCountdownTimer.cancel();

        //TODO: STOP COUNTDOWN ONPAUSE ONSTOP
        if (mCountdownPlayer != null) {
            mCountdownPlayer.stop();
        }

        for (AppCompatButton b : answerButtons) {
            b.setEnabled(false);
        }

        mLifeline5050.setEnabled(false);
        mLifelinePassover.setEnabled(false);

        //ACIERTA
        if (answerButtons.indexOf(view) == mCorrectButtonIndex) {
            correctAnswer();
        } else { //FALLA
            ViewCompat.setBackgroundTintList(view,ColorStateList.valueOf(Color.RED));
            view.setOnClickListener(null);

            mHasBonus=false;
            mBonusTime=0;

            incorrectAnswerOrTimeOut();
        }
    }

    private void loadOperation() {
        long delay = 0L;
        mCountdownPlayed=false;

        for (AppCompatButton b : answerButtons) {
            ViewCompat.setBackgroundTintList(b,defButtonColor);
        }

        if (firstTime) {
            defTimerColor=mTimer.getBackground();
            defColor = op1Op1TV.getTextColors();
            defOp1Size = op1Op1TV.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            defOp2Size = op2Op1TV.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
            //getTextSize returns px -> This converts to sp
            op1 = calculateOperation();
            op2 = calculateOperation();
        } else {
            op1 = op2;
            op2 = calculateOperation();
        }

        if (mCorrectAnswers %10==0 && !isFirstAnswer && mAnswerWasCorrect) {
            level++;
            delay=2000L;

            if (level == 2 || level == 3) {
                mEnabledButtons+=2;
            }

            mLevelText.setText(getString(R.string.level) + " " + level);

            mLevelUp.setVisibility(View.VISIBLE);
            mLevelUp.setText(getString(R.string.level) + " " + level);
            mLevelUpPlayer.start();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLevelUp.setVisibility(View.GONE);
                mTimer.setBackgroundDrawable(defTimerColor);

                if (mLifeline5050Used) {
                    mLifeline5050.setEnabled(false);
                } else {
                    mLifeline5050.setEnabled(true);
                }

                if (mLifelinePassoverUsed) {
                    mLifelinePassover.setEnabled(false);
                } else {
                    mLifelinePassover.setEnabled(true);
                }

                //Poner símbolo de operación

                op1OpType.setText(op1.getOpTypeStr());
                op2OpType.setText(op2.getOpTypeStr());

                //Poner campos según toque
                switch (op1.getHiddenField()) {
                    case 0:
                        op1Op1TV.setText("?");
                        op1Op1TV.setTextSize(60);
                        op1Op1TV.setTextColor(Color.RED);
                        op1Op1TV.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_question));


                        op1Op2TV.setText(op1.getOp2()+"");
                        op1Op2TV.setTextColor(defColor);
                        op1Op2TV.setTextSize(defOp1Size);
                        op1Op2TV.setBackground(defQuestionBackground);

                        op1ResTV.setText(op1.getRes()+"");
                        op1ResTV.setTextColor(defColor);
                        op1ResTV.setTextSize(defOp1Size);
                        op1ResTV.setBackground(defQuestionBackground);
                        break;
                    case 1:
                        op1Op1TV.setText(op1.getOp1()+"");
                        op1Op1TV.setTextColor(defColor);
                        op1Op1TV.setTextSize(defOp1Size);
                        op1Op1TV.setBackground(defQuestionBackground);


                        op1Op2TV.setText("?");
                        op1Op2TV.setTextSize(60);
                        op1Op2TV.setTextColor(Color.RED);
                        op1Op2TV.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_question));

                        op1ResTV.setText(op1.getRes()+"");
                        op1ResTV.setTextColor(defColor);
                        op1ResTV.setTextSize(defOp1Size);
                        op1ResTV.setBackground(defQuestionBackground);

                        break;
                    case 2:
                        op1Op1TV.setText(op1.getOp1()+"");
                        op1Op1TV.setTextColor(defColor);
                        op1Op1TV.setTextSize(defOp1Size);
                        op1Op1TV.setBackground(defQuestionBackground);


                        op1Op2TV.setText(op1.getOp2()+"");
                        op1Op2TV.setTextColor(defColor);
                        op1Op2TV.setTextSize(defOp1Size);
                        op1Op2TV.setBackground(defQuestionBackground);


                        op1ResTV.setText("?");
                        op1ResTV.setTextSize(60);
                        op1ResTV.setTextColor(Color.RED);
                        op1ResTV.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.square_back_question));
                        break;
                }

                switch (op2.getHiddenField()) {
                    case 0:
                        op2Op1TV.setText("?");
                        op2Op1TV.setTextSize(40);
                        op2Op1TV.setTextColor(Color.RED);

                        op2Op2TV.setText(op2.getOp2()+"");
                        op2Op2TV.setTextColor(defColor);
                        op2Op2TV.setTextSize(defOp2Size);

                        op2ResTV.setText(op2.getRes()+"");
                        op2ResTV.setTextColor(defColor);
                        op2ResTV.setTextSize(defOp2Size);
                        break;
                    case 1:
                        op2Op1TV.setText(op2.getOp1()+"");
                        op2Op1TV.setTextColor(defColor);
                        op2Op1TV.setTextSize(defOp2Size);

                        op2Op2TV.setText("?");
                        op2Op2TV.setTextSize(40);
                        op2Op2TV.setTextColor(Color.RED);

                        op2ResTV.setText(op2.getRes()+"");
                        op2ResTV.setTextColor(defColor);
                        op2ResTV.setTextSize(defOp2Size);

                        break;
                    case 2:
                        op2Op1TV.setText(op2.getOp1()+"");
                        op2Op1TV.setTextColor(defColor);
                        op2Op1TV.setTextSize(defOp2Size);


                        op2Op2TV.setText(op2.getOp2()+"");
                        op2Op2TV.setTextColor(defColor);
                        op2Op2TV.setTextSize(defOp2Size);


                        op2ResTV.setText("?");
                        op2ResTV.setTextSize(40);
                        op2ResTV.setTextColor(Color.RED);
                        break;
                }

                //Rellenar teclado

                //Clear buttons
                for (AppCompatButton b : answerButtons) {
                    b.setText("");
                }

                int maxButtons;

                switch (level) {
                    case 1:
                        maxButtons=4;
                        answerButtons.get(0).setVisibility(View.VISIBLE);
                        answerButtons.get(1).setVisibility(View.VISIBLE);
                        answerButtons.get(2).setVisibility(View.VISIBLE);
                        answerButtons.get(3).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        answerButtons.get(4).setVisibility(View.VISIBLE);
                        answerButtons.get(5).setVisibility(View.VISIBLE);
                        maxButtons=6;
                        break;
                    case 3 :
                        answerButtons.get(6).setVisibility(View.VISIBLE);
                        answerButtons.get(7).setVisibility(View.VISIBLE);
                        maxButtons=8;
                        break;
                    default:
                        maxButtons=8;
                }

                mCorrectButtonIndex = (int) (Math.random() * maxButtons);

                switch (op1.getHiddenField()) {
                    case 0:
                        answerButtons.get(mCorrectButtonIndex).setText(op1.getOp1() + "");
                        break;
                    case 1:
                        answerButtons.get(mCorrectButtonIndex).setText(op1.getOp2() + "");
                        break;
                    case 2:
                        answerButtons.get(mCorrectButtonIndex).setText(op1.getRes() + "");
                        break;
                }

                //TODO: REMOVE ON RELEASE. FOR DEBUG ONLY
                //ViewCompat.setBackgroundTintList(answerButtons.get(mCorrectButtonIndex),ColorStateList.valueOf(Color.GREEN));

                for (int i = 0; i < answerButtons.size(); i++) {
                    if (answerButtons.get(i).getText()=="") {
                        int answerRange=0;
                        int answer = 0;

                        //TODO: Siguen saliendo repetidos
                        switch (op1.getHiddenField()) {
                            case 0:
                                answer=op1.getOp1();
                                break;
                            case 1:
                                answer=op1.getOp2();
                                break;
                            case 2:
                                answer=op1.getRes();
                                break;
                        }

                        int min = 0;
                        int max = 0;

                        if (answer <= 10) {
                            min=1;
                            max=answer+10+(10-answer);
                        } else {
                            min=answer-10;
                            max=answer+10;
                        }
                        answerRange = (max - min) + 1;

                        String option;
                        boolean found;
                        do {
                            option = ((int)(Math.random() * answerRange) + min) + "";

                            found = false;

                            for (int j = 0; j < i; j++) {
                                if (answerButtons.get(j).getText().toString().equals(option)) {
                                    found=true;
                                    break;
                                }
                            }
                        } while (found || option.equals(answerButtons.get(mCorrectButtonIndex).getText().toString()));

                        answerButtons.get(i).setText(option);
                    }
                }

                for (AppCompatButton b : answerButtons) {
                    b.setOnClickListener(MathsActivity.this);
                }

                if (mMillisLeft>10000) {
                    if (mBonusTime + mMillisLeft > 59000) {
                        mInitialMillis = 59000;
                    } else {
                        mInitialMillis = mBonusTime + mMillisLeft;
                    }
                } else {
                    if (mAnswerWasCorrect) {
                        mInitialMillis=10000+mMillisLeft;
                    } else {
                        mInitialMillis=10000;
                    }
                }

                mHasBonus=true;

                mCountdownTimer = new CountDownTimer(mInitialMillis, 500) {

                    public void onTick(long millisUntilFinished) {
                        mMillisLeft = millisUntilFinished;
                        mTimer.setText("00:" + String.format("%02d",(millisUntilFinished/1000)+1));

                        if (millisUntilFinished <= mInitialMillis-10000) {
                            mHasBonus=false;
                        }

                        if (millisUntilFinished / 1000 == 4 && mTimer.getCurrentTextColor() != Color.RED) {
                            mTimer.setBackgroundColor(Color.RED);

                            //Sonido countdown
                            if (!mCountdownPlayed && !mPaused) {
                                mCountdownPlayer.start();
                                mCountdownPlayed = true;
                            }
                        }

                        if (millisUntilFinished < mInitialMillis - 5000 && !mLifelineHintShown) {
                            mLifelineHintShown=true;
                            mLifelineHint.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mLifelineHint.setVisibility(View.GONE);
                                }
                            },5000);
                        }
                    }

                    public void onFinish() {
                        mLifeline5050.setEnabled(false);
                        mLifelinePassover.setEnabled(false);
                        for (AppCompatButton b : answerButtons) {
                            b.setEnabled(false);
                        }
                        mTimer.setText("00:00");

                        if (!mPaused) {
                            Toast.makeText(MathsActivity.this, "TIME UP!", Toast.LENGTH_SHORT).show();


                            incorrectAnswerOrTimeOut();
                        } else {
                            mTimeoutOnPause = true;
                        }

                    }
                }.start();
            }
        },delay);




    }

    public Operation calculateOperation() {
        Operation op1 = new Operation();;
        int op1Range=1, op2Range=1, newOp2=1;

        do {
            //Seleccionar operación
            op1.setOpType(Operation.OpType.values()[(int) (Math.random() * 4)]);

            //Calcular operandos
            switch (level) {
                case 1:
                    if (op1.getOpTypeStr().equals("÷")) {
                        op1Range = (10 - 1) + 1;
                    } else {
                        op1Range = (5 - 1) + 1;
                    }


                    if (op1.getOpTypeStr().equals("÷")) {
                        op2Range = (10 - 1) + 1;
                    } else {
                        op2Range = (5 - 1) + 1;
                    }

                    break;
                case 2:
                    op1Range = (10 - 1) + 1;

                    op2Range = (10 - 1) + 1;

                    break;
                case 3:
                    op1Range = (20 - 1) + 1;

                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op2Range = (10 - 1) + 1;
                    } else {
                        op2Range = (20 - 1) + 1;
                    }

                    break;
                case 4:
                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op1Range = (30 - 1) + 1;
                    } else {
                        op1Range = (40 - 1) + 1;
                    }


                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op2Range = (10 - 1) + 1;
                    } else {
                        op2Range = (40 - 1) + 1;
                    }

                    break;
                case 5:
                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op1Range = (40 - 1) + 1;
                    } else {
                        op1Range = (50 - 1) + 1;
                    }


                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op2Range = (20 - 1) + 1;
                    } else {
                        op2Range = (50 - 1) + 1;
                    }

                    break;
                default:
                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op1Range = (scalableLevelsStartValueOp1Bis - 1) + 1;
                        scalableLevelsStartValueOp1Bis+=10;
                    } else {
                        op1Range = (scalableLevelsStartValueOp1 - 1) + 1;
                        scalableLevelsStartValueOp1+=10;
                    }


                    if (op1.getOpTypeStr().equals("x") || op1.getOpTypeStr().equals("÷")) {
                        op2Range = (scalableLevelsStartValueOp2Bis - 1) + 1;
                        scalableLevelsStartValueOp2Bis+=5;
                    } else {
                        op2Range = (scalableLevelsStartValueOp2+10 - 1) + 1;
                        scalableLevelsStartValueOp2+=10;
                    }

                    break;
            }

            op1.setOp1((int)(Math.random() * op1Range) + 1);

            newOp2 = (int)(Math.random() * op2Range) + 1;
            while (op1.getOpTypeStr().equals("÷") && op1.getOp1()%newOp2 !=0) {
                newOp2 = (int)(Math.random() * op2Range) + 1;
            }
            op1.setOp2(newOp2);

        } while ((op1.getOpTypeStr().equals("÷") && (op1.getOp1()/op1.getOp2()) <= 0) || (op1.getOpTypeStr().equals("-") && op1.getOp1()-op1.getOp2() < 0));

        //Guardar resultado
        op1.calculate();

        //Calcular campo escondido
        op1.setHiddenField((int)(Math.random() * ((2 - 0) + 1)) + 0);

        return op1;
    }

    private void incorrectAnswerOrTimeOut() {
        firstTime=false;
        mAnswerWasCorrect=false;

        if (mScore-50>0) {
            mScore-=50;
            mAddedScoreText.setText("-50");
            mAddedScoreText.setVisibility(View.VISIBLE);
        } else {
            mAddedScoreText.setText("-" + mScore);
            mAddedScoreText.setVisibility(View.VISIBLE);
            mScore=0;
        }

        mScoreText.setText(mScore+"");

        ViewCompat.setBackgroundTintList(answerButtons.get(mCorrectButtonIndex),ColorStateList.valueOf(Color.GREEN));

        mIncorrectPlayer.start();

        mLives--;
        //Quitar vida
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mLives) {
                    case 2:
                        findViewById(R.id.life3).setVisibility(View.GONE);
                        break;
                    case 1:
                        findViewById(R.id.life2).setVisibility(View.GONE);

                        mLastLife.setVisibility(View.VISIBLE);
                        mMusicPlayer.stop();
                        mLastLifePlayer.start();

                        mLastLifePlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                mFastMusicPlayer.start();
                            }
                        });

                        break;
                    case 0:
                        findViewById(R.id.life1).setVisibility(View.GONE);
                        break;
                }
            }
        }, 500L);

        long delay;

        if (mLives == 1) {
            delay=2500L;
        } else {
            delay=1500L;
        }

        if (mLives > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLastLife.setVisibility(View.GONE);
                    mAddedScoreText.setVisibility(View.GONE);
                    for (AppCompatButton b : answerButtons) {
                        b.setEnabled(true);
                    }
                    loadOperation();
                }
            }, delay);
        } else {
            gameOver();
        }
    }

    private void correctAnswer() {
        isFirstAnswer=false;
        mCorrectAnswers++;
        mAnswerWasCorrect=true;

        mScore+=100;
        mScore+= (mMillisLeft/1000)*10+10;
        mScoreText.setText(mScore+"");

        mAddedScoreText.setText("+100\n" + " +" + ((mMillisLeft/1000)*10+10));
        mAddedScoreText.setVisibility(View.VISIBLE);

        if (mHasBonus) {
            mBonusTime=10000-(mInitialMillis-mMillisLeft);
            mAddedTimeText.setText("+00:" + String.format("%02d",((int) mBonusTime/1000 +1)));
            mAddedTimeText.setVisibility(View.VISIBLE);
        } else {
            mBonusTime=0;
        }

        answerButtons.get(mCorrectButtonIndex).setOnClickListener(null);
        firstTime=false;

        //Triquiñuelas para API <21
        ViewCompat.setBackgroundTintList(answerButtons.get(mCorrectButtonIndex),ColorStateList.valueOf(Color.GREEN));

        mCorrectPlayer.start();

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(MathsActivity.this,R.animator.op2_movement);
        LinearLayout op2Layout = findViewById(R.id.op2_layout);
        set.setTarget(op2Layout);
        set.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAddedScoreText.setVisibility(View.GONE);
                mAddedTimeText.setVisibility(View.GONE);
                for (AppCompatButton b : answerButtons) {
                    b.setEnabled(true);
                }
                //TODO: CHANGE ON RELEASE. FOR DEBUG ONLY
                loadOperation();
            }
        }, 1500L);
    }

    private void gameOver() {
        mLifelineHint.setVisibility(View.GONE);
        mLifeline5050.setEnabled(false);
        ViewCompat.setBackgroundTintList(mLifeline5050,ColorStateList.valueOf(Color.GRAY));
        mLifelinePassover.setEnabled(false);
        ViewCompat.setBackgroundTintList(mLifelinePassover,ColorStateList.valueOf(Color.GRAY));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Necesario ante intent??
//                mAddedTimeText.setVisibility(View.GONE);
//                mAddedScoreText.setVisibility(View.GONE);
                Intent resultIntent = new Intent(MathsActivity.this, ResultActivity.class);
                resultIntent.putExtra("score",mScore);
                startActivity(resultIntent);
                MathsActivity.this.finish();
            }
        }, 1500L);
    }
}
