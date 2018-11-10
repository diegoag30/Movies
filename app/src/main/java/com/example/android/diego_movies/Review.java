package com.example.android.diego_movies;

import android.os.Parcelable;
// Here we define the variables and methods that contains a review
public class Review {
    private String userName;
    private String reviewText;

    public Review(String userName, String reviewText){
        this.userName = userName;
        this.reviewText = reviewText;
    }

    //Getters
    public String getUserName() {
        return userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    //Setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
