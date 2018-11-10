package com.example.android.diego_movies.Adapters;

import android.content.Context;
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
import com.example.android.diego_movies.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewsAdapter extends ArrayAdapter<Review> {
    public ReviewsAdapter(Context rContext, ArrayList<Review> lReviews){
        super(rContext,0,lReviews);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Review review = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_items,parent,false);

        }
        //Creating the view that contains the username who writes the review
        TextView userName_tv = (TextView) convertView.findViewById(R.id.Username);
        userName_tv.setText(review.getUserName());

        //Creating the view of the content of the review
        TextView contentReview_tv = (TextView) convertView.findViewById(R.id.ContentReview);
        contentReview_tv.setText(review.getReviewText());

        return convertView;
    }
}
