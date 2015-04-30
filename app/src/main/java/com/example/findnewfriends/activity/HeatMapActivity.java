package com.example.findnewfriends.activity;

//TODO: maybe add a marker for current location?
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

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final int ALT_HEATMAP_RADIUS = 50;
    private String searchUrl;
    private String callingActivity;
    private double latitude;
    private double longitude;
    /**
     * Alternative opacity of heatmap overlay
     */
    private static final double ALT_HEATMAP_OPACITY = 1;

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
//    private final String BASEURL_SEARCH_PROFILE = "https://api.mongolab.com/api/1/databases/twitter_db/collections/geo_tweets/?";
//    private final String APIKEY_SEARCH_PROFILE = "5xOXnbzry10fFTmd28DOX4y_TzKwYT4n";
//
//    private final String BASEURL_SEARCH_TWEETS  = "https://api.mongolab.com/api/1/databases/project_db/collections/simple_tweets/?";
//    private final String APIKEY_SEARCH_TWEETS = "zmTpDSixS5MN2Kb6txgHDM9GvxE5sksX";

//    private static final String USER_FOOTBALL_URL =
//            "https://api.mongolab.com/api/1/databases/twitter_db/collections/geo_tweets/?l=1000&apiKey=5xOXnbzry10fFTmd28DOX4y_TzKwYT4n";
//
//    private static final String MONGOLAB_HIKING_URL =
//            "https://api.mongolab.com/api/1/databases/project_db/collections/simple_tweets/?q={$text:{$search:%22hiking%22}}&l=100&apiKey=zmTpDSixS5MN2Kb6txgHDM9GvxE5sksX";
//
//
//    private static final String MONGOLAB_READING_URL =
//            "https://api.mongolab.com/api/1/databases/project_db/collections/simple_tweets/?q={$text:{$search:%22reading%20book%22}}&l=100&apiKey=zmTpDSixS5MN2Kb6txgHDM9GvxE5sksX";
//    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent heatmapIntent = getIntent();
        Bundle extras = heatmapIntent.getExtras();
        searchUrl = extras.getString("EXTRA_SEARCH_URL");
        callingActivity = extras.getString("CALLING_ACTIVITY");

        setContentView(R.layout.activity_heatmap);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
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

//            int[] colors = {
//                    Color.rgb(0, 0, 255), // blue
//                    Color.rgb(255, 0, 0)    // red
//            };
//
//            float[] startPoints = {
//                    0.2f, 1f
//            };
//
//            Gradient gradient = new Gradient(colors, startPoints);
//
//// Create the tile provider.
//            if (mProvider == null) {
//                mProvider = new HeatmapTileProvider.Builder()
//                        .data(latLngList)
//                        .opacity(0.9)
//                        .radius(50)
//                        .gradient(gradient)
//                        .build();
//                mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
//            }else{
//                mProvider.setData(latLngList);
//                mOverlay.clearTileCache();
//            }
////

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
//TODO: see if markers work on heatmap
//            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

        }


    }

}
