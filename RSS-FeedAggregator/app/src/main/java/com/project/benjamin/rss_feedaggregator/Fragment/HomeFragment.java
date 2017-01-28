package com.project.benjamin.rss_feedaggregator.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.project.benjamin.rss_feedaggregator.Adapter.FeedsListCustomAdapter;
import com.project.benjamin.rss_feedaggregator.Interface.WebServerIntf;
import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.Model.Feed.FeedRequest;
import com.project.benjamin.rss_feedaggregator.Model.Login.Credential;
import com.project.benjamin.rss_feedaggregator.Model.Feed.Feed;
import com.project.benjamin.rss_feedaggregator.R;
import com.project.benjamin.rss_feedaggregator.Retrofit.ApiManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Benjamin on 24/01/2017.
 */
public class HomeFragment extends Fragment {
    private ListView mFeedsList;
    private FeedsListCustomAdapter mAdapter;
    private ArrayList<Feed> mFeeds;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mFeedsList = (ListView)rootView.findViewById(R.id.listViewFeeds);

        mFeeds = ((MainActivity)getActivity()).getFeeds();

        mAdapter = new FeedsListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, mFeeds);
        mFeedsList.setAdapter(mAdapter);
        mFeedsList.setOnItemClickListener(new ListItemClickListener());

        searchView = (SearchView)rootView.findViewById(R.id.feedsSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.isEnabled() && TextUtils.isEmpty(newText)) {
                    mAdapter = new FeedsListCustomAdapter(((MainActivity) getActivity()), R.layout.listview_all_feeds, mFeeds);
                    mFeedsList.setAdapter(mAdapter);
                } else {
                    callSearch(newText);
                }
                return true;
            }

            public void callSearch(String query) {
                ArrayList<Feed> searchResult = new ArrayList<>();
                for (int i = 0; i < mFeeds.size(); i++) {
                    if (searchTitle(query.toLowerCase(), mFeeds.get(i).getUrl().toLowerCase())) {
                        searchResult.add(mFeeds.get(i));
                    }
                }
                mAdapter = new FeedsListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, searchResult);
                mFeedsList.setAdapter(mAdapter);
            }

        });


        /* TEST */

        testAddFeed();

        return rootView;
    }

    private boolean searchTitle(String query, String title) {
        if (query.length() > title.length()) {
            return false;
        }
        for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) != title.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private class ListItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    }


    /* TEST */

    private Credential user;
    private Feed feed;

    private void testAddFeed() {
        user = ((MainActivity)getActivity()).getCurrentuser();
        feed = new Feed();
        feed.setUrl("http://feeds.bbci.co.uk/news/world/rss.xml");

        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Feed> authentificateUserCall;
        final FeedRequest feedRequest = new FeedRequest();
        feedRequest.setUrl("http://feeds.bbci.co.uk/news/world/rss.xml");
        authentificateUserCall = webServerIntf.addFeed("Bearer " + user.getToken(), feedRequest);
        authentificateUserCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    feed.setId(response.body().getId());
                    System.out.println("FeedRequest response : " + feed.getId());
                    testGetFeed();
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                System.out.println("On failure login : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testGetFeed() {
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Feed> authentificateUserCall;
        authentificateUserCall = webServerIntf.getFeed("Bearer " + user.getToken(), feed.getId());
        authentificateUserCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    feed.setArticles(response.body().getArticles());
                    ArrayList<String> articlesList = feed.getArticles();
                    for (int i = 0; i < articlesList.size(); i++) {
                        System.out.println("FeedRequest response : " + articlesList.get(i));
                    }
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                System.out.println("On failure login : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    /* TEST */
}
