package com.moringa.myrestaurants;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

public class MyRestaurantsArrayAdapter extends ArrayAdapter {
    private Context mContext;
    private String[] mRestaurants;
    private String[] mCategories;

    public MyRestaurantsArrayAdapter(Context mContext, int resource, String[] mRestaurants, String[] mCategories){
        super(mContext, resource);
        this.mContext = mContext;
        this.mRestaurants = mRestaurants;
        this.mCategories = mCategories;
    }

    @Override
    public Object getItem(int position) {
        String restaurant = mRestaurants[position];
        String categories = mCategories[position];
        return String.format("%s \nServes greta: %s", restaurant, categories);
    }

    @Override
    public int getCount() {
        return mRestaurants.length;
    }
}
