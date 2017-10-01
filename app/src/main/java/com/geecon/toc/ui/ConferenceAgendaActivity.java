package com.geecon.toc.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.adapters.ConferenceAgendaAdapter;
import com.geecon.toc.async.PollingAsync;
import com.geecon.toc.async.PollingSubmitAsync;
import com.geecon.toc.async.RatingSubmitAsync;
import com.geecon.toc.async.SessionDetailsAsync;
import com.geecon.toc.async.SpeakerAsync;
import com.geecon.toc.interfaces.PollingInterface;
import com.geecon.toc.interfaces.RatingInterface;
import com.geecon.toc.interfaces.SessionDetailsInterface;
import com.geecon.toc.interfaces.SpeakerInterface;
import com.geecon.toc.models.CalendarModel;
import com.geecon.toc.models.SessionsModel;
import com.geecon.toc.utils.AppUtils;
import com.geecon.toc.utils.CalendarContentResolver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ConferenceAgendaActivity extends AppCompatActivity implements SessionDetailsInterface,SpeakerInterface,PollingInterface,RatingInterface {

    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 2;
    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading;
    private ImageView img_back;
    private RecyclerView recConAgenda;
    private ConferenceAgendaAdapter conAgendaAdapter;
    private List<SessionsModel> sessionsModelList;
    private SessionDetailsInterface sessionDetailsInterface;
    private SpeakerInterface speakerInterface;
    private PollingInterface pollingInterface;
    private RatingInterface ratingInterface;
    private SessionsModel sessionsModelForOther;
    private boolean permissionsReadFlag=false;
    private boolean permissionsWriteFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_agenda);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        sessionDetailsInterface = this;
        speakerInterface = this;
        pollingInterface = this;
        ratingInterface = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("CONFERENCE AGENDA");
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

        sessionsModelList = new ArrayList<>();
        recConAgenda = (RecyclerView) findViewById(R.id.rec_con_agenda);
        recConAgenda.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recConAgenda.setLayoutManager(layoutManager);
        conAgendaAdapter = new ConferenceAgendaAdapter(mAppContext, sessionsModelList, new ConferenceAgendaAdapter.OnItemClickListener() {
            @Override
            public void onReadMoreClicked(String session_ID) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    new SessionDetailsAsync(sessionDetailsInterface).execute(session_ID);
                }
            }

            @Override
            public void onSpeakerClicked(String speakerID) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    new SpeakerAsync(speakerInterface).execute(speakerID);
                }
            }

            @Override
            public void onPollingClicked() {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    new PollingAsync(pollingInterface).execute();
                    /*Intent intent = new Intent(mActivityContext,PollingBlankActivity.class);
                    startActivity(intent);*/
                }
            }

            @Override
            public void onDateClicked(SessionsModel sessionsModel) {
                //addEvent(mActivityContext,sessionsModel.getAll().getHEADING(),sessionsModel.getAll().getSTART_TIME(),sessionsModel.getAll().getEND_TIME());
                sessionsModelForOther = sessionsModel;
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
                    //permissionsReadFlag = true;
                    addEvent(mActivityContext,sessionsModelForOther.getAll().getHEADING(),sessionsModelForOther.getAll().getSTART_TIME(),sessionsModelForOther.getAll().getEND_TIME());
                }

                /*if (ContextCompat.checkSelfPermission(mActivityContext,
                        Manifest.permission.WRITE_CALENDAR)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivityContext,
                            Manifest.permission.WRITE_CALENDAR)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(mActivityContext,
                                new String[]{Manifest.permission.WRITE_CALENDAR},
                                MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    permissionsWriteFlag = true;
                    //addEvent(mActivityContext,sessionsModelForOther.getAll().getHEADING(),sessionsModelForOther.getAll().getSTART_TIME(),sessionsModelForOther.getAll().getEND_TIME());
                }

                if(permissionsReadFlag == true && permissionsWriteFlag == true){
                    addEvent(mActivityContext,sessionsModelForOther.getAll().getHEADING(),sessionsModelForOther.getAll().getSTART_TIME(),sessionsModelForOther.getAll().getEND_TIME());
                }*/
            }

            @Override
            public void onSubmitRatingClicked(View v,String confID, String rating) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    if(rating.equals("0")){
                        Toast.makeText(mActivityContext, "Please rate the Conference!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*String imeistring = null;

                    TelephonyManager telephonyManager = (TelephonyManager
                            ) getSystemService(Context.TELEPHONY_SERVICE);*/
                                    /*
                                     * getDeviceId() function Returns the unique device ID.
                                     * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
                                     */
                    //imeistring = telephonyManager.getDeviceId();
                    String imeistring = Settings.Secure.getString(mActivityContext.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    //Toast.makeText(mAppContext,txt_ques_ans.getText()+"_"+imeistring,Toast.LENGTH_SHORT).show();
                    new RatingSubmitAsync(ratingInterface).execute(confID + "_" + rating + "_" + imeistring);
                    v.setVisibility(View.GONE);
                    // Here, thisActivity is the current activity
                    /*if (ContextCompat.checkSelfPermission(mActivityContext,
                            Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivityContext,
                                Manifest.permission.READ_PHONE_STATE)) {

                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(mActivityContext,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_TELEPHONE);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }*/
                }


            }

            @Override
            public void onAskClicked(String session_id, String session_name) {
                //need to pass the session data as well
                Intent intent = new Intent(mActivityContext,AskQuestionActivity.class);
                intent.putExtra("SESSION_ID",session_id);
                intent.putExtra("SESSION",session_name);
                startActivity(intent);
            }
        });
        recConAgenda.setAdapter(conAgendaAdapter);

        /*recConAgenda.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                totalItemCount = layoutManager.getItemCount();
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold && !allOderLoader) {
                    isLoading = true;
                    //Toast.makeText(mAppContext,"total - "+ String.valueOf(totalItemCount)+" last - "+String.valueOf(lastVisibleItem)+" tresh - "+String.valueOf(visibleThreshold),Toast.LENGTH_SHORT).show();
                    onLoadMore();
                }
            }
        });*/

        String result_list = getIntent().getStringExtra("CONFERENCE_AGENDA_LIST");

        try {
            conAgendaAdapter.removeNullLastItem();
            List<SessionsModel> sessionsModel = new Gson().fromJson(result_list,
                    new TypeToken<List<SessionsModel>>() {
                    }.getType());

            sessionsModelList.addAll(sessionsModel);
            conAgendaAdapter.addConferenceAgenda(sessionsModelList);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addEvent(mActivityContext,sessionsModelForOther.getAll().getHEADING(),sessionsModelForOther.getAll().getSTART_TIME(),sessionsModelForOther.getAll().getEND_TIME());

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(mAppContext,"Sorry you cant access this functionality without permissions",Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(Confere.this,MainActivity.class);
                    startActivity(intent);
                    finish();*/
                }
                return;
            }

            /*case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addEvent(mActivityContext,sessionsModelForOther.getAll().getHEADING(),sessionsModelForOther.getAll().getSTART_TIME(),sessionsModelForOther.getAll().getEND_TIME());

                } else {
                    Toast.makeText(mAppContext,"Sorry you cant access this functionality without permissions",Toast.LENGTH_SHORT).show();
                }
                return;
            }*/
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

    // Add an event to the calendar of the user.
    public void addEvent(Context context, String title, String start_time, String end_time) {

        CalendarContentResolver cal = new CalendarContentResolver(context);
        CalendarModel calM = cal.getCalendarsByTitle(title);

        if(calM.getTITLE() != null){
            Toast.makeText(context,"Event already added",Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        Date st_date,en_date;
        try {
            st_date = formatter.parse(start_time);
            en_date = formatter.parse(end_time);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Calendar st_c = Calendar.getInstance();
        st_c.setTime(st_date);
        Calendar en_c = Calendar.getInstance();
        en_c.setTime(en_date);

        GregorianCalendar calstDate = new GregorianCalendar(st_c.get(Calendar.YEAR),st_c.get(Calendar.MONTH),st_c.get(Calendar.DATE),st_c.get(Calendar.HOUR_OF_DAY),st_c.get(Calendar.MINUTE));

        GregorianCalendar calenDate = new GregorianCalendar(en_c.get(Calendar.YEAR),en_c.get(Calendar.MONTH),en_c.get(Calendar.DATE),en_c.get(Calendar.HOUR_OF_DAY),en_c.get(Calendar.MINUTE));
        try {
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, calstDate.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, calenDate.getTimeInMillis());
            values.put(CalendarContract.Events.TITLE, "TOC - " + title);
            /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                values.put("Calendars_id", 1);
            } else {*/
                values.put(CalendarContract.Events.CALENDAR_ID, 1);
            //}
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance()
                    .getTimeZone().getID());
            System.out.println(Calendar.getInstance().getTimeZone().getID());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Toast.makeText(context,"Added to Calendar",Toast.LENGTH_SHORT).show();
            // Save the eventId into the Task object for possible future delete.
            /*this._eventId = Long.parseLong(uri.getLastPathSegment());
            // Add a 5 minute, 1 hour and 1 day reminders (3 reminders)
            setReminder(cr, this._eventId, 5);
            setReminder(cr, this._eventId, 60);
            setReminder(cr, this._eventId, 1440);
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pollingResponse(String response) {
        Intent intent = new Intent(mActivityContext,PollingActivity.class);
        intent.putExtra("POLLING_DATA",response);
        startActivity(intent);
    }

    @Override
    public void speakerResponse(String response) {
        Intent intent = new Intent(mActivityContext,SpeakerInfoActivity.class);
        intent.putExtra("SPEAKER_DATA",response);
        startActivity(intent);
    }

    @Override
    public void sessionDetailsResponse(String response){
        Intent intent = new Intent(mActivityContext,ConferenceAgendaSingleActivity.class);
        intent.putExtra("SESSION_DATA",response);
        startActivity(intent);
    }

    @Override
    public void ratingResponse(String response) {

        Toast.makeText(mActivityContext,response.replace("\n", ""),Toast.LENGTH_SHORT).show();

    }
}
