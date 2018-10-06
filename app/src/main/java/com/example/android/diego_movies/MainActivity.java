package com.example.android.diego_movies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String SORT_DESCENDANT ="&sort_by=popularity.desc";
    private movies_adapter mAdapter;
    private GridView mainGrid;
    private Spinner sp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handling the spinner onselectitem listener.Start whit the movies sorted by popularity
        sp1 = (Spinner) findViewById(R.id.sort_spinner);
        spinnerAction spTest = new spinnerAction(sp1);
        spTest.setSpinnerListener();


    }

//Here I create a class and a method to convert the request to a String and print it in a textView
//My code was based on https://github.com/codepath/android_guides/wiki/Using-OkHttp
public class handle_request {
        public OkHttpClient myClient = new OkHttpClient();

        public void responseToString(String url)throws IOException {
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
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Instantiate the adapter
                                ArrayList<movies> testJson = jsonParser.movieParser(myResponse);
                                //for (int a=0;a<testJson.size();a++){
                                //Log.d("THIS IS A MOVIE", a+String.valueOf(testJson.get(a).getImage()));
                                //}
                                mAdapter = new movies_adapter(MainActivity.this,testJson);
                                mainGrid = findViewById(R.id.main_gridview);
                                mainGrid.setAdapter(mAdapter);
                            }
                        });

                }
            });
        }
    }
    //HERE WE ARE GOING TO HANDLE HOW WE SORT THE MOVIES DEPENDING ON THE ITEM SELECTED.
    public class spinnerAction  {
        public Spinner spinner;
        public spinnerAction(Spinner spinner){
            this.spinner = spinner;
        }

        public void setSpinnerListener(){
            spinner.setOnItemSelectedListener(new SpinnerListener());
        }

        public class SpinnerListener implements AdapterView.OnItemSelectedListener{
            //Definir que se hace al seleccionar el item
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                switch (i) {
                    case 0:
                        URL popularityUrl = url_build.create_url(url_build.POPULAR_MOVIES);
                        String popularityString = popularityUrl.toString();
                        handle_request popularityRequest = new handle_request();
                        try {
                            popularityRequest.responseToString(popularityString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        URL topRatedUrl = url_build.create_url(url_build.TOP_RATED);
                        String topRatedString = topRatedUrl.toString();
                        handle_request topRatedRequest = new handle_request();
                        try {
                            topRatedRequest.responseToString(topRatedString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }


    }

}

