package com.example.android.employeesmanagementapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment implements RecyclerViewItemClickListener {

    private final String TAG = TasksFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragments_rv, container, false);

        // Inflate the layout for this fragment
        mRecyclerView = view.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new TasksAdapter(AppUtils.getTasksFakeData(), this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemIndex) {
        Log.d(TAG, "Item at index " + clickedItemIndex + " is clicked");


        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        //todo:pass rv.getTag ---> item id in db instead index in rv
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, clickedItemIndex + 0.0);
        startActivity(intent);
    }

}



