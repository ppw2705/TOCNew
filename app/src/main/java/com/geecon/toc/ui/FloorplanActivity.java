package com.geecon.toc.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.adapters.Downloader;
import com.geecon.toc.async.ExhibitorListPDFAsync;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class FloorplanActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private WebView pdfWebview;
    private Button downloadBtn;
    private TextView pageHeading;
    private ImageView img_back;
    private ProgressDialog progressDialog;
    final String pdf = "http://www.event24seven.com/upload/floor_plan/TOCAMERICAS2017SALES.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorplan);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressDialog = new ProgressDialog(FloorplanActivity.this);
        progressDialog.setMessage("Loading...");

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("FLOORPLAN");
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
        progressDialog.show();
        downloadBtn = (Button)findViewById(R.id.download_btn);
        pdfWebview = (WebView)findViewById(R.id.web_floorplan);

        String doc="<iframe src='http://docs.google.com/gview?embedded=true&url="+pdf +"' width='100%' height='100%' style='border: none;'></iframe>";
        pdfWebview.getSettings().setJavaScriptEnabled(true);
        //pdfWebview.getSettings().setPluginsEnabled(true);
        pdfWebview.getSettings().setAllowFileAccess(true);
        //pdfWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        //pdfWebview.setWebViewClient(new Callback());
        pdfWebview.loadData( doc , "text/html", "UTF-8");
        progressDialog.cancel();
        //pdfWebview.getSettings().setJavaScriptEnabled(true);
        //pdfWebview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=http://www.tocevents-asia.com/images/Floorplans/2017_ASIA_FLOORPLAN_30.01.17_SALES.pdf");
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(mActivityContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivityContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // String test = "asa";
                        ActivityCompat.requestPermissions(mActivityContext,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(mActivityContext,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    progressDialog.show();
                    try {
                        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdf));

                        // This put the download in the same Download dir the browser uses
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TOCAMERICAS2017SALES.pdf");
                        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS) + "/" + "TOCAMERICAS2017SALES.pdf");
                        if (file != null && file.exists()) {
                            //Toast.makeText(mActivityContext,"Exits",Toast.LENGTH_SHORT).show();
                            Random ramdom = new Random();
                            int i1 = (ramdom.nextInt(80) + 65);
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TOCAMERICAS2017SALES" + String.valueOf(i1) + ".pdf");
                        }
                        // When downloading music and videos they will be listed in the player
                        // (Seems to be available since Honeycomb only)
                        r.allowScanningByMediaScanner();

                        // Notify user when download is completed
                        // (Seems to be available since Honeycomb only)
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        // Start download
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.getMessage());
                        ex.printStackTrace();
                    }
                    progressDialog.cancel();
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    progressDialog.show();
                    try {
                        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdf));

                        // This put the download in the same Download dir the browser uses
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TOCAMERICAS2017SALES.pdf");
                        File file = new File(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS) + "/" + "TOCAMERICAS2017SALES.pdf");
                        if (file != null && file.exists()) {
                            //Toast.makeText(mActivityContext,"Exits",Toast.LENGTH_SHORT).show();
                            Random ramdom = new Random();
                            int i1 = (ramdom.nextInt(80) + 65);
                            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TOCAMERICAS2017SALES" + String.valueOf(i1) + ".pdf");
                        }
                        // When downloading music and videos they will be listed in the player
                        // (Seems to be available since Honeycomb only)
                        r.allowScanningByMediaScanner();

                        // Notify user when download is completed
                        // (Seems to be available since Honeycomb only)
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        // Start download
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.getMessage());
                        ex.printStackTrace();
                    }
                    progressDialog.cancel();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(mAppContext,"Sorry you cant access this functionality without permissions",Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(ExhibitorListActivity.this,EventMainActivity.class);
                    //startActivity(intent);
                    //finish();
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
}
