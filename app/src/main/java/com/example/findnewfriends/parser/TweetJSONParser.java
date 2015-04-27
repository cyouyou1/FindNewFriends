package com.example.findnewfriends.parser;

import com.example.findnewfriends.model.Tweet;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TweetJSONParser {


        public static List<Tweet> parseFeed(String content) {

            try {
                JSONArray ar = new JSONArray(content);
                List<Tweet> tweetsList = new ArrayList<>();

                for (int i = 0; i < ar.length(); i++) {
                    JSONObject object = ar.getJSONObject(i);
                    Tweet tweet = new Tweet();

                    tweet.setTweet_text(object.getString("tweet_text"));
                    tweet.setCreated_at(object.getString("created_at"));
                    tweet.setUser_description(object.getString("user_description"));
                    tweet.setUser_screen_name(object.getString("user_screen_name"));
                    tweet.setProfile_image_url(object.getString("profile_image_url"));
                    if(!(object.getString("latLng").equals("null"))) {
                        double latitude = object.getJSONObject("latLng").getJSONArray("coordinates").getDouble(0);
                        double longitude = object.getJSONObject("latLng").getJSONArray("coordinates").getDouble(1);
                        tweet.setLatLng(new LatLng(latitude,longitude));
                    }else{
                        tweet.setLatLng(null);
                    }
                    tweetsList.add(tweet);
                }

                return tweetsList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
}

