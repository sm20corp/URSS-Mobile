package com.project.benjamin.rss_feedaggregator.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.R;

/**
 * Created by Benjamin on 24/01/2017.
 */
public class LoginFragment extends Fragment {

    private Button mLogin, mRegister;
    private EditText mEmailField, mPasswordField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        mLogin = (Button)rootView.findViewById(R.id.connect_button);
        mRegister = (Button)rootView.findViewById(R.id.register_button);

        mEmailField = (EditText)rootView.findViewById(R.id.text_email);
        mPasswordField = (EditText)rootView.findViewById(R.id.text_password);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mEmailField.getText().toString().length() > 0 && mPasswordField.getText().toString().length() > 0) {
                    String email = mEmailField.getText().toString();
                    String password = mPasswordField.getText().toString();
                    ((MainActivity)getActivity()).displayHomePage();
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mEmailField.getText().toString().length() > 0 && mPasswordField.getText().toString().length() > 0) {
                    String email = mEmailField.getText().toString();
                    String password = mPasswordField.getText().toString();
                }
            }
        });

        return rootView;
    }
}
