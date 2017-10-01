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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geecon.toc.R;
import com.geecon.toc.utils.AppPreferences;

public class VenueInfoActivity extends AppCompatActivity {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading,address,email,telephone,website,txt_venue_title,txt_venue_sub_title,venue_info;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_info);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("INFORMATION");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);

        address = (TextView)findViewById(R.id.venue_address);
        email = (TextView)findViewById(R.id.venue_email);
        telephone = (TextView)findViewById(R.id.venue_telephone);
        website = (TextView)findViewById(R.id.venue_website);
        txt_venue_title = (TextView)findViewById(R.id.txt_venue_title);
        txt_venue_sub_title = (TextView)findViewById(R.id.txt_venue_sub_title);
        venue_info = (TextView) findViewById(R.id.venue_info);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);

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

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });

        address.setText(Html.fromHtml(AppPreferences.getInstance().getConfData().getVENUE_ADDRESS()));
        email.setText(AppPreferences.getInstance().getConfData().getEMAIL());
        telephone.setText(AppPreferences.getInstance().getConfData().getTELEPHONE());
        website.setText(AppPreferences.getInstance().getConfData().getWEBSITE());
        venue_info.setText(Html.fromHtml(AppPreferences.getInstance().getConfData().getGENERAL_INFO()));
        //txt_venue_title.setText(AppPreferences.getInstance().getConfData().getCONF_NAME());
        //txt_venue_sub_title.setText(Html.fromHtml(AppPreferences.getInstance().getConfData().getSHORT_DESCRIPTION()));
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
}
