package com.getirhackathon.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getirhackathon.App;
import com.getirhackathon.MainActivity;
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
    //private ProgressDialog dialog = new ProgressDialog(getActivity());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        //Get product details from Arguments..
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

        if(App.getInstance().getSepet().containsKey(product))
            counterTextView.setText(App.getInstance().getSepet().get(product));


        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(counterTextView.getText().toString()) + 1;
                if(count<=10){
                    counterTextView.setText(count + "");
                }else{
                    Toast.makeText(getActivity(),"En fazla 10 adet sipariş verebilirsiniz.",Toast.LENGTH_SHORT);
                    productPriceTextView.setText(Float.parseFloat(product.getPrice()) * count + "");
                }


            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(counterTextView.getText().toString()) - 1;
                if(count>=1){
                    counterTextView.setText(count + "");
                    productPriceTextView.setText(Float.parseFloat(product.getPrice())*count + "");
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInstance().addToSepet(product, Integer.parseInt(counterTextView.getText().toString()));

                SharedPreferences pref = getActivity().getSharedPreferences("GETIR",Context.MODE_PRIVATE);
                Order order = new Order().setOrderCount(counterTextView.getText().toString())
                                        .setProductId(product.getId()+"")
                                        .setStoreId(pref.getString("STORE_ID","1"))
                                        .setToken(pref.getString("TOKEN","0"));
                Log.d("DATA TO SOCKET",new Gson().toJson(order));
                ((MainActivity)getActivity()).attemptSend(new Gson().toJson(order));
                DetailFragment.this.dismiss();
            }
        });

        return v;
    }

    class Order{
        private String token;
        private String store_id;
        private String product_id;
        private String order_count;

        public Order(){

        }

        public Order setToken(String token) {
            this.token = token;
            return this;
        }

        public Order setStoreId(String store_id) {
            this.store_id = store_id;
            return this;
        }

        public Order setProductId(String product_id) {
            this.product_id = product_id;
            return this;
        }

        public Order setOrderCount(String order_count) {
            this.order_count = order_count;
            return this;
        }
    }

}
