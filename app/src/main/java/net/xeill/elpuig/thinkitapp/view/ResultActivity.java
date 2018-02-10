package net.xeill.elpuig.thinkitapp.view;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences settings;
    int mScore;
    TextView mScoreTextView,countTextView;
    EditText nameEdit;
    FloatingActionButton restartFAB, exitFAB,saveFAB;

    ScoreViewModel scoreViewModel;

    MediaPlayer mResultPlayer;
    private int nameMaxLenght = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mResultPlayer = MediaPlayer.create(ResultActivity.this,R.raw.bensound_jazzcomedy);
        mResultPlayer.setLooping(true);

        settings=getSharedPreferences("prefs", 0);
        if (settings.getBoolean("mute",true)) {
            mResultPlayer.setVolume(0f,0f);
        } else {
            mResultPlayer.setVolume(1f,1f);
        }

        mResultPlayer.start();

        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);

        mScore = getIntent().getIntExtra("score", 0);

        mScoreTextView = findViewById(R.id.score);

        mScoreTextView.setText(mScore + "");

        nameEdit = findViewById(R.id.name_edit);
        countTextView = findViewById(R.id.counter);
        countTextView.setText(nameMaxLenght + "/" + nameMaxLenght + " " + getResources().getString(R.string.result_characters));
        exitFAB = findViewById(R.id.fab_exit);

        exitFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmExit();
            }
        });

        saveFAB = findViewById(R.id.fab_save);

        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEdit.getText().toString().trim().equals("")) {
                    nameEdit.setError(getResources().getText(R.string.result_name_required));
                } else{
                    Score score = new Score();
                    score.setUser("" + nameEdit.getText());
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

        restartFAB = findViewById(R.id.fab_restart);
        restartFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmExit();
            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
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
                countTextView.setText((nameMaxLenght - s.toString().length()) + "/" + nameMaxLenght + " " + getResources().getString(R.string.result_characters));
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

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    void confirmExit() {
        new AlertDialog.Builder(ResultActivity.this)
                //.setMessage(R.string.tutorial_msg)
                .setMessage(R.string.dialog_result_exit_withoutsave)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent exitIntent = new Intent(ResultActivity.this,MainActivity.class);
                        startActivity(exitIntent);
                        ResultActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //NOTHING
                    }
                }).create().show();
    }
}
