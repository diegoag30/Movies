package com.example.android.diego_movies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class jsonParser {
    //jsonParser is a method that loop over an arraylist that comes form the response
    // And creates new movies object, to later append it into an arrayList.
    public static ArrayList<movies> movieParser(String jsonString){
        try{
            JSONObject rawData = new JSONObject(jsonString);
            JSONArray moviesList = rawData.getJSONArray("results");
            ArrayList<movies> theList = new ArrayList<movies>();
            //Here I create a for loop to iterate the movies and get the values.
            for(int i=0; i<moviesList.length();i++){

                JSONObject currentMovie = moviesList.getJSONObject(i);
                String title = currentMovie.getString("title");
                String date = currentMovie.getString("release_date");
                String image = currentMovie.getString("poster_path");
                String votes = currentMovie.getString("vote_average");
                String synopsys = currentMovie.getString("overview");
                String id = currentMovie.getString("id");
                // After getting all the values, we create a new movie object and add this to the list.

                movies objects = new movies(title,date,image,votes,synopsys,id);
                objects.createImage();
                theList.add(objects);
            }
            return theList;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    //Parse the request and get the YT key
    public static String trailerParser(String movieIdUrl){
        try {
            JSONObject rawTrailerData = new JSONObject(movieIdUrl);
            JSONArray trailerList = rawTrailerData.getJSONArray("results");
            JSONObject trailerInfo = trailerList.getJSONObject(0);
            String trailerKey = trailerInfo.getString("key");
            return trailerKey;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    };

    //Method to parse the request and create the reviews data to use later in the adapter
    public static ArrayList<Review> reviewParser(String rJsonUrl){
        try {
            JSONObject rawReviewData = new JSONObject(rJsonUrl);
            JSONArray reviewsList = rawReviewData.getJSONArray("results");
            ArrayList<Review> reviews = new ArrayList<Review>();

            for(int i=0; i<reviewsList.length();i++){
                JSONObject currentReview = reviewsList.getJSONObject(i);
                String userName = currentReview.getString("author");
                String content = currentReview.getString("content");

                Review reviewObj = new Review(userName,content);
                reviews.add(reviewObj);
            }
            return reviews;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
