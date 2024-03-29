package com.moringa.myrestaurants.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringa.myrestaurants.Constants;
import com.moringa.myrestaurants.R;
import com.moringa.myrestaurants.models.Business;
import com.moringa.myrestaurants.models.Category;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantDetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RestaurantDetailFragment.class.getSimpleName();

    @BindView(R.id.restaurantImageView) ImageView mImageLabel;
    @BindView(R.id.restaurantNameTextView) TextView mNameLabel;
    @BindView(R.id.cuisineTextView) TextView mCategoriesLabel;
    @BindView(R.id.ratingTextView) TextView mRatingLabel;
    @BindView(R.id.websiteTextView) TextView mWebsiteLabel;
    @BindView(R.id.phoneTextView) TextView mPhoneLabel;
    @BindView(R.id.addressTextView) TextView mAddressLabel;
    @BindView(R.id.saveRestaurantButton) TextView mSaveRestaurantButton;

    // TODO: Rename and change types of parameters
    private Business restaurant;

    public RestaurantDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantDetailFragment newInstance(Business restaurant) {
        RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("restaurant", Parcels.wrap(restaurant));
        restaurantDetailFragment.setArguments(args);
        return restaurantDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurant = Parcels.unwrap(getArguments().getParcelable("restaurant"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.get().load(restaurant.getImageUrl()).into(mImageLabel);

        List<String> categories = new ArrayList<>();

        for (Category category: restaurant.getCategories()) {
            categories.add(category.getTitle());
        }

        mNameLabel.setText(restaurant.getName());
        mCategoriesLabel.setText(android.text.TextUtils.join(", ", categories));
        mRatingLabel.setText(Double.toString(restaurant.getRating()) + "/5");
        mPhoneLabel.setText(restaurant.getPhone());
        mAddressLabel.setText(restaurant.getLocation().toString());

        //Setting up of Implicit intent
        mWebsiteLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);
        mAddressLabel.setOnClickListener(this);
        mSaveRestaurantButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mWebsiteLabel) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(restaurant.getUrl()));
            startActivity(webIntent);
        }

        if (v == mPhoneLabel){
            Intent PhoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + restaurant.getPhone()));
            startActivity(PhoneIntent);
        }

        if (v == mAddressLabel){
            Intent addressLabel = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+ restaurant.getCoordinates().getLatitude() +
                    "," + restaurant.getCoordinates().getLongitude()+
                    "?q=(" + restaurant.getName() + ")"));
            startActivity(addressLabel);
        }
        if (v == mSaveRestaurantButton) {
            DatabaseReference databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_SAVED_RESTAURANTS);
            databaseReference.push().setValue(restaurant);
            Log.d(TAG, "onClick: Saved Succesfully");
            Toast.makeText(getContext(),"Restaurant saved succesfully", Toast.LENGTH_LONG).show();
        }


    }
}