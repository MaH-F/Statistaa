package com.htbr.statistaa.classes;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;



@Keep //try this for proguard, so firebase database is still readable
@IgnoreExtraProperties
public class Questionnaire implements Serializable {
    private String id;
    private int number;
    private ArrayList<String> questions;
    private String name;



    public Questionnaire() {}

    public Questionnaire(String id, int number, ArrayList<String> questions, String name){
        this.id = id;
        this.number = number;
        this.questions = questions;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }
}


