package com.moringa.myrestaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringa.myrestaurants.Constants;
import com.moringa.myrestaurants.R;
import com.moringa.myrestaurants.models.Business;
import com.moringa.myrestaurants.ui.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class FirebaseViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

    //The views of this view holder are displayed in savedRestaurantActivity



    private View view;
    private Context viewHolderContext;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        viewHolderContext = itemView.getContext();
        view.setOnClickListener(this);
    }

    public void bindViewFromFirebase(Business business) {
        ImageView imageview = (ImageView) view.findViewById(R.id.restaurantImageView) ;
        TextView nameView = (TextView) view.findViewById(R.id.restaurantsNameTextView);
        TextView categoryView = (TextView) view.findViewById(R.id.categoryTextView);
        TextView ratingView = (TextView) view.findViewById(R.id.ratingTextView);

        //binding the views
        Picasso.get().load(business.getImageUrl()).into(imageview);
        nameView.setText(business.getName());
        categoryView.setText(business.getCategories().get(0).getTitle());
        ratingView.setText("Rating: " + business.getRating().toString() + "/5");
    }

    @Override
    public void onClick(View v) {
        ArrayList<Business> mBussinessProperties = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_SAVED_RESTAURANTS);
        //Grabbing the current list of restaurants from the firebase
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    mBussinessProperties.add(snapshot1.getValue(Business.class));
                }

                int itemPosition = getLayoutPosition();
                //Passing this viewholders context to restaurantdetailactivity on onclick
                Intent intent = new Intent(viewHolderContext, RestaurantDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("restaurants", Parcels.wrap(mBussinessProperties));

                viewHolderContext.startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
