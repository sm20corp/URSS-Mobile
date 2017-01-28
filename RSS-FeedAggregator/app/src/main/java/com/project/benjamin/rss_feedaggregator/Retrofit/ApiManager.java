package com.project.benjamin.rss_feedaggregator.Retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.project.benjamin.rss_feedaggregator.Interface.WebServerIntf;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Benjamin on 27/01/2017.
 */

public class ApiManager {
    public static final String BASE_URL = "http://79.137.78.39:4242/";

    private static WebServerIntf restClient;
    private static Retrofit retrofit;

    private ApiManager() {
    }

    public static Retrofit getRetrofit() {
        return (retrofit);
    }

    public static WebServerIntf getWebServerIntf() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restClient = retrofit.create(WebServerIntf.class);
        return restClient;
    }
}
