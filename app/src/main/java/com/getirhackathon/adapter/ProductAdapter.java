package com.getirhackathon.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getirhackathon.MainActivity;
import com.getirhackathon.R;
import com.getirhackathon.activity.DetailFragment;
import com.getirhackathon.model.Product;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by erkam on 20.02.2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<Product> mProductList;
    private Context mContext;

    //Constructor
    public ProductAdapter(Context context, ArrayList<Product> products) {
        mProductList = products;
        mContext = context;
    }


    public void add(int position, Product item) {
        mProductList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mProductList.indexOf(item);
        mProductList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_cell, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = mProductList.get(position).getName();
        holder.txtHeader.setText(mProductList.get(position).getName());
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });
        holder.txtFooter.setText("₺ " + mProductList.get(position).getPrice());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Info", mProductList.get(position).getName());
                fragmentJump(mProductList.get(position));
            }
        });
        ImageLoader.getInstance().displayImage(mProductList.get(position).getImgUrl(), holder.imageView);

    }

    private void fragmentJump(Product product) {
        DetailFragment mFragment = new DetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("selectedProduct", new Gson().toJson(product));
        mFragment.setArguments(mBundle);
        switchContent(mFragment);
    }

    public void switchContent(DetailFragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            DetailFragment frag = fragment;
            mainActivity.switchContent(frag);
        }

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    //Viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageView;
        public RelativeLayout view;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.productNameTextView);
            txtFooter = (TextView) v.findViewById(R.id.productPriceTextView);
            imageView = (ImageView) v.findViewById(R.id.productImageView);
            view = (RelativeLayout) v.findViewById(R.id.view);


        }

    }

}
