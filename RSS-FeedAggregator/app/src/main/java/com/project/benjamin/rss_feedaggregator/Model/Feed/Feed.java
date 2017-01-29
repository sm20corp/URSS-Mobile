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

    @SerializedName("_id")
    private String _id;

    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

