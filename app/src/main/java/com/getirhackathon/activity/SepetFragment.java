package com.getirhackathon.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getirhackathon.App;
import com.getirhackathon.R;
import com.getirhackathon.adapter.OrderAdapter;
import com.getirhackathon.adapter.ProductAdapter;
import com.getirhackathon.model.Product;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SepetFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public SepetFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sepet, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

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


}
