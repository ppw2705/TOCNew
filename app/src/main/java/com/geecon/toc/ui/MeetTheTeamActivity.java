package com.geecon.toc.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.geecon.toc.R;
import com.geecon.toc.adapters.TeamAdapter;

public class MeetTheTeamActivity extends AppCompatActivity {

    ListView list_one, list_two, list_three, list_four;
    RelativeLayout rel_one, rel_two, rel_three, rel_four;
    private Toolbar toolbar;
    String[] names = {
            "PAUL HOLLOWAY",
            "SEAN DEANE",
            "LEONARD FIELD"
    } ;

    String[] Firstnames = {
            "Paul",
            "Sean",
            "Leonard"
    } ;

    String[] phones = {
            "Tel: +44(0)2070174394",
            "Tel: +44(0)2070174391",
            "Tel: +44(0)2070174661"
    } ;

    String[] titles = {
            "Event Director",
            "Event Manager",
            "Event Manager"
    } ;
    Integer[] imageId = {
            R.drawable.mtt_three,
            R.drawable.mmt_two,
            R.drawable.mmt_one
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_the_team);

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TeamAdapter adapter = new
                TeamAdapter(MeetTheTeamActivity.this, names,titles,phones,Firstnames, imageId);
        list_one = (ListView)findViewById(R.id.team_list_one);
        list_two = (ListView)findViewById(R.id.team_list_two);
        list_three = (ListView)findViewById(R.id.team_list_three);
        list_four = (ListView)findViewById(R.id.team_list_four);
        rel_one = (RelativeLayout)findViewById(R.id.title_one);
        rel_two = (RelativeLayout)findViewById(R.id.title_two);
        rel_three = (RelativeLayout)findViewById(R.id.title_three);
        rel_four = (RelativeLayout)findViewById(R.id.title_four);

        rel_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_one.setVisibility(View.VISIBLE);
                list_two.setVisibility(View.GONE);
                list_three.setVisibility(View.GONE);
                list_four.setVisibility(View.GONE);
                rel_one.setBackgroundResource(R.color.titlebar_color);
                rel_two.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_three.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_four.setBackgroundResource(R.color.mtt_titlebar_color);
            }
        });

        rel_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_one.setVisibility(View.GONE);
                list_two.setVisibility(View.VISIBLE);
                list_three.setVisibility(View.GONE);
                list_four.setVisibility(View.GONE);
                rel_one.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_two.setBackgroundResource(R.color.titlebar_color);
                rel_three.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_four.setBackgroundResource(R.color.mtt_titlebar_color);
            }
        });

        rel_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_one.setVisibility(View.GONE);
                list_two.setVisibility(View.GONE);
                list_three.setVisibility(View.VISIBLE);
                list_four.setVisibility(View.GONE);
                rel_one.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_two.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_three.setBackgroundResource(R.color.titlebar_color);
                rel_four.setBackgroundResource(R.color.mtt_titlebar_color);
            }
        });

        rel_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_one.setVisibility(View.GONE);
                list_two.setVisibility(View.GONE);
                list_three.setVisibility(View.GONE);
                list_four.setVisibility(View.VISIBLE);
                rel_one.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_two.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_three.setBackgroundResource(R.color.mtt_titlebar_color);
                rel_four.setBackgroundResource(R.color.titlebar_color);
            }
        });

        list_one.setAdapter(adapter);
        list_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        list_two.setAdapter(adapter);
        list_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        list_three.setAdapter(adapter);
        list_three.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        list_four.setAdapter(adapter);
        list_four.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
