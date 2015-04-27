package com.example.findnewfriends.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findnewfriends.R;
import com.example.findnewfriends.model.Tweet;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter<Tweet> {

    private Context context;
    private List<Tweet> tweetsList;

    public ProfileAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        this.context = context;
        this.tweetsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_profile, parent, false);


        Tweet tweet = tweetsList.get(position);
        TextView tv1 = (TextView) view.findViewById(R.id.textView1);
        tv1.setText(tweet.getUser_description());
        TextView tv2 = (TextView) view.findViewById(R.id.textView2);
        tv2.setText(tweet.getUser_screen_name());
        ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        image.setImageBitmap(tweet.getProfile_pic());


        return view;
    }

}