package com.getirhackathon.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.getirhackathon.App;
import com.getirhackathon.R;
import com.getirhackathon.adapter.OrderAdapter;
import com.getirhackathon.adapter.ProductAdapter;
import com.getirhackathon.model.Product;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SepetFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView totalPriceTextView;
    private Button onayButton;
    private String totalPrice;

    public SepetFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sepet, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        totalPriceTextView = (TextView) v.findViewById(R.id.totalPriceTextView);
        onayButton = (Button) v.findViewById(R.id.onayButton);

        totalPriceTextView.setText("₺ " + getTotalPrice());

        onayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TakipFragment();
                fragment.setArguments(new Bundle());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_body, fragment)
                        .addToBackStack("frag")
                        .commit();
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new OrderAdapter(getActivity(), App.getInstance().getSepet());
        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        return v;
    }


    public String getTotalPrice() {
        Iterator<Product> iterator =
                App.getInstance().getSepet().keySet().iterator();
        float totalPrice=0;
        while (iterator.hasNext()){
            Product product = iterator.next();
            totalPrice+=Float.parseFloat(product.getPrice())*App.getInstance().getSepet().get(product);
        }
        return totalPrice+"";
    }
}
