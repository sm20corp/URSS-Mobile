package com.project.benjamin.rss_feedaggregator.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.benjamin.rss_feedaggregator.Interface.WebServerIntf;
import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.Model.Login.Credential;
import com.project.benjamin.rss_feedaggregator.Model.Login.LoginRequest;
import com.project.benjamin.rss_feedaggregator.R;
import com.project.benjamin.rss_feedaggregator.Retrofit.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Benjamin on 24/01/2017.
 */
public class LoginFragment extends Fragment {

    private Button mLogin, mRegister;
    private EditText mEmailField, mPasswordField;
    private Credential currentUser;
    private ProgressDialog progressDialog;
    private boolean autoLogin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mLogin = (Button)rootView.findViewById(R.id.connect_button);
        mRegister = (Button)rootView.findViewById(R.id.register_button);

        mEmailField = (EditText)rootView.findViewById(R.id.text_email);
        mPasswordField = (EditText)rootView.findViewById(R.id.text_password);

        if (autoLogin) {
            System.out.println("AutoLogin");
            System.out.println("Creds : " + currentUser.getEmail() + " - " + currentUser.getPassword());
            showProgress("Connecting ...");
            connect(currentUser.getEmail(), currentUser.getPassword());
        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                showProgress("Connecting ...");

                if (mEmailField.getText().toString().length() > 0 && mPasswordField.getText().toString().length() > 0) {
                    final String email = mEmailField.getText().toString();
                    final String password = mPasswordField.getText().toString();
                    connect(email, password);
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                showProgress("Creating account and connecting ...");

                if (mEmailField.getText().toString().length() > 0 && mPasswordField.getText().toString().length() > 0) {
                    final String email = mEmailField.getText().toString();
                    final String password = mPasswordField.getText().toString();
                    register(email, password);
                }
            }
        });

        return rootView;
    }

    private void connect(final String email, final String password) {
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Credential> authentificateUserCall;
        authentificateUserCall = webServerIntf.login(new LoginRequest(email, password));
        authentificateUserCall.enqueue(new Callback<Credential>() {
            @Override
            public void onResponse(Call<Credential> call, Response<Credential> response) {
                System.out.println("response = " + response.raw().toString());
                hideDialog();
                if (response.isSuccessful()) {
                    currentUser = response.body();
                    currentUser.setEmail(email);
                    currentUser.setPassword(password);
                    ((MainActivity) getActivity()).displayHomePage(currentUser);
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed to connect, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Credential> call, Throwable t) {
                System.out.println("On failure login : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed to connect, please try again", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    private void register(final String email, final String password) {
        final WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Credential> authentificateUserCall;
        authentificateUserCall = webServerIntf.register(new LoginRequest(email, password));
        authentificateUserCall.enqueue(new Callback<Credential>() {
            @Override
            public void onResponse(Call<Credential> call, Response<Credential> response) {
                System.out.println("response = " + response.raw().toString());

                if (response.isSuccessful()) {

                    /* LOGIN */
                    Call<Credential> authentificateUserCall;
                    authentificateUserCall = webServerIntf.login(new LoginRequest(email, password));
                    authentificateUserCall.enqueue(new Callback<Credential>() {
                        @Override
                        public void onResponse(Call<Credential> call, Response<Credential> response) {
                            System.out.println("response = " + response.raw().toString());
                            hideDialog();
                            if (response.isSuccessful()) {
                                currentUser = response.body();
                                currentUser.setEmail(email);
                                currentUser.setPassword(password);
                                ((MainActivity) getActivity()).displayHomePage(currentUser);
                            } else {
                                Toast.makeText((MainActivity) getActivity(), "Failed to connect, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Credential> call, Throwable t) {
                            System.out.println("On failure login : " + t.getMessage());
                            Toast.makeText((MainActivity) getActivity(), "Failed to connect, please try again", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }
                    });
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed to register, please try again", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<Credential> call, Throwable t) {
                System.out.println("On failure login : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed to register, please try again", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    private void showProgress(String text) {
        progressDialog = new ProgressDialog(((MainActivity)getActivity()));
        progressDialog.setCancelable(false);
        progressDialog.setMessage(text);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setAutoLoginOn(Credential user) {
        this.autoLogin = true;
        this.currentUser = user;
    }
}
