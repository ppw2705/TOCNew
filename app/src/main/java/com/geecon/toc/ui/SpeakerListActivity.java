package com.geecon.toc.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
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
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.adapters.SpeakerAdapter;
import com.geecon.toc.async.SpeakerAsync;
import com.geecon.toc.interfaces.SpeakerInterface;
import com.geecon.toc.models.SpeakerModel;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SpeakerListActivity extends AppCompatActivity implements SpeakerInterface {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private RecyclerView recyclerViewSpeakerList;
    private SpeakerAdapter speakerAdapter;
    private List<SpeakerModel> masterSpeakerList;
    private List<SpeakerModel> grandMasterSpeakerList;
    //private OnLoadMoreListener onLoadMoreListener;
    private ProgressDialog progress;
    private int page = 1;
    private TextView pageHeading;
    private ImageView img_back;
    private SpeakerInterface speakerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker_list);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        speakerInterface = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("SPEAKERS");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);

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

        masterSpeakerList = new ArrayList<>();

        recyclerViewSpeakerList = (RecyclerView) findViewById(R.id.recycler_view_speaker_list);
        recyclerViewSpeakerList.setHasFixedSize(true);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSpeakerList.addItemDecoration(new GridSpaceItemDecoration(10));
        speakerAdapter = new SpeakerAdapter(mActivityContext,masterSpeakerList, new SpeakerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SpeakerModel item) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    new SpeakerAsync(speakerInterface).execute(item.getSPEAKER_ID());
                }
            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(SpeakerListActivity.this, 2);
        recyclerViewSpeakerList.setLayoutManager(layoutManager);
        recyclerViewSpeakerList.setAdapter(speakerAdapter);

        String result_list = getIntent().getStringExtra("SPEAKER_LIST");

        try {
            speakerAdapter.removeNullLastItem();
            List<SpeakerModel> speakerModel = new Gson().fromJson(result_list,
                    new TypeToken<List<SpeakerModel>>() {
                    }.getType());

            masterSpeakerList.addAll(speakerModel);
            speakerAdapter.addSpeaker(masterSpeakerList);
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

    public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public GridSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;

            int i = parent.getChildAdapterPosition(view);
            if ((i % 2) == 0) {
                outRect.right = mVerticalSpaceHeight;
            }
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

    @Override
    public void speakerResponse(String response){
        Intent intent = new Intent(mActivityContext,SpeakerInfoActivity.class);
        intent.putExtra("SPEAKER_DATA",response);
        startActivity(intent);
    }
}
