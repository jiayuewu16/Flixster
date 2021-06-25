package com.example.flixster.models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Collections;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String VIDEO_ID = "video_id";

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.black));
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        binding.tvTitle.setText(movie.getTitle());
        binding.tvTitle.setTextColor(Color.YELLOW);
        binding.tvOverview.setText(movie.getOverview());
        binding.tvOverview.setTextColor(Color.WHITE);
        binding.rbVoteAverage.setRating((float)(movie.getVoteAverage()/2.0));

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 5; // crop margin, set to 0 for corners with no crop
        String imageURL = movie.getPosterPath();
        int placeholderPath = R.drawable.flicks_movie_placeholder;
        int playButtonPath = R.drawable.play_button_image;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageURL = movie.getBackdropPath();
            placeholderPath = R.drawable.flicks_backdrop_placeholder;
        }
        Glide.with(this)
                .load(imageURL)
                .placeholder(placeholderPath)
                .error(placeholderPath)
                .centerCrop()
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(binding.ivPoster);
        binding.ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                intent.putExtra(VIDEO_ID, movie.getVideoId());
                startActivity(intent);
            }
        });
    }
}