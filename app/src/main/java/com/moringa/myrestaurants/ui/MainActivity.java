package com.moringa.myrestaurants.ui;

import androidx.annotation.NonNull;
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
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringa.myrestaurants.Constants;
import com.moringa.myrestaurants.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.moringa.myrestaurants.Constants.FIREBASE_LOCATION_SEARCHED_LOCATION;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = " in MainActivity";

    //Initializing Event Listener for listening to the location node
    private ValueEventListener valueEventListener;
//Initializing the firebase referrence
   DatabaseReference reference;


    @BindView(R.id.appNameTextView) TextView mAppNameTextView;
    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.locationEditText) EditText mLocationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Add the instance of searched location database reference
        //instantiating it here
        //pass in Firebase_location_searched location a an argument.
        reference = FirebaseDatabase
                //gets instance of the firebasedatabase for a specific dfirebase appp
                .getInstance()
                //get reference gets dtabasereference for the database node
                .getReference()
                .child(FIREBASE_LOCATION_SEARCHED_LOCATION);

        //Call the addValueEventListener to the database reference,
        // Assign the valueEventListener to the reference of the location so that it
        //Should not return nullPointerException of the listener.
        //Snapshot is now stored in the listener so that it can be destroyed.
         valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Snapshot is the data that changed on our node
                //Basically we are adding listener to check the data which is being saved in our node
                for (DataSnapshot searchedSnapshot : snapshot.getChildren()){
                    //Getting the value of the updated location in our node.
                    String location = searchedSnapshot.getValue().toString();
                    Log.d(TAG, "onDataChange: This is the data that is updated/added into our method: " + location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " +  error );

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);



//        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/ostrich-regular.ttf");
//        mAppNameTextView.setTypeface(ostrichFont);






        mFindRestaurantsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mFindRestaurantsButton) {

            String location = mLocationEditText.getText().toString();
            //Saving the searched location to firebase
            //Push() method ensures each entry is added to the node and saved
            reference.push().setValue(location);

            Intent intent = new Intent(MainActivity.this, RestaurantsListActivity.class);
            intent.putExtra("location", location);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // removing the eventListener from the node
        reference.removeEventListener(valueEventListener);
        Log.i(TAG, "onDestroy: App destroyed successfully");
    }
}