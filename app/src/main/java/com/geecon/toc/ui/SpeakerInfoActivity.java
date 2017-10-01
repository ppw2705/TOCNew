package com.geecon.toc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.geecon.toc.R;
import com.geecon.toc.models.AllDataModel;
import com.geecon.toc.models.SpeakerInfoModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

public class SpeakerInfoActivity extends AppCompatActivity {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading;
    private ImageView img_back,img_speaker_info;
    private TextView txt_speaker_name, txt_asia_date,speaking_at;
    private LinearLayout ll_main;
    private DocumentView txt_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_info);


        mActivityContext = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);
        img_speaker_info = (ImageView) findViewById(R.id.img_speaker_info);
        txt_speaker_name = (TextView) findViewById(R.id.txt_speaker_name);
        txt_asia_date = (TextView) findViewById(R.id.txt_asia_date);
        txt_description = (DocumentView) findViewById(R.id.txt_description);
        ll_main = (LinearLayout) findViewById(R.id.ll_links);
        speaking_at = (TextView) findViewById(R.id.speaking_at);

        pageHeading.setText("SPEAKER INFO");


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_ico, mActivityContext.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                    drawer.bringToFront();
                    //drawer.requestLayout();
                }
            }


        });

        String result_list = getIntent().getStringExtra("SPEAKER_DATA");

        //Toast.makeText(mAppContext, result_list, Toast.LENGTH_SHORT).show();
        try {
            SpeakerInfoModel speakerModel = new Gson().fromJson(result_list,
                    new TypeToken<SpeakerInfoModel>() {
                    }.getType());
            if(speakerModel.getSpeaker_info().getIMAGE()!= null && speakerModel.getSpeaker_info().getIMAGE()!= ""){
                Picasso.with(mAppContext).load(speakerModel.getSpeaker_info().getIMAGE()).placeholder(R.drawable.pro).resize(200,200).into(img_speaker_info);
            }
            txt_speaker_name.setText(speakerModel.getSpeaker_info().getSPEAKER_NAME());
            txt_asia_date.setText(speakerModel.getSpeaker_info().getCOMPANY_NAME());
            txt_description.setText(Html.fromHtml(speakerModel.getSpeaker_info().getDESCRIPTION()));
            if(speakerModel.getSession_info().size()!=0) {
                for (AllDataModel single_session :
                        speakerModel.getSession_info()) {
                    View SessionView = LayoutInflater.from(mActivityContext).inflate(R.layout.single_session, null, false);
                    TextView txt_name = (TextView) SessionView.findViewById(R.id.txt_s_title);
                    txt_name.setText(single_session.getHEADING());
                    TextView txt_date = (TextView) SessionView.findViewById(R.id.txt_s_date);
                    txt_date.setText(single_session.getSTART_DATE_TIME());
                    TextView txt_desc_one = (TextView) SessionView.findViewById(R.id.txt_s_desc_one);
                    TextView txt_desc_two = (TextView) SessionView.findViewById(R.id.txt_s_desc_two);
                    if (single_session.getCONF_ID().equals("6")) {
                        txt_desc_one.setText("Container Supply Chain Conference");
                        txt_desc_two.setText("CSC");
                    }
                    if (single_session.getCONF_ID().equals("7")) {
                        txt_desc_one.setText("TECH TOC Conference");
                        txt_desc_two.setText("TECH TOC");
                    }
                    if (single_session.getCONF_ID().equals("8")) {
                        txt_desc_one.setText("BULK Seminar");
                        txt_desc_two.setText("BULK");
                    }
                    ll_main.addView(SessionView);
                }
            } else {
                speaking_at.setVisibility(View.GONE);
            }
            //Toast.makeText(mAppContext, speakerModel.getSession_info().get(0).getHEADING(), Toast.LENGTH_SHORT).show();
        } catch(Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });
    }

    public void navClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_rel_home:
                intent = new Intent(mActivityContext,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_rel_asia:
                intent = new Intent(mActivityContext,Web_View_asia.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_rel_africa:
                intent = new Intent(mActivityContext,Web_View_africa.class);
                startActivity(intent);
                break;
            case R.id.nav_rel_america:
                intent = new Intent(mActivityContext,EventMainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rel_europe:
                intent = new Intent(mActivityContext,Web_View_europe.class);
                //intent.putExtra("LOCATION","EUROPE");
                startActivity(intent);
                break;
            case R.id.nav_rel_contact:
                intent = new Intent(mActivityContext,StandEnquiryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_rel_events:
                intent = new Intent(mActivityContext,CalendarActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_rel_ex_fav:
                intent = new Intent(mActivityContext,FavExhibitorActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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
