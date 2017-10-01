package com.geecon.toc.ui;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.EAApplication;
import com.geecon.toc.R;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;
import com.geecon.toc.utils.BadgeUtils;
import com.geecon.toc.views.TypeWriter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private RelativeLayout rel_asia,rel_europe,rel_america,top_part,bottom_part;
    private ImageView imgAmerica, imgEurope, imgAfrica, imgAsia, imgNotify,imgLogo;
    private WebView webView;
    private TextView pageHeading,pageHeading2,txtNotifyCount;
    private TypeWriter tw;
    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]


    /*final String HOME_PACKAGE_SONY = "com.sonyericsson.home";
    final String HOME_PACKAGE_SAMSUNG = "com.sec.android.app.launcher";
    final String HOME_PACKAGE_LG = "com.lge.launcher2";
    final String HOME_PACKAGE_HTC = "com.htc.launcher";
    final String HOME_PACKAGE_ANDROID = "com.android.launcher";
    final String HOME_PACKAGE_APEX = "com.anddoes.launcher";
    final String HOME_PACKAGE_ADW = "org.adw.launcher";
    final String HOME_PACKAGE_ADW_EX = "org.adwfreak.launcher";
    final String HOME_PACKAGE_NOVA = "com.teslacoilsw.launcher";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TOKEN", "Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("tocandroid");
        Log.e(TAG, "Subscribed to toc topic");
        // [START shared_app_measurement]
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // [END shared_app_measurement]
        mActivityContext = this;
        mAppContext = getApplicationContext();
        //Toast.makeText(mAppContext,refreshedToken,Toast.LENGTH_SHORT).show();
        sendScreenImageName();
        //rel_asia = (RelativeLayout)findViewById(R.id.rel_toc_asia);
        //rel_europe = (RelativeLayout)findViewById(R.id.rel_toc_europe);
        rel_america = (RelativeLayout)findViewById(R.id.rel_toc_america);
        //imgAmerica = (ImageView)findViewById(R.id.img_america);
        imgEurope = (ImageView)findViewById(R.id.img_europe);
        imgAsia = (ImageView)findViewById(R.id.img_asia);
        imgAfrica = (ImageView)findViewById(R.id.img_africa);
        imgLogo = (ImageView)findViewById(R.id.img_logo);
        top_part = (RelativeLayout)findViewById(R.id.top_part);
        bottom_part = (RelativeLayout)findViewById(R.id.bottom_part);
        webView = (WebView) findViewById(R.id.webView);
        tw = (TypeWriter) findViewById(R.id.tv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        //toolbar.setNavigationIcon(R.drawable.ico_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgNotify = (ImageView)toolbar.findViewById(R.id.img_help);
        pageHeading = (TextView) toolbar.findViewById(R.id.txt_heading);
        pageHeading2 = (TextView) toolbar.findViewById(R.id.txt_heading_two);
        txtNotifyCount = (TextView) toolbar.findViewById(R.id.text_notify_count);
        pageHeading.setVisibility(View.GONE);
        pageHeading2.setVisibility(View.GONE);
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

        //processIntent(getIntent());
        //int badgeCount = 5;
        //ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+

        /*BadgeUtils.setBadge(mActivityContext,10);
        String classPath = "com.geecon.toc.ui.MainActivity";

        ContentValues cv = new ContentValues();
        cv.put("package", mAppContext.getPackageName());
        cv.put("class", classPath);
        cv.put("badgecount", 12);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_SAMSUNG), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_ADW), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_ADW_EX), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_ANDROID), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_APEX), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_HTC), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_LG), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_NOVA), cv);
        mAppContext.getContentResolver().insert(Uri.parse(HOME_PACKAGE_SONY), cv);*/
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> lst = getPackageManager().queryIntentActivities(intent, 0);
        if (!lst.isEmpty()) {
            for (ResolveInfo resolveInfo : lst) {
                Log.e("BINFO", "New Launcher Found: " + resolveInfo.activityInfo.packageName);
            }
        }*/
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= 21) {
            // Call some material design APIs here
            //Toast.makeText(mActivityContext,"Lollipop",Toast.LENGTH_SHORT).show();
        } else {
            // Implement this feature without material design
            //Toast.makeText(mActivityContext,"Kitkat",Toast.LENGTH_SHORT).show();
            //rel_asia.setBackground( getResources().getDrawable(R.drawable.background_border));
        }
        String notifyCount = AppPreferences.getInstance().getNotifyCount();
        if(notifyCount == null){
            txtNotifyCount.setVisibility(View.GONE);
        } else {
            txtNotifyCount.setText(notifyCount);
            txtNotifyCount.setVisibility(View.VISIBLE);
        }

        imgAsia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivityContext,Web_View_asia.class);
                startActivity(intent);
            }
        });

        rel_america.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivityContext,EventMainActivity.class);
