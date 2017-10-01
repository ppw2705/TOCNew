package com.geecon.toc.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import com.geecon.toc.models.CalendarModel;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by grevin on 05/06/17.
 */

public class CalendarContentResolver {
    public static final String[] FIELDS = {
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
            /*CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE*/
    };

    public static final Uri CALENDAR_URI = CalendarContract.Events.CONTENT_URI;//Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    List<CalendarModel> calendars = new ArrayList<>();

    public  CalendarContentResolver(Context ctx) {
        contentResolver = ctx.getContentResolver();
    }

    public List<CalendarModel> getCalendars() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor;
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            cursor = contentResolver.query(CALENDAR_URI, FIELDS, "Calendars_id=1 and deleted!=1", null, CalendarContract.Events.DTSTART + " ASC");
        } else {*/
            cursor = contentResolver.query(CALENDAR_URI, FIELDS, "deleted!=1", null, CalendarContract.Events.DTSTART + " ASC");
        //}


        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    //String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    //String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                   // Boolean selected = !cursor.getString(3).equals("0");
                    CalendarModel cal = new CalendarModel();
                    cal.setTITLE(cursor.getString(0));
                    cal.setDTSTART(cursor.getString(1));
                    cal.setDTEND(cursor.getString(2));
                    // event id
                    //cal.setEVENT_ID(cursor.getString(cursor.getColumnIndex("_id")));
                    if(!cursor.getString(0).isEmpty()){
                        if(cursor.getString(0).substring(0,3).equals("TOC"))
                            calendars.add(cal);
                    }
                }
            }
        } catch (AssertionError ex) { /*TODO: log exception and bail*/ }

        return calendars;
    }

    public CalendarModel getCalendarsByTitle(String title) {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor;
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            cursor = contentResolver.query(CALENDAR_URI, FIELDS, CalendarContract.Events.TITLE +" = \"TOC - "+ title +"\"  and Calendars_id=1 and deleted!=1", null, CalendarContract.Events.DTSTART + " ASC");
        } else {*/
            cursor = contentResolver.query(CALENDAR_URI, FIELDS, CalendarContract.Events.TITLE +" = \"TOC - "+ title +"\"  and deleted!=1", null, CalendarContract.Events.DTSTART + " ASC");
        //}
        CalendarModel cal = new CalendarModel();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    //String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    //String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    // Boolean selected = !cursor.getString(3).equals("0");
                    cal.setTITLE(cursor.getString(0));
                    cal.setDTSTART(cursor.getString(1));
                    cal.setDTEND(cursor.getString(2));
                    // event id
                    //cal.setEVENT_ID(cursor.getString(cursor.getColumnIndex("_id")));
                }
            }
        } catch (AssertionError ex) { /*TODO: log exception and bail*/ }

        return cal;
    }

    public void DeleteCalendarEntry(String title) {
        try{
            /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                contentResolver.delete(CALENDAR_URI,"Calendars_id=? and "+CalendarContract.Events.TITLE+"=?" ,new String[]{"1",title} );
            } else {*/
              //  contentResolver.delete(CALENDAR_URI,CalendarContract.Events.CALENDAR_ID+"=? and "+CalendarContract.Events.TITLE+"=?" ,new String[]{"1",title} );
            //}
            contentResolver.delete(CALENDAR_URI,CalendarContract.Events.CALENDAR_ID+"=? and "+CalendarContract.Events.TITLE+"=?" ,new String[]{"1",title} );
        } catch(Exception ex){
            ex.printStackTrace();
            Log.e("CALENDAR",ex.getMessage());
        }

    }
}
