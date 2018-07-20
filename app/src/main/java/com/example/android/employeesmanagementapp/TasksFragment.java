package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment implements RecyclerViewItemClickListener {

    private final String TAG = TasksFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;
    private AppDatabase mDb;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        mDb = AppDatabase.getInstance(getContext());

        View view =inflater.inflate(R.layout.fragments_rv, container, false);

        // Inflate the layout for this fragment
        mRecyclerView = view.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new TasksAdapter(AppUtils.getTasksFakeData(),this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemIndex) {
        Log.d(TAG,"Item at index " + clickedItemIndex + " is clicked");


//        Intent intent = new Intent(getActivity() , AddTaskActivity.class);
//        //todo:pass rv.getTag ---> item id in db instead index in rv
//        intent.putExtra(AddTaskActivity.TASK_ID_KEY , clickedItemIndex);
//        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
//        AppExecutor.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                final List<TaskEntry> list = mDb.tasksDao().loadAllRunningTasks();
//
//                getActivity().runOnUiThread(new Runnable(){
//                    @Override
//                    public void run(){
//                        mAdapter.setData(list);
//                    }
//                });
//            }
//        });

    }


}



