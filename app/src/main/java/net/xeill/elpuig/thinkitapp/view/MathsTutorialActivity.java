package net.xeill.elpuig.thinkitapp.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnCompleteListener;

public class MathsTutorialActivity extends AppCompatActivity {
    //    ShowcaseView sv;
    //    int clickCount=0;
    VideoView bgVideo;

    LinearLayout keyboard;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths);

        bgVideo = findViewById(R.id.bg_video);
        bgVideo.setVideoURI(Uri.parse("android.resource://net.xeill.elpuig.thinkitapp/" + R.raw.bg_maths));
        bgVideo.start();

        bgVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

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

        keyboard = findViewById(R.id.keyboard_line1);

        mScoreText = findViewById(R.id.score);
        mLevelText = findViewById(R.id.level);


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

        mScoreText.setText("0");
        mLevelText.setText(getString(R.string.level) + " 1");


//        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//        lps.setMargins(margin, margin, margin, margin);
//
//        sv = new ShowcaseView.Builder(this)
//                .withMaterialShowcase()
//                .build();
//        sv.setButtonPosition(lps);
//
//        sv.overrideButtonClick(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                clickCount++;
//                switch (clickCount) {
//                    case 1:
//                        sv.setTarget(new ViewTarget(R.id.op1_layout, MathsTutorialActivity.this));
//                        sv.setContentTitle("Operación principal");
//                        sv.setContentText("Esta es la operación que debes completar");
//                        sv.setShowcaseX(300);
//                        sv.setButtonText("next");
//                        break;
//                    case 2:
//                        sv.setTarget(new ViewTarget(R.id.op2_layout, MathsTutorialActivity.this));
//                        sv.setContentTitle("Siguiente operación");
//                        sv.setContentText("¡Prepárate para la siguiente operación echándole un vistazo rápido!");
//                        sv.setButtonText("next");
//                        break;
//                    case 3:
//                        sv.setTarget(new ViewTarget(R.id.keyboard_line1, MathsTutorialActivity.this));
//                        sv.setContentTitle("Respuestas posibles");
//                        sv.setContentText("Observa las respuestas posibles, piensa bien y pulsa en la que creas correcta");
//                        sv.setButtonText("next");
//                        break;
//                }
//            }
//        });





        final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                .title("Esta es la operación principal.\nEl objetivo del juego es completar la operación seleccionando el número que falta.")
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(op1)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .build();

        final FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(this)
                .title("Esta es la operación secundaria")
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(op2)
                .build();

        final FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(this)
                .title("Third Queue Item")
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .focusOn(keyboard)
                .build();

        FancyShowCaseQueue mQueue = new FancyShowCaseQueue()
                .add(fancyShowCaseView1)
                .add(fancyShowCaseView2)
                .add(fancyShowCaseView3);

        mQueue.show();

        mQueue.setCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(MathsTutorialActivity.this, "ACABÓ", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bgVideo!=null && !bgVideo.isPlaying()){
            bgVideo.start();
        }
    }
}
