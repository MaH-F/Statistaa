package com.htbr.statistaa.ui.login;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Restaurant POJO.
 */
@IgnoreExtraProperties
public class Exercise implements Serializable {
    private String name;
    private String subtitle;
    private String content;
    private int number;


    public Exercise(String name, String subtitle, String content, int number) {
        this.name = name;
        this.content = content;
        this.subtitle = subtitle;
        this.number = number;

    }
    public Exercise() {}

    public String getSubtitle() {
        return subtitle;
    }

    public String getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}


