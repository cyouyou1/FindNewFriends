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
import com.example.findnewfriends.model.Tweet;
import com.example.findnewfriends.model.UserProfile;
import com.example.findnewfriends.parser.ProfileJSONParser;
import com.example.findnewfriends.parser.TweetJSONParser;
import com.example.findnewfriends.adapter.ProfileAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchProfileActivity extends AppCompatActivity {
    //TODO: modify database

    private ListView lv;
    private ProgressBar pb;
    private List<MyTask> tasks;
    private List<UserProfile> oldProfileList;
    private List<UserProfile> newProfileList;
    private String profileSearchUrl;
    private double latitude;
    private double longitude;
    private double radius_meters;
    private int resultNumber;

    private static final int DEFAULT_RADIUS_IN_MILES = 10;
    private static final int DEFAULT_RESULTS_NUMBER = 20;

    private final String BASEURL_SEARCH_PROFILE = "https://api.mongolab.com/api/1/databases/twitter_db/collections/profile/?";
    private final String APIKEY_SEARCH_PROFILE = "5xOXnbzry10fFTmd28DOX4y_TzKwYT4n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);

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

        String interest_query = Uri.encode(interest_string);
        profileSearchUrl = BASEURL_SEARCH_PROFILE + "q={$text:{$search:%22" + interest_query + "%22}}&apiKey=" + APIKEY_SEARCH_PROFILE;

        lv = (ListView) findViewById(R.id.list_view);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();
        if (isOnline()) {
            requestData(profileSearchUrl);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.heat_map ) {
            Intent intent = new Intent(SearchProfileActivity.this, HeatMapActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EXTRA_SEARCH_URL", profileSearchUrl);
            extras.putString("CALLING_ACTIVITY","searchProfile");
            intent.putExtras(extras);
            startActivity(intent);
            return true;
        }else if (id == R.id.google_map ) {
            Intent intent = new Intent(SearchProfileActivity.this, MapActivity.class);
            Bundle extras = new Bundle();
            extras.putDouble("EXTRA_LATITUDE", latitude);
            extras.putDouble("EXTRA_LONGITUDE", longitude);
            extras.putString("EXTRA_SEARCH_URL", profileSearchUrl);
            extras.putString("CALLING_ACTIVITY","searchProfile");
            extras.putDouble("RADIUS_IN_METER",radius_meters);
            extras.putInt("RESULT_NUMBER",resultNumber);
            intent.putExtras(extras);
            startActivity(intent);
            return true;
        }else if(id == R.id.tag_cloud ) {
            Intent intent = new Intent(SearchProfileActivity.this, TagCloudActivity.class);
            intent.putExtra("URL", profileSearchUrl);
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
        ProfileAdapter adapter = new ProfileAdapter(this, R.layout.user_profile, newProfileList);
        lv.setAdapter(adapter);
    }


    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class MyTask extends AsyncTask<String, String, List<UserProfile>> {

        @Override
        protected void onPreExecute() {

            if(tasks.size() == 0){
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<UserProfile> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            oldProfileList = ProfileJSONParser.parseFeed(content);
            int count = 0;
            newProfileList = new ArrayList<>();

            for (UserProfile profile:oldProfileList) {
                double profile_lat = profile.getLatLng().latitude;
                double profile_lng = profile.getLatLng().longitude;
                float[]distance = new float[3];
                Location.distanceBetween(profile_lat, profile_lng, latitude, longitude, distance);
                if ((double)distance[0] <= radius_meters && count < resultNumber) {
                    count ++;
                    try {
                        String imageUrl = profile.getProfile_image_url();
                        if (imageUrl != null) {
                            InputStream in = (InputStream) new URL(imageUrl).getContent();
                            Bitmap bitmap = BitmapFactory.decodeStream(in);
                            profile.setProfile_pic(bitmap);
                            in.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    newProfileList.add(profile);
                }
            }
            return newProfileList;
        }

        @Override
        protected void onPostExecute(List<UserProfile> result) {

            tasks.remove(this);
            if(tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
            if (result == null){
                Toast.makeText(SearchProfileActivity.this, "Cannot connect to web service", Toast.LENGTH_LONG).show();
                return;
            }

            updateDisplay();
        }
    }
}

