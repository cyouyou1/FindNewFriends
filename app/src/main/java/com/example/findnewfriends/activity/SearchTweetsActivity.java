package com.example.findnewfriends.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.findnewfriends.model.HttpManager;
import com.example.findnewfriends.R;
import com.example.findnewfriends.adapter.TweetAdapter;
import com.example.findnewfriends.model.Tweet;
import com.example.findnewfriends.parser.TweetJSONParser;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchTweetsActivity extends AppCompatActivity {
//TODO: add sent tweet function
    private ListView lv;
    private ProgressBar pb;
    private List<MyTask> tasks;
    private List<Tweet> oldTweetsList;
    private List<Tweet> newTweetsList;
    private String tweetSearchUrl;
    private double latitude;
    private double longitude;
    private double radius_meters;
    private int resultNumber;
    private String interest_query;
    private static final int DEFAULT_RADIUS_IN_MILES = 10;
    private static final int DEFAULT_RESULTS_NUMBER = 20;



    private final String BASEURL_SEARCH_TWEETS  = "https://api.mongolab.com/api/1/databases/twitter_db/collections/geo_tweets/?";
    private final String APIKEY_SEARCH_TWEETS = "5xOXnbzry10fFTmd28DOX4y_TzKwYT4n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_tweets);

        Intent optionChooserIntent = getIntent();
        Bundle extras = optionChooserIntent.getExtras();
        String location_string = extras.getString("EXTRA_LOCATION");
        String radius_string = extras.getString("EXTRA_RADIUS");
        String interest_string = extras.getString("EXTRA_INTEREST");
        String resultNumber_string = extras.getString("EXTRA_RESULT_NUMBER");
        double currentLat = extras.getDouble("EXTRA_CURRENT_LAT");
        double currentLng = extras.getDouble("EXTRA_CURRENT_LNG");
        double lat = extras.getDouble("EXTRA_LAT");
        double lng = extras.getDouble("EXTRA_LNG");

        if(resultNumber_string.equals("")){
            resultNumber = DEFAULT_RESULTS_NUMBER;
        }else {
            resultNumber = Integer.parseInt(resultNumber_string);
        }
        if(location_string.equals("")){
            latitude = currentLat;
            longitude = currentLng;
        }else {
            latitude = lat;
            longitude = lng;
        }

        if(radius_string.equals("")){
            radius_meters = DEFAULT_RADIUS_IN_MILES * 1609.34;
        }else {
            radius_meters = Double.parseDouble(radius_string) * 1609.34;
        }

        interest_query = Uri.encode(interest_string);
        tweetSearchUrl = BASEURL_SEARCH_TWEETS + "q={$text:{$search:\"\\\"" + interest_query + "\\\"\"}}&apiKey=" + APIKEY_SEARCH_TWEETS;

        lv = (ListView) findViewById(R.id.list_view);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();
        if (isOnline()) {
            requestData(tweetSearchUrl);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_tweets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.heat_map ) {
            Intent intent = new Intent(SearchTweetsActivity.this, HeatMapActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EXTRA_SEARCH_URL", tweetSearchUrl);
            extras.putString("CALLING_ACTIVITY","searchTweets");
            intent.putExtras(extras);
            startActivity(intent);
            return true;
        }else if (id == R.id.google_map ) {
            Intent intent = new Intent(SearchTweetsActivity.this, MapActivity.class);
            Bundle extras = new Bundle();
            extras.putDouble("EXTRA_LATITUDE", latitude);
            extras.putDouble("EXTRA_LONGITUDE", longitude);
            extras.putString("EXTRA_SEARCH_URL", tweetSearchUrl);
            extras.putString("CALLING_ACTIVITY","searchTweets");
            extras.putDouble("RADIUS_IN_METER",radius_meters);
            extras.putInt("RESULT_NUMBER",resultNumber);
            intent.putExtras(extras);
            startActivity(intent);
            return true;
        }else if (id == R.id.goback ) {
            finish();
            return true;
        }

        return false;
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);

    }

    protected void updateDisplay() {
        TweetAdapter adapter = new TweetAdapter(this, R.layout.tweet, newTweetsList);
        lv.setAdapter(adapter);
    }


    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class MyTask extends AsyncTask<String, String, List<Tweet>> {

        @Override
        protected void onPreExecute() {

            if(tasks.size() == 0){
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Tweet> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            oldTweetsList = TweetJSONParser.parseFeed(content);
            int count = 0;
            newTweetsList = new ArrayList<>();

            for (Tweet tweet:oldTweetsList) {
                double tweet_lat = tweet.getLatLng().latitude;
                double tweet_lng = tweet.getLatLng().longitude;
                float[]distance = new float[3];
                Location.distanceBetween(tweet_lat, tweet_lng, latitude, longitude,distance);
                if ((double)distance[0] <= radius_meters && count < resultNumber) {
                    count ++;
                    try {
                        String imageUrl = tweet.getProfile_image_url();
                        if (imageUrl != null) {
                            InputStream in = (InputStream) new URL(imageUrl).getContent();
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            tweet.setProfile_pic(bitmap);
                            in.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    newTweetsList.add(tweet);
                }
            }
            return newTweetsList;


        }

        @Override
        protected void onPostExecute(List<Tweet> result) {

            tasks.remove(this);
            if(tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
            if (result == null){
                Toast.makeText(SearchTweetsActivity.this, "Cannot connect to web service", Toast.LENGTH_LONG).show();
                return;
            }

            updateDisplay();
        }
    }
}

