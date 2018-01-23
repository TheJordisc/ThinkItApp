package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import net.xeill.elpuig.thinkitapp.view.adapter.ExpandableListAdapter;
import net.xeill.elpuig.thinkitapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    MediaPlayer musicPlayer;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        settings=getSharedPreferences("prefs", 0);

        musicPlayer = MediaPlayer.create(this, R.raw.bensound_thelounge);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();
        musicPlayer.setLooping(true); // Set looping

        final FloatingActionButton homeFAB = findViewById(R.id.fab_home);

        homeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(HelpActivity.this,MainActivity.class);
                startActivity(homeIntent);
                HelpActivity.this.finish();
            }
        });

        final ImageButton logoButton = findViewById(R.id.logoButton);
        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/TheJordisc/ThinkItApp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        final ImageButton switchButton = findViewById(R.id.switchLogo);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://elpuig.xeill.net/el-centre/erasmus/switch-project");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Créditos");
        listDataHeader.add("Menú principal");

        List<String> creditos = new ArrayList<String>();
        creditos.add("By Jordi & alejandro");

        List<String> menuHelp = new ArrayList<String>();
        menuHelp.add("Ayuda del menú principal:");

        listDataChild.put(listDataHeader.get(0), creditos);
        listDataChild.put(listDataHeader.get(1), menuHelp);
    }

    private void setUnmute() {
        musicPlayer.setVolume(1f,1f);
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