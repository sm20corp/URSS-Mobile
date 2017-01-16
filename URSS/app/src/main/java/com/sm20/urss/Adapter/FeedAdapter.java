package com.sm20.urss.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.sm20.urss.Model.Feed;

import java.util.List;
import com.sm20.urss.R;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder>  {
    private List<Feed> feedList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

    public FeedAdapter(List<Feed> feedList) {
        this.feedList = feedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Feed feed = feedList.get(position);
        holder.title.setText(feed.getTitle());
        holder.description.setText(feed.getDescription());
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
