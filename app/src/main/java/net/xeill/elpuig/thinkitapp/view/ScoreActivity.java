package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.model.Score;
import net.xeill.elpuig.thinkitapp.view.adapter.ScoreRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    static List<Score> scoreList = new ArrayList<>();
    static ScoreRecyclerAdapter scoreRecyclerAdapter = new ScoreRecyclerAdapter(scoreList);
    MediaPlayer musicPlayer;
    SharedPreferences settings;
    TextView noScoreSaved;
    CardView table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        settings=getSharedPreferences("prefs", 0);

        musicPlayer = MediaPlayer.create(this, R.raw.bensound_summer);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();
        musicPlayer.setLooping(true); // Set looping



        RecyclerView recyclerView = findViewById(R.id.score_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(scoreRecyclerAdapter);
        table = findViewById(R.id.cardView);
        noScoreSaved = findViewById(R.id.noScoreSaved);

        if (scoreRecyclerAdapter.getItemCount() == 0){
            table.setVisibility(View.GONE);
            noScoreSaved.setVisibility(View.VISIBLE);
        }else{
            noScoreSaved.setVisibility(View.GONE);
            table.setVisibility(View.VISIBLE);
            Collections.sort(scoreList);
        }


        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);
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
