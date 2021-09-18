package com.moringa.myrestaurants.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.FirebaseDataSource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringa.myrestaurants.Constants;
import com.moringa.myrestaurants.R;
import com.moringa.myrestaurants.adapters.FirebaseViewHolder;
import com.moringa.myrestaurants.models.Business;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantActivity extends AppCompatActivity {
    //Initialise Firebase reference
    private DatabaseReference reference;
    //Import the FirebaseViewHolder class;
    private FirebaseRecyclerAdapter<Business, FirebaseViewHolder> firebaseViewHolderFirebaseRecyclerAdapter;

    @BindView(R.id.recycler1View) RecyclerView recyclerView;
    @BindView(R.id.progressBar1) ProgressBar progressBar;
    @BindView(R.id.error1TextView) TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_restaurant);

        ButterKnife.bind(this);

        //set the Reference using the "restaurants" child node key from our Constants class.
        reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_SAVED_RESTAURANTS);
        //calling the setup firebase method here
        setFirebaseAdapter();
        //
        showRestaurants();
        hideProgressBar();

    }

    //Method for setting up firebaseAdapter
    private void setFirebaseAdapter(){
        FirebaseRecyclerOptions<Business> options = new FirebaseRecyclerOptions.Builder<Business>()
                .setQuery(reference, Business.class)
                .build();
        firebaseViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Business, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull Business model) {
                //Binding our view with the given content from b/s model
                holder.bindViewFromFirebase(model);
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_restaurant_list, parent, false);
                return new FirebaseViewHolder(view);
            }

        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(firebaseViewHolderFirebaseRecyclerAdapter);
    }

    //Method to start listening
    protected void onStart(){
        super.onStart();
        firebaseViewHolderFirebaseRecyclerAdapter.startListening();
    }

    //Method to start Stopping
    protected void onStop() {
        super.onStop();
        if (firebaseViewHolderFirebaseRecyclerAdapter != null){
            firebaseViewHolderFirebaseRecyclerAdapter.stopListening();
        }
    }
    private void showRestaurants() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}