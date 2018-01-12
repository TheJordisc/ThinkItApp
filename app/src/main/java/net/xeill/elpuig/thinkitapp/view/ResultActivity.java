package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.xeill.elpuig.thinkitapp.R;

public class ResultActivity extends AppCompatActivity {
    int mScore;
    TextView mScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //TODO: Que el editText no sea multiline y limitar caracteres?
        //TODO: Si está vacío, diálogo -> volver o no guardar
        mScore = getIntent().getIntExtra("score",0);

        mScoreTextView = findViewById(R.id.score);

        mScoreTextView.setText(mScore+"");

        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(homeIntent);
                ResultActivity.this.finish();
            }
        });
    }
}
