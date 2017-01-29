package com.project.benjamin.rss_feedaggregator.Fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.benjamin.rss_feedaggregator.Adapter.ArticlesListCustomAdapter;
import com.project.benjamin.rss_feedaggregator.Adapter.FeedsListCustomAdapter;
import com.project.benjamin.rss_feedaggregator.Interface.WebServerIntf;
import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.Model.Articles.Article;
import com.project.benjamin.rss_feedaggregator.Model.Articles.ArticleRequest;
import com.project.benjamin.rss_feedaggregator.Model.Feed.FeedRequest;
import com.project.benjamin.rss_feedaggregator.Model.History;
import com.project.benjamin.rss_feedaggregator.Model.Login.Credential;
import com.project.benjamin.rss_feedaggregator.Model.Feed.Feed;
import com.project.benjamin.rss_feedaggregator.R;
import com.project.benjamin.rss_feedaggregator.Retrofit.ApiManager;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Benjamin on 24/01/2017.
 */
public class HomeFragment extends Fragment {
    private ListView mListView;
    private FeedsListCustomAdapter mAdapterFeeds;
    private ArticlesListCustomAdapter mAdapterArticles;
    private ArrayList<Feed> mFeeds;
    private ArrayList<Article> mArticlesToShow;
    private SearchView searchView;
    private Category onCategory = Category.FEEDS;
    private RelativeLayout mListLayout, mArticleMoreLayout;
    private TextView mTitle, mDescription;
    private Button mVisitWebsiteButton;
    private String mWebsiteUrl;
    private ImageView mArticleImage;
    private WebView mWebView;
    private Credential user;
    private ArrayList<String> mViewedArticles;

    public enum Category {
        FEEDS,
        ARTICLES,
        ARTICLES_MORE,
        WEBVIEW
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mListView = (ListView)rootView.findViewById(R.id.listViewFeeds);

        mFeeds = ((MainActivity)getActivity()).getFeeds();

        loadMyFeeds();

        mWebView = (WebView)rootView.findViewById(R.id.webview);
        mWebView.setVisibility(View.GONE);

        mAdapterFeeds = new FeedsListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, mFeeds);
        mListView.setAdapter(mAdapterFeeds);
        mListView.setOnItemClickListener(new ListItemClickListener());

        mListLayout = (RelativeLayout)rootView.findViewById(R.id.listView_layout);
        mArticleMoreLayout = (RelativeLayout)rootView.findViewById(R.id.article_more_layout);
        mArticleMoreLayout.setVisibility(View.GONE);
        mListLayout.setVisibility(View.VISIBLE);

        mArticleImage = (ImageView)rootView.findViewById(R.id.article_image);

