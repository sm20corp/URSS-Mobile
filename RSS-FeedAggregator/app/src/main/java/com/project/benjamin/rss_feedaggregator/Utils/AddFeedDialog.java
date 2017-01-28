package com.project.benjamin.rss_feedaggregator.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.benjamin.rss_feedaggregator.MainActivity;
import com.project.benjamin.rss_feedaggregator.R;

/**
 * Created by Benjamin on 28/01/2017.
 */


public class AddFeedDialog extends Dialog {

    public EditText feedURL;
    private Button addFeed;
    private Context _context;

    public AddFeedDialog(Context context) {
        super(context);
        _context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_feed_dialog);
        feedURL = (EditText) findViewById(R.id.add_feed_text);
        addFeed = (Button) findViewById(R.id.add_feed_button);
        addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedURL != null && feedURL.length() > 0) {
                    ((MainActivity)_context).addFeed(feedURL.getText().toString());
                } else {
                    Toast.makeText(_context, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
