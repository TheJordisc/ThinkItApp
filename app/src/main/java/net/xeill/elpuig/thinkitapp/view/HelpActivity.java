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

        final FloatingActionButton homeFAB = findViewById(R.id.fab_stop);

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

        listDataHeader.add("Objetivo del juego");
        listDataHeader.add("Controles del juego");
        listDataHeader.add("Pantalla principal");
        listDataHeader.add("Ranking de puntuaciones");
        listDataHeader.add("Cambiar idioma");
        listDataHeader.add("Desactivar o activar el sonido");

        List<String> gameHelp = new ArrayList<String>();
        gameHelp.add("El objetivo del juego consiste en...");

        List<String> controllerHelp = new ArrayList<String>();
        controllerHelp.add("Para lograr el objetivo del juego debes...");

        List<String> menuHelp = new ArrayList<String>();
        menuHelp.add("Iconos del menú principal:");

        List<String> rankingHelp = new ArrayList<String>();
        rankingHelp.add("Si guardamos la puntuación al finalizar la partida...");

        List<String> languageHelp = new ArrayList<String>();
        languageHelp.add("Para selecionar un idioma pulsamos sobre la bandera...");

        List<String> soundHelp = new ArrayList<String>();
        soundHelp.add("Si queremos silenciar la aplicación...");

        listDataChild.put(listDataHeader.get(0), gameHelp);
        listDataChild.put(listDataHeader.get(1), controllerHelp);
        listDataChild.put(listDataHeader.get(2), menuHelp);
        listDataChild.put(listDataHeader.get(3), rankingHelp);
        listDataChild.put(listDataHeader.get(4), languageHelp);
        listDataChild.put(listDataHeader.get(5), soundHelp);
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