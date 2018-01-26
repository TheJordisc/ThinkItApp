package net.xeill.elpuig.thinkitapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;
import net.xeill.elpuig.thinkitapp.viewmodel.ScoreViewModel;

public class ResultActivity extends AppCompatActivity {
    int mScore;
    TextView mScoreTextView,countTextView;
    EditText name_edit;

    ScoreViewModel scoreViewModel;

    MediaPlayer mResultPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);

        mScore = getIntent().getIntExtra("score", 0);

        mResultPlayer = MediaPlayer.create(ResultActivity.this,R.raw.bensound_jazzcomedy);
        mResultPlayer.setLooping(true);
        mResultPlayer.start();

        mScoreTextView = findViewById(R.id.score);

        mScoreTextView.setText(mScore + "");

        name_edit = findViewById(R.id.name_edit);
        countTextView = findViewById(R.id.counter);

        final FloatingActionButton homeFAB = findViewById(R.id.fab_stop);

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(homeIntent);
                ResultActivity.this.finish();
            }
        });

        final FloatingActionButton saveScore = findViewById(R.id.save_score);

        saveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name_edit.getText().length() == 0) {
                    name_edit.setError("Se requiere un nombre para guardar la puntuación");
                } else{
                    Score score = new Score();
                    score.setUser("" + name_edit.getText());
                    score.setScore(mScore);

                    scoreViewModel.insertScore(score).observe(ResultActivity.this, new Observer<Long>() {
                        @Override
                        public void onChanged(@Nullable Long aLong) {
                            //scoreRecyclerAdapter.notifyDataSetChanged();

                            Intent scoreIntent = new Intent(ResultActivity.this,ScoreActivity.class);
                            scoreIntent.putExtra("scoreId",aLong);
                            startActivity(scoreIntent);

                        }
                    });
                }
            }
        });

        final FloatingActionButton restartButton = findViewById(R.id.restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(ResultActivity.this, MathsActivity.class);
                startActivity(playIntent);
                finish();
            }
        });

        name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft){

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                countTextView.setText((20 - s.toString().length()) + "/20 caracteres restantes");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mResultPlayer !=null && mResultPlayer.isPlaying()){
            mResultPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mResultPlayer !=null && !mResultPlayer.isPlaying()){
            mResultPlayer.start();
        }
    }
}
