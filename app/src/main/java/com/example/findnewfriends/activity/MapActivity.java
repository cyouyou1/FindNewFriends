package com.example.findnewfriends.activity;


import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.findnewfriends.model.HttpManager;
import com.example.findnewfriends.R;
import com.example.findnewfriends.model.Tweet;
import com.example.findnewfriends.model.UserProfile;
import com.example.findnewfriends.parser.ProfileJSONParser;
import com.example.findnewfriends.parser.TweetJSONParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap;
    private String searchUrl;
    private String callingActivity;
    private double latitude;
    private double longitude;
    private int resultNumber;
    private double radius_in_meter;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent searchIntent = getIntent();
        Bundle extras = searchIntent.getExtras();
        latitude = extras.getDouble("EXTRA_LATITUDE");
        longitude = extras.getDouble("EXTRA_LONGITUDE");
        searchUrl = extras.getString("EXTRA_SEARCH_URL");
        callingActivity = extras.getString("CALLING_ACTIVITY");
        resultNumber = extras.getInt("RESULT_NUMBER");
        radius_in_meter = extras.getDouble("RADIUS_IN_METER");

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {

        AddMarkerTask task = new AddMarkerTask();
        task.execute(searchUrl);
    }


    private class AddMarkerTask extends AsyncTask<String, String, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            int count = 0;

            List<LatLng> latLngList = new ArrayList<>();
            if(callingActivity.equals("searchTweets")) {
                List<Tweet> tweetsList = TweetJSONParser.parseFeed(content);
                for (Tweet tweet:tweetsList){
                    double tweet_lat = tweet.getLatLng().latitude;
                    double tweet_lng = tweet.getLatLng().longitude;
                    float[]distance = new float[3];
                    Location.distanceBetween(tweet_lat, tweet_lng, latitude, longitude, distance);
                    if ((double)distance[0] <= radius_in_meter && count < resultNumber) {
                        count++;
                        latLngList.add(new LatLng(tweet_lat, tweet_lng));
                    }
                }
            }else if (callingActivity.equals("searchProfile")) {
                List<UserProfile> profileList = ProfileJSONParser.parseFeed(content);
                for (UserProfile profile:profileList){
                    double profile_lat = profile.getLatLng().latitude;
                    double profile_lng = profile.getLatLng().longitude;
                    float[]distance = new float[3];
                    Location.distanceBetween(profile_lat, profile_lng, latitude, longitude, distance);
                    if ((double)distance[0] <= radius_in_meter && count < resultNumber) {
                        count++;
                        latLngList.add(new LatLng(profile_lat, profile_lng));
                    }
                }
            }
            return latLngList;
        }

        @Override
        protected void onPostExecute(List<LatLng> latLngList) {

            for (LatLng latLng: latLngList){
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        }
    }
}
