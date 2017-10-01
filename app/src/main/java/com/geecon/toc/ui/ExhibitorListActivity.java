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
import com.geecon.toc.adapters.ExhibitorNewAdapter;
import com.geecon.toc.async.ExhibitorListAsync;
import com.geecon.toc.async.ExhibitorListPDFAsync;
import com.geecon.toc.interfaces.ExhibitorListInterface;
import com.geecon.toc.interfaces.ExhibitorListPDFInterface;
import com.geecon.toc.models.ExhibitorModel;
import com.geecon.toc.utils.AppPreferences;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viethoa.RecyclerViewFastScroller;
import com.viethoa.models.AlphabetItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExhibitorListActivity extends AppCompatActivity implements ExhibitorListInterface,ExhibitorListPDFInterface {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private RecyclerView recyclerViewExhibitorList;
    private RecyclerViewFastScroller recyclerViewFastScroller;
    private ExhibitorNewAdapter exhibitorAdapter;
    private ExhibitorListInterface exhibitorListInterface;
    private ExhibitorListPDFInterface exhibitorListPDFInterface;
    private List<ExhibitorModel> masterExhibitorList;
    //private List<SpeakerModel> grandMasterSpeakerList;
    //private OnLoadMoreListener onLoadMoreListener;
    private ProgressDialog progress;
    private int page = 1;
    private TextView pageHeading;
    private ImageView img_back,ex_download,img_fav;
    private ProgressDialog progressDialog;
    private List<AlphabetItem> mAlphabetItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_list);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);

        mActivityContext = this;
        exhibitorListInterface = this;
        exhibitorListPDFInterface = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("EXHIBITOR LIST");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        ex_download = (ImageView)toolbar.findViewById(R.id.img_ex_download);
        img_fav = (ImageView)toolbar.findViewById(R.id.img_ex_fav);

        progressDialog = new ProgressDialog(ExhibitorListActivity.this);
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

        //Log.e("ERROR", "onCreate: "+AppPreferences.getInstance().getConfData().getCONF_ID());

        if (!AppUtils.isNetworkAvailable(mActivityContext)) {
            Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            //new ExhibitorListAsync(exhibitorListInterface).execute(AppPreferences.getInstance().getConfData().getCONF_ID());
            new ExhibitorListAsync(exhibitorListInterface).execute("2");
        }

        masterExhibitorList = new ArrayList<>();
        recyclerViewExhibitorList = (RecyclerView) findViewById(R.id.recycler_view_exhibitor_list);
        recyclerViewFastScroller = (RecyclerViewFastScroller) findViewById(R.id.fast_scroller) ;
        exhibitorAdapter = new ExhibitorNewAdapter(mActivityContext,masterExhibitorList, new ExhibitorNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ExhibitorModel item) {
                //To pass the data to ExhibitorInfo page
                Intent intent = new Intent(mActivityContext,ExhibitionInfoActivity.class);
                intent.putExtra("EXHIBITION_INFO",new Gson().toJson(item));
                //intent.putExtra("SESSION",session_name);
                startActivity(intent);
            }

            @Override
            public void onImageClick(ImageView view,ExhibitorModel item) {
                //ExhibitorID
                boolean exits = false;
                List<ExhibitorModel> exhibitorModelList = AppPreferences.getInstance().getExhibitorData();
                if(exhibitorModelList == null){
                    exhibitorModelList = new ArrayList<>();
                } else {
                    if(exhibitorModelList.contains(item))
                        exits = true;
                }

                if(exits){
                    exhibitorModelList.remove(item);
                    AppPreferences.getInstance().setExhibitorData(new Gson().toJson(exhibitorModelList));
                    view.setImageResource(R.drawable.star);
                } else {
                    exhibitorModelList.add(item);
                    AppPreferences.getInstance().setExhibitorData(new Gson().toJson(exhibitorModelList));
                    view.setImageResource(R.drawable.star_checked);
                }
                //exhibitorModelList = AppPreferences.getInstance().getExhibitorData();
                //Log.e("EX",new Gson().toJson(exhibitorModelList));
            }

            @Override
            public void onRemoveClick(View v, ExhibitorModel item) {

            }
        });

        final GridLayoutManager layoutManager = new GridLayoutManager(ExhibitorListActivity.this, 1);
        recyclerViewExhibitorList.setLayoutManager(layoutManager);
        recyclerViewExhibitorList.setAdapter(exhibitorAdapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });

        ex_download.setVisibility(View.VISIBLE);
        ex_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                    Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                } else {

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
                        new ExhibitorListPDFAsync(exhibitorListPDFInterface).execute("2");
                    }
                }
            }
        });

        img_fav.setVisibility(View.VISIBLE);
        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExhibitorListActivity.this,FavExhibitorActivity.class);
                startActivity(intent);
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
                    new ExhibitorListPDFAsync(exhibitorListPDFInterface).execute("2");

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

    @Override
    protected void onResume() {
        super.onResume();
        exhibitorAdapter.clearList();
        exhibitorAdapter.removeNullLastItem();
        exhibitorAdapter.addSpeaker(masterExhibitorList);
        exhibitorAdapter.notifyDataSetChanged();
    }

    @Override
    public void exhibitorListResponse(String response) {
        if(response.isEmpty()){
            Toast.makeText(mActivityContext,"Please try again",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        try {
            exhibitorAdapter.removeNullLastItem();
            List<ExhibitorModel> exhibitorModels = new Gson().fromJson(response,
                    new TypeToken<List<ExhibitorModel>>() {
                    }.getType());

            masterExhibitorList.addAll(exhibitorModels);
            exhibitorAdapter.addSpeaker(masterExhibitorList);
            initialiseData();
            recyclerViewFastScroller.setRecyclerView(recyclerViewExhibitorList);
            recyclerViewFastScroller.setUpAlphabet(mAlphabetItems);
            progressDialog.cancel();
        } catch(Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void exhibitorListPDFResponse(String response) {
        if(response.isEmpty()){
            Toast.makeText(mActivityContext,"Please try again",Toast.LENGTH_SHORT).show();
        }

        try {
            String URL = response;
            DownloadManager.Request r = new DownloadManager.Request(Uri.parse(URL));

            // This put the download in the same Download dir the browser uses
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Exhibitor_list.pdf");
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS)+ "/"+"Exhibitor_list.pdf");
            if(file != null && file.exists()){
                //Toast.makeText(mActivityContext,"Exits",Toast.LENGTH_SHORT).show();
                Random ramdom = new Random();
                int i1 = (ramdom.nextInt(80) + 65);
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Exhibitor_list_"+String.valueOf(i1)+".pdf");
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
            progressDialog.cancel();
        } catch(Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected void initialiseData() {
        //Recycler view data
        //mDataArray = DataHelper.getAlphabetData();

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < masterExhibitorList.size(); i++) {
            String name = masterExhibitorList.get(i).getEXHIBITOR_NAME();
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
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
