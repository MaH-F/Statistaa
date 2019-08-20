package com.htbr.statistaa.ui.login;
import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Restaurant POJO.
 */
@IgnoreExtraProperties
public class Exercise {
    private String name;


    public Exercise() {}

    public Exercise(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}


