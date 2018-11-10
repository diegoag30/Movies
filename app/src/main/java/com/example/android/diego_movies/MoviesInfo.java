package com.example.android.diego_movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.diego_movies.movies_database.AppDatabase;
import com.example.android.diego_movies.movies_database.MoviesDAO;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MoviesInfo extends AppCompatActivity {
    private static final String BUTTON_STATE = "buttonState";
    TextView title_tv;
    TextView release_tv;
    TextView vote_average_tv;
    TextView synopsis_TV;
    Button trailer_btn;
    ImageView poster_image_iv;
    Button reviews_btn;
    String title;
    String releaseDate;
    String voteAverage;
    String plotSynopsys;
    String posterImage;
    String movieId;
    MaterialFavoriteButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_info);

        //For the layout I use some tips from http://www.aprendeandroid.com/l4/interface1.htm

        //Intent Instantiation
        Intent initIntent = getIntent();
        movies movie = initIntent.getParcelableExtra("movies");

        populateUI(movie);
    }

    //Method defined to verify if the String passes is not null, and then setText to the TextView.
    public static void setInfo(TextView textView, String stringKey) {
        if (stringKey != null) {
            textView.setText(stringKey);
        }
    }

    //Method that passes the image Url, and show it as an image
    public void setImage() {
        poster_image_iv = (ImageView) findViewById(R.id.poster_image);
        if (posterImage != null) {
            Picasso.with(getApplicationContext())
                    .load(posterImage)
                    .into(poster_image_iv);
        }
    }

    // Method that sets all the visible elements in the UI
    public void populateUI(final movies myMovie) {
        //Defining the Variables
        title = myMovie.getMainTitle();
        releaseDate = myMovie.getReleaseDate();
        posterImage = myMovie.getPosterImage();
        voteAverage = myMovie.getAverageVotes();
        plotSynopsys = myMovie.getOverview();
        movieId = myMovie.getMovieId();
        favoriteButton = (MaterialFavoriteButton) findViewById(R.id.favorite_button);


        // Getting the Trailer of the movie.
        String movieIdUrl = urlBuild.getVideoUrl(myMovie.getMovieId());
        try {
            trailerRequest(movieIdUrl,myMovie);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setting the on clik listener to the favorite button.
        favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                //If Button is clicked, inserts the movie to the database, else the movie will be removed.
                Context context = getApplicationContext();
                AppDatabase database = AppDatabase.createInstance(context);
                Executor favExecutor = AppExecutor.getInstance().mainExecutor();
                final MoviesDAO moviesDAO = database.getMoviesDAO();
                if (favorite) {
                    String toastText = "Added to Favorites";
                    Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
                    toast.show();
                    favExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            moviesDAO.insertMovie(myMovie);
                        }
                    });

                } else {
                    String toastText = "Movie removed from favorites";
                    Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_SHORT);
                    toast.show();
                    favExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            moviesDAO.deleteMovie(myMovie);
                        }
                    });

                }
            }
        });

        reviews_btn = (Button)findViewById(R.id.reviews_button);
        reviews_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewsIntent = new Intent(view.getContext(),ReviewsActivity.class);
                reviewsIntent.putExtra("MOVIE",myMovie);
                startActivity(reviewsIntent);
            }
        });

        title_tv = (TextView) findViewById(R.id.main_title);
        setInfo(title_tv, title);

        release_tv = (TextView) findViewById(R.id.release_date);
        setInfo(release_tv, releaseDate);

        vote_average_tv = (TextView) findViewById(R.id.average_vote);
        setInfo(vote_average_tv, voteAverage);

        synopsis_TV = (TextView) findViewById(R.id.overview);
        setInfo(synopsis_TV, plotSynopsys);

        setImage();


    }

    //Method to get te trailer the final trailer url and open it via implicit intent
    public void trailerRequest(String url, final movies passedMovie) throws IOException {
        OkHttpClient tClient = new OkHttpClient();
        Log.d("THIS IS MY URL", url);

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
                MoviesInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Instantiate the adapter, and set the youtube trailer to the movie.
                        String trailerKey = jsonParser.trailerParser(myResponse);
                        String trailer = urlBuild.youtubeUrl(trailerKey);
                        passedMovie.setMovieTrailer(trailer);
                        trailer_btn = (Button)findViewById(R.id.trailer_bt);
                        trailer_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openYoutubeTrailer(passedMovie.getMovieTrailer());
                            }
                        });
                    }
                });

            }
        });
    }

    //Here we save the button state to preserve it if we rotate the device
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final Boolean bState = favoriteButton.isFavorite();
        outState.putBoolean(BUTTON_STATE,bState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!= null){
            if(savedInstanceState.containsKey(BUTTON_STATE)){
                Boolean savedButtonState = savedInstanceState.getBoolean(BUTTON_STATE);
                favoriteButton.setFavorite(savedButtonState);
            }
        }
    }

    //Implicit Intent to open the movie trailer.
    public void openYoutubeTrailer(String url){
        Uri youtubeUri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,youtubeUri);
        if(intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);
        }
    }
}
