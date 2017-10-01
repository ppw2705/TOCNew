package com.geecon.toc.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geecon.toc.R;
import com.geecon.toc.async.PollingSubmitAsync;
import com.geecon.toc.interfaces.PollingSubmitInterface;
import com.geecon.toc.models.AnswerModel;
import com.geecon.toc.models.PollingModel;
import com.geecon.toc.models.QuestionModel;
import com.geecon.toc.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PollingActivity extends AppCompatActivity implements PollingSubmitInterface{

    private Activity mActivityContext;
    private Context mAppContext;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navDrawerHeaderView;
    private TextView pageHeading;
    private ImageView img_back;
    private LinearLayout ll_polling;
    private PollingSubmitInterface pollingSubmitInterface;
    private int i = 0;
    private String[] ansStringListglobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);

        mActivityContext = this;
        mAppContext = getApplicationContext();
        pollingSubmitInterface = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pageHeading = (TextView)toolbar.findViewById(R.id.txt_heading);
        pageHeading.setText("POLLS");
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

        ll_polling = (LinearLayout)findViewById(R.id.ll_polling);

        String result_list = getIntent().getStringExtra("POLLING_DATA");

        try {
            PollingModel pollingModel = new Gson().fromJson(result_list,
                    new TypeToken<PollingModel>() {
                    }.getType());

            if(pollingModel.getAllQuestions().size() == 0){
                Intent intent = new Intent(PollingActivity.this,PollingBlankActivity.class);
                startActivity(intent);
                finish();
            }

            for (QuestionModel questionModel :
                    pollingModel.getAllQuestions()) {
                final View QuestionsView = LayoutInflater.from(mActivityContext).inflate(R.layout.single_polling, null, false);
                final TextView txt_question = (TextView) QuestionsView.findViewById(R.id.txt_ques);
                txt_question.setTag(questionModel.getQUESTION_ID());
                txt_question.setText(questionModel.getQUESTION_DESC());
                final Button submit_poll = (Button)QuestionsView.findViewById(R.id.submit_poll);
                //final RadioGroup radioGroup = (RadioGroup) QuestionsView.findViewById(R.id.radioGroup);
                final LinearLayout relAnsGroup = (LinearLayout) QuestionsView.findViewById(R.id.relAnsGroup);
                final TextView txt_ques_ans = (TextView) QuestionsView.findViewById(R.id.txt_ques_ans);
                final List<RadioButton> radioButtonList = new ArrayList<>();
                String[] ansStringList = new String[pollingModel.getAllAnswerOptions().get(i).size()];
                ansStringListglobal = ansStringList;
                int j = 0;

                for (AnswerModel answerModel:
                     pollingModel.getAllAnswerOptions().get(i)) {
                    ansStringList[j++] = answerModel.getINPUT_OPTION_DESC();
                    View AnswersView = LayoutInflater.from(mActivityContext).inflate(R.layout.single_polling_ans, null, false);
                    /*TextView txt_ans = (TextView) AnswersView.findViewById(R.id.txt_ans);
                    txt_ans.setText(answerModel.getINPUT_OPTION_DESC());*/
                    //final RadioButton radio_ans = (RadioButton) AnswersView.findViewById(R.id.radio);
                    final EditText radio_ans = (EditText) AnswersView.findViewById(R.id.radio);
                    //radio_ans.setTag(Integer.parseInt(answerModel.getSURVEY_ANSWERS_OPTIONS_ID()));
                    //radioButtonList.add(radio_ans);
                    RelativeLayout rel_ans = (RelativeLayout)AnswersView.findViewById(R.id.rel_ans);
                    final String[] ansStringListpassed = ansStringListglobal;
                    rel_ans.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*for (RadioButton single_radio:
                                 radioButtonList) {
                                if(radio_ans.getTag()!=single_radio.getTag()){
                                    single_radio.setChecked(false);
                                } else {
                                    single_radio.setChecked(true);
                                    txt_ques_ans.setText(txt_question.getTag()+"_"+single_radio.getTag());
                                }

                            }*/
                            AppUtils.hideKeyboard(PollingActivity.this);
                            if (ansStringListpassed != null) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PollingActivity.this);
                                dialogBuilder.setTitle("Select Preference");
                                dialogBuilder.setItems(ansStringListpassed, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int k) {
                                        radio_ans.setText(ansStringListpassed[k]);
                                        List<String> ansList = new ArrayList<String>();
                                        ansList.addAll(Arrays.asList(ansStringListglobal));
                                        ansList.remove(k);
                                        ansStringListglobal = (String[])ansList.toArray();
                                    }
                                });
                                AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.show();
                            }
                        }
                    });

                    radio_ans.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*for (RadioButton single_radio:
                                    radioButtonList) {
                                if(radio_ans.getTag()!=single_radio.getTag()){
                                    single_radio.setChecked(false);
                                } else {
                                    single_radio.setChecked(true);
                                    txt_ques_ans.setText(txt_question.getTag()+"_"+single_radio.getTag());
                                }

                            }*/
                            AppUtils.hideKeyboard(PollingActivity.this);
                            if (ansStringListpassed != null) {
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PollingActivity.this);
                                dialogBuilder.setTitle("Please Select Preference");
                                dialogBuilder.setItems(ansStringListpassed, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int k) {
                                        radio_ans.setText(ansStringListpassed[k]);
                                        List<String> ansList = new ArrayList<String>();
                                        ansList.addAll(Arrays.asList(ansStringListglobal));
                                        ansList.remove(k);
                                        ansStringListglobal = (String[])ansList.toArray();
                                    }
                                });
                                AlertDialog alertDialog = dialogBuilder.create();
                                alertDialog.show();
                            }
                        }
                    });

                    //radioGroup.addView(AnswersView);
                    relAnsGroup.addView(AnswersView);
                }
                submit_poll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txt_ques_ans.getText().equals("")){
                            Toast.makeText(mAppContext,"Please select an option before submitting",Toast.LENGTH_SHORT).show();
                        } else {
                            if (!AppUtils.isNetworkAvailable(mActivityContext)) {
                                Toast.makeText(mActivityContext, getApplicationContext().getString(R.string.no_network_toast), Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(mAppContext,txt_ques_ans.getText(),Toast.LENGTH_SHORT).show();
                                Animation a = AnimationUtils.loadAnimation(
                                        mActivityContext, android.R.anim.fade_out);
                                a.setDuration(1000);
                                a.setAnimationListener(new Animation.AnimationListener() {

                                    public void onAnimationEnd(Animation animation) {
                                        // Do what ever you need, if not remove it.
                                        QuestionsView.setVisibility(View.GONE);

                                        String imeistring = Settings.Secure.getString(mActivityContext.getContentResolver(),
                                                Settings.Secure.ANDROID_ID);

                                        //String imeistring = null;

                                        TelephonyManager telephonyManager = (TelephonyManager
                                                ) getSystemService(Context.TELEPHONY_SERVICE);
                                    /*
                                     * getDeviceId() function Returns the unique device ID.
                                     * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
                                     */
                                        //imeistring = telephonyManager.getDeviceId();
                                        //Toast.makeText(mAppContext,txt_ques_ans.getText()+"_"+imeistring,Toast.LENGTH_SHORT).show();
                                        new PollingSubmitAsync(pollingSubmitInterface).execute(txt_ques_ans.getText() + "_" + imeistring);
                                    }

                                    public void onAnimationRepeat(Animation animation) {
                                        // Do what ever you need, if not remove it.
                                    }

                                    public void onAnimationStart(Animation animation) {
                                        // Do what ever you need, if not remove it.
                                    }

                                });
                                QuestionsView.startAnimation(a);
                            }
                        }
                    }
                });

                ll_polling.addView(QuestionsView);
                i++;
            }
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
    public void pollingSubmitResponse(String response) {
        i--;
        if(i <= 0){
            Intent intent = new Intent(mActivityContext,thankyou.class);
            intent.putExtra("TITLE","Thank You For Completing Our Survey!");
            intent.putExtra("DESC","Thank you for taking time out to participate in our survey. We truly value the information you have provided. Your responses are vital in helping TOC to provide a better experience that meets the highest standards of excellence. ");
            startActivity(intent);
        }

        Toast.makeText(mAppContext,"Thank you for your response",Toast.LENGTH_SHORT).show();
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

    public void thanks(View view)
    {
        Intent intent = new Intent(PollingActivity.this,thankyou.class);
        intent.putExtra("TITLE","Thank You For Completing Our Survey!");
        intent.putExtra("DESC","Thank you for taking time out to participate in our survey. We truly value the information you have provided. Your responses are vital in helping TOC to provide a better experience that meets the highest standards of excellence. ");
        startActivity(intent);
    }
}
