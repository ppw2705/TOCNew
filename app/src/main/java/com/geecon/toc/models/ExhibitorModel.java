package com.geecon.toc.models;

import java.util.List;

/**
 * Created by MI on 3/21/2017.
 */

public class ExhibitorModel {

    private String EXHIBITOR_ID;
    private String EXHIBITOR_NAME;
    private String BOOTH;
    private String DESCRIPTION;
    private String CONTACT;
    private String IS_SPONSORED;
    private String FACEBOOK;
    private String LINKEDIN;
    private String TWITTER;
    private String URL;
    private String PRESS_RELEASE;
    private String EPIC_PICTURE;
    private String EPIC_FILENAME;
    private String EPIC_TYPE;
    private String EPIC_FILE_SIZE;
    private String EPIC_FILE_WIDTH;
    private String EPIC_FILE_HEIGHT;
    private String OA_ID;
    private String OA_BRAND_ID;
    private String USER_ID;
    private String IS_DELETED;
    private String CREATED_BY;
    private String CREATED_ON;
    private String UPDATED_BY;
    private String UPDATED_ON;
    private List<ProductModel> PRODUCTS;

    public List<ProductModel> getPRODUCTS() {
        return PRODUCTS;
    }

    public void setPRODUCTS(List<ProductModel> PRODUCTS) {
        this.PRODUCTS = PRODUCTS;
    }

    public String getEXHIBITOR_ID() {
        return EXHIBITOR_ID;
    }

    public void setEXHIBITOR_ID(String EXHIBITOR_ID) {
        this.EXHIBITOR_ID = EXHIBITOR_ID;
    }

    public String getEXHIBITOR_NAME() {
        return EXHIBITOR_NAME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getIS_SPONSORED() {
        return IS_SPONSORED;
    }

    public void setIS_SPONSORED(String IS_SPONSORED) {
        this.IS_SPONSORED = IS_SPONSORED;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCONTACT() {
        return CONTACT;
    }

    public void setCONTACT(String CONTACT) {
        this.CONTACT = CONTACT;
    }

    public String getFACEBOOK() {
        return FACEBOOK;
    }

    public void setFACEBOOK(String FACEBOOK) {
        this.FACEBOOK = FACEBOOK;
    }

    public String getLINKEDIN() {
        return LINKEDIN;
    }

    public void setLINKEDIN(String LINKEDIN) {
        this.LINKEDIN = LINKEDIN;
    }

    public String getTWITTER() {
        return TWITTER;
    }

    public void setTWITTER(String TWITTER) {
        this.TWITTER = TWITTER;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPRESS_RELEASE() {
        return PRESS_RELEASE;
    }

    public void setPRESS_RELEASE(String PRESS_RELEASE) {
        this.PRESS_RELEASE = PRESS_RELEASE;
    }

    public void setEXHIBITOR_NAME(String EXHIBITOR_NAME) {
        this.EXHIBITOR_NAME = EXHIBITOR_NAME;
    }

    public String getBOOTH() {
        return BOOTH;
    }

    public void setBOOTH(String BOOTH) {
        this.BOOTH = BOOTH;
    }

    public String getEPIC_PICTURE() {
        return EPIC_PICTURE;
    }

    public void setEPIC_PICTURE(String EPIC_PICTURE) {
        this.EPIC_PICTURE = EPIC_PICTURE;
    }

    public String getEPIC_FILENAME() {
        return EPIC_FILENAME;
    }

    public void setEPIC_FILENAME(String EPIC_FILENAME) {
        this.EPIC_FILENAME = EPIC_FILENAME;
    }

    public String getEPIC_TYPE() {
        return EPIC_TYPE;
    }

    public void setEPIC_TYPE(String EPIC_TYPE) {
        this.EPIC_TYPE = EPIC_TYPE;
    }

    public String getEPIC_FILE_SIZE() {
        return EPIC_FILE_SIZE;
    }

    public void setEPIC_FILE_SIZE(String EPIC_FILE_SIZE) {
        this.EPIC_FILE_SIZE = EPIC_FILE_SIZE;
    }

    public String getEPIC_FILE_WIDTH() {
        return EPIC_FILE_WIDTH;
    }

    public void setEPIC_FILE_WIDTH(String EPIC_FILE_WIDTH) {
        this.EPIC_FILE_WIDTH = EPIC_FILE_WIDTH;
    }

    public String getEPIC_FILE_HEIGHT() {
        return EPIC_FILE_HEIGHT;
    }

    public void setEPIC_FILE_HEIGHT(String EPIC_FILE_HEIGHT) {
        this.EPIC_FILE_HEIGHT = EPIC_FILE_HEIGHT;
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


    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ExhibitorModel))
            return false;
        ExhibitorModel u = (ExhibitorModel) obj;
        return this.EXHIBITOR_ID == null ? false : this.EXHIBITOR_ID
                .equals(u.EXHIBITOR_ID);
    }

    @Override
    public int hashCode() {
        return this.EXHIBITOR_ID == null ? 0 : this.EXHIBITOR_ID.hashCode();
    }


}
