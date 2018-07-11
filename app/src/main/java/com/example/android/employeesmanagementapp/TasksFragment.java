package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

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
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_tasks, container, false);

        // Inflate the layout for this fragment
        mRecyclerView = view.findViewById(R.id.rv_tasks_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new TasksAdapter(getFakeData(),this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    private List<TaskEntry> getFakeData(){
        TaskEntry taskEntry1 = new TaskEntry(2,"App code refactor","wfjjwnfiwnfiwenf",new Date(),new Date());
        TaskEntry taskEntry2 = new TaskEntry(0,"Add new Feature","wfjjwnfiwnfiwenfasdasd",new Date(),new Date());
        TaskEntry taskEntry3 = new TaskEntry(3,"rererererererererererer","wfjjwnfiwnfiwenfsdasdasdadsad",new Date(),new Date());
        TaskEntry taskEntry4 = new TaskEntry(3,"el3ab baleee","wfjjwnfiwnfiwenfasdasd",new Date(),new Date());
        TaskEntry taskEntry5 = new TaskEntry(1,"skalob baneee","asdasdsdasdadsad",new Date(),new Date());
        TaskEntry taskEntry6 = new TaskEntry(4,"eboo msh baskoota","wfjjwnfiwnfiwenfasdasdasdsadasdasdsadasdsads",new Date(),new Date());

        List<TaskEntry> list = new ArrayList<>();
        list.add(taskEntry1);
        list.add(taskEntry2);
        list.add(taskEntry3);
        list.add(taskEntry4);
        list.add(taskEntry5);
        list.add(taskEntry6);

        return list;
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        //todo: open task detail activity
        Log.d(TAG,"Item at index " + clickedItemIndex + " is clicked");
    }
}



