package com.geecon.toc.models;

import java.util.List;

/**
 * Created by MI on 3/16/2017.
 */

public class SpeakerInfoModel {
    private SpeakerModel speaker_info;
    private List<AllDataModel> session_info;

    public List<AllDataModel> getSession_info() {
        return session_info;
    }

    public SpeakerModel getSpeaker_info() {
        return speaker_info;
    }

    public void setSession_info(List<AllDataModel> session_info) {
        this.session_info = session_info;
    }

    public void setSpeaker_info(SpeakerModel speaker_info) {
        this.speaker_info = speaker_info;
    }
}
