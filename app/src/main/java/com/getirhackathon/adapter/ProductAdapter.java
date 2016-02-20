package com.getirhackathon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getirhackathon.R;
import com.getirhackathon.model.Product;

import java.util.ArrayList;

/**
 * Created by erkam on 20.02.2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private ArrayList<Product> mProductList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductAdapter(ArrayList<Product> products) {
        mProductList = products;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
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
        holder.txtFooter.setText(mProductList.get(position).getPrice());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProductList.size();
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageView;
        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.productNameTextView);
            txtFooter = (TextView) v.findViewById(R.id.productPriceTextView);
            imageView = (ImageView) v.findViewById(R.id.productImageView);
        }
    }

}
