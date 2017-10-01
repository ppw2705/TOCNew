package com.geecon.toc.models;

/**
 * Created by MI on 3/16/2017.
 */

public class QuestionModel {

    private String QUESTION_ID;
    private String SURVEY_QUESTION_BANK_ID;
    private String QUESTION_DESC;
    private String QUESTION_INPUT_VALUE_TYPE_ID;
    private String ANSWER_GROUP_ID;
    private String INPUT_OPTION_TYPE_ID;
    private String NO_OF_OPTIONS;
    private String IS_DELETED;
    private String OA_ID;
    private String OA_BRAND_ID;
    private String CREATED_BY;
    private String CREATED_ON;
    private String UPDATED_BY;
    private String UPDATED_ON;

    public String getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(String QUESTION_ID) {
        this.QUESTION_ID = QUESTION_ID;
    }

    public String getSURVEY_QUESTION_BANK_ID() {
        return SURVEY_QUESTION_BANK_ID;
    }

    public void setSURVEY_QUESTION_BANK_ID(String SURVEY_QUESTION_BANK_ID) {
        this.SURVEY_QUESTION_BANK_ID = SURVEY_QUESTION_BANK_ID;
    }

    public String getQUESTION_DESC() {
        return QUESTION_DESC;
    }

    public void setQUESTION_DESC(String QUESTION_DESC) {
        this.QUESTION_DESC = QUESTION_DESC;
    }

    public String getQUESTION_INPUT_VALUE_TYPE_ID() {
        return QUESTION_INPUT_VALUE_TYPE_ID;
    }

    public void setQUESTION_INPUT_VALUE_TYPE_ID(String QUESTION_INPUT_VALUE_TYPE_ID) {
        this.QUESTION_INPUT_VALUE_TYPE_ID = QUESTION_INPUT_VALUE_TYPE_ID;
    }

    public String getANSWER_GROUP_ID() {
        return ANSWER_GROUP_ID;
    }

    public void setANSWER_GROUP_ID(String ANSWER_GROUP_ID) {
        this.ANSWER_GROUP_ID = ANSWER_GROUP_ID;
    }

    public String getINPUT_OPTION_TYPE_ID() {
        return INPUT_OPTION_TYPE_ID;
    }

    public void setINPUT_OPTION_TYPE_ID(String INPUT_OPTION_TYPE_ID) {
        this.INPUT_OPTION_TYPE_ID = INPUT_OPTION_TYPE_ID;
    }

    public String getNO_OF_OPTIONS() {
        return NO_OF_OPTIONS;
    }

    public void setNO_OF_OPTIONS(String NO_OF_OPTIONS) {
        this.NO_OF_OPTIONS = NO_OF_OPTIONS;
    }

    public String getIS_DELETED() {
        return IS_DELETED;
    }

    public void setIS_DELETED(String IS_DELETED) {
        this.IS_DELETED = IS_DELETED;
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
