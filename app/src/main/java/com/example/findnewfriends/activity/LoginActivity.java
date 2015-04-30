package com.example.findnewfriends.activity;


import android.os.Bundle;



import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import com.example.findnewfriends.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;


//TODO:network suspended problem for nexus 6

public class LoginActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "e5yQjs9GQLW2uqierEoWVyiVX";
    private static final String TWITTER_SECRET = "4ILu6NGvaRl8aRf6hip1Drd7dwxYovyW8CUf8QJO35w0knCQC5";


    private TwitterLoginButton twitterButton;
    private DigitsAuthButton phoneButton;
    private String current_username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        setUpViews();
    }

    private void setUpViews() {
        setUpSkip();
        setUpTwitterButton();
        setUpDigitsButton();
    }

    private void setUpTwitterButton() {
        twitterButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.toast_twitter_signin_success),
                        Toast.LENGTH_SHORT).show();
                current_username = result.data.getUserName();

                startOptionChooser();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.toast_twitter_signin_fail),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpDigitsButton() {
        phoneButton = (DigitsAuthButton) findViewById(R.id.digit_auth_button);
        phoneButton.setAuthTheme(R.style.AppTheme);
        phoneButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession digitsSession, String phoneNumber) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.toast_twitter_digits_success),Toast.LENGTH_SHORT).show();
                startOptionChooser();
            }

            @Override
            public void failure(DigitsException e) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.toast_twitter_digits_fail),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpSkip() {
        TextView skipButton;
        skipButton = (TextView) findViewById(R.id.skip);
        skipButton.setText(getString(R.string.skip_login));
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOptionChooser();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterButton.onActivityResult(requestCode, resultCode, data);
    }

    private void startOptionChooser() {
        final Intent optionChooserIntent = new Intent(LoginActivity.this, OptionChooserActivity.class);
        optionChooserIntent.putExtra("CURRENT_USERNAME", current_username);
        startActivity(optionChooserIntent);
        finish();
    }
}
