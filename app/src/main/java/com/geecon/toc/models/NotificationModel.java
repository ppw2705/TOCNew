package com.geecon.toc.models;

/**
 * Created by MI on 3/22/2017.
 */

public class NotificationModel {
    private String ID;
    private String TITLE;
    private String MESSAGE;
    private String OA_ID;
    private String OA_BRAND_ID;
    private String USER_ID;
    private String IS_DELETED;
    private String CREATED_BY;
    private String CREATED_ON;
    private String UPDATED_BY;
    private String UPDATED_ON;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getOA_ID() {
        return OA_ID;
    }

    public void setOA_ID(String OA_ID) {
        this.OA_ID = OA_ID;
    }

    public String getOA_BRAND_ID() {
        return OA_BRAND_ID;
    }

    public void setOA_BRAND_ID(String OA_BRAND_ID) {
        this.OA_BRAND_ID = OA_BRAND_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getIS_DELETED() {
        return IS_DELETED;
    }

    public void setIS_DELETED(String IS_DELETED) {
        this.IS_DELETED = IS_DELETED;
    }

    public String getCREATED_BY() {
        return CREATED_BY;
    }

    public void setCREATED_BY(String CREATED_BY) {
        this.CREATED_BY = CREATED_BY;
    }

    public String getCREATED_ON() {
        return CREATED_ON;
    }

    public void setCREATED_ON(String CREATED_ON) {
        this.CREATED_ON = CREATED_ON;
    }

    public String getUPDATED_BY() {
        return UPDATED_BY;
    }

    public void setUPDATED_BY(String UPDATED_BY) {
        this.UPDATED_BY = UPDATED_BY;
    }

    public String getUPDATED_ON() {
        return UPDATED_ON;
    }

    public void setUPDATED_ON(String UPDATED_ON) {
        this.UPDATED_ON = UPDATED_ON;
    }
}
