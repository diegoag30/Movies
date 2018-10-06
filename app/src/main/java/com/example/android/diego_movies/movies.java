package com.example.android.diego_movies;

import android.net.Uri;

public class movies {
    private String mainTitle;
    private String releaseDate;
    private String posterImage;
    private String averageVotes;
    private String overview;

    public movies(String title, String date, String image, String votes, String synopsys){
            this.mainTitle = title;
            this.releaseDate = date;
            this.posterImage = createImage(image);
            this.averageVotes = votes;
            this.overview = synopsys;
    }

    //Create the image url

    private String createImage(String imagePath){
        String baseImageUrl = "http://image.tmdb.org/t/p/";
        String imageSize = "w185";
        Uri builder = Uri.parse(baseImageUrl).buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(imagePath)
                .build();
        String mUrl = builder.toString();
        return mUrl;
    }
    // Getter Methods

    public String getImage(){
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
}

