package com.example.findnewfriends.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    //TODO: load pic when scrolling
    private ListView lv;
    private ProgressBar pb;
    private List<MyTask> tasks;
    private List<Tweet> tweetsList;
    private String searchUrl;

    private final String BASEURL_SEARCH_TWEETS  = "https://api.mongolab.com/api/1/databases/project_db/collections/simple_tweets/?";
    private final String APIKEY_SEARCH_TWEETS = "zmTpDSixS5MN2Kb6txgHDM9GvxE5sksX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_tweets);

        Intent optionChooserIntent = getIntent();
        Bundle extras = optionChooserIntent.getExtras();
        String location_string = extras.getString("EXTRA_LOCATION");
        String radius_string = extras.getString("EXTRA_RADIUS");
        String interest_string = extras.getString("EXTRA_INTEREST");
//

        String location_query = Uri.encode(location_string);
        String radius_query = Uri.encode(radius_string);
        String interest_query = Uri.encode(interest_string);

        searchUrl = BASEURL_SEARCH_TWEETS + "q={$text:{$search:%22" + interest_query + "%22}}&l=100&apiKey=" + APIKEY_SEARCH_TWEETS;




        lv = (ListView) findViewById(R.id.list_view);
        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks = new ArrayList<>();
        if (isOnline()) {
            requestData(searchUrl);
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
            Intent intent = new Intent(this, HeatMapActivity.class);
            intent.putExtra("URL", searchUrl);
            startActivity(intent);
            return true;
        }else if (id == R.id.google_map ) {
            Intent intent = new Intent(SearchTweetsActivity.this, MapActivity.class);
            intent.putExtra("URL", searchUrl);
            startActivity(intent);
            return true;
        }

        return false;
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);

    }

    protected void updateDisplay() {
        TweetAdapter adapter = new TweetAdapter(this, R.layout.tweet, tweetsList);
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
            tweetsList = TweetJSONParser.parseFeed(content);

            for (Tweet tweet:tweetsList){
                try {
                    String imageUrl = tweet.getProfile_image_url();
                    if(imageUrl != null){
                        InputStream in = (InputStream) new URL(imageUrl).getContent();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        tweet.setProfile_pic(bitmap);
                        in.close();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
            return tweetsList;


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

