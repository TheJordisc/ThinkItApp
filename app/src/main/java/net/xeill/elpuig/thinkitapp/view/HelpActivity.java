package net.xeill.elpuig.thinkitapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import net.xeill.elpuig.thinkitapp.R;
import net.xeill.elpuig.thinkitapp.view.adapter.ExpandableListAdapter;

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
    private int lastPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //TODO: Añadir atribución nuevo tema principal
        //TODO: Atribución video: "Floating Bokeh Particles Blue" - Videvo

        Button tutorialButton = findViewById(R.id.button_repeat_tutorial);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        settings=getSharedPreferences("prefs", 0);

        musicPlayer = MediaPlayer.create(this, R.raw.bensound_thelounge);

        if(settings.getBoolean("mute",true)) {
            setMute();
        } else {
            setUnmute();
        }

        musicPlayer.start();
        musicPlayer.setLooping(true); // Set looping

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(HelpActivity.this, MathsTutorialActivity.class);
                startActivity(playIntent);
                finish();
            }
        });


        /*final ImageButton logoButton = findViewById(R.id.logoButton);
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

*/
        expListView = findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

       expListView.expandGroup(0);
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastPosition != -1
                        && groupPosition != lastPosition) {
                    expListView.collapseGroup(lastPosition);
                }
                lastPosition = groupPosition;
                expListView.smoothScrollToPosition(groupPosition);
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();


        listDataHeader.add(getString(R.string.help_credits));
        listDataHeader.add(getString(R.string.help_menu));
        listDataHeader.add(getString(R.string.help_goals));
        listDataHeader.add(getString(R.string.help_bonus));
        listDataHeader.add(getString(R.string.help_ranking));
        listDataHeader.add(getString(R.string.help_language));
        listDataHeader.add(getString(R.string.attribution));

        List<String> creditsHelp = new ArrayList<>();
        creditsHelp.add("");

        List<String> menuHelp = new ArrayList<>();
        menuHelp.add("");

        List<String> goalsHelp = new ArrayList<>();
        goalsHelp.add("");

        List<String> bonusHelp = new ArrayList<>();
        bonusHelp.add("");

        List<String> rankingHelp = new ArrayList<>();
        rankingHelp.add("");

        List<String> languageHelp = new ArrayList<>();
        languageHelp.add("");

        List<String> attribution = new ArrayList<>();
        attribution.add("");

        listDataChild.put(listDataHeader.get(0), creditsHelp);
        listDataChild.put(listDataHeader.get(1), menuHelp);
        listDataChild.put(listDataHeader.get(2), goalsHelp);
        listDataChild.put(listDataHeader.get(3), bonusHelp);
        listDataChild.put(listDataHeader.get(4), rankingHelp);
        listDataChild.put(listDataHeader.get(5), languageHelp);
        listDataChild.put(listDataHeader.get(6), attribution);

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

    @Override
    public void onBackPressed() {
        finish();
    }
}