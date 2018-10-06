package com.example.android.diego_movies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class url_build {
    private static final String apiKey = "api_key";
    private static final String myKey = "";
    private static final String baseUrl = "https://api.themoviedb.org/3/";
    public static final String TOP_RATED ="movie/top_rated";
    public static final String POPULAR_MOVIES = "movie/popular";

//This method create the URL for your request, and receives a String to change the search path.
    public static URL create_url(String sortBy){
        Uri myUri = Uri.parse(baseUrl).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(apiKey,myKey)
                .build();


        URL url = null;
        try {
            url = new URL(myUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    };

}
