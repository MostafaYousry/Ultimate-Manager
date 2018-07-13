package com.example.android.employeesmanagementapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements RecyclerViewItemClickListener{

    private static final String TAG = EmployeesFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

        // method to setup employees recycler view
        setupRecyclerView(rootView);

        return rootView;
    }

    private void setupRecyclerView(View rootView) {

        //access the employee recycler view
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //set the employee recycler view layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //create object of EmployeesAdapter and send data
        mEmployeesAdapter = new EmployeesAdapter(AppUtils.getEmployeesFakeData(), this);

        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeesAdapter);
    }



    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemIndex) {
        //todo:open employee details

        Log.d(TAG,"Item at index " + clickedItemIndex + " is clicked");
        Snackbar.make(getView(), "Item at index " + clickedItemIndex + " is clicked", Snackbar.LENGTH_SHORT)
                .show();
    }



}