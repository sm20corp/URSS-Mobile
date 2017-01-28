package com.project.benjamin.rss_feedaggregator.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.R;

/**
 * Created by Benjamin on 27/01/2017.
 */

public class DisconnectFragment extends Fragment {
    private Button mCancel, mConfirm;
    private Fragment oldFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_disconnect, container, false);

        mCancel = (Button)rootView.findViewById(R.id.cancel_disconnect_button);
        mConfirm = (Button)rootView.findViewById(R.id.disconnect_comfirm_button);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
                Fragment fragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                ((MainActivity)getActivity()).setSelectedLeftItem(0);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
                SharedPreferences settings = ((MainActivity)getActivity()).getSharedPreferences("RSSFeedUserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();
                Fragment fragment = new LoginFragment();
                fragmentManager.beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right, 0, 0).replace(R.id.content_frame, fragment).commit();
                ((MainActivity)getActivity()).mBar.setDisplayHomeAsUpEnabled(false);
            }
        });

        return rootView;
    }
}