        mTitle = (TextView)rootView.findViewById(R.id.title_text);
        mDescription = (TextView)rootView.findViewById(R.id.description_text);
        mVisitWebsiteButton = (Button)rootView.findViewById(R.id.visit_website_button);
        mVisitWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebsiteUrl != null && mWebsiteUrl.length() > 0) {
                    onCategory = Category.WEBVIEW;
                    mWebView.setVisibility(View.VISIBLE);
                    mArticleMoreLayout.setVisibility(View.GONE);
                    mListLayout.setVisibility(View.GONE);
                    mWebView.loadUrl(mWebsiteUrl);
                }
            }
        });

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
                    mAdapterFeeds = new FeedsListCustomAdapter(((MainActivity) getActivity()), R.layout.listview_all_feeds, mFeeds);
                    mListView.setAdapter(mAdapterFeeds);
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
                mAdapterFeeds = new FeedsListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, searchResult);
                mListView.setAdapter(mAdapterFeeds);
            }

        });

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
            if (onCategory == Category.FEEDS) {
                mArticlesToShow = new ArrayList<>();
                ArrayList<String> articles = mFeeds.get(position).getArticles();
                for (int i = 0; i < articles.size(); i++) {
                    getArticle(articles.get(i));
                }
                onCategory = Category.ARTICLES;
            } else if (onCategory == Category.ARTICLES) {
                if (mArticlesToShow.get(position).getEnclosureUrl() != null && mArticlesToShow.get(position).getEnclosureUrl().length() > 0) {
                    new DownloadImageTask().execute(mArticlesToShow.get(position).getEnclosureUrl());
                }
                onCategory = Category.ARTICLES_MORE;
                mTitle.setText(mArticlesToShow.get(position).getTitle());
                mDescription.setText(mArticlesToShow.get(position).getDescription());
                mWebsiteUrl = mArticlesToShow.get(position).getLink();
                mListLayout.setVisibility(View.GONE);
                mArticleMoreLayout.setVisibility(View.VISIBLE);
                mArticlesToShow.get(position).setViewed(true);
                addViewed(mArticlesToShow.get(position).getId());
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public DownloadImageTask() {
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            mArticleImage.setImageBitmap(result);
        }
    }

    public void updateFeedsList() {
        loadMyFeeds();

        mAdapterFeeds = new FeedsListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, mFeeds);
        mListView.setAdapter(mAdapterFeeds);
    }

    public void refreshFeedsListView() {
        mAdapterFeeds = new FeedsListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, mFeeds);
        mListView.setAdapter(mAdapterFeeds);
    }

    public void refreshArticlesListView() {
        mAdapterArticles = new ArticlesListCustomAdapter(((MainActivity)getActivity()), R.layout.listview_all_feeds, mArticlesToShow, mViewedArticles);
        mListView.setAdapter(mAdapterArticles);
    }

    private void loadAllFeeds() {
        onCategory = Category.FEEDS;
        user = ((MainActivity)getActivity()).getCurrentuser();
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<ArrayList<Feed>> authentificateUserCall;
        authentificateUserCall = webServerIntf.listFeeds("Bearer " + user.getToken());
        authentificateUserCall.enqueue(new Callback<ArrayList<Feed>>() {
            @Override
            public void onResponse(Call<ArrayList<Feed>> call, Response<ArrayList<Feed>> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    mFeeds = response.body();
                    if (mFeeds != null && ((MainActivity) getActivity()) != null) {
                        ((MainActivity) getActivity()).setFeeds(mFeeds);
                    }
                    refreshFeedsListView();
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Feed>> call, Throwable t) {
                System.out.println("On failure load Feeds : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMyFeeds() {
        mFeeds = new ArrayList<>();
        onCategory = Category.FEEDS;
        user = ((MainActivity)getActivity()).getCurrentuser();
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Credential> authentificateUserCall;
        authentificateUserCall = webServerIntf.getUser("Bearer " + user.getToken(), user.getuserId());
        authentificateUserCall.enqueue(new Callback<Credential>() {
            @Override
            public void onResponse(Call<Credential> call, Response<Credential> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    user.setHistory(((Credential)response.body()).getHistory());
                    loadHistory(user.getHistory());
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Credential> call, Throwable t) {
                System.out.println("On failure load Feeds : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHistory(String historyID) {
        user = ((MainActivity)getActivity()).getCurrentuser();
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<History> authentificateUserCall;
        authentificateUserCall = webServerIntf.getHistory("Bearer " + user.getToken(), user.getHistory());
        authentificateUserCall.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    History history = response.body();
                    mViewedArticles = response.body().getViewedArticles();
                    ArrayList<String> bookmarks = history.getBookmarks();
                    for (int i = 0; i < bookmarks.size(); i++) {
                        displayHistory(bookmarks.get(i));
                    }
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                System.out.println("On failure load Feeds : " + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayHistory(final String feedID) {
        final Feed feed = new Feed();
        feed.setId(feedID);
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Feed> authentificateUserCall;
        authentificateUserCall = webServerIntf.getFeed("Bearer " + user.getToken(), feed.getId());
        authentificateUserCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    feed.setArticles(response.body().getArticles());
                    feed.setUrl(response.body().getUrl());
                    mFeeds.add(feed);
                    refreshFeedsListView();
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

    private void getFeed(final String feedID) {
        final Feed feed = new Feed();
        feed.setId(feedID);
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

    private void getArticle(final String articleID) {
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Article> authentificateUserCall;
        authentificateUserCall = webServerIntf.getArticle("Bearer " + user.getToken(), articleID);
        authentificateUserCall.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    Article article = response.body();
                    article.setId(articleID);
                    mArticlesToShow.add(article);
                    refreshArticlesListView();
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {
                System.out.println("Failed" + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addViewed(final String articleID) {
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<String> authentificateUserCall;
        authentificateUserCall = webServerIntf.addViewed("Bearer " + user.getToken(), articleID);
        authentificateUserCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    mViewedArticles.add(articleID);
                } else {
                    Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Failed" + t.getMessage());
                Toast.makeText((MainActivity) getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onBack() {
        switch (onCategory) {
            case FEEDS:
                break;
            case ARTICLES:
                onCategory = Category.FEEDS;
                updateFeedsList();
                break;
            case ARTICLES_MORE:
                mListLayout.setVisibility(View.VISIBLE);
                mArticleMoreLayout.setVisibility(View.GONE);
                refreshArticlesListView();
                onCategory = Category.ARTICLES;
                //getArticle();
                break;
            case WEBVIEW:
                onCategory = Category.ARTICLES_MORE;
                mWebView.setVisibility(View.GONE);
                mArticleMoreLayout.setVisibility(View.VISIBLE);
            default:
                break;
        }

    }
}
