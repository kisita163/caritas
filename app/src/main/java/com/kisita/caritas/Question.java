package com.kisita.caritas;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by HuguesKi on 01-12-17.
 */

public class Question implements Serializable {
    public  enum EntryType{
        CHOICES,
        MULTIPLE_CHOICES,
        NUMERIC,
        TEXT
    }
    private String question;
    private ArrayList<String> choices = new ArrayList<>();
    private String choice = "";

    private int pos  = 0;
    private EntryType mEntryType = EntryType.CHOICES;

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

    public EntryType getEntryType() {
        return mEntryType;
    }

    public void setEntryType(String type) {
        switch (type.toLowerCase()){
            case "numeric":
                this.mEntryType = EntryType.NUMERIC;
                break;
            case "text":
                this.mEntryType = EntryType.TEXT;
                break;
            case "multiple choices":
                this.mEntryType = EntryType.MULTIPLE_CHOICES;
                break;
            case "choices":
                this.mEntryType = EntryType.CHOICES;
                break;
            default:
                this.mEntryType = EntryType.CHOICES;
                break;
        }
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
