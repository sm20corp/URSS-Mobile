package com.project.benjamin.rss_feedaggregator.Retrofit;

/**
 * Created by Benjamin on 27/01/2017.
 */

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

}