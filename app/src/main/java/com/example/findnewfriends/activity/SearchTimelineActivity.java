package com.example.findnewfriends.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.findnewfriends.R;

//TODO: Add default anchor user
public class SearchTimelineActivity extends Activity {
    private WebView webView;
    private final String BaseUrl = "http://quiteconfused.ddns.net/py/hello.py/handler?";

//TODO: add a progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_timeline);

        Intent optionChooserIntent = getIntent();
        Bundle extras = optionChooserIntent.getExtras();
        String location_string = extras.getString("EXTRA_LOCATION");
        String radius_string = extras.getString("EXTRA_RADIUS");
        String username_string = extras.getString("EXTRA_USERNAME");

        String location_query = Uri.encode(location_string);
        String radius_query = Uri.encode(radius_string);
        String username_query = Uri.encode(username_string);

        String search_timeline_url = BaseUrl + "user=" + username_query + "&location=" + location_query + "&radius=" + radius_query;

//        Uri uri = Uri.parse("http://quiteconfused.ddns.net/py/hello.py/handler?user=quiteconfused&location=13834%20Leighfield%20St.,%20Chantilly%20VA,%2020151&count=100");
        Uri uri = Uri.parse(search_timeline_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.webView = (WebView)findViewById(R.id.webView);
        this.webView.loadUrl(search_timeline_url);
    }
}
