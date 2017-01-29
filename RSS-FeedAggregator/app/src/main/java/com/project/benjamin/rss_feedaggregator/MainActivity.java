package com.project.benjamin.rss_feedaggregator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.benjamin.rss_feedaggregator.Adapter.LeftDrawerCustomAdapter;
import com.project.benjamin.rss_feedaggregator.Fragment.AllFragment;
import com.project.benjamin.rss_feedaggregator.Fragment.DisconnectFragment;
import com.project.benjamin.rss_feedaggregator.Fragment.HomeFragment;
import com.project.benjamin.rss_feedaggregator.Fragment.LoginFragment;
import com.project.benjamin.rss_feedaggregator.Interface.WebServerIntf;
import com.project.benjamin.rss_feedaggregator.Model.Feed.Feed;
import com.project.benjamin.rss_feedaggregator.Model.Feed.FeedRequest;
import com.project.benjamin.rss_feedaggregator.Model.History;
import com.project.benjamin.rss_feedaggregator.Model.Login.Credential;
import com.project.benjamin.rss_feedaggregator.Model.DrawerItem;
import com.project.benjamin.rss_feedaggregator.Retrofit.ApiManager;
import com.project.benjamin.rss_feedaggregator.Utils.AddFeedDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListLeft;
    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private LeftDrawerCustomAdapter mAdapterLeft;
    private int mSelectedLeftItem;
    public ActionBar mBar;
    private Credential currentuser;
    private ImageView addFeedButton;
    private AddFeedDialog addFeedDialog;
    private TextView titleText;
    private ArrayList<Feed> mFeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBar = getSupportActionBar();
        mBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        mBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mBar.setCustomView(R.layout.actionbar_custom);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerListLeft = (ListView) findViewById(R.id.left_drawer);
        mFeeds = new ArrayList<>();

        displayMenuDrawer();
        iconControl();

        mFragmentManager = getFragmentManager();

        if (currentuser == null) {
            LoginFragment loginFragment = new LoginFragment();
            SharedPreferences settings = getSharedPreferences("RSSFeedUserInfo", 0);
            if (settings != null && settings.getString("Email", "").length() > 0) {
                currentuser = new Credential(settings.getString("Token", ""),
                        settings.getString("UserID", ""),
                        settings.getString("Email", ""),
                        settings.getString("Password", ""));
                loginFragment.setAutoLoginOn(currentuser);
            }
            mFragmentManager.beginTransaction().replace(R.id.content_frame, loginFragment).commit();
        } else {
            HomeFragment homeFragment = new HomeFragment();
            mFragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
            mBar.setDisplayHomeAsUpEnabled(true);
            mSelectedLeftItem = 0;
            if (mAdapterLeft != null) {
                mAdapterLeft.notifyDataSetChanged();
            }
        }

        addFeedButton = (ImageView)findViewById(R.id.action_add);
        addFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFeedDialog();
            }
        });
        addFeedButton.setVisibility(View.GONE);

        titleText = (TextView)findViewById(R.id.title_text);
        titleText.setText("URSS");
    }

    public void setTitleBar(String title) {
        mBar.setTitle(title);
    }

    public void hideAction() {
        addFeedButton.setVisibility(View.GONE);
    }

    public void iconControl() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mBar.setDisplayHomeAsUpEnabled(false);
        mBar.setHomeButtonEnabled(true);

        Drawable dr = getResources().getDrawable(R.drawable.ic_menu);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 80, 80, true));
        mBar.setHomeAsUpIndicator(d);
    }

    public void displayMenuDrawer() {
        DrawerItem[] drawerItem = new DrawerItem[2];

        drawerItem[0] = new DrawerItem(R.drawable.ic_home, "Home");
        drawerItem[1] = new DrawerItem(R.drawable.ic_disconnect, "Disconnect");

        mAdapterLeft = new LeftDrawerCustomAdapter(this, R.layout.left_listview_item_row, drawerItem);
        mDrawerListLeft.setAdapter(mAdapterLeft);

        mDrawerListLeft.setOnItemClickListener(new DrawerLeftClickListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerLeftClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectLeftItem(position);
        }
    }

    public int getSelectedLeftItem() {
        if (mSelectedLeftItem == -1) {
            return (1);
        } else {
            return (mSelectedLeftItem);
        }
    }

    public void setSelectedLeftItem(int position) {
        mSelectedLeftItem = position;
        if (mAdapterLeft != null) {
            mAdapterLeft.notifyDataSetChanged();
        }
    }

    private void selectLeftItem(int position) {

        Log.i("MainActivity", "selectLeftItem -> position : " + position);
        Fragment fragment = null;

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                titleText.setText("Home");
                break;
            case 1:
                fragment = new DisconnectFragment();
                titleText.setText("Disconnect");
                break;
            default:
                break;
        }
        setSelectedLeftItem(position);

        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mAdapterLeft.notifyDataSetChanged();
            mDrawerLayout.closeDrawer(mDrawerListLeft);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void displayHomePage(Credential currentUser) {
        addFeedButton.setVisibility(View.VISIBLE);
        this.currentuser = currentUser;
        System.out.println("User ID : " + currentUser.getuserId());
        HomeFragment homeFragment = new HomeFragment();
        mFragmentManager.beginTransaction().setCustomAnimations(R.animator.slide_out_left,
                R.animator.slide_in_right, 0, 0).replace(R.id.content_frame, homeFragment).commit();

        mBar.setDisplayHomeAsUpEnabled(true);
        mSelectedLeftItem = 0;
        if (mAdapterLeft != null) {
            mAdapterLeft.notifyDataSetChanged();
        }

        SharedPreferences settings = getSharedPreferences("RSSFeedUserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Email", currentUser.getEmail());
        editor.putString("Password", currentUser.getPassword());
        editor.putString("UserID", currentUser.getuserId());
        editor.putString("Token", currentUser.getToken());
        editor.apply();
    }

    public Credential getCurrentuser() {
        return currentuser;
    }

    private void showAddFeedDialog() {
        addFeedDialog = new AddFeedDialog(this);
        addFeedDialog.show();
    }

    public void addFeed(final String feedURL) {
        if (addFeedDialog != null && addFeedDialog.isShowing()) {
            addFeedDialog.dismiss();
        }

        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<Feed> authentificateUserCall;
        final FeedRequest feedRequest = new FeedRequest();
        feedRequest.setUrl(feedURL);
        authentificateUserCall = webServerIntf.addFeed("Bearer " + currentuser.getToken(), feedRequest);
        authentificateUserCall.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    Feed feed = response.body();
                    feed.setUrl(feedURL);
                    mFeeds.add(feed);
                    addToHistory(feed.getId());
                    System.out.println("FeedRequest response : " + feed.getId());
                } else {
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                System.out.println("On failure login : " + t.getMessage());
            }
        });
    }

    private void addToHistory(final String feedID) {
        WebServerIntf webServerIntf = ApiManager.getWebServerIntf();
        Call<String> authentificateUserCall;
        authentificateUserCall = webServerIntf.addToHistory("Bearer " + currentuser.getToken(), feedID);
        authentificateUserCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("response = " + response.raw().toString());
                if (response.isSuccessful()) {
                    Fragment fg = getFragmentManager().findFragmentById(R.id.content_frame);
                    if (fg instanceof HomeFragment) {
                        ((HomeFragment)fg).updateFeedsList();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("On failure load Feeds : " + t.getMessage());
            }
        });
    }

    public ArrayList<Feed> getFeeds() {
        return mFeeds;
    }

    public void setFeeds(ArrayList<Feed> feeds) {
        mFeeds = feeds;
    }

    public void addFeed(Feed feed) {
        mFeeds.add(feed);
    }

    private boolean mLeaveCheck = false;
    final Handler handler_back = new Handler();
    final Runnable runnable_back = new Runnable() {
        @Override
        public void run() {
            mLeaveCheck = false;
        }
    };

    @Override
    public void onBackPressed() {
        Fragment fg = getFragmentManager().findFragmentById(R.id.content_frame);
        if (fg instanceof HomeFragment) {
            ((HomeFragment)fg).onBack();
        } else {
            Toast.makeText(this, "Press once again to leave", Toast.LENGTH_SHORT).show();
            if (mLeaveCheck == true) {
                mLeaveCheck = false;
                super.onBackPressed();
            } else {
                handler_back.postDelayed(runnable_back, 1500);
                mLeaveCheck = true;
            }
        }
    }
}
