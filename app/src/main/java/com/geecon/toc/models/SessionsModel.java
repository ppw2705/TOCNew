package com.geecon.toc.models;

import java.util.List;

/**
 * Created by MI on 3/14/2017.
 */

public class SessionsModel {
    private AllDataModel all;
    private List<SpeakerModel> speaker;

    public AllDataModel getAll() {
        return all;
    }

    public List<SpeakerModel> getSpeaker() {
        return speaker;
    }

    public void setAll(AllDataModel all) {
        this.all = all;
    }

    public void setSpeaker(List<SpeakerModel> speaker) {
        this.speaker = speaker;
    }
}
