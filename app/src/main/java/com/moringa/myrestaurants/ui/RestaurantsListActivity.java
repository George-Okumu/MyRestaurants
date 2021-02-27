package com.moringa.myrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.moringa.myrestaurants.Constants;
import com.moringa.myrestaurants.adapters.RestaurantListAdapter;
import com.moringa.myrestaurants.models.Business;
import com.moringa.myrestaurants.R;
import com.moringa.myrestaurants.YelpBusinessesSearchResponse;
import com.moringa.myrestaurants.network.YelpApi;
import com.moringa.myrestaurants.network.YelpClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsListActivity extends AppCompatActivity {
    private static final String TAG = RestaurantsListActivity.class.getSimpleName();

//    private SharedPreferences mSharedPreference;
    private String mAddress;

    @BindView(R.id.recyclerView) RecyclerView mRecycleView;
    private RestaurantListAdapter mAdapter;
    public List<Business> restaurants;

    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);


//        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
//        mAddress = mSharedPreference.getString(Constants.PREFERENCES_LOCATION_KEY, null);
//        Log.d("Shared Pref Location", mAddress);
//        if(mAddress != null){
//
//        }

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        YelpApi client = YelpClient.getClient();

        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");

        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    restaurants = response.body().getBusinesses();
                    mAdapter = new RestaurantListAdapter(RestaurantsListActivity.this, restaurants);
                    mRecycleView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantsListActivity.this);
                    mRecycleView.setLayoutManager(layoutManager);
                    mRecycleView.setHasFixedSize(true);// informs recycle view that its width and height should remain the same.

                    showRestaurants();
                }
                else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                hideProgressBar();
                showFailureMessage();
            }

        });
    }

    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showRestaurants() {
        mRecycleView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}