package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.data.TasksAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_tasks, container, false);

        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tasks_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        //mAdapter = new TasksAdapter(myDataset);
        //set myDataset to 2 lists and continue
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
