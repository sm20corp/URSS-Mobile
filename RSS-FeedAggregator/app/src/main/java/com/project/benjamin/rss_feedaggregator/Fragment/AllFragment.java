package com.project.benjamin.rss_feedaggregator.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.benjamin.rss_feedaggregator.R;

/**
 * Created by Benjamin on 24/01/2017.
 */
public class AllFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }

}
