package com.example.findnewfriends.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.findnewfriends.R;

//TODO: Add default anchor user
public class SearchTimelineActivity extends Activity {

    private final String BaseUrl = "http://quiteconfused.ddns.net/py/hello.py/handler?";

//TODO: add a progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_search_timeline);
//        this.webView = (WebView)findViewById(R.id.webView);
//
//
//        this.webView.setWebChromeClient(new WebChromeClient()
//        {
//            public void onProgressChanged(WebView view, int progress){
//                SearchTimelineActivity.this.setProgress(progress*1000);
//            }
//        });
//        this.webView.getSettings().setBuiltInZoomControls(true);
//        this.webView.getSettings().setJavaScriptEnabled(true);

        Intent optionChooserIntent = getIntent();
        Bundle extras = optionChooserIntent.getExtras();
        String location_string = extras.getString("EXTRA_LOCATION");
        String radius_string = extras.getString("EXTRA_RADIUS");
        String username_string = extras.getString("EXTRA_USERNAME");
        String current_username_string = extras.getString("EXTRA_CURRENT_USERNAME");
        String resultNumber_string = extras.getString("EXTRA_RESULT_NUMBER");


        String username_query = (username_string.equals("")) ? Uri.encode(current_username_string) : Uri.encode(username_string);
        String location_query = (location_string.equals("")) ? "current_location" : Uri.encode(location_string);
        String radius_query = (radius_string.equals("")) ? "10" : Uri.encode(radius_string);
        String resultNumber_query = (resultNumber_string.equals("")) ? "50" : Uri.encode(resultNumber_string);



        String search_timeline_url = BaseUrl + "user=" + username_query + "&location=" + location_query + "&radius=" + radius_query + "&count=" + resultNumber_query;

        Uri uri = Uri.parse(search_timeline_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        finish();

    }
}
