package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;

public class ResultActivity extends AppCompatActivity {
    int mScore;
    TextView mScoreTextView;
    EditText name_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mScore = getIntent().getIntExtra("score", 0);

        mScoreTextView = findViewById(R.id.score);

        mScoreTextView.setText(mScore + "");

        name_edit = findViewById(R.id.name_edit);

        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);

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
                    ScoreActivity.scoreList.add(score);
                    //ordenar
                    ScoreActivity.scoreRecyclerAdapter.notifyDataSetChanged();
                    Intent scoreIntent = new Intent(ResultActivity.this,ScoreActivity.class);
                    startActivity(scoreIntent);
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
    }
}
