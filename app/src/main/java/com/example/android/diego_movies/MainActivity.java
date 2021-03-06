package com.example.android.diego_movies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.android.diego_movies.Adapters.movies_adapter;
import com.example.android.diego_movies.movies_database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    public Spinner sp1;
    public FragmentSpinner fSpinner;
    public static final String FRAGMENT_SPINNER_TAG = "fSpinner";
    public String itemSelected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            fSpinner = new FragmentSpinner();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,fSpinner,FRAGMENT_SPINNER_TAG)
                    .commit();
        }else {
            fSpinner = (FragmentSpinner)getSupportFragmentManager().findFragmentByTag(FRAGMENT_SPINNER_TAG);
        }

        //Handling the spinner onselectitem listener.Start whit the movies sorted by popularity
        sp1 = (Spinner) findViewById(R.id.sort_spinner);
        spinnerAction spTest = new spinnerAction(sp1);
        spTest.setSpinnerListener();


    }

    //Settings the selection options to the Spinner
    public class spinnerAction  {
        public Spinner spinner;
        public spinnerAction(Spinner spinner){
            this.spinner = spinner;
        }

        public void setSpinnerListener(){
            spinner.setOnItemSelectedListener(new SpinnerListener());
        }

        public class SpinnerListener  implements AdapterView.OnItemSelectedListener{

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = adapterView.getItemAtPosition(i).toString();
                // We Basically change the Url when we select an item of the spinner.
                // From POPULAR_MOVIES to TOP_RATED.
                switch (i) {
                    case 0:
                        fSpinner.setPopularity();
                        break;
                    case 1:
                        fSpinner.setTopRated();
                        break;
                    case 2:
                        fSpinner.createViewModel();
                         break;
                }                                              
                                                               
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }

    }
}

