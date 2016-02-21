package com.getirhackathon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getirhackathon.App;
import com.getirhackathon.MainActivity;
import com.getirhackathon.R;
import com.getirhackathon.activity.DetailFragment;
import com.getirhackathon.model.Product;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by erkam on 20.02.2016.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<Integer> mCountList = new ArrayList<>();
    private ArrayList<Product> mProductList = new ArrayList<>();
    private Context mContext;

    //Constructor
    public OrderAdapter(Context context, HashMap<Product,Integer> sepet) {

        mContext = context;
        Iterator<Product> iterator = sepet.keySet().iterator();
        while(iterator.hasNext()){
            Product product = iterator.next();
            Log.d("INFO",product.getName());
            mProductList.add(product);
            mCountList.add(sepet.get(product));
        }
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sepet_row_layout, parent, false);

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
        holder.productNameTextView.setText(mProductList.get(position).getName());
        holder.productPriceTextView.setText("₺ " + Float.parseFloat(mProductList.get(position).getPrice())
                                                                                * mCountList.get(position));
        holder.counterTextView.setText(App.getInstance().getSepet().get(mProductList.get(position)));
        ImageLoader.getInstance()
                .displayImage(mProductList.get(position).getImgUrl(), holder.productImageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    //Viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView productNameTextView;
        public TextView productPriceTextView;
        public TextView counterTextView;
        public ImageView productImageView;
        public RelativeLayout view;

        public ViewHolder(View v) {
            super(v);
            productNameTextView = (TextView) v.findViewById(R.id.productNameTextView);
            productPriceTextView = (TextView) v.findViewById(R.id.productPriceTextView);
            counterTextView = (TextView) v.findViewById(R.id.counterTextView);
            productImageView = (ImageView) v.findViewById(R.id.productImageView);
            view = (RelativeLayout) v.findViewById(R.id.view);



        }

    }

}
