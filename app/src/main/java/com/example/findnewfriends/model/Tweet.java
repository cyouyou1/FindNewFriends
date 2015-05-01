package com.example.findnewfriends.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;


public class Tweet {


    private String tweet_text;
    private String profile_image_url;
    private String created_at;
    private Bitmap profile_pic;
    private String user_screen_name;
    private LatLng latLng;
    private String user_description;


    public String getTweet_text() {
        return tweet_text;
    }

    public void setTweet_text(String tweet_text) {
        this.tweet_text = tweet_text;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Bitmap getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(Bitmap profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUser_screen_name() {
        return user_screen_name;
    }

    public void setUser_screen_name(String user_screen_name) {
        this.user_screen_name = user_screen_name;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getUser_description() {
        return user_description;
    }

    public void setUser_description(String user_description) {
        this.user_description = user_description;
    }
}
