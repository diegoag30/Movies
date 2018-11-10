package com.example.android.diego_movies;

import android.app.ListActivity;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

@Entity(tableName="favorites")
public class movies implements Parcelable {
    //Her we define all the methods and attributes for a movie.
    private String mainTitle;
    private String releaseDate;
    private String posterImage;
    private String averageVotes;
    private String overview;
    @PrimaryKey
    @NonNull
    private String movieId;
    private String movieTrailer;

    public movies(String mainTitle, String releaseDate, String posterImage, String averageVotes, String overview,
                  @NonNull String movieId){
            this.mainTitle = mainTitle;
            this.releaseDate = releaseDate;
            this.posterImage = posterImage;
            this.averageVotes = averageVotes;
            this.overview = overview;
            this.movieId = movieId;
    }


    //Create the image url

    public   void createImage(){
        String baseImageUrl = "http://image.tmdb.org/t/p/";
        String imageSize = "w185";
        Uri builder = Uri.parse(baseImageUrl).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(posterImage)
                .build();
        String mUrl = builder.toString();
        this.posterImage = mUrl;
    }
    // Getter Methods

    public String getPosterImage(){
        return posterImage;
    }

    public String getMainTitle(){
        return mainTitle;
    }

    public String getAverageVotes() {
        return averageVotes;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public String getMovieTrailer() {
        return movieTrailer;
    }

    // Setter Methods

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public void setAverageVotes(String averageVotes) {
        this.averageVotes = averageVotes;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    public void setMovieTrailer(String movieTrailer) {
        this.movieTrailer = movieTrailer;
    }

    //Parcel Methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mainTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(averageVotes);
        parcel.writeString(overview);
        parcel.writeString(posterImage);
        parcel.writeString(movieId);
        parcel.writeString(movieTrailer);
    }

    public movies(Parcel parcel){
        mainTitle = parcel.readString();
        releaseDate = parcel.readString();
        averageVotes = parcel.readString();
        overview = parcel.readString();
        posterImage = parcel.readString();
        movieId = parcel.readString();
        movieTrailer = parcel.readString();
    }

    public static final Parcelable.Creator<movies>CREATOR = new Parcelable.Creator<movies>(){

        @Override
        public movies createFromParcel(Parcel parcel){
            return new movies(parcel);
        }

        @Override
        public movies[] newArray(int i) {
            return new movies[0];
        }
    };
}

