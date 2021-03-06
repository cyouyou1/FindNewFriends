package com.example.findnewfriends.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.findnewfriends.model.HttpManager;
import com.example.findnewfriends.R;
import com.example.findnewfriends.model.StopWords;
import com.example.findnewfriends.model.Tag;
import com.example.findnewfriends.model.TagFrequency;
import com.example.findnewfriends.model.UserProfile;
import com.example.findnewfriends.parser.ProfileJSONParser;
import com.example.findnewfriends.view.TagCloudView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagCloudActivity extends Activity {

    private String searchUrl;
    private TagCloudView mTagCloudView;
    //TODO: Please change WINDOW_SIZE so that the tag cloud ball is centered on the screen.
    private static final int WINDOW_SIZE = 1800;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tagcloud);

        Intent SearchIntent = getIntent();
        searchUrl = SearchIntent.getStringExtra("URL");

        setTagCloud();

	}

    private void setTagCloud() {

        TagCloudTask task = new TagCloudTask ();
        task.execute(searchUrl);
    }

    private class TagCloudTask  extends AsyncTask<String, String, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            List<UserProfile> profileList = ProfileJSONParser.parseFeed(content);
            Map<String, Integer> words_count_map = new HashMap<>();
            StopWords stopWords = new StopWords();

            for (UserProfile profile : profileList) {
                String[] words = profile.getUser_description().toLowerCase().split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String s = words[i].replaceAll("[^a-z]", "");
                    if(s!= null && !stopWords.isStopWord(s)) {
                        if (words_count_map.keySet().contains(s)) {
                            Integer count = words_count_map.get(s) + 1;
                            words_count_map.put(s, count);
                        } else {
                            words_count_map.put(s, 1);
                        }
                    }

                }

            }

            List<Tag> mTagList = TagFrequency.createTags(words_count_map);
            return mTagList;
        }

        @Override
        protected void onPostExecute(List<Tag> tagList) {
            LinearLayout container = (LinearLayout) findViewById(R.id.container);
            mTagCloudView = new TagCloudView(TagCloudActivity.this, WINDOW_SIZE, WINDOW_SIZE, tagList, 0, 0); // passing
            mTagCloudView.requestFocus();
            mTagCloudView.setFocusableInTouchMode(true);
            container.addView(mTagCloudView);
        }
    }

}
