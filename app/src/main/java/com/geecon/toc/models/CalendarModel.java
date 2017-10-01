package com.geecon.toc.models;

/**
 * Created by grevin on 05/06/17.
 */

public class CalendarModel {
    private String TITLE;
    private String DTSTART;
    private String DTEND;
    private String DATE;
    private String TIME;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDTSTART() {
        return DTSTART;
    }

    public void setDTSTART(String DTSTART) {
        this.DTSTART = DTSTART;
    }

    public String getDTEND() {
        return DTEND;
    }

    public void setDTEND(String DTEND) {
        this.DTEND = DTEND;
    }
}
