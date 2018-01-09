package net.xeill.elpuig.thinkitapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class MathsActivity extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer musicPlayer;
    int correctAnswers=0;
    List<AppCompatButton> answerButtons;
    int correctButtonIndex;
    boolean firstTime = true;
    VideoView bgVideo;
    ColorStateList defColor = ColorStateList.valueOf(Color.GRAY);
    float defOp1Size = 40;
    float defOp2Size = 24;
    Operation op1=null;
    Operation op2=null;
    int mLives=3;
    TextView mTimer;
    CountDownTimer mCountdownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maths);

        //TODO: Añadir créditos bensound.com en help/about
        musicPlayer=MediaPlayer.create(this,R.raw.bensound_jazzyfrenchy);
        musicPlayer.setLooping(true);
        musicPlayer.setVolume(0.8f,0.8f);
        musicPlayer.start();

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
                        .setPositiveButton(R.string.exit_yes, new DialogInterface.OnClickListener() {
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

        mTimer=findViewById(R.id.timer);

        loadOperation();
    }

    private void loadOperation() {
        TextView op1Op1TV = findViewById(R.id.oper1_op1);
        TextView op1Op2TV = findViewById(R.id.oper1_op2);
        TextView op1ResTV = findViewById(R.id.oper1_res);

        TextView op2Op1TV = findViewById(R.id.oper2_op1);
        TextView op2Op2TV = findViewById(R.id.oper2_op2);
        TextView op2ResTV = findViewById(R.id.oper2_res);

        if (firstTime) {
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

        mTimer.setTextColor(Color.WHITE);

        //Poner símbolo de operación
        TextView op1OpType = findViewById(R.id.oper1_opType);
        op1OpType.setText(op1.getOpTypeStr());

        TextView op2OpType = findViewById(R.id.oper2_opType);
        op2OpType.setText(op2.getOpTypeStr());

        //Poner campos según toque
        switch (op1.getHiddenField()) {
            case 0:
                op1Op1TV.setText("?");
                op1Op1TV.setTextSize(60);
                op1Op1TV.setTextColor(Color.RED);

                op1Op2TV.setText(op1.getOp2()+"");
                op1Op2TV.setTextColor(defColor);
                op1Op2TV.setTextSize(defOp1Size);

                op1ResTV.setText(op1.getRes()+"");
                op1ResTV.setTextColor(defColor);
                op1ResTV.setTextSize(defOp1Size);
                break;
            case 1:
                op1Op1TV.setText(op1.getOp1()+"");
                op1Op1TV.setTextColor(defColor);
                op1Op1TV.setTextSize(defOp1Size);


                op1Op2TV.setText("?");
                op1Op2TV.setTextSize(60);
                op1Op2TV.setTextColor(Color.RED);

                op1ResTV.setText(op1.getRes()+"");
                op1ResTV.setTextColor(defColor);
                op1ResTV.setTextSize(defOp1Size);

                break;
            case 2:
                op1Op1TV.setText(op1.getOp1()+"");
                op1Op1TV.setTextColor(defColor);
                op1Op1TV.setTextSize(defOp1Size);


                op1Op2TV.setText(op1.getOp2()+"");
                op1Op2TV.setTextColor(defColor);
                op1Op2TV.setTextSize(defOp1Size);


                op1ResTV.setText("?");
                op1ResTV.setTextSize(60);
                op1ResTV.setTextColor(Color.RED);
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
        answerButtons = new ArrayList<>();
        answerButtons.add((AppCompatButton) findViewById(R.id.answer1));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer2));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer3));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer4));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer5));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer6));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer7));
        answerButtons.add((AppCompatButton) findViewById(R.id.answer8));

        //Clear buttons
        for (AppCompatButton b : answerButtons) {
            b.setText("");
        }

        correctButtonIndex = (int) (Math.random() * 8);

        switch (op1.getHiddenField()) {
            case 0:
                answerButtons.get(correctButtonIndex).setText(op1.getOp1() + "");
                break;
            case 1:
                answerButtons.get(correctButtonIndex).setText(op1.getOp2() + "");
                break;
            case 2:
                answerButtons.get(correctButtonIndex).setText(op1.getRes() + "");
                break;
        }

        //Margen +-10
        for (int i = 0; i < answerButtons.size(); i++) {
            if (answerButtons.get(i).getText()=="") {
                int answerRange=0;

                //TODO: Siguen saliendo repetidos
                switch (op1.getHiddenField()) {
                    case 0:
                        answerRange = ((op1.getOp1()+10) - (op1.getOp1()-10)) + 1;
                        answerButtons.get(i).setText(((int)(Math.random() * answerRange) + op1.getOp1()-10) + "");
                        while (answerButtons.get(i).getText().equals(answerButtons.get(correctButtonIndex).getText())) {
                            answerButtons.get(i).setText(((int)(Math.random() * answerRange) + op1.getOp1()-10) + "");
                        }
                        break;
                    case 1:
                        answerRange = ((op1.getOp2()+10) - (op1.getOp2()-10)) + 1;
                        answerButtons.get(i).setText(((int)(Math.random() * answerRange) + op1.getOp2()-10) + "");
                        while (answerButtons.get(i).getText().equals(answerButtons.get(correctButtonIndex).getText())) {
                            answerButtons.get(i).setText(((int)(Math.random() * answerRange) + op1.getOp2()-10) + "");
                        }
                        break;
                    case 2:
                        answerRange = ((op1.getRes()+10) - (op1.getRes()-10)) + 1;
                        answerButtons.get(i).setText(((int)(Math.random() * answerRange) + op1.getRes()-10) + "");
                        while (answerButtons.get(i).getText().equals(answerButtons.get(correctButtonIndex).getText())) {
                            answerButtons.get(i).setText(((int)(Math.random() * answerRange) + op1.getRes()-10) + "");
                        }
                        break;
                }
                //TODO: Se repiten a veces con las divisiones
            }
        }

        for (AppCompatButton b : answerButtons) {
            b.setOnClickListener(this);
        }

        mCountdownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimer.setText("00:" + String.format("%02d",millisUntilFinished/1000) + "");
                if (millisUntilFinished / 1000 == 5 && mTimer.getCurrentTextColor() != Color.RED) {
                    mTimer.setTextColor(Color.RED);
                }
            }

            public void onFinish() {
                firstTime=false;
                Toast.makeText(MathsActivity.this, "TIME UP!", Toast.LENGTH_SHORT).show();
                for (AppCompatButton b : answerButtons) {
                    b.setEnabled(false);
                }
                mLives--;
                if (mLives>=0) {
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
                                    break;
                                case 0:
                                    findViewById(R.id.life1).setVisibility(View.GONE);
                                    findViewById(R.id.no_lives).setVisibility(View.VISIBLE);
                            }
                        }
                    }, 500L);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (AppCompatButton b : answerButtons) {
                                b.setEnabled(true);
                            }
                            loadOperation();
                        }
                    }, 1500L);
                } else {
                    Toast.makeText(MathsActivity.this, "GAME OVER", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    public Operation calculateOperation() {
        Operation op1;

        do {
            //Operación vacía
            op1 = new Operation();

            //Seleccionar operación
            op1.setOpType(Operation.OpType.values()[(int) (Math.random() * 4)]);

            //Calcular operando 1
            int range = (100 - 1) + 1;
            op1.setOp1((int)(Math.random() * range) + 1);

            //Calcular operando 2
            if(op1.getOpTypeStr().equals("÷") || op1.getOpTypeStr().equals("x")) {
                range = (10 - 1) + 1;
            }

            int newOp2 = (int)(Math.random() * range) + 1;
            while (op1.getOpTypeStr().equals("÷") && op1.getOp1()%newOp2 !=0) {
                newOp2 = (int)(Math.random() * range) + 1;
            }
            op1.setOp2(newOp2);
        } while ((op1.getOpTypeStr().equals("÷") && op1.getOp1()/op1.getOp2() < 0) ||(op1.getOpTypeStr().equals("-") && op1.getOp1()-op1.getOp2() < 0));

        //Guardar resultado
        op1.calculate();

        //Calcular campo escondido
        op1.setHiddenField((int)(Math.random() * ((2 - 0) + 1)) + 0);

        return op1;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(musicPlayer!=null && musicPlayer.isPlaying()){
            musicPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(musicPlayer!=null && !musicPlayer.isPlaying()){
            musicPlayer.start();
        }
    }

    @Override
    public void onClick(final View view) {
        for (AppCompatButton b : answerButtons) {
            b.setEnabled(false);
        }
        if (answerButtons.indexOf(view) == correctButtonIndex) {
            mCountdownTimer.cancel();
            correctAnswers++;
            view.setOnClickListener(null);
            firstTime=false;

            //Triquiñuelas para API <21
            final ColorStateList defButtonColor = ViewCompat.getBackgroundTintList(view);
            ViewCompat.setBackgroundTintList(view,ColorStateList.valueOf(Color.GREEN));

            MediaPlayer.create(MathsActivity.this,R.raw.correct).start();

            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(MathsActivity.this,R.animator.op2_movement);
            LinearLayout op2Layout = findViewById(R.id.op2_layout);
            set.setTarget(op2Layout);
            set.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (AppCompatButton b : answerButtons) {
                        b.setEnabled(true);
                    }
                    ViewCompat.setBackgroundTintList(view,defButtonColor);
                    loadOperation();
                }
            }, 1500L);
        } else {
            mCountdownTimer.cancel();
            view.setOnClickListener(null);
            firstTime=false;

            final ColorStateList defButtonColor = ViewCompat.getBackgroundTintList(view);
            ViewCompat.setBackgroundTintList(view,ColorStateList.valueOf(Color.RED));

            MediaPlayer.create(MathsActivity.this,R.raw.incorrect).start();

            mLives--;
            if (mLives>=0) {
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
                                break;
                            case 0:
                                findViewById(R.id.life1).setVisibility(View.GONE);
                                findViewById(R.id.no_lives).setVisibility(View.VISIBLE);
                        }
                    }
                }, 500L);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (AppCompatButton b : answerButtons) {
                            b.setEnabled(true);
                        }
                        ViewCompat.setBackgroundTintList(view,defButtonColor);
                        loadOperation();
                    }
                }, 1500L);
            } else {
                Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
