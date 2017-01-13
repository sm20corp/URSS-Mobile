package com.sm20.urss.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.LinearLayout;
import android.view.inputmethod.InputMethodManager;
import android.support.design.widget.Snackbar;

import com.sm20.urss.R;

public class LoginActivity extends AppCompatActivity {
    Button      loginButton;
    EditText    userName;
    EditText    password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.signInButton);
        userName = (EditText) findViewById(R.id.userMailString);
        password = (EditText) findViewById(R.id.userPasswordString);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                if (userName.getText().toString().isEmpty()) {
                    showSnackBar(getString(R.string.errorLogin));
                } else {
                    checkUserCredentials(userName.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    void checkUserCredentials(String userName, String password) {
        //to do: api func
        Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mIntent);
    }

    public void hideKeyBoard() {
        LinearLayout coordinatorLayout = (LinearLayout) findViewById(R.id.activity_login);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coordinatorLayout.getWindowToken(), 0);
    }

    void showSnackBar(String message) {
        LinearLayout coordinatorLayout = (LinearLayout) findViewById(R.id.activity_login);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}