package com.example.findnewfriends.activity;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findnewfriends.R;

//TODO: clean up the location update part as well as put extra part


public class OptionChooserActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

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
    private GoogleApiClient googleApiClient;
    private double currentLat;
    private double currentLng;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private Location currentLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_chooser);


        googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();


        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds




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


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
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
                extras.putDouble("EXTRA_CURRENT_LAT", currentLat);
                extras.putDouble("EXTRA_CURRENT_LNG", currentLng);
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
                extras.putDouble("EXTRA_CURRENT_LAT", currentLat);
                extras.putDouble("EXTRA_CURRENT_LNG", currentLng);

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
                extras.putDouble("EXTRA_CURRENT_LAT", currentLat);
                extras.putDouble("EXTRA_CURRENT_LNG", currentLng);

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


    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (currentLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            currentLat = currentLocation.getLatitude();
            currentLng = currentLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Location services connection has been suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
                Toast.makeText(this, "Location services connection failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

}

