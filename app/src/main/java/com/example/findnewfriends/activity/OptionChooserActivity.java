package com.example.findnewfriends.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.findnewfriends.R;

public class OptionChooserActivity extends Activity {

    private Button searchTimelineButton;
    private Button searchTweetsButton;
    private Button searchProfileButton;
    private EditText interestET;
    private EditText locationET;
    private EditText radiusET;
    private EditText usernameET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_chooser);

        interestET = (EditText)findViewById(R.id.interest_input);
        locationET = (EditText)findViewById(R.id.location_input);
        radiusET = (EditText)findViewById(R.id.radius_input);
        usernameET = (EditText)findViewById(R.id.username_input);
        searchTimelineButton = (Button) findViewById(R.id.search_timeline_button);
        searchTweetsButton = (Button) findViewById(R.id.search_tweets_button);
        searchProfileButton = (Button) findViewById(R.id.search_profile_button);

        setUpViews();

    }


    private void setUpViews() {
        setUpSearchTweets();
        setUpSearchProfile();
        setUpSearchTimeline();
    }

    private void setUpSearchTweets() {

        searchTweetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = String.valueOf(locationET.getText());
                String radius = String.valueOf(radiusET.getText());
                String interest = String.valueOf(interestET.getText());

                final Intent intent = new Intent(OptionChooserActivity.this,
                        SearchTweetsActivity.class);

                Bundle extras = new Bundle();
                extras.putString("EXTRA_LOCATION", location);
                extras.putString("EXTRA_RADIUS", radius);
                extras.putString("EXTRA_INTEREST", interest);
//                extras.putString("EXTRA_ACTIVITY", "Search Tweets");
                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }


    private void setUpSearchProfile() {

        searchProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = String.valueOf(locationET.getText());
                String radius = String.valueOf(radiusET.getText());
                String interest = String.valueOf(interestET.getText());

                final Intent intent = new Intent(OptionChooserActivity.this,
                        SearchProfileActivity.class);

                Bundle extras = new Bundle();
                extras.putString("EXTRA_LOCATION", location);
                extras.putString("EXTRA_RADIUS", radius);
                extras.putString("EXTRA_INTEREST", interest);
//                extras.putString("EXTRA_ACTIVITY", "Search Profile");
                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }


    private void setUpSearchTimeline() {

        searchTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = String.valueOf(locationET.getText());
                String radius = String.valueOf(radiusET.getText());
                String username = String.valueOf(usernameET.getText());

                 final Intent intent = new Intent(OptionChooserActivity.this,
                        SearchTimelineActivity.class);

                Bundle extras = new Bundle();
                extras.putString("EXTRA_LOCATION", location);
                extras.putString("EXTRA_RADIUS", radius);
                extras.putString("EXTRA_USERNAME", username);

                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }

}

