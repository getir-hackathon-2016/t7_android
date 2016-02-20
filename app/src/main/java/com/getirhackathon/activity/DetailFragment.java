package com.getirhackathon.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.getirhackathon.App;
import com.getirhackathon.R;
import com.getirhackathon.model.Product;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends BlurDialogFragment {


    public DetailFragment() {
        // Required empty public constructor
    }


    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView counterTextView;
    private ImageView upButton;
    private ImageView downButton;
    private ImageView addButton;
    private Product product;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        product = new Gson().fromJson(getArguments().getString("selectedProduct"),Product.class);
        upButton = (ImageView) v.findViewById(R.id.upButton);
        downButton = (ImageView) v.findViewById(R.id.downButton);
        addButton = (ImageView) v.findViewById(R.id.addButton);
        productNameTextView = (TextView) v.findViewById(R.id.productNameTextView);
        productPriceTextView = (TextView) v.findViewById(R.id.productPriceTextView);
        counterTextView = (TextView) v.findViewById(R.id.counterTextView);
        productImageView = (ImageView) v.findViewById(R.id.productImageView);

        ImageLoader.getInstance().displayImage(product.getImgUrl(), productImageView);
        productNameTextView.setText(product.getName());
        productPriceTextView.setText(product.getPrice());
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(counterTextView.getText().toString()) + 1;
                counterTextView.setText(count + "");

                productPriceTextView.setText(Float.parseFloat(product.getPrice()) * count + "");
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(counterTextView.getText().toString()) - 1;
                counterTextView.setText(count + "");
                productPriceTextView.setText(Float.parseFloat(product.getPrice())*count + "");
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().addToSepet(product,Integer.parseInt(counterTextView.getText().toString()));
                DetailFragment.this.dismiss();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }


}
