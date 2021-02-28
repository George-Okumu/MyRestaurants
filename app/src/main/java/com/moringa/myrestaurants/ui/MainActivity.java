package com.moringa.myrestaurants.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringa.myrestaurants.Constants;
import com.moringa.myrestaurants.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moringa.myrestaurants.Constants.FIREBASE_LOCATION_SEARCHED_LOCATION;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = " in MainActivity";
//    private Button mFindRestaurantsButton;
//    private EditText mLocationEditText;


//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    //Calling the get instance method to access the database
//    DatabaseReference ref = database.getReference();

    

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    private DatabaseReference mSearchedLocationReference;

    @BindView(R.id.appNameTextView) TextView mAppNameTextView;
    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.locationEditText) EditText mLocationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FIREBASE_LOCATION_SEARCHED_LOCATION);

//        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/ostrich-regular.ttf");
//        mAppNameTextView.setTypeface(ostrichFont);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mFindRestaurantsButton.setOnClickListener(this);
    }
            @Override
            public void onClick(View v) {
        if(v == mFindRestaurantsButton){

            String location = mLocationEditText.getText().toString();
            addToSharedPreferences(location);
            Intent intent = new Intent(MainActivity.this, RestaurantsListActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }
            }


    public void saveLocationToFirebase(String location) {
        mSearchedLocationReference.setValue(location);
    }

    public void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();

    }
}