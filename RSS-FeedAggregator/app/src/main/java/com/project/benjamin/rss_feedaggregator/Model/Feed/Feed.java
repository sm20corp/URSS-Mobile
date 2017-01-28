package com.project.benjamin.rss_feedaggregator.Model.Feed;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Benjamin on 28/01/2017.
 */

public class Feed {
    @SerializedName("url")
    private String url;

    @SerializedName("id")
    private String id;

    @SerializedName("articles")
    private ArrayList<String> articles;

    public Feed() {
    }

    public Feed(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<String> articles) {
        this.articles = articles;
    }
}

