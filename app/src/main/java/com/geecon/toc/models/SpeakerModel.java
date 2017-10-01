package com.geecon.toc.models;

import android.content.Intent;

/**
 * Created by MI on 2/9/2017.
 */

public class SpeakerModel {
    private String SS_ID;
    private String SESSION_ID;
    private String SPEAKER_ID;
    private String SPEAKER_NAME;
    private String COMPANY_NAME;
    private String DESIGNATION;
    private String DESCRIPTION;
    private String LOCATION;
    private String IMAGE;
    private String name;
    private String title;
    private String division;
    private Integer image;

    public String getSESSION_ID() {
        return SESSION_ID;
    }

    public String getSS_ID() {
        return SS_ID;
    }

    public String getSPEAKER_ID() {
        return SPEAKER_ID;
    }

    public String getSPEAKER_NAME() {
        return SPEAKER_NAME;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public void setSESSION_ID(String SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public void setSS_ID(String SS_ID) {
        this.SS_ID = SS_ID;
    }

    public void setSPEAKER_ID(String SPEAKER_ID) {
        this.SPEAKER_ID = SPEAKER_ID;
    }

    public void setSPEAKER_NAME(String SPEAKER_NAME) {
        this.SPEAKER_NAME = SPEAKER_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDivision() {
        return division;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
