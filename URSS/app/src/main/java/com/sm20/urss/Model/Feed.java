package com.sm20.urss.Model;

/**
 * Created by Maxence on 13/01/2017.
 */

public class Feed {
    private String title, description;

    public Feed() {
    }

    public Feed(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
