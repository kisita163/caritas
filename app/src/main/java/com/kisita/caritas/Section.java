package com.kisita.caritas;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HuguesKi on 01-12-17.
 */

public class Section implements Serializable{
    private String name;
    private ArrayList<Question> questions = new ArrayList<>();

    public Section(String name){
        this.name = name;
    }
    public void addNewQuestion(Question question){
        if(question != null)
            this.questions.add(question);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }
}
