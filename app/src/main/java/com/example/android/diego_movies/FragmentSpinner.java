package com.example.android.diego_movies;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.diego_movies.Adapters.movies_adapter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class FragmentSpinner extends Fragment {

    private movies_adapter mAdapter;
    private GridView mainGrid;
    FragmentActivity listener;


    public FragmentSpinner() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView= inflater.inflate(R.layout.fragment_fragment_spinner, container, false);
        mainGrid = fragmentView.findViewById(R.id.main_gridview);
        return fragmentView;
    }

    //Here we make the connection betwen main activity and the fragmentSpinner
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //Here we create the movies for most popular option
    public void setPopularity(){
        URL popularityUrl = urlBuild.create_url(urlBuild.POPULAR_MOVIES);
        String popularityString = popularityUrl.toString();
        try {
            responseToString(popularityString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Here we create the movies for top rated movies option
    public void setTopRated(){
        URL topRatedUrl = urlBuild.create_url(urlBuild.TOP_RATED);
        String topRatedString = topRatedUrl.toString();
        try {
            responseToString(topRatedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Here we query the database if the database have favorite movies
    public void setFavorites(){
        // We instantiate the database, and the executor to make the db query.
        MainViewModel vModel = ViewModelProviders.of(this).get(MainViewModel.class);
        vModel.getFavoriteList().observe(this, new Observer<List<movies>>() {
            @Override
            public void onChanged(@Nullable List<movies> movies) {
                assert movies != null;
                final ArrayList<movies> favArrayList = new ArrayList<movies>(movies);
                setClickAndAdapter(listener,favArrayList);
            }
        });
    }

        //Method to make a request, create movies objects and set this to an adapter
        public void responseToString(String url)throws IOException {
            OkHttpClient myClient = new OkHttpClient();
            Log.d("THIS IS MY URL",url);
            Request myRequest = new Request.Builder()
                    .url(url)
                    .build();
            myClient.newCall(myRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String myResponse = response.body().string();
                    if(!response.isSuccessful()) {
                        throw new IOException();
                    }

                    listener.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Instantiate the adapter
                            ArrayList<movies> testJson = jsonParser.movieParser(myResponse);
                            setClickAndAdapter(listener,testJson);
                        }
                    });

                }
            });
        }

 //Method to create and set the adapter, also creates the intent to conect with movies_info activity
    public void setClickAndAdapter(Context activityContext, ArrayList<movies>movieObjects){
        mAdapter = new movies_adapter(activityContext,movieObjects);
        mainGrid.setAdapter(mAdapter);
        mainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                movies selectedItem = (movies)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(),MoviesInfo.class);
                // Variables for Intent.
                intent.putExtra("movies",selectedItem);
                startActivity(intent);
            }
        });
    }
}
