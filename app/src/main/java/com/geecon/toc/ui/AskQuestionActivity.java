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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.async.AskQuestionAsync;
import com.geecon.toc.interfaces.AskQuestionInterface;

public class AskQuestionActivity extends AppCompatActivity implements AskQuestionInterface{

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading;
    private ImageView img_back;
    private EditText edt_first_name,edt_surname,edt_company_name,edt_session,edt_session_id,edt_info;
    private Button btn_submit;
    private AskQuestionInterface askQuestionInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        mActivityContext = this;
        askQuestionInterface = this;
        mAppContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navDrawerHeaderView = navigationView.getHeaderView(0);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("ASK A QUESTION");
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        edt_first_name = (EditText)findViewById(R.id.edt_first_name);
        edt_surname = (EditText)findViewById(R.id.edt_surname);
        edt_company_name = (EditText)findViewById(R.id.edt_company_name);
        edt_session_id = (EditText)findViewById(R.id.edt_session_id);
        edt_session = (EditText)findViewById(R.id.edt_session);
        edt_info = (EditText)findViewById(R.id.edt_info);
        btn_submit = (Button)findViewById(R.id.btn_submit);


        String session_id = getIntent().getStringExtra("SESSION_ID");
        String session = getIntent().getStringExtra("SESSION");

        edt_session.setText(session);
        edt_session_id.setText(session_id);

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

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //return true;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_info.getText().toString().equals("")){
                    Toast.makeText(mAppContext,"Please enter all details",Toast.LENGTH_SHORT).show();
                    return;
                }
                new AskQuestionAsync(askQuestionInterface).execute(edt_first_name.getText().toString(),edt_company_name.getText().toString(),edt_session_id.getText().toString(),edt_session.getText().toString(),edt_info.getText().toString());
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
    public void askQResponse(String response) {
        Intent intent = new Intent(mActivityContext,thankyou.class);
        intent.putExtra("TITLE","Thank you for your comments");
        intent.putExtra("DESC","We will endeavour to put as many questions as possible to the panel.");
        startActivity(intent);
    }
}
