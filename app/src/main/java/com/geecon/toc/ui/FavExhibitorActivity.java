package com.geecon.toc.ui;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geecon.toc.R;
import com.geecon.toc.adapters.ExhibitorNewAdapter;
import com.geecon.toc.models.ExhibitorModel;
import com.geecon.toc.utils.AppPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viethoa.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

public class FavExhibitorActivity extends AppCompatActivity {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading,noData;
    private ImageView img_back;
    private RecyclerView recyclerViewExhibitorList;
    private ExhibitorNewAdapter exhibitorAdapter;
    private List<ExhibitorModel> masterExhibitorList;
    private ProgressDialog progress;
    private int page = 1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_exhibitor);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("MY EXHIBITOR FAVORITES");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        noData = (TextView) findViewById(R.id.no_data_txt);
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


        masterExhibitorList = new ArrayList<>();
        recyclerViewExhibitorList = (RecyclerView) findViewById(R.id.recycler_view_exhibitor_list);
        exhibitorAdapter = new ExhibitorNewAdapter(mActivityContext,masterExhibitorList, new ExhibitorNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ExhibitorModel item) {
                //To pass the data to ExhibitorInfo page
                Intent intent = new Intent(mActivityContext,ExhibitionInfoActivity.class);
                intent.putExtra("EXHIBITION_INFO",new Gson().toJson(item));
                //intent.putExtra("SESSION",session_name);
                startActivity(intent);
            }

            @Override
            public void onImageClick(ImageView view,ExhibitorModel item) {

            }

            @Override
            public void onRemoveClick(View v, ExhibitorModel item) {
                /*List<ExhibitorModel> exhibitorModelsList = new ArrayList<>();
                exhibitorModelsList.addAll(AppPreferences.getInstance().getExhibitorData());
                exhibitorModelsList.remove(item);*/
                masterExhibitorList.remove(item);
                AppPreferences.getInstance().setExhibitorData(new Gson().toJson(masterExhibitorList));
                exhibitorAdapter.remove(item);
                if(masterExhibitorList.size() == 0){
                    recyclerViewExhibitorList.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(FavExhibitorActivity.this, 1);
        recyclerViewExhibitorList.setLayoutManager(layoutManager);
        recyclerViewExhibitorList.setAdapter(exhibitorAdapter);

        try {
            List<ExhibitorModel> exhibitorModels = AppPreferences.getInstance().getExhibitorData();
            if(exhibitorModels.size() != 0){
                exhibitorAdapter.removeNullLastItem();
                masterExhibitorList.addAll(exhibitorModels);
                exhibitorAdapter.addSpeaker(masterExhibitorList);
            } else {
                recyclerViewExhibitorList.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
            progressDialog.cancel();
        } catch(Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
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
