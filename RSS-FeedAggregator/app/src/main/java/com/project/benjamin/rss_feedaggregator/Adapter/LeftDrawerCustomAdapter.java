package com.project.benjamin.rss_feedaggregator.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.Model.DrawerItem;
import com.project.benjamin.rss_feedaggregator.R;

/**
 * Created by Benjamin on 24/01/2017.
 */
public class LeftDrawerCustomAdapter extends ArrayAdapter<DrawerItem> {

    private Context mContext;
    private int layoutResourceId;
    private DrawerItem data[] = null;
    private View listItem;

    public LeftDrawerCustomAdapter(Context mContext, int layoutResourceId, DrawerItem[] data) {
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

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageView_left);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textView_Left);

        DrawerItem folder = data[position];

        if (position == ((MainActivity)mContext).getSelectedLeftItem() && ((MainActivity)mContext).getSelectedLeftItem() != 11) {
            listItem.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimaryDark)));
        } else {
            listItem.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.colorNavDrawer)));
        }
        if (folder != null) {
            if (folder.icon != -1) {
                imageViewIcon.setImageResource(folder.icon);
            }
            textViewName.setText(folder.name);
        }

        return listItem;
    }
}
