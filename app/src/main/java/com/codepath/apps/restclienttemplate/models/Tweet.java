package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public String mediaUrl;
    public int tweetId;

    public Tweet() {} // empty constructor for parcel

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.tweetId = jsonObject.getInt("id");

        // left off after getting the img url
        // only putting in image if one exists?
        // detail view?
        try {
            // do everything you need to  in order to pull out the media url
            JSONObject entities = jsonObject.getJSONObject("entities");
            JSONArray mediaArray = entities.getJSONArray("media");
            JSONObject mediaHttpsObj = (JSONObject) mediaArray.get(0);
            String mediaHttpsUrl = mediaHttpsObj.getString("media_url_https");
            tweet.mediaUrl = mediaHttpsUrl;
        } catch(JSONException e) {
            // if an error occurs, this code will be executed
            Log.i("media", "no media for this tweet");
        }

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i< jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    // manipulate created at
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            int flag = DateUtils.FORMAT_ABBREV_RELATIVE;
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, flag).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String getBody() {
        return body;
    }

    public User getUser() {
        return user;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public int getTweetId() {
        return tweetId;
    }
}
