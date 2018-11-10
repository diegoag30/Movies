package com.example.android.diego_movies;

import android.net.Uri;
import java.net.MalformedURLException;
import java.net.URL;


public class urlBuild {
    // Strings for Movies DB URL
    private static final String apiKeyP = "api_key";
    //Api Key called from the build.gradle.
    private static final String myKey = BuildConfig.ApiKey;
    private static final String baseUrl = "https://api.themoviedb.org/3/";
    public static final String TOP_RATED ="movie/top_rated";
    public static final String POPULAR_MOVIES = "movie/popular";
    public static final String MOVIES = "movie";
    public static final String MOVIE_ID_VIDEOS = "videos";
    public static final String REVIEWS = "reviews";

    // Strings for Youtube URL
    private static final String YOUTUBE_BASE_URL ="https://www.youtube.com/watch";
    private static final String YOUTUBE_KEY="v";

//This method create the URL for your request, and receives a String to change the search path.
    public static URL create_url(String sortBy){
        Uri myUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(apiKeyP,myKey)
                .build();
        URL url = null;
        try {
            url = new URL(myUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    };

    //Here we get the url to make the review request
    public static String getReviewsUrl(String movieId) {
        Uri myUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(MOVIES)
                .appendEncodedPath(movieId)
                .appendEncodedPath(REVIEWS)
                .appendQueryParameter(apiKeyP, myKey)
                .build();
        URL url = null;
        String reviewsUrl = null;
        try {
            url = new URL(myUri.toString());
            reviewsUrl = url.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return reviewsUrl;
    }

    // Method to get the youtube URL(this doesn't include the YT keys)
    public static String getVideoUrl(String movieId){
        Uri myUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(MOVIES)
                .appendEncodedPath(movieId)
                .appendEncodedPath(MOVIE_ID_VIDEOS)
                .appendQueryParameter(apiKeyP,myKey)
                .build();
        URL url = null;
        String thisUrl = null;
        try {
            url = new URL(myUri.toString());
            thisUrl = url.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return thisUrl;
    };

    //Here we construct the final  YOUTUBE URL
    public static String youtubeUrl(String videoKey){
        Uri youtubeUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY,videoKey)
                .build();
        URL url = null;
        String urlString = null;
        try {
            url = new URL(youtubeUri.toString());
            urlString = url.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlString;
    };
}
