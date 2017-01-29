package com.project.benjamin.rss_feedaggregator.Model.Articles;

/**
 * Created by Benjamin on 29/01/2017.
 */

public class ArticleRequest {
    private String viewedArticles;

    public ArticleRequest(String viewedArticles) {
        this.viewedArticles = viewedArticles;
    }

    public String getViewedArticles() {
        return viewedArticles;
    }

    public void setViewedArticles(String viewedArticles) {
        this.viewedArticles = viewedArticles;
    }
}
