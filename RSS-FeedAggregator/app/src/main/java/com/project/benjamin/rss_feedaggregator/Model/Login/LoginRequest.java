package com.project.benjamin.rss_feedaggregator.Model.Login;

/**
 * Created by Benjamin on 27/01/2017.
 */

public class LoginRequest {
    public String email;
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
