package com.example.findnewfriends.parser;

import com.example.findnewfriends.model.Tweet;
import com.example.findnewfriends.model.UserProfile;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileJSONParser {


        public static List<UserProfile> parseFeed(String content) {

            try {
                JSONArray ar = new JSONArray(content);
                List<UserProfile> profileList = new ArrayList<>();

                for (int i = 0; i < ar.length(); i++) {
                    JSONObject object = ar.getJSONObject(i);
                    UserProfile profile = new UserProfile();

                    profile.setProfile_image_url(object.getString("profile_image_url"));
                    profile.setUser_description(object.getString("user_description"));
                    profile.setUser_screen_name(object.getString("user_screen_name"));

                    if(!(object.getString("latLng").equals("null"))) {
                        double latitude = object.getJSONObject("latLng").getJSONArray("coordinates").getDouble(0);
                        double longitude = object.getJSONObject("latLng").getJSONArray("coordinates").getDouble(1);
                        profile.setLatLng(new LatLng(latitude,longitude));
                    }else{
                        profile.setLatLng(null);
                    }
                    profileList.add(profile);
                }

                return profileList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
}

