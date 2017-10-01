package com.geecon.toc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.geecon.toc.interfaces.ConferenceDetailsInterface;
import com.geecon.toc.models.ConferenceDetailsModel;
import com.geecon.toc.models.ExhibitorModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by MI on 3/11/2017.
 */

public class AppPreferences {
    private static final String TAG = AppPreferences.class.getSimpleName();
    private static AppPreferences ourInstance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String CONF_DETAILS = "conf_details";
    public static final String EXHIBITOR_DETAILS = "ex_details";
    public static final String NOTIFY_COUNT = "notify_count";

    private AppPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void registerSharedPreferenceListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }
    public static void init(Context context) {
        ourInstance = new AppPreferences(context);
    }

    public static AppPreferences getInstance() {
        return ourInstance;
    }

    public void clearAppPref() {
        editor.clear().apply();
    }

    public void remove(String key) {
        editor.remove(key).apply();
    }

    public ConferenceDetailsModel getConfData() {
        String confData = sharedPreferences.getString(CONF_DETAILS, "");
        if(confData.isEmpty()) {
            return null;
        }

        List<ConferenceDetailsModel> conferenceDetailsModel = new Gson().fromJson(confData, new TypeToken<List<ConferenceDetailsModel>>(){}.getType());
        return conferenceDetailsModel.get(0);
    }

    public void setConfData(String confData) {
        editor.putString(CONF_DETAILS, confData);
        editor.apply();
    }

    public List<ExhibitorModel> getExhibitorData() {
        String exData = sharedPreferences.getString(EXHIBITOR_DETAILS, "");
        if(exData.isEmpty()) {
            return null;
        }

        List<ExhibitorModel> exDetailsModel = new Gson().fromJson(exData, new TypeToken<List<ExhibitorModel>>(){}.getType());
        return exDetailsModel;
    }

    public void setExhibitorData(String exData) {
        editor.putString(EXHIBITOR_DETAILS, exData);
        editor.apply();
    }

    public String getNotifyCount() {
        String notifyData = sharedPreferences.getString(NOTIFY_COUNT, "");
        if(notifyData.isEmpty()) {
            return null;
        }
        return notifyData;
    }

    public void setNotifyCount(String notifyData) {
        editor.putString(NOTIFY_COUNT, notifyData);
        editor.apply();
    }

    public void clearConfData() {
        AppPreferences.getInstance().remove(AppPreferences.CONF_DETAILS);
    }
}
