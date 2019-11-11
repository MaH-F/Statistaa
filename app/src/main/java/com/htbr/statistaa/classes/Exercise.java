package com.htbr.statistaa.classes;
import androidx.annotation.Keep;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;


@Keep //try this for proguard, so firebase database is still readable
@IgnoreExtraProperties
public class Exercise implements Serializable {
    private String id;
    private String name;
    private String subtitle;
    private String content;
    private int number;
    private String html;
    private String solution;
    private String url;
    private String statement;
    private String hint;
    private String type;

    //this is only for "offline"
    private int box;




    public Exercise(String id, String name, String subtitle, String content, int number, String html, String solution, String url, String statement, String hint, String type) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.subtitle = subtitle;
        this.number = number;
        this.html = html;
        this.solution = solution;
        this.url = url;
        this.statement = statement;
        this.hint = hint;
        this.type = type;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


