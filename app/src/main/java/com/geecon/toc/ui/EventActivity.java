package com.geecon.toc.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.geecon.toc.async.SessionsAsync;
import com.geecon.toc.async.SpeakerListAsync;
import com.geecon.toc.interfaces.BannersInterface;
import com.geecon.toc.interfaces.PollingInterface;
import com.geecon.toc.interfaces.SessionsInterface;
import com.geecon.toc.interfaces.SpeakerListInterface;
import com.geecon.toc.models.BannerModel;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.List;

public class EventActivity extends AppCompatActivity implements SessionsInterface,SpeakerListInterface,PollingInterface,BannersInterface, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

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
    //private String LocStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        img_help = (ImageView)toolbar.findViewById(R.id.img_help);
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        img_help.setVisibility(View.GONE);
        img_back.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(EventActivity.this);
        progressDialog.setMessage("Loading...");

        img_logo = (ImageView) findViewById(R.id.img_logo);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        sessionsInterface = this;
        speakerListInterface = this;
        pollingInterface = this;
        bannersInterface = this;

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

        relFloorplan = (RelativeLayout) findViewById(R.id.rel_floorplan);
        relConAgenda = (RelativeLayout) findViewById(R.id.rel_con_agenda);
        relVenueInfo = (RelativeLayout) findViewById(R.id.rel_venue_info);
        relSpeakerInfo = (RelativeLayout) findViewById(R.id.rel_speaker_info);
        relContactUs = (RelativeLayout) findViewById(R.id.rel_contact_us);
        relSurvey = (RelativeLayout) findViewById(R.id.rel_survey);
        img_info = (ImageView)findViewById(R.id.img_info);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        if(AppPreferences.getInstance().getConfData().getCONF_ID().equals("2") || AppPreferences.getInstance().getConfData().getCONF_ID().equals("6")){
            img_logo.setImageResource(R.drawable.csc_cc);
        }
        if(AppPreferences.getInstance().getConfData().getCONF_ID().equals("5") || AppPreferences.getInstance().getConfData().getCONF_ID().equals("7")){
            img_logo.setImageResource(R.drawable.tts_cc);
        }
        if(AppPreferences.getInstance().getConfData().getCONF_ID().equals("8")){
            img_logo.setImageResource(R.drawable.bs_cc);
        }

        //progressDialog.show();
        new BannersAsync(bannersInterface).execute();

        img_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext, VenueInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        relContactUs.setOnClickListener(new View.OnClickListener() {
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

        relConAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    //new PollingAsync(pollingInterface).execute();
                    Intent intent = new Intent(mActivityContext,PollingBlankActivity.class);
                    startActivity(intent);
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

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.addOnPageChangeListener(this);
        DefaultSliderView textSliderView = new DefaultSliderView (mActivityContext);
        DefaultSliderView  textSliderView2 = new DefaultSliderView (mActivityContext);
        DefaultSliderView  textSliderView3 = new DefaultSliderView (mActivityContext);
        textSliderView
                .image(R.drawable.banner_one)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(EventActivity.this);
        textSliderView2
                .image(R.drawable.banner_two_new)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(EventActivity.this);
        textSliderView3
                .image(R.drawable.banner_three)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(EventActivity.this);

        //textSliderView.bundle(new Bundle());
        //textSliderView.getBundle()
         //       .putString("extra", imagePath);

        mDemoSlider.addSlider(textSliderView);

        //textSliderView.bundle(new Bundle());
        //textSliderView.getBundle()
          //      .putString("extra", imagePath);

        mDemoSlider.addSlider(textSliderView2);

        //textSliderView3.bundle(new Bundle());
        //textSliderView3.getBundle()
           //     .putString("extra", imagePath);

      //  mDemoSlider.addSlider(textSliderView3);
        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
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
                    intent = new Intent(EventActivity.this, WebView_tweet.class);
                    startActivity(intent);
                    break;

                case R.id.linkedin:
                    intent = new Intent(EventActivity.this, Web_View_linkedin.class);
                    startActivity(intent);
                    break;

                case R.id.youtube:
                    intent = new Intent(EventActivity.this, Web_View_youtube.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void sessionResponse(String response){
        progressDialog.dismiss();
        Intent intent = new Intent(mActivityContext,ConferenceAgendaActivity.class);
        intent.putExtra("CONFERENCE_AGENDA_LIST",response);
        startActivity(intent);
    }

    @Override
    public void speakerListResponse(String response){
        progressDialog.dismiss();
        Intent intent = new Intent(mActivityContext,SpeakerListActivity.class);
        intent.putExtra("SPEAKER_LIST",response);
        startActivity(intent);
    }

    @Override
    public void pollingResponse(String response) {
        progressDialog.dismiss();
        Intent intent = new Intent(mActivityContext,PollingActivity.class);
        intent.putExtra("POLLING_DATA",response);
        startActivity(intent);
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
                        .setOnSliderClickListener(EventActivity.this);
                mDemoSlider.addSlider(textSliderView);
            }


            mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }
    }
}
