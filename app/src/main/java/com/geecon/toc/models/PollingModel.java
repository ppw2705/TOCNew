package com.geecon.toc.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI on 3/16/2017.
 */

public class PollingModel {
    private List<QuestionModel> allQuestions;
    private List<List<AnswerModel>> allAnswerOptions;

    public List<QuestionModel> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(List<QuestionModel> allQuestions) {
        this.allQuestions = allQuestions;
    }

    public List<List<AnswerModel>> getAllAnswerOptions() {
        return allAnswerOptions;
    }

    public void setAllAnswerOptions(List<List<AnswerModel>> allAnswerOptions) {
        this.allAnswerOptions = allAnswerOptions;
    }
}
