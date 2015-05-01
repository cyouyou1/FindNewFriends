package com.example.findnewfriends.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.findnewfriends.model.HttpManager;
import com.example.findnewfriends.R;
import com.example.findnewfriends.model.Tweet;
import com.example.findnewfriends.model.UserProfile;
import com.example.findnewfriends.parser.ProfileJSONParser;
import com.example.findnewfriends.parser.TweetJSONParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

public class HeatMapActivity extends FragmentActivity {

    private GoogleMap mMap;
    private static final int ALT_HEATMAP_RADIUS = 40;
    private String searchUrl;
    private String callingActivity;
    private double latitude;
    private double longitude;
    /**
     * Alternative opacity of heatmap overlay
     */
    private static final double ALT_HEATMAP_OPACITY = 0.9;

    /**
     * Alternative heatmap gradient (blue -> red)
     * Copied from Javascript version
     */
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };

    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent heatmapIntent = getIntent();
        Bundle extras = heatmapIntent.getExtras();
        searchUrl = extras.getString("EXTRA_SEARCH_URL");
        callingActivity = extras.getString("CALLING_ACTIVITY");
        latitude = extras.getDouble("EXTRA_LATITUDE");
        longitude = extras.getDouble("EXTRA_LONGITUDE");

        setContentView(R.layout.activity_heatmap);
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.heatmap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        HeatMapTask task = new HeatMapTask ();
        task.execute(searchUrl);
    }


    private class HeatMapTask  extends AsyncTask<String, String, List<LatLng>> {

        @Override
        protected List<LatLng> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            List<LatLng> latLngList = new ArrayList<>();
            if(callingActivity.equals("searchTweets")) {
                 List<Tweet> tweetsList = TweetJSONParser.parseFeed(content);
                 for (Tweet tweet:tweetsList){

                    try {
                        LatLng markerLatLng = tweet.getLatLng();
                        if(markerLatLng != null){
                            latLngList.add(markerLatLng);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }else if (callingActivity.equals("searchProfile")) {
                List<UserProfile> profileList = ProfileJSONParser.parseFeed(content);

                for (UserProfile profile:profileList){

                    try {
                        LatLng markerLatLng = profile.getLatLng();
                        if(markerLatLng != null){
                            latLngList.add(markerLatLng);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            return latLngList;


        }

        @Override
        protected void onPostExecute(List<LatLng> latLngList) {


            if (mProvider == null) {
                mProvider = new HeatmapTileProvider.Builder()
                        .data(latLngList)
                        .build();
                mProvider.setOpacity(ALT_HEATMAP_OPACITY);
                mProvider.setGradient(ALT_HEATMAP_GRADIENT);
                mProvider.setRadius(ALT_HEATMAP_RADIUS);
                // Add a tile overlay to the map, using the heat map tile provider.
                mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            }else {
                mProvider.setData(latLngList);
                mOverlay.clearTileCache();
            }
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        }
    }
}
