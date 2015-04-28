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


public class SearchTimelineActivity extends Activity {

    private final String BaseUrl = "http://quiteconfused.ddns.net/py/hello.py/handler?";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_timeline);


        Intent optionChooserIntent = getIntent();
        Bundle extras = optionChooserIntent.getExtras();
        String location_string = extras.getString("EXTRA_LOCATION");
        String radius_string = extras.getString("EXTRA_RADIUS");
        String username_string = extras.getString("EXTRA_USERNAME");
        String current_username_string = extras.getString("EXTRA_CURRENT_USERNAME");
        String resultNumber_string = extras.getString("EXTRA_RESULT_NUMBER");
        double current_lat = extras.getDouble("EXTRA_CURRENT_LAT");
        double current_lng = extras.getDouble("EXTRA_CURRENT_LNG");


        String username_query = (username_string.equals("")) ? Uri.encode(current_username_string) : Uri.encode(username_string);
        String location_query = (location_string.equals("")) ? ("&latlng=" + current_lat + "," + current_lng) : ("&location=" + Uri.encode(location_string));
        String radius_query = (radius_string.equals("")) ? "10" : Uri.encode(radius_string);
        String resultNumber_query = (resultNumber_string.equals("")) ? "50" : Uri.encode(resultNumber_string);


        String search_timeline_url = BaseUrl + "user=" + username_query + location_query + "&radius=" + radius_query + "&count=" + resultNumber_query;

        Uri uri = Uri.parse(search_timeline_url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        finish();

    }
}
