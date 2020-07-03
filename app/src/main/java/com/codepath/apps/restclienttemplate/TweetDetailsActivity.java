package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {

    // tweet to display
    Tweet tweet;
    User user;
    TextView tvUserDetail;
    TextView tvScreenname;
    TextView tvBodyDetail;
    ImageView ivProfileDetail;
    ImageView ivMediaDetail;
    ImageView ivLike;
    ImageView ivUnlike;
    Button btnRetweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        client = TwitterApp.getRestClient(this);

        // resolve view objects
        tvUserDetail = findViewById(R.id.tvUserDetail);
        tvScreenname = findViewById(R.id.tvScreenname);
        tvBodyDetail = findViewById(R.id.tvBodyDetail);
        ivProfileDetail = findViewById(R.id.ivProfileDetail);
        ivMediaDetail = findViewById(R.id.ivMediaDetail);
        ivLike = findViewById(R.id.ivLike);
        ivUnlike = findViewById(R.id.ivUnlike);
        btnRetweet = findViewById(R.id.btnRetweet);


        // get tweet and user
        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        user = tweet.getUser();

        // fill in
        tvUserDetail.setText(user.getName());
        tvScreenname.setText("@"+user.getScreenName());
        tvBodyDetail.setText(tweet.getBody());

        // set photos
        Glide.with(this)
                .load(user.getProfileImageUrl())
                .transform(new RoundedCornersTransformation(15, 3))
                .into(ivProfileDetail);

        if(tweet.mediaUrl == null) {
            ivMediaDetail.setVisibility(View.GONE);
        }
        else {
            ivMediaDetail.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(tweet.getMediaUrl())
                    .into(ivMediaDetail);
        }

        // like
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TweetDetailsActivity", "here at like");
                client.likeTweet(tweet.getTweetId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(TweetDetailsActivity.this, "Liked!", Toast.LENGTH_SHORT).show();
                     // change heart image?
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("TweetDetailsActivity", "onFailure TweetActivity like", throwable);
                    }
                });

            }
        });

        ivUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.unlikeTweet(tweet.getTweetId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(TweetDetailsActivity.this, "Unliked!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("TweetDetailsActivity", "onFailure TweetActivity unlike");
                    }
                });
            }
        });

        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.retweet(tweet.getTweetId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(TweetDetailsActivity.this, "Retweeted!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("TweetDetailsActivity", "angghhahableh", throwable);
                    }
                });
            }
        });


    }
}