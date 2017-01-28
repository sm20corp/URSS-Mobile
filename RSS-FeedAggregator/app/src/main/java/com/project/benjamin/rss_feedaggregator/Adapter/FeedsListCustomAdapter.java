package com.project.benjamin.rss_feedaggregator.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.benjamin.rss_feedaggregator.Model.Feed.Feed;
import com.project.benjamin.rss_feedaggregator.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Benjamin on 28/01/2017.
 */

public class FeedsListCustomAdapter extends ArrayAdapter<Feed> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Feed> data = null;
    private View listItem;
    private ImageView imageView;

    public FeedsListCustomAdapter(Context mContext, int layoutResourceId, ArrayList<Feed> data) {
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView textTitle = (TextView) listItem.findViewById(R.id.label_first);
        TextView textDescription = (TextView) listItem.findViewById(R.id.label_second);
        imageView = (ImageView) listItem.findViewById(R.id.image_label);

        Feed folder = data.get(position);

        textTitle.setText(folder.getUrl());
        textDescription.setText(folder.getId());

        return listItem;
    }
}

