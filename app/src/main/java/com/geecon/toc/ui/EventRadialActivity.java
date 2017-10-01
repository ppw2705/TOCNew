package com.geecon.toc.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.geecon.toc.R;
import com.geecon.toc.async.BannersAsync;
import com.geecon.toc.async.PollingAsync;
import com.geecon.toc.async.SessionsAsync;
import com.geecon.toc.async.SpeakerListAsync;
import com.geecon.toc.interfaces.BannersInterface;
import com.geecon.toc.interfaces.PollingInterface;
import com.geecon.toc.interfaces.SessionsInterface;
import com.geecon.toc.interfaces.SpeakerListInterface;
import com.geecon.toc.models.BannerModel;
import com.geecon.toc.models.SessionsModel;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class EventRadialActivity extends AppCompatActivity implements SessionsInterface,SpeakerListInterface,PollingInterface, BannersInterface, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private Activity mActivityContext;
    private Context mAppContext;
    private RelativeLayout relFloorplan, relConAgenda, relPolling, relVenueInfo, relSpeakerInfo, relConQA, relExhibitorList, relContactUs, relSurvey;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private ImageView img_help,img_back,img_info,img_logo;
    private SessionsInterface sessionsInterface;
    private SpeakerListInterface speakerListInterface;
    private PollingInterface pollingInterface;
    private BannersInterface bannersInterface;
    private ProgressDialog progressDialog;
    private SliderLayout mDemoSlider;
    private TextView pageHeading;

    private float radiousOfAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_radial);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        sessionsInterface = this;
        speakerListInterface = this;
        pollingInterface = this;
        bannersInterface = this;

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*SharedPreferences sharedPref = mActivityContext.getPreferences(Context.MODE_PRIVATE);
        if (!sharedPref.getBoolean("FIRST APP OPEN", false)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("FIRST APP OPEN", true);
            editor.apply();
            showNotification();
        }*/
        progressDialog = new ProgressDialog(EventRadialActivity.this);
        progressDialog.setMessage("Loading...");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);

        //final String LocStr = getIntent().getStringExtra("LOCATION");

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

        relConAgenda = (RelativeLayout) findViewById(R.id.rel_con_agenda);
        relFloorplan = (RelativeLayout) findViewById(R.id.rel_floorplan);
        relSpeakerInfo = (RelativeLayout) findViewById(R.id.rel_speaker_info);
        relSurvey = (RelativeLayout) findViewById(R.id.rel_survey);
        relVenueInfo = (RelativeLayout) findViewById(R.id.rel_venue_info);
        relConAgenda.setVisibility(View.GONE);
        relFloorplan.setVisibility(View.GONE);
        relSpeakerInfo.setVisibility(View.GONE);
        relSurvey.setVisibility(View.GONE);
        relVenueInfo.setVisibility(View.GONE);

        img_logo = (ImageView) findViewById(R.id.img_logo);
        img_help = (ImageView)toolbar.findViewById(R.id.img_help);
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        img_help.setVisibility(View.GONE);
        img_back.setVisibility(View.VISIBLE);

        img_info = (ImageView)findViewById(R.id.img_info);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        if(AppPreferences.getInstance().getConfData().getCONF_ID().equals("2") || AppPreferences.getInstance().getConfData().getCONF_ID().equals("6")){
            img_logo.setImageResource(R.drawable.america_csc_cc);
        }
        if(AppPreferences.getInstance().getConfData().getCONF_ID().equals("5") || AppPreferences.getInstance().getConfData().getCONF_ID().equals("7")){
            img_logo.setImageResource(R.drawable.tt_r_circ);
        }
        if(AppPreferences.getInstance().getConfData().getCONF_ID().equals("8")){
            img_logo.setImageResource(R.drawable.bulk_r_circ);
        }

        //progressDialog.show();
        new BannersAsync(bannersInterface).execute();

        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideRadialMenu();
                /*if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {*/
                Intent intent = new Intent(mActivityContext, InformationActivity.class);
                startActivity(intent);
                //}
            }
        });

        relFloorplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideRadialMenu();
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext, FloorplanActivity.class);
                    startActivity(intent);
                }
            }
        });

        relSpeakerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    new SpeakerListAsync(speakerListInterface).execute(AppPreferences.getInstance().getConfData().getCONF_ID());
                }
            }
        });

        relVenueInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideRadialMenu();
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext, VenueInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        /*relContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRadialMenu();
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext, ExhibitorListActivity.class);
                    startActivity(intent);
                }
            }
        });*/

        relConAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideRadialMenu();
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    new SessionsAsync(sessionsInterface).execute(AppPreferences.getInstance().getConfData().getCONF_ID());
                }
            }
        });

        relSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    //progressDialog.show();
                    new PollingAsync(pollingInterface).execute();
                    //hideRadialMenu();
                    //Intent intent = new Intent(mActivityContext,PollingBlankActivity.class);
                    //startActivity(intent);
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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        radiousOfAnimation = size.y/2 - size.y/4.5f;//300

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relConAgenda.clearAnimation();
                relFloorplan.clearAnimation();
                relSpeakerInfo.clearAnimation();
                relSurvey.clearAnimation();
                relVenueInfo.clearAnimation();

                relConAgenda.setVisibility(View.GONE);
                relFloorplan.setVisibility(View.GONE);
                relSpeakerInfo.setVisibility(View.GONE);
                relSurvey.setVisibility(View.GONE);
                relVenueInfo.setVisibility(View.GONE);

                /*relConAgenda.setVisibility(View.VISIBLE);
                animateDiagonalPan(relConAgenda, 750, 140f);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relFloorplan.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relFloorplan, 600, 115f);
                    }
                }, 150);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relSpeakerInfo.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relSpeakerInfo, 450, 90f);
                    }
                }, 300);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relSurvey.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relSurvey, 300, 65f);
                    }
                }, 450);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relVenueInfo.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relVenueInfo, 150, 40f);
                    }
                }, 600);*/

                relConAgenda.setVisibility(View.VISIBLE);
                animateDiagonalPan(relConAgenda, 750, 140f);

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relSpeakerInfo.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relSpeakerInfo, 600, 115f);
                    }
                }, 150);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relSurvey.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relSurvey, 450, 90);
                    }
                }, 300);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relVenueInfo.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relVenueInfo, 300, 65f);
                    }
                }, 450);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        relFloorplan.setVisibility(View.VISIBLE);
                        animateDiagonalPan(relFloorplan, 150, 40f);
                    }
                }, 600);


            }
        });
    }

    private void hideRadialMenu() {
        animateFadeView(relConAgenda, 500);
        animateFadeView(relFloorplan, 500);
        animateFadeView(relSpeakerInfo, 500);
        animateFadeView(relSurvey, 500);
        animateFadeView(relVenueInfo, 500);
    }

    private void animateFadeView(final View v, final int duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0); // values from 0 to 1
        animator.setDuration(duration); // 5 seconds duration from 0 to 1
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                v.setAlpha((float) valueAnimator.getAnimatedValue());
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(View.GONE);
                v.setAlpha((float)1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void animateDiagonalPan(final View v, final int duration, final float angle) {

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1); // values from 0 to 1
        animator.setDuration(duration); // 5 seconds duration from 0 to 1
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angleDeg = ((float) animation.getAnimatedValue() * angle) % 180;
                float angleRad = (float) Math.toRadians(angleDeg);
                v.setTranslationX((float)(radiousOfAnimation * Math.sin(angleRad)));
                v.setTranslationY((float)(radiousOfAnimation * Math.cos(angleRad)));
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }


    public void web_view(View view)
    {
        if (!AppUtils.isNetworkAvailable(mActivityContext)) {
            Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent;
            switch (view.getId()) {

                case R.id.tweet:
                    intent = new Intent(EventRadialActivity.this, WebView_tweet.class);
                    startActivity(intent);
                    break;

                case R.id.linkedin:
                    intent = new Intent(EventRadialActivity.this, Web_View_linkedin.class);
                    startActivity(intent);
                    break;

                case R.id.youtube:
                    intent = new Intent(EventRadialActivity.this, Web_View_youtube.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void sessionResponse(String response){
        progressDialog.dismiss();
        if(response.isEmpty()){
            Toast.makeText(mActivityContext,"Please try again",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        Intent intent = new Intent(mActivityContext,ConferenceAgendaActivity.class);
        intent.putExtra("CONFERENCE_AGENDA_LIST",response);
        startActivity(intent);
    }

    @Override
    public void speakerListResponse(String response){
        progressDialog.dismiss();
        if(response.isEmpty()){
            Toast.makeText(mActivityContext,"Please try again",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        Intent intent = new Intent(mActivityContext,SpeakerListActivity.class);
        intent.putExtra("SPEAKER_LIST",response);
        startActivity(intent);
    }

    @Override
    public void pollingResponse(String response) {
        progressDialog.dismiss();
        if(response.isEmpty()){
            Toast.makeText(mActivityContext,"Please try again",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        Intent intent = new Intent(mActivityContext,PollingActivity.class);
        intent.putExtra("POLLING_DATA",response);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        relConAgenda.clearAnimation();
        relFloorplan.clearAnimation();
        relSpeakerInfo.clearAnimation();
        relSurvey.clearAnimation();
        relVenueInfo.clearAnimation();

        relConAgenda.setVisibility(View.GONE);
        relFloorplan.setVisibility(View.GONE);
        relSpeakerInfo.setVisibility(View.GONE);
        relSurvey.setVisibility(View.GONE);
        relVenueInfo.setVisibility(View.GONE);

        relConAgenda.setVisibility(View.VISIBLE);
        animateDiagonalPan(relConAgenda, 750, 140f);

        Handler handler = new Handler();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relFloorplan.setVisibility(View.VISIBLE);
                animateDiagonalPan(relFloorplan, 600, 115f);
            }
        }, 150);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relSpeakerInfo.setVisibility(View.VISIBLE);
                animateDiagonalPan(relSpeakerInfo, 450, 90f);
            }
        }, 300);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relSurvey.setVisibility(View.VISIBLE);
                animateDiagonalPan(relSurvey, 300, 65f);
            }
        }, 450);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relVenueInfo.setVisibility(View.VISIBLE);
                animateDiagonalPan(relVenueInfo, 150, 40f);
            }
        }, 600);*/

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relSpeakerInfo.setVisibility(View.VISIBLE);
                animateDiagonalPan(relSpeakerInfo, 600, 115f);
            }
        }, 150);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relSurvey.setVisibility(View.VISIBLE);
                animateDiagonalPan(relSurvey, 450, 90);
            }
        }, 300);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relVenueInfo.setVisibility(View.VISIBLE);
                animateDiagonalPan(relVenueInfo, 300, 65f);
            }
        }, 450);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                relFloorplan.setVisibility(View.VISIBLE);
                animateDiagonalPan(relFloorplan, 150, 40f);
            }
        }, 600);
    }

    @Override
    public void bannersResponse(String response) {

        List<BannerModel> bannersModel = new Gson().fromJson(response,
                new TypeToken<List<BannerModel>>() {
                }.getType());
        //progressDialog.dismiss();
        if(response.isEmpty()){
            mDemoSlider.setVisibility(View.GONE);
        } else {
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.startAutoCycle();
            mDemoSlider.addOnPageChangeListener(this);

            for (BannerModel bannerModel : bannersModel) {
                DefaultSliderView textSliderView = new DefaultSliderView(mActivityContext);
                textSliderView
                        .image(bannerModel.getEPIC_PICTURE())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(EventRadialActivity.this);
                mDemoSlider.addSlider(textSliderView);
            }


            mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }
    }

    /*private void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.info_new)
                        .setContentTitle("App open notification")
                        .setContentText("Welcome to the Radial Menu!!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(mActivityContext);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }*/
}
