package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;
import net.xeill.elpuig.thinkitapp.view.adapter.ScoreRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    int i, j;
    static List<Score> scoreList = new ArrayList<>();
    static ScoreRecyclerAdapter scoreRecyclerAdapter = new ScoreRecyclerAdapter(scoreList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        RecyclerView recyclerView = findViewById(R.id.score_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView trophy = findViewById(R.id.trophy);

        recyclerView.setAdapter(scoreRecyclerAdapter);


        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);
        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(homeIntent);
                ScoreActivity.this.finish();
            }
        });

        Collections.sort(scoreList);
 
    }
}
