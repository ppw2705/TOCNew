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
import com.geecon.toc.adapters.ExhibitorAdapter;
import com.geecon.toc.async.ExhibitorListAsync;
import com.geecon.toc.async.SpeakerListAsync;
import com.geecon.toc.interfaces.ExhibitorListInterface;
import com.geecon.toc.models.ExhibitorModel;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ExhibitionListActivity extends AppCompatActivity implements ExhibitorListInterface {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private RecyclerView recyclerViewExhibitorList;
    private ExhibitorAdapter exhibitorAdapter;
    private ExhibitorListInterface exhibitorListInterface;
    private List<ExhibitorModel> masterExhibitorList;
    //private List<SpeakerModel> grandMasterSpeakerList;
    //private OnLoadMoreListener onLoadMoreListener;
    private ProgressDialog progress;
    private int page = 1;
    private TextView pageHeading;
    private ImageView img_back;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_list);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        mActivityContext = this;
        exhibitorListInterface = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("EXHIBITORS");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);

        progressDialog = new ProgressDialog(ExhibitionListActivity.this);
        progressDialog.setMessage("Loading...");


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

        if (!AppUtils.isNetworkAvailable(mActivityContext)) {
            Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            new ExhibitorListAsync(exhibitorListInterface).execute(AppPreferences.getInstance().getConfData().getCONF_ID());
        }

        masterExhibitorList = new ArrayList<>();


        recyclerViewExhibitorList = (RecyclerView) findViewById(R.id.recycler_view_exhibitor_list);
        recyclerViewExhibitorList.setHasFixedSize(true);
        //final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewExhibitorList.addItemDecoration(new GridSpaceItemDecoration(10));
        exhibitorAdapter = new ExhibitorAdapter(mActivityContext,masterExhibitorList, new ExhibitorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ExhibitorModel item) {
//                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
//                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
//                } else {
//                    new SpeakerAsync(speakerInterface).execute(item.getSPEAKER_ID());
//                }
            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(ExhibitionListActivity.this, 2);
        recyclerViewExhibitorList.setLayoutManager(layoutManager);
        recyclerViewExhibitorList.setAdapter(exhibitorAdapter);

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
intent = new Intent(mActivityContext,Web_View_asia.class);                 startActivity(intent);
                finish();
                break;
            case R.id.nav_rel_africa:
                intent = new Intent(mActivityContext,Web_View_africa.class);
                startActivity(intent);
                break;
            case R.id.nav_rel_america:
                intent = new Intent(mActivityContext,Web_View_america.class);
                startActivity(intent);
                break;
            case R.id.nav_rel_europe:
                intent = new Intent(mActivityContext,EventMainActivity.class);
                intent.putExtra("LOCATION","EUROPE");
                startActivity(intent);
                break;
            case R.id.nav_rel_contact:
                intent = new Intent(mActivityContext,StandEnquiryActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_rel_events:                 intent = new Intent(mActivityContext,CalendarActivity.class);                 startActivity(intent);                 finish();                 break;             case R.id.nav_rel_ex_fav:                 intent = new Intent(mActivityContext,FavExhibitorActivity.class);                 startActivity(intent);                 finish();                 break;
        }
    }

    @Override
    public void exhibitorListResponse(String response) {
        try {
            exhibitorAdapter.removeNullLastItem();
            List<ExhibitorModel> exhibitorModels = new Gson().fromJson(response,
                    new TypeToken<List<ExhibitorModel>>() {
                    }.getType());

            masterExhibitorList.addAll(exhibitorModels);
            exhibitorAdapter.addSpeaker(masterExhibitorList);
            progressDialog.cancel();
        } catch(Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
        }
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
