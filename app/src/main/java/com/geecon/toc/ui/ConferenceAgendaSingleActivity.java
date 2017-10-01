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
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.geecon.toc.R;
import com.geecon.toc.async.SpeakerAsync;
import com.geecon.toc.interfaces.SpeakerInterface;
import com.geecon.toc.models.CalendarModel;
import com.geecon.toc.models.SessionsModel;
import com.geecon.toc.models.SpeakerModel;
import com.geecon.toc.utils.AppUtils;
import com.geecon.toc.utils.CalendarContentResolver;
import com.geecon.toc.utils.UlTagHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.geecon.toc.R.drawable.c;

public class ConferenceAgendaSingleActivity extends AppCompatActivity implements SpeakerInterface {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading, con_title, txt_date, con_keypoints_title, speaker_list_title;
    private HorizontalScrollView horizontalScrollView;
    private ImageView img_back;
    private List<SessionsModel> sessionsModel;
    private LinearLayout speakerList,lin_date;
    private SpeakerInterface speakerInterface;
    private DocumentView con_desc, con_keypoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_agenda_single);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        speakerInterface = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String result_list = getIntent().getStringExtra("SESSION_DATA");
        //Toast.makeText(mAppContext, result_list, Toast.LENGTH_SHORT).show();
        try {
            sessionsModel = new Gson().fromJson(result_list,
                    new TypeToken<List<SessionsModel>>() {
                    }.getType());

            //Toast.makeText(mAppContext, sessionsModel.get(0).getAll().getHEADING(), Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
            ex.printStackTrace();
        }

        pageHeading = (TextView) toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("CONFERENCE AGENDA");
        img_back = (ImageView) toolbar.findViewById(R.id.img_back);
        con_title = (TextView) findViewById(R.id.con_title);
        txt_date = (TextView) findViewById(R.id.txt_date);
        con_desc = (DocumentView) findViewById(R.id.con_desc);
        speakerList = (LinearLayout) findViewById(R.id.speaker_list);
        con_keypoints = (DocumentView) findViewById(R.id.con_keypoints);
        con_keypoints_title = (TextView) findViewById(R.id.con_keypoints_title);
        speaker_list_title = (TextView) findViewById(R.id.speaker_list_title);
        lin_date = (LinearLayout)findViewById(R.id.lin_date);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
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

        con_title.setText(sessionsModel.get(0).getAll().getHEADING());
        txt_date.setText(sessionsModel.get(0).getAll().getSTART_DATE_TIME());
        con_desc.setText(Html.fromHtml(sessionsModel.get(0).getAll().getDESCRIPTION()));
        String keypoints = sessionsModel.get(0).getAll().getKEYPOINTS();
        keypoints = keypoints.replace("<ul>", "");
        keypoints = keypoints.replace("</ul>", "");
        keypoints = keypoints.replace("<li>", "&#8226;");
        keypoints = keypoints.replace("</li>", "<br>");
        con_keypoints.setText(Html.fromHtml(keypoints, null, new UlTagHandler()));
        if (sessionsModel.get(0).getAll().getDESCRIPTION().isEmpty()) {
            con_desc.setVisibility(View.GONE);
        }
        if (sessionsModel.get(0).getAll().getKEYPOINTS().isEmpty()) {
            con_keypoints.setVisibility(View.GONE);
            con_keypoints_title.setVisibility(View.GONE);
        }
        if (sessionsModel.get(0).getSpeaker().size() == 0) {
            speaker_list_title.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);
        } else {
            for (SpeakerModel single_speaker :
                    sessionsModel.get(0).getSpeaker()) {
                View SpeakerView = LayoutInflater.from(mActivityContext).inflate(R.layout.ca_single_speaker, null, false);
                TextView txt_name = (TextView) SpeakerView.findViewById(R.id.speaker_name);
                txt_name.setText(single_speaker.getSPEAKER_NAME());
                ImageView imgSpeaker = (ImageView) SpeakerView.findViewById(R.id.speaker_imv);
                if (single_speaker.getIMAGE() != null && single_speaker.getIMAGE() != "") {
                    Picasso.with(mActivityContext).load(single_speaker.getIMAGE()).placeholder(R.drawable.pro).resize(200, 200).into(imgSpeaker);
                }
                LinearLayout lin_single_speaker = (LinearLayout) SpeakerView.findViewById(R.id.lin_single_speaker);
                final String speaker_id = single_speaker.getSPEAKER_ID();
                lin_single_speaker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                            Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                        } else {
                            new SpeakerAsync(speakerInterface).execute(speaker_id);
                        }
                    }
                });
                speakerList.addView(SpeakerView);
            }
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });

        /*lin_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent(mActivityContext,sessionsModel.get(0).getAll().getHEADING(),sessionsModel.get(0).getAll().getSTART_TIME(),sessionsModel.get(0).getAll().getEND_TIME());
            }
        });*/
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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

        GregorianCalendar calstDate = new GregorianCalendar(st_c.get(Calendar.YEAR),st_c.get(Calendar.MONTH),st_c.get(Calendar.DATE),st_c.get(Calendar.HOUR),st_c.get(Calendar.MINUTE),st_c.get(Calendar.SECOND));

        GregorianCalendar calenDate = new GregorianCalendar(en_c.get(Calendar.YEAR),en_c.get(Calendar.MONTH),en_c.get(Calendar.DATE),en_c.get(Calendar.HOUR),en_c.get(Calendar.MINUTE),en_c.get(Calendar.SECOND));
        try {
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, calstDate.getTimeInMillis());
            values.put(CalendarContract.Events.DTEND, calenDate.getTimeInMillis());
            values.put(CalendarContract.Events.TITLE, "TOC - " + title);
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
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
    public void speakerResponse(String response){
        Intent intent = new Intent(mActivityContext,SpeakerInfoActivity.class);
        intent.putExtra("SPEAKER_DATA",response);
        startActivity(intent);
    }
}
