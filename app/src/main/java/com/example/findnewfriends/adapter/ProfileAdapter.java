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
import com.example.findnewfriends.model.UserProfile;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter<UserProfile> {

    private Context context;
    private List<UserProfile> profileList;

    public ProfileAdapter(Context context, int resource, List<UserProfile> objects) {
        super(context, resource, objects);
        this.context = context;
        this.profileList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_profile, parent, false);


        UserProfile profile = profileList.get(position);
        TextView tv1 = (TextView) view.findViewById(R.id.textView1);
        tv1.setText(profile.getUser_description());
        TextView tv2 = (TextView) view.findViewById(R.id.textView2);
        tv2.setText(profile.getUser_screen_name());
        ImageView image = (ImageView) view.findViewById(R.id.imageView1);
        image.setImageBitmap(profile.getProfile_pic());


        return view;
    }

}