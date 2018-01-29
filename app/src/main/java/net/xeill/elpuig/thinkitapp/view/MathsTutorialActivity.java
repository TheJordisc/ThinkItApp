package net.xeill.elpuig.thinkitapp.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.xeill.elpuig.thinkitapp.R;

import java.util.ArrayList;
import java.util.List;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnCompleteListener;

public class MathsTutorialActivity extends AppCompatActivity {

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

    AppCompatTextView mTimer;

    List<AppCompatButton> buttons = new ArrayList<>();

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

        mTimer=findViewById(R.id.timer);


        buttons.add((AppCompatButton) findViewById(R.id.answer1));
        buttons.add((AppCompatButton) findViewById(R.id.answer2));
        buttons.add((AppCompatButton) findViewById(R.id.answer3));
        buttons.add((AppCompatButton) findViewById(R.id.answer4));
        buttons.add((AppCompatButton) findViewById(R.id.answer5));
        buttons.add((AppCompatButton) findViewById(R.id.answer6));
        buttons.add((AppCompatButton) findViewById(R.id.answer7));
        buttons.add((AppCompatButton) findViewById(R.id.answer8));


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

        mTimer.setText("00:10");

        buttons.get(0).setText("1");
        buttons.get(1).setText("8");
        buttons.get(2).setText("2");
        buttons.get(3).setText("10");
        buttons.get(4).setText("4");
        buttons.get(5).setText("6");
        buttons.get(6).setText("11");
        buttons.get(7).setText("3");


        final FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(this)
                .title("Esta es la operación principal.\nEl objetivo del juego es completar la operación con el número que falta.")
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .focusOn(op1)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .build();

        final FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(this)
                .title("Esta es la siguiente operación.\n Cuando contestes la actual, esta será el objetivo. ¡Échale un vistazo para ganar unos segundos!")
                .titleStyle(R.style.tutorial_title_style,Gravity.BOTTOM | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(op2)
                .build();

        final FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(this)
                .title("Aquí están los botones con las posibles respuestas. ¡Piensa bien antes de elegir una!")
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(keyboard)
                .build();

        final FancyShowCaseView fancyShowCaseView4 = new FancyShowCaseView.Builder(this)
                .title("No tardes mucho en pensar, porque hay un cronómetro en tu contra. ¡Vigila el tiempo!")
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mTimer)
                .build();

        final FancyShowCaseView fancyShowCaseView5 = new FancyShowCaseView.Builder(this)
                .title("Según tu respuesta, subirá o bajará la puntuación. Si aciertas, sumarás puntos. Si fallas, restarás. Y si aciertas rápido, obtendrás una bonificación")
                .titleStyle(R.style.tutorial_title_style,Gravity.CENTER | Gravity.CENTER)
                .backgroundColor(getResources().getColor(R.color.color_showcase))
                .focusOn(mScoreText)
                .build();

        FancyShowCaseQueue mQueue = new FancyShowCaseQueue()
                .add(fancyShowCaseView1)
                .add(fancyShowCaseView2)
                .add(fancyShowCaseView3)
                .add(fancyShowCaseView4)
                .add(fancyShowCaseView5);

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
