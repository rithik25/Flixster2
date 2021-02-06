package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;


    private static final String YOUTUBE_API_KEY="AIzaSyA08hRt9oNWFbnP3volqWahbEH8mnCzPJ8";
    public static final String VIDEOS_URL="https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle=findViewById(R.id.tvTitle);
        tvOverview=findViewById(R.id.tvOverview);
        ratingBar=findViewById(R.id.ratingBar);
        youTubePlayerView=findViewById(R.id.player);

        Movie movie= Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
               try {
                   JSONArray results = json.jsonObject.getJSONArray("results");
                   if(results.length()==0)
                   {
                       return;
                   }
                   String youtubeKey= results.getJSONObject(0).getString("key");
                   Log.d("DetailActivity", youtubeKey);
                   initializeYouTube(youtubeKey);
               } catch (JSONException e) {
                   Log.e("DetailActivity", "Failed to parse JSON", e);
               }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });


    }
    private void initializeYouTube(final String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {

                // do any work here to cue video, play video, etc.
                Log.d("DetailActivity", "onSuccess");
                youTubePlayer.cueVideo(youtubeKey);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailActivity", "onInitializationFailure");
            }
        });
    }

}
