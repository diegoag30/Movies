package com.example.android.diego_movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.diego_movies.Adapters.ReviewsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReviewsActivity extends AppCompatActivity {
    public ListView reviews_lv;
    public TextView reviews_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);


        Intent start = getIntent();
        movies passedMovie = Objects.requireNonNull(start.getExtras()).getParcelable("MOVIE");
        setReviewsUi(passedMovie);
    }
    //Set the UI for the reviewsActivity
    public void setReviewsUi(movies selectedMovie){

        assert selectedMovie != null;

        // Getting the Reviews URL and making the request
        String getReviews = urlBuild.getReviewsUrl(selectedMovie.getMovieId());
        try {
            reviewsRequest(getReviews,selectedMovie.getMovieId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        reviews_tv = (TextView)findViewById(R.id.reviews_textView);
        reviews_tv.setText(String.format("Reviews for %s", selectedMovie.getMainTitle()));

    }
    // Make the response and get the reviews, also creates the adapter and set this
    public void reviewsRequest(String url, String id) throws IOException {
        OkHttpClient tClient = new OkHttpClient();
        Request myRequest = new Request.Builder()
                .url(url)
                .build();
        tClient.newCall(myRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                if (!response.isSuccessful()) {
                    throw new IOException();

                }
                ReviewsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Instantiate the Reviews ArrayList and set the adapter.
                        ArrayList<Review> reviewsPassed = jsonParser.reviewParser(myResponse);
                        setReviewsAdapter(ReviewsActivity.this,reviewsPassed);

                    }
                });

            }
        });
    }
    // Create the adapter and set this.
    public void setReviewsAdapter(Context rContext, ArrayList<Review>reviewsList){
        ReviewsAdapter rList = new ReviewsAdapter(rContext,reviewsList);
        reviews_lv =(ListView)findViewById(R.id.reviews_lv);
        reviews_lv.setAdapter(rList);

    }

}
