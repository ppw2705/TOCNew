package com.geecon.toc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.geecon.toc.R;
import com.geecon.toc.adapters.ConferenceAdapter;

public class ConferenceListActivity extends AppCompatActivity {

    ListView list_con;
    private Toolbar toolbar;

    String[] titles = {
            "CONTAINER SUPPLY CHAIN CONFERENCE",
            "PORT TECHNOLOGY EXHIBITION",
            "TECH TOC CONFERENCE"
    };

    String[] content = {
            "A high-level conference attended by representatives of the whole container supply chain",
            "Meet port equipment manufacturers, second tier suppliers & port services providers",
            "A high-level conference attended by representatives of the whole container supply chain"
    };

    Integer[] imageId = {
            R.drawable.conference_one,
            R.drawable.conference_two,
            R.drawable.conference_three
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_list);

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ConferenceAdapter adapter = new
                ConferenceAdapter(ConferenceListActivity.this,titles,content, imageId);
        list_con = (ListView)findViewById(R.id.list_con);
        list_con.setAdapter(adapter);
        list_con.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
