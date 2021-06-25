package com.example.flixster.models;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

@Parcel
public class Movie implements Comparable<Movie>{

    private static final String TRAILER_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=%s&language=en-US";
    private static final String TAG = "Movie";

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Double popularity;
    String videoId;

    public Movie() {}

    public Movie(JSONObject jsonObject, Context context) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        popularity = jsonObject.getDouble("popularity");
        int id = jsonObject.getInt("id");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILER_URL, id, context.getString(R.string.tmdb_api_key)), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    videoId = results.getJSONObject(0).getString("key");
                    Log.d(TAG, videoId);
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray, Context context) throws JSONException {
        List<Movie> movies = new ArrayList();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i), context));
        }
        return movies;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int compareTo(Movie o) {
        return Double.compare(o.popularity, this.popularity);
    }
}
