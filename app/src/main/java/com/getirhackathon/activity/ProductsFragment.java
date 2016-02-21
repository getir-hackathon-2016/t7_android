package com.getirhackathon.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.getirhackathon.App;
import com.getirhackathon.R;
import com.getirhackathon.adapter.ProductAdapter;
import com.getirhackathon.model.Product;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    public ProductsFragment() {
        // Required empty public constructor
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> productList = new ArrayList<>();
    private RelativeLayout loadingPanel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);
       // loadingPanel = (RelativeLayout) getActivity().findViewById(R.id.loadingPanel1);

        //loadingPanel.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProductAdapter(getActivity(), productList);

        String url = "https://getir-hackathon.herokuapp.com/location?x=" + App.getInstance().getLocation().getLatitude() +
                                                                    "&y=" + App.getInstance().getLocation().getLongitude();
        if(productList.size()==0) {
            JsonObjectRequest req = new JsonObjectRequest(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("info", response.toString());
                            //loadingPanel.setVisibility(View.INVISIBLE);
                            Gson gson = new Gson();
                            try {
                                JSONArray products = response
                                        .getJSONArray("products");
                                App.getInstance().getPref().edit()
                                        .putString("STORE_ID", response.getString("store_id")).commit();
                                App.getInstance().getPref().edit()
                                        .putString("STORE_LAT", response.getString("lat")).commit();
                                App.getInstance().getPref().edit()
                                        .putString("STORE_LNG", response.getString("lng")).commit();
                                // Parsing json array response
                                // loop through each json object
                                for (int i = 0; i < products.length(); i++) {
                                    JSONObject product = products.getJSONObject(i);
                                    productList.add(gson.fromJson(product.toString(), Product.class));

                                }

                                mRecyclerView.setAdapter(mAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(),
                                        "Error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("info", "Error: " + error.getMessage());
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the request to the queue
            Volley.newRequestQueue(getActivity()).add(req);
        }
        // Inflate the layout for this fragment
        return v;
    }


}
