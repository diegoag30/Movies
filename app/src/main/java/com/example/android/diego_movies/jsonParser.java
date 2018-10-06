package com.example.android.diego_movies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class jsonParser {
    public static ArrayList<movies> movieParser(String jsonString){
        try{
            JSONObject rawData = new JSONObject(jsonString);
            JSONArray moviesList = rawData.getJSONArray("results");
            ArrayList<movies> theList = new ArrayList<movies>();
            //Here I create a for loop to iterate the movies and get the values.
            for(int i=0; i<moviesList.length();i++){

                JSONObject currentMovie = moviesList.getJSONObject(i);
                String title = currentMovie.getString("title");
                //Log.d("THIS IS THE TITLE",title+".");
                String date = currentMovie.getString("release_date");
                String image = currentMovie.getString("poster_path");
                String votes = currentMovie.getString("vote_average");
                String synopsys = currentMovie.getString("overview");
                // After getting all the values, we create a new movie object and add this to the list.

                movies objects = new movies(title,date,image,votes,synopsys);
                theList.add(objects);
            }
            //for (int a=0;a<=theList.size();a++){
                //Log.d("THIS IS A MOVIE", a+String.valueOf(theList.get(a).getImage()));
            //}
            return theList;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