//                Intent intent = new Intent(mActivityContext,CalendarActivity.class);
                //intent.putExtra("LOCATION","EUROPE");
                startActivity(intent);
            }
        });

        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mActivityContext,NotificationsActivity.class);
                    startActivity(intent);
                }
            }
        });

        //For typwriting effect
        tw.setText("");
        tw.setCharacterDelay(150);
        tw.animateText("Always update App before each event, Thank You");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    };

    private void processIntent(Intent intent){
        //get your extras
        PendingIntent contentIntent =
                PendingIntent.getActivity(mActivityContext, 0, new Intent(mAppContext, NotificationsActivity.class), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        recordScreenView();
        String notifyCount = AppPreferences.getInstance().getNotifyCount();
        if(notifyCount == null){
            txtNotifyCount.setVisibility(View.GONE);
        } else {
            txtNotifyCount.setText(notifyCount);
            txtNotifyCount.setVisibility(View.VISIBLE);
        }

        imgLogo.setImageResource(R.drawable.globe);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        //animation.setRepeatCount(0);
        imgLogo.startAnimation(animation);

        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        final Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                imgLogo.startAnimation(fadeOut);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                //Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                //imgLogo.startAnimation(fadeOut);
                imgLogo.setImageResource(R.drawable.round_toc);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


    }

    /**
     * Record a screen view hit for the visible
     */
    private void sendScreenImageName() {
        String name = TAG;

        // [START screen_view_hit]
        //Log.i(TAG, "Setting screen name: " + name);
        EAApplication.tracker().setScreenName("Image~" + name);
        EAApplication.tracker().send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]
    }

    /**
     * This sample has a single Activity, so we need to manually record "screen views" as
     * we change fragments.
     */
    private void recordScreenView() {
        // This string must be <= 36 characters long in order for setCurrentScreen to succeed.
        String screenName = TAG;

        // [START set_current_screen]
        mFirebaseAnalytics.setCurrentScreen(this, screenName, null /* class override */);
        // [END set_current_screen]
    }

    public void navClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nav_rel_home:
                break;
            case R.id.nav_rel_asia:
                intent = new Intent(mActivityContext,Web_View_asia.class);
                startActivity(intent);
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
                startActivity(intent);
                break;
            case R.id.nav_rel_contact:
                intent = new Intent(mActivityContext,StandEnquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rel_events:
                intent = new Intent(mActivityContext,CalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_rel_ex_fav:
                intent = new Intent(mActivityContext,FavExhibitorActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void web_view_click(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.img_asia:
                intent = new Intent(MainActivity.this,Web_View_asia.class);
                startActivity(intent);
                break;

            case R.id.img_europe:
                intent = new Intent(MainActivity.this,Web_View_europe.class);
                startActivity(intent);
                break;
            case R.id.img_africa:
                intent = new Intent(MainActivity.this,Web_View_africa.class);
                startActivity(intent);
                break;

        }

    }


    public void info(View view)
    {
        //Intent intent = new Intent(MainActivity.this,informaton_page.class);
        //startActivity(intent);
    }
}
