package net.xeill.elpuig.thinkitapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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
}