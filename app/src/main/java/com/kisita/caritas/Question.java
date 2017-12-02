package com.kisita.caritas;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by HuguesKi on 01-12-17.
 */

public class Question implements Serializable {

    private String question;
    private ArrayList<String> choices = new ArrayList<>();
    private String choice;

    public Question(String question) {
        this.question = question;
    }

    public void setChoice(String choice) {
        if(choices.contains(choice)){
            this.choice = choice;
        }
        //TODO
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
}
