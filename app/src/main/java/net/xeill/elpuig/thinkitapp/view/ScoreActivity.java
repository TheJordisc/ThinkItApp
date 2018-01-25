package net.xeill.elpuig.thinkitapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;
import net.xeill.elpuig.thinkitapp.view.adapter.ScoreRecyclerAdapter;
import net.xeill.elpuig.thinkitapp.viewmodel.ScoreViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    List<Score> scoreList = new ArrayList<>();
    ScoreRecyclerAdapter scoreRecyclerAdapter;
    MediaPlayer musicPlayer;
    SharedPreferences settings;
    TextView noScoreSaved;
    ScoreViewModel scoreViewModel;
    CardView table;

    //@android:color/holo_orange_light
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        settings=getSharedPreferences("prefs", 0);
        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);

        Long scoreId = getIntent().getLongExtra("scoreId",0);
        scoreRecyclerAdapter = new ScoreRecyclerAdapter(scoreList,scoreId,getApplicationContext());

        table = findViewById(R.id.cardView);
        noScoreSaved = findViewById(R.id.noScoreSaved);

        musicPlayer = MediaPlayer.create(this, R.raw.bensound_summer);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();
        musicPlayer.setLooping(true); // Set looping

        scoreViewModel.getScores().observe(ScoreActivity.this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scores) {
                scoreList.addAll(scores);
                Collections.sort(scoreList);
                scoreRecyclerAdapter.notifyDataSetChanged();

                if (scoreList.isEmpty()){
                    table.setVisibility(View.GONE);
                    noScoreSaved.setVisibility(View.VISIBLE);
                }else{
                    noScoreSaved.setVisibility(View.GONE);
                    table.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.score_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(scoreRecyclerAdapter);


        final FloatingActionButton homeFAB = findViewById(R.id.fab_stop);
        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(homeIntent);
                ScoreActivity.this.finish();
            }
        });
    }

    private void setUnmute() {
        musicPlayer.setVolume(0.7f,0.7f);
    }

    private void setMute() {
        musicPlayer.setVolume(0f,0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(musicPlayer!=null && musicPlayer.isPlaying()){
            musicPlayer.pause();
        }

//        if(bgVideo!=null && bgVideo.isPlaying()){
//            bgVideo.pause();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(musicPlayer!=null && !musicPlayer.isPlaying()){
            musicPlayer.start();
        }

//        if(bgVideo!=null && !bgVideo.isPlaying()){
//            bgVideo.start();
//        }
    }
}
