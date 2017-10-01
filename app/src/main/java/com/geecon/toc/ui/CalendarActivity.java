package com.geecon.toc.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.geecon.toc.adapters.CalendarAdapter;
import com.geecon.toc.adapters.ConferenceAgendaAdapter;
import com.geecon.toc.models.CalendarModel;
import com.geecon.toc.utils.CalendarContentResolver;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading,txt_no_entries;
    private ImageView img_back;
    private RecyclerView recCalEvents;
    private CalendarAdapter calendarAdapter;
    private List<CalendarModel> calendarModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("MY SEMINAR CALENDAR");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);
        txt_no_entries = (TextView)findViewById(R.id.txt_no_entries);

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

        calendarModelList = new ArrayList<>();
        recCalEvents = (RecyclerView) findViewById(R.id.rec_cal_events);
        recCalEvents.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recCalEvents.setLayoutManager(layoutManager);

        calendarAdapter = new CalendarAdapter(mAppContext, calendarModelList, new CalendarAdapter.OnItemClickListener(){
            @Override
            public void onDeleteClicked(View v, CalendarModel cal) {

                CalendarContentResolver calres = new CalendarContentResolver(mActivityContext);
                calres.DeleteCalendarEntry(cal.getTITLE());
                calendarModelList.remove(cal);
                calendarAdapter.remove(cal);
                if(calendarModelList.size() == 0){
                    recCalEvents.setVisibility(View.GONE);
                    txt_no_entries.setVisibility(View.VISIBLE);
                }
            }
        });
        recCalEvents.setAdapter(calendarAdapter);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(mActivityContext,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivityContext,
                    Manifest.permission.READ_CALENDAR)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(mActivityContext,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(mActivityContext,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            CalendarContentResolver cal = new CalendarContentResolver(this);
            List<CalendarModel> calStr = cal.getCalendars();
            if(calStr != null){
                if(calStr.size() != 0){
                    for (CalendarModel calModel: calStr
                            ) {
                        //String date = getDate(Long.parseLong(calModel.getDTSTART()),"");
                        String start_time = getDate(Long.parseLong(calModel.getDTSTART()),"yyyy-MM-dd HH:mm:ss");
                        String end_time = getDate(Long.parseLong(calModel.getDTEND()),"yyyy-MM-dd HH:mm:ss");
                        //calModel.setDATE(date);
                        calModel.setTIME(start_time + " to " + end_time);
                    }

                    calendarModelList.addAll(calStr);
                    calendarAdapter.addEvents(calendarModelList);
                } else {
                    txt_no_entries.setVisibility(View.VISIBLE);
                    recCalEvents.setVisibility(View.GONE);
                }
            } else {
                txt_no_entries.setVisibility(View.VISIBLE);
                recCalEvents.setVisibility(View.GONE);
            }
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CalendarContentResolver cal = new CalendarContentResolver(this);
                    List<CalendarModel> calStr = cal.getCalendars();
                    if(calStr != null){
                        if(calStr.size() != 0){
                            for (CalendarModel calModel: calStr
                                    ) {
                                //String date = getDate(Long.parseLong(calModel.getDTSTART()),"");
                                String start_time = getDate(Long.parseLong(calModel.getDTSTART()),"yyyy-MM-dd HH:mm:ss");
                                String end_time = getDate(Long.parseLong(calModel.getDTEND()),"yyyy-MM-dd HH:mm:ss");
                                //calModel.setDATE(date);
                                calModel.setTIME(start_time + " to " + end_time);
                            }

                            calendarModelList.addAll(calStr);
                            calendarAdapter.addEvents(calendarModelList);
                        } else {
                            txt_no_entries.setVisibility(View.VISIBLE);
                            recCalEvents.setVisibility(View.GONE);
                        }
                    } else {
                        txt_no_entries.setVisibility(View.VISIBLE);
                        recCalEvents.setVisibility(View.GONE);
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(mAppContext,"Sorry you cant access this functionality without permissions",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
                break;
            case R.id.nav_rel_ex_fav:
                intent = new Intent(mActivityContext,FavExhibitorActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
