package com.example.android.employeesmanagementapp;


import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements EmployeeAdapter.EmployeeOnClickListener{

    ArrayList<EmployeeData> mEmployeeData = new ArrayList<EmployeeData>();
    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mEmployeeData.add(new EmployeeData("Robert Dawney", R.drawable.family_father));
        mEmployeeData.add(new EmployeeData("Emma Watson", R.drawable.family_mother));
        mEmployeeData.add(new EmployeeData("Tom Holland",R.drawable.family_son));
        mEmployeeData.add(new EmployeeData("Elezabeth Olsen",R.drawable.family_daughter));
        mEmployeeData.add(new EmployeeData("Chris Evans",R.drawable.family_older_brother));
        mEmployeeData.add(new EmployeeData("Robert Dawney", R.drawable.family_father));
        mEmployeeData.add(new EmployeeData("Emma Watson", R.drawable.family_mother));
        mEmployeeData.add(new EmployeeData("Tom Holland",R.drawable.family_son));
        mEmployeeData.add(new EmployeeData("Elezabeth Olsen",R.drawable.family_daughter));
        mEmployeeData.add(new EmployeeData("Chris Evans",R.drawable.family_older_brother));
        mEmployeeData.add(new EmployeeData("Robert Dawney", R.drawable.family_father));
        mEmployeeData.add(new EmployeeData("Emma Watson", R.drawable.family_mother));
        mEmployeeData.add(new EmployeeData("Tom Holland",R.drawable.family_son));
        mEmployeeData.add(new EmployeeData("Elezabeth Olsen",R.drawable.family_daughter));
        mEmployeeData.add(new EmployeeData("Chris Evans",R.drawable.family_older_brother));

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_employees, container, false);

        // method to setup employee recycler view
        setupRecyclerView(rootView);

        return rootView;
    }
    private void setupRecyclerView(View rootview) {
        //access the employee recycler view
        mRecyclerView = rootview.findViewById(R.id.employees_recycler_view);

        //set fixed size to the items of the recycler view
        mRecyclerView.setHasFixedSize(true);

        //set the employee recycler view layout manger
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        //add divider between items
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //create object of EmployeeAdapter and send data
        mEmployeeAdapter = new EmployeeAdapter(mEmployeeData, this);

        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeeAdapter);
    }

    @Override
    public void onListItemCLicked(int clickedItemIndex) {
       Toast.makeText(getContext(),"open employee details",Toast.LENGTH_LONG).show();

        //intent to open the employee data activity
//        Intent intent = new Intent(getContext(),AddEmployeeActivity.class);
//        startActivity(intent);
    }
}