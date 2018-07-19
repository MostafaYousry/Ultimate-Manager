package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
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

    public static class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

        private List<TaskEntry> mData;
        private RecyclerViewItemClickListener mListClickListener;


        public TasksAdapter(List<TaskEntry> data, RecyclerViewItemClickListener clickListener) {
            mData = data;
            mListClickListener = clickListener;
        }

        @NonNull
        @Override
        public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tasks_rv, parent, false);

            TasksViewHolder tasksViewHolder = new TasksViewHolder(itemView);
            return tasksViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setData(List<TaskEntry> newData) {
            mData = newData;
            notifyDataSetChanged();
        }

        class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView mTextView;

            TasksViewHolder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.item_task_title);
                itemView.setOnClickListener(this);
            }

            void bind(int position) {
                mTextView.setText(mData.get(position).getTaskTitle());
            }

            @Override
            public void onClick(View v) {
                int clickedItemIndex = getAdapterPosition();
                mListClickListener.onItemClick(clickedItemIndex);
            }
        }

    }
}



