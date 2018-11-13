package com.example.android.diego_movies;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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

    public movies_adapter mAdapter;
    private GridView mainGrid;
    public movies_adapter anotherAdapter;
    public movies_adapter favAdapter;
    FragmentActivity listener;
    private ArrayList<movies>moviesItems;
    private ArrayList<movies>moviesItemsTR;
    private int gridPosition;




    public FragmentSpinner() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //When the activity recreates,
        if(savedInstanceState != null) {
            moviesItems = savedInstanceState.getParcelableArrayList("myAdapter");
            gridPosition = savedInstanceState.getInt("myGridPosition");
            moviesItemsTR = savedInstanceState.getParcelableArrayList("anotherAdapter");
        }
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mAdapter != null){
            outState.putParcelableArrayList("myAdapter",mAdapter.getItems());
        }
        if (anotherAdapter != null){
            outState.putParcelableArrayList("anotherAdapter",anotherAdapter.getItems());
        }
        gridPosition = mainGrid.getFirstVisiblePosition();
        outState.putInt("myGridPosition",gridPosition);

    }

    //Here we create the movies for most popular option
    public void setPopularity(){
        URL popularityUrl = urlBuild.create_url(urlBuild.POPULAR_MOVIES);
        String popularityString = popularityUrl.toString();
        if(moviesItems== null){

               /* responseToString(popularityString,mAdapter,moviesItems);*/
                OkHttpClient myClient = new OkHttpClient();
                Log.d("THIS IS MY URL",popularityString);
                Request myRequest = new Request.Builder()
                        .url(popularityString)
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
                                moviesItems = jsonParser.movieParser(myResponse);
                                popularityClickAndAdapter(listener,moviesItems);
                            }
                        });
                    }
                });
            }
         else{
            popularityReCreate(listener,moviesItems);
        }

    }
    //Here we create the movies for top rated movies option
    public void setTopRated(){
        URL topRatedUrl = urlBuild.create_url(urlBuild.TOP_RATED);
        String topRatedString = topRatedUrl.toString();
        if(moviesItemsTR== null){
/*                responseToString(topRatedString,anotherAdapter,moviesItemsTR);*/
                OkHttpClient myClient = new OkHttpClient();
                Log.d("THIS IS MY URL",topRatedString);
                Request myRequest = new Request.Builder()
                        .url(topRatedString)
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
                                moviesItemsTR = jsonParser.movieParser(myResponse);
                                topRatedClickAndAdapter(listener,moviesItemsTR);
                            }
                        });
                    }
                });
        } else {
            topRatedReCreate(listener,moviesItemsTR);
        }

    }

    // Here we query the database if the database have favorite movies
    public void setFavorites(){
        // We instantiate the database, and the executor to make the db query.

        final MainViewModel vModel = ViewModelProviders.of(this).get(MainViewModel.class);

        vModel.getFavoriteList().observe(this, new Observer<List<movies>>() {
            @Override
            public void onChanged(@Nullable List<movies> movies) {

                assert movies != null;
                final ArrayList<movies> favArrayList = new ArrayList<movies>(movies);
                favClickAndAdapter(listener,favArrayList);

            }
        });

    }

 //Method to create and set the adapter, also creates the intent to conectt with movies_info activity
    public void setClickAndAdapter(Context activityContext, ArrayList<movies>movieObjects, movies_adapter adapter){
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

    // Sorry for the non automated code, i couldn't pass the global adapters variables as an argument
    // to retrieve their values in OnsaveInstance.

    // Set the favorites movies to the Gridview
    public void favClickAndAdapter(Context activityContext, ArrayList<movies>movieObjects){
        favAdapter = new movies_adapter(activityContext,movieObjects);
        mainGrid.setAdapter(favAdapter);
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

    public void popularityClickAndAdapter(Context activityContext, ArrayList<movies>movieObjects){
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

    public void topRatedClickAndAdapter(Context activityContext, ArrayList<movies>movieObjects){
        anotherAdapter = new movies_adapter(activityContext,movieObjects);
        mainGrid.setAdapter(anotherAdapter);
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

    public void popularityReCreate(Context fragmentCtx,ArrayList<movies>moviesArray) {
        mAdapter = new movies_adapter(fragmentCtx, moviesArray);
        mainGrid.setAdapter(mAdapter);
        mainGrid.setSelection(gridPosition);
        mainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                movies selectedItem = (movies) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), MoviesInfo.class);
                // Variables for Intent.
                intent.putExtra("movies", selectedItem);
                startActivity(intent);
            }

        });
    }

    public void topRatedReCreate(Context fragmentCtx,ArrayList<movies>moviesArray){
        anotherAdapter = new movies_adapter(fragmentCtx,moviesArray);
        mainGrid.setAdapter(anotherAdapter);
        mainGrid.setSelection(gridPosition);
        mainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                movies selectedItem = (movies) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), MoviesInfo.class);
                // Variables for Intent.
                intent.putExtra("movies", selectedItem);
                startActivity(intent);
            }

        });
    }


}
