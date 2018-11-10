package com.example.android.diego_movies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.diego_movies.R;
import com.example.android.diego_movies.movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class movies_adapter extends ArrayAdapter<movies> {

    public movies_adapter(Context mContext, ArrayList<movies>lMovies){
        super(mContext,0,lMovies);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        movies movie = getItem(position);
        //Log.d("THIS ARE MY MOVIES", movie.getMainTitle());
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_items,parent,false);

        }

        //Setting the image to the imageview
        ImageView movieImage = (ImageView)convertView.findViewById(R.id.movie_image);

        assert movie != null;
        Picasso.with(getContext())
                .load(movie.getPosterImage())
                .into(movieImage);

        //Setting the title to the textview
        TextView mTitle = (TextView)convertView.findViewById(R.id.movie_title);
        mTitle.setText(movie.getMainTitle());
        return convertView;
    }
}
