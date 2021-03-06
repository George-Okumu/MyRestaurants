package com.moringa.myrestaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moringa.myrestaurants.R;
import com.moringa.myrestaurants.models.Business;
import com.moringa.myrestaurants.ui.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private List<Business> mRestaurants;
    private Context mContext;// We need this to create the viewHolder

    public RestaurantListAdapter(Context context, List<Business> restaurants) {
        mContext = context;
        mRestaurants = restaurants;
    }


//Inflates the layout
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    //updates the contents of the ItemView to reflect the restaurant in the given position.
    @Override
    public void onBindViewHolder(RestaurantListAdapter.RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));
    }

    //Sets the number of items the adapter will display
    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class RestaurantViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @BindView(R.id.restaurantsNameTextView) TextView mRestaurantsNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;

        private Context mContext;


        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //used getAdapterPosition instead of layoutPosition
            int itemPosition = getAdapterPosition();
            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
            mContext.startActivity(intent);
        }


        public void bindRestaurant(Business restaurant){
            Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);// allows Picasso to handle the image loading
            mRestaurantsNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating "+ restaurant.getRating() + "/5");
        }

    }

}
