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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.adapters.NotificationsAdapter;
import com.geecon.toc.async.NotificationsAsync;
import com.geecon.toc.interfaces.NotificationsInterface;
import com.geecon.toc.models.NotificationModel;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationsActivity extends AppCompatActivity implements NotificationsInterface {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private RecyclerView recyclerViewNotificationsList;
    private NotificationsAdapter notificationAdapter;
    private List<NotificationModel> masterNotificationList;
    private List<NotificationModel> grandMasterNotificationList;
    //private OnLoadMoreListener onLoadMoreListener;
    private ProgressDialog progress;
    private int page = 1;
    private TextView pageHeading;
    private ImageView img_back;
    private NotificationsInterface notificationsInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        notificationsInterface = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("NOTIFICATIONS");
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

        AppPreferences.getInstance().setNotifyCount(null);

        ShortcutBadger.removeCount(mAppContext); //for 1.1.4+
        //ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

        masterNotificationList = new ArrayList<>();

        recyclerViewNotificationsList = (RecyclerView) findViewById(R.id.recycler_view_notifications_list);
        recyclerViewNotificationsList.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewNotificationsList.setLayoutManager(layoutManager);
        notificationAdapter = new NotificationsAdapter(mActivityContext,masterNotificationList, new NotificationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NotificationModel item) {
                /*if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    new SpeakerAsync(speakerInterface).execute(item.getSPEAKER_ID());
                }*/
            }
        });

        recyclerViewNotificationsList.setAdapter(notificationAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });
        if (!AppUtils.isNetworkAvailable(mActivityContext)) {
            Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
        } else {
            progress.show();
            new NotificationsAsync(notificationsInterface).execute();
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
                intent = new Intent(mActivityContext,EventMainActivity.class);
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
    public void notifyResponse(String response) {
        Log.d("NOTIFY",response);
        progress.dismiss();
        try {
            notificationAdapter.removeNullLastItem();
            List<NotificationModel> notificationModels = new Gson().fromJson(response,
                    new TypeToken<List<NotificationModel>>() {
                    }.getType());

            masterNotificationList.addAll(notificationModels);
            notificationAdapter.addNotifications(masterNotificationList);
        } catch(Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
        }

    }
}
