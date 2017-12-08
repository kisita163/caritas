package com.kisita.caritas;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by HuguesKi on 01-12-17.
 */

public class Question implements Serializable {
    public  enum AnswerType {
        CHOICES,
        MULTIPLE_CHOICES,
        NUMERIC,
        TEXT
    }
    private boolean mandatory         = false;
    private String  question          = "";
    private ArrayList<String> choices = new ArrayList<>();
    private String choice             = "";
    private String comment            = "";
    private int pos                   = 0;
    private AnswerType mAnswerType    = AnswerType.CHOICES;

    public Question(String question) {
        this.question = question;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void addChoice(String choice) {
        this.choices.add(choice);
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public String getChoice() {
        return choice;
    }

    public AnswerType getEntryType() {
        return mAnswerType;
    }

    public void setEntryType(String type) {
        switch (type.toLowerCase()){
            case "numeric":
                this.mAnswerType = AnswerType.NUMERIC;
                break;
            case "text":
                this.mAnswerType = AnswerType.TEXT;
                break;
            case "multiple choices":
                this.mAnswerType = AnswerType.MULTIPLE_CHOICES;
                break;
            case "choices":
                this.mAnswerType = AnswerType.CHOICES;
                break;
            default:
                this.mAnswerType = AnswerType.CHOICES;
                break;
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
