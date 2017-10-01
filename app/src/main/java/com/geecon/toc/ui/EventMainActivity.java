package com.geecon.toc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.async.ConferenceDetailsAsync;
import com.geecon.toc.interfaces.ConferenceDetailsInterface;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;

public class EventMainActivity extends AppCompatActivity implements ConferenceDetailsInterface {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private ImageView imgContainerSupply, imgTechTOC, imgBulk, imgExhibition, imgNotify,img_logo;
    private ConferenceDetailsInterface conferenceDetailsInterface;
    private TextView txtNotifyCount,pageHeading;
    //private String LocStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_main);
        mActivityContext = this;
        mAppContext = getApplicationContext();
        conferenceDetailsInterface = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);
        imgContainerSupply = (ImageView) findViewById(R.id.img_container_supply);
        imgTechTOC = (ImageView)findViewById(R.id.img_tech_toc);
        imgBulk = (ImageView)findViewById(R.id.img_bulk);
        imgExhibition =(ImageView)findViewById(R.id.img_exhibition);
        img_logo = (ImageView)findViewById(R.id.img_logo);
        imgNotify = (ImageView)toolbar.findViewById(R.id.img_help);
        txtNotifyCount = (TextView) toolbar.findViewById(R.id.text_notify_count);

        //LocStr = getIntent().getStringExtra("LOCATION");

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

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading_two);
        pageHeading.setText("Americas");
        //pageHeading.setText(LocStr.substring(0, 1).toUpperCase() + LocStr.substring(1).toLowerCase());


        /*if(LocStr.equals("EUROPE")){
            img_logo.setImageResource(R.drawable.toc_europe_circ);
        }*/
        String notifyCount = AppPreferences.getInstance().getNotifyCount();
        if(notifyCount == null){
            txtNotifyCount.setVisibility(View.GONE);
        } else {
            txtNotifyCount.setText(notifyCount);
            txtNotifyCount.setVisibility(View.VISIBLE);
        }

        imgContainerSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    /*int loc = 2;
                    if(LocStr.equals("EUROPE")){
                        loc = 6;
                    }*/
                    new ConferenceDetailsAsync(conferenceDetailsInterface).execute("6");
                }
            }
        });

        imgTechTOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    /*int loc = 5;
                    if(LocStr.equals("EUROPE")){
                        loc = 7;
                    }*/
                    new ConferenceDetailsAsync(conferenceDetailsInterface).execute("7");
                }
            }
        });

        imgBulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    /*int loc = 8;
                    if(LocStr.equals("EUROPE")){
                        new ConferenceDetailsAsync(conferenceDetailsInterface).execute(String.valueOf(loc));
                    }*/
                    new ConferenceDetailsAsync(conferenceDetailsInterface).execute("8");
                }
            }
        });

        imgExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext, ExhibitorListActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext, NotificationsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        String notifyCount = AppPreferences.getInstance().getNotifyCount();
        if(notifyCount == null){
            txtNotifyCount.setVisibility(View.GONE);
        } else {
            txtNotifyCount.setText(notifyCount);
            txtNotifyCount.setVisibility(View.VISIBLE);
        }
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
    public void responseReceived(String response){
        if(response.isEmpty()){
            Toast.makeText(mActivityContext,"Please try again",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        AppPreferences.getInstance().setConfData(response);
        Intent intent = new Intent(mActivityContext,EventRadialActivity.class);
        //Intent intent = new Intent(mActivityContext,EventActivity.class);
        //intent.putExtra("LOCATION",LocStr);
        startActivity(intent);
    }
}
