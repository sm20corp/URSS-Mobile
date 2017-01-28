package com.project.benjamin.rss_feedaggregator.Model.Login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Benjamin on 27/01/2017.
 */

public class Credential {
    @SerializedName("token")
    private String token;

    @SerializedName("userId")
    private String userId;

    private String email = "";
    private String password = "";


    public Credential(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    public Credential(String token, String userId, String email, String password) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
