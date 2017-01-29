package com.project.benjamin.rss_feedaggregator.Interface;

import com.project.benjamin.rss_feedaggregator.Model.Feed.FeedRequest;
import com.project.benjamin.rss_feedaggregator.Model.Login.Credential;
import com.project.benjamin.rss_feedaggregator.Model.Login.LoginRequest;
import com.project.benjamin.rss_feedaggregator.Model.Feed.Feed;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Benjamin on 27/01/2017.
 */

public interface WebServerIntf {

    /* CREDENTIALS */

    @POST("auth/local")
    Call<Credential> login(@Body LoginRequest body);
    
    @POST("api/users/createAccount")
    Call<Credential> register(@Body LoginRequest body);

    @GET("api/credentials/{userID}")
    Call<Credential> listCreds(@Header("Authorization")String authBearer, @Path("userID") String userID);

    /* FEEDS */

    @POST("api/feeds/fromURL")
    Call<Feed> addFeed(@Header("Authorization")String authBearer, @Body FeedRequest feedRequest);

    @GET("api/feeds/{feedID}")
    Call<Feed> getFeed(@Header("Authorization")String authBearer, @Path("feedID") String feedID);

}