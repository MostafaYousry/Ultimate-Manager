package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment implements DepartmentsAdapter.GridItemClickListener {
    private final String TAG = DepartmentsFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private DepartmentsAdapter mAdapter;

    public DepartmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_departments, container, false);

        //get recycler view
        mRecyclerView = rootView.findViewById(R.id.rv_departments_fragment);

        //initialise recycler view adapter
        mAdapter = new DepartmentsAdapter(getFakeData() , this);
        mRecyclerView.setAdapter(mAdapter);

        //hooking recycler view with grid layout manager (2 columns)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        return rootView;

    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        //todo: open department detail activity
        Log.d(TAG,"Item at index " + clickedItemIndex + " is clicked");
    }




    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    private List<DepartmentEntry> getFakeData(){
        DepartmentEntry departmentEntry1 = new DepartmentEntry("Production" );
        DepartmentEntry departmentEntry2 = new DepartmentEntry("Research and Development");
        DepartmentEntry departmentEntry3 = new DepartmentEntry("Purchasing");
        DepartmentEntry departmentEntry4 = new DepartmentEntry("Marketing");
        DepartmentEntry departmentEntry5 = new DepartmentEntry("Human Resource Management");
        DepartmentEntry departmentEntry6 = new DepartmentEntry("Accounting and Finance");

        List<DepartmentEntry> list = new LinkedList<DepartmentEntry>();
        list.add(departmentEntry1);
        list.add(departmentEntry2);
        list.add(departmentEntry3);
        list.add(departmentEntry4);
        list.add(departmentEntry5);
        list.add(departmentEntry6);

        return list;
    }
}
