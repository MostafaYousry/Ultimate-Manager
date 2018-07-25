//package com.example.android.employeesmanagementapp.fragments;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.android.employeesmanagementapp.R;
//import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
//import com.example.android.employeesmanagementapp.RecyclerViewItemLongClickListener;
//import com.example.android.employeesmanagementapp.activities.AddTaskActivity;
//import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class TaskBottomSheetFragment extends BottomSheetDialogFragment  implements RecyclerViewItemClickListener {
//    private final String TAG = TasksFragment.class.getSimpleName();
//    private RecyclerView mRecyclerView;
//    private TasksAdapter mAdapter;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view =inflater.inflate(R.layout.fragments_rv, container, false);
//
//        // Inflate the layout for this fragment
//        mRecyclerView = view.findViewById(R.id.rv_fragment);
//
//        // this setting to improves performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        // specify an adapter
//        mAdapter = new TasksAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
//
//        return view;
//    }
//
//
//    /**
//     * called when a list item is clicked
//     */
//    @Override
//    public void onItemClick(int clickedItemId, int clickedItemPosition) {
//        Log.d(TAG,"Item at index " + clickedItemId + " is clicked");
//
//
//        Intent intent = new Intent(getActivity() , AddTaskActivity.class);
//        //todo:pass rv.getTag ---> item id in db instead index in rv
//        intent.putExtra(AddTaskActivity.TASK_ID_KEY , clickedItemId + 0.5);
//        startActivity(intent);
//    }
//
//}
