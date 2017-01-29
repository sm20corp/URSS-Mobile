package com.project.benjamin.rss_feedaggregator.Interface;

import com.project.benjamin.rss_feedaggregator.Model.Articles.Article;
import com.project.benjamin.rss_feedaggregator.Model.Articles.ArticleRequest;
import com.project.benjamin.rss_feedaggregator.Model.Feed.FeedRequest;
import com.project.benjamin.rss_feedaggregator.Model.History;
import com.project.benjamin.rss_feedaggregator.Model.Login.Credential;
import com.project.benjamin.rss_feedaggregator.Model.Login.LoginRequest;
import com.project.benjamin.rss_feedaggregator.Model.Feed.Feed;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Benjamin on 27/01/2017.
 */

public interface WebServerIntf {

    /* CREDENTIALS */

    @POST("auth/local")
    Call<Credential> login(@Body LoginRequest body);

    @POST("api/users/createAccount/")
    Call<Credential> register(@Body LoginRequest body);

    @GET("api/credentials/{userID}")
    Call<Credential> listCreds(@Header("Authorization")String authBearer, @Path("userID") String userID);

    @GET("api/users/{userID}")
    Call<Credential> getUser(@Header("Authorization")String authBearer, @Path("userID") String userID);

    @GET("api/histories/{historyID}")
    Call<History> getHistory(@Header("Authorization")String authBearer, @Path("historyID") String historyID);

    @PUT("api/users/bookmarkFeed/{feedID}")
    Call<String> addToHistory(@Header("Authorization")String authBearer, @Path("feedID") String feedID);

    @PUT("/api/users/viewArticle/{articleID}")
    Call<String> addViewed(@Header("Authorization")String authBearer, @Path("articleID") String articleID);

    /* FEEDS */

    @GET("api/feeds/")
    Call<ArrayList<Feed>> listFeeds(@Header("Authorization")String authBearer);

    @POST("api/feeds/fromURL")
    Call<Feed> addFeed(@Header("Authorization")String authBearer, @Body FeedRequest feedRequest);

    @GET("api/feeds/{feedID}")
    Call<Feed> getFeed(@Header("Authorization")String authBearer, @Path("feedID") String feedID);

    @GET("api/articles/{articleID}")
    Call<Article> getArticle(@Header("Authorization")String authBearer, @Path("articleID") String articleID);

}