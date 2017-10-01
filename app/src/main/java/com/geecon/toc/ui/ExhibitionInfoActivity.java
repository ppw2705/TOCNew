package com.geecon.toc.ui;

import android.app.Activity;
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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.geecon.toc.models.ExhibitorModel;
import com.geecon.toc.models.ProductModel;
import com.geecon.toc.models.SessionsModel;
import com.geecon.toc.models.SpeakerInfoModel;
import com.geecon.toc.models.SpeakerModel;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExhibitionInfoActivity extends AppCompatActivity {

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading,ex_name,ex_stand,press_release_title,products_title;
    private LinearLayout product_list;
    private ExhibitorModel exhibitorModel;
    private HorizontalScrollView horizontalScrollView;
    private ImageView img_back,img_ex;
    private DocumentView ex_desc,ex_press_release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_info);

        mActivityContext = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String result = getIntent().getStringExtra("EXHIBITION_INFO");
        try {
            exhibitorModel = new Gson().fromJson(result,
                    new TypeToken<ExhibitorModel>() {
                    }.getType());
        } catch (Exception ex){
            ex.printStackTrace();
        }

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        String heading = exhibitorModel.getEXHIBITOR_NAME().replace("&amp;","&");
        pageHeading.setText("EXHIBITOR INFO");//heading
        /*String[] headArray = heading.split(" ");
        if(headArray.length>2){
            int offset = heading.indexOf(" ", heading.indexOf(" ") + 1);
            pageHeading.setText(Html.fromHtml(heading.substring(0,offset)+"..."));
        }*/

        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        ex_name = (TextView)findViewById(R.id.txt_ex_name);
        ex_stand = (TextView)findViewById(R.id.txt_stand_name);
        press_release_title = (TextView)findViewById(R.id.press_release_title);
        products_title = (TextView)findViewById(R.id.products_list_title);
        ex_desc = (DocumentView)findViewById(R.id.txt_ex_info);
        ex_press_release = (DocumentView)findViewById(R.id.txt_ex_press_release);
        img_ex = (ImageView)findViewById(R.id.img_exhibitor);
        product_list = (LinearLayout)findViewById(R.id.products_list);
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
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

        ex_name.setText(Html.fromHtml(exhibitorModel.getEXHIBITOR_NAME().replace("&amp;","&")));
        ex_stand.setText(exhibitorModel.getBOOTH());
        if(exhibitorModel.getDESCRIPTION()!=null){
            ex_desc.setText(Html.fromHtml(exhibitorModel.getDESCRIPTION()));
        } else {
            ex_desc.setVisibility(View.GONE);
        }

        if(exhibitorModel.getPRESS_RELEASE()!=null && exhibitorModel.getPRESS_RELEASE() != ""){
            ex_press_release.setText(Html.fromHtml(exhibitorModel.getPRESS_RELEASE()));
        } else {
            press_release_title.setVisibility(View.GONE);
            ex_press_release.setVisibility(View.GONE);
        }

        if (exhibitorModel.getEPIC_FILENAME() != null && exhibitorModel.getEPIC_FILENAME() != "") {
            Picasso.with(mActivityContext).load(exhibitorModel.getEPIC_FILENAME()).placeholder(R.drawable.pro).into(img_ex);
        }

        if (exhibitorModel.getPRODUCTS().size() == 0) {
            products_title.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);
        } else {
            for (ProductModel single_product :
                    exhibitorModel.getPRODUCTS()) {
                View ProductView = LayoutInflater.from(mActivityContext).inflate(R.layout.single_product, null, false);
                TextView txt_name = (TextView) ProductView.findViewById(R.id.product_name);
                txt_name.setText(single_product.getPRODUCT_NAME());
                ImageView imgProduct = (ImageView) ProductView.findViewById(R.id.product_imv);
                if (single_product.getIMAGE() != null && single_product.getIMAGE() != "") {
                    Picasso.with(mActivityContext).load(single_product.getIMAGE()).placeholder(R.drawable.pro).resize(200, 200).into(imgProduct);
                }
                product_list.addView(ProductView);
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
