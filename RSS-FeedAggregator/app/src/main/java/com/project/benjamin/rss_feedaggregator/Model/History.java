package com.project.benjamin.rss_feedaggregator.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Benjamin on 29/01/2017.
 */

public class History {
    @SerializedName("bookmarks")
    private ArrayList<String> bookmarks;

    @SerializedName("viewedArticles")
    private ArrayList<String> viewedArticles;

    @SerializedName("starredArticles")
    private ArrayList<String> starredArticles;

    public History() {

    }

    public ArrayList<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<String> getViewedArticles() {
        return viewedArticles;
    }

    public void setViewedArticles(ArrayList<String> viewedArticles) {
        this.viewedArticles = viewedArticles;
    }

    public ArrayList<String> getStarredArticles() {
        return starredArticles;
    }

    public void setStarredArticles(ArrayList<String> starredArticles) {
        this.starredArticles = starredArticles;
    }
}
