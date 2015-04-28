package com.example.findnewfriends.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.findnewfriends.R;

public class OptionChooserActivity extends Activity {

    private Button searchTimelineButton;
    private Button searchTweetsButton;
    private Button searchProfileButton;
    private Button signOutButton;
    private EditText interestET;
    private EditText locationET;
    private EditText radiusET;
    private EditText usernameET;
    private EditText resultNumberET;
    private String current_username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_chooser);

        Intent loginIntent = getIntent();
        current_username = loginIntent.getStringExtra("CURRENT_USERNAME");

        TextView welcomeText = (TextView)findViewById(R.id.welcome_message);
        if(current_username != null) {
            welcomeText.setText("Welcome to Find Friends, " + current_username);
        }else {
            welcomeText.setText("Welcome to Find Friends!");
        }

        interestET = (EditText)findViewById(R.id.interest_input);
        locationET = (EditText)findViewById(R.id.location_input);
        radiusET = (EditText)findViewById(R.id.radius_input);
        usernameET = (EditText)findViewById(R.id.username_input);
        resultNumberET = (EditText)findViewById(R.id.result_number_input);

        searchTimelineButton = (Button) findViewById(R.id.search_timeline_button);
        searchTweetsButton = (Button) findViewById(R.id.search_tweets_button);
        searchProfileButton = (Button) findViewById(R.id.search_profile_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);

        setUpViews();

    }


    private void setUpViews() {
        setUpSearchTweets();
        setUpSearchProfile();
        setUpSearchTimeline();
        setUpSignOut();
    }

    private void setUpSearchTweets() {

        searchTweetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = String.valueOf(locationET.getText());
                String radius = String.valueOf(radiusET.getText());
                String interest = String.valueOf(interestET.getText());
                String resultNumber = String.valueOf(resultNumberET.getText());

                final Intent intent = new Intent(OptionChooserActivity.this,
                        SearchTweetsActivity.class);

                Bundle extras = new Bundle();
                extras.putString("EXTRA_LOCATION", location);
                extras.putString("EXTRA_RADIUS", radius);
                extras.putString("EXTRA_INTEREST", interest);
                extras.putString("EXTRA_RESULT_NUMBER", resultNumber);

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
                String resultNumber = String.valueOf(resultNumberET.getText());

                final Intent intent = new Intent(OptionChooserActivity.this,
                        SearchProfileActivity.class);

                Bundle extras = new Bundle();
                extras.putString("EXTRA_LOCATION", location);
                extras.putString("EXTRA_RADIUS", radius);
                extras.putString("EXTRA_INTEREST", interest);
                extras.putString("EXTRA_RESULT_NUMBER", resultNumber);

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
                String resultNumber = String.valueOf(resultNumberET.getText());

                 final Intent intent = new Intent(OptionChooserActivity.this,
                        SearchTimelineActivity.class);

                Bundle extras = new Bundle();
                extras.putString("EXTRA_LOCATION", location);
                extras.putString("EXTRA_RADIUS", radius);
                extras.putString("EXTRA_USERNAME", username);
                extras.putString("EXTRA_CURRENT_USERNAME", current_username);
                extras.putString("EXTRA_RESULT_NUMBER", resultNumber);

                intent.putExtras(extras);

                startActivity(intent);
            }
        });
    }

    private void setUpSignOut() {

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

