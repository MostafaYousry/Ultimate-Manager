package com.example.android.employeesmanagementapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;


public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<TaskEntry> mData;
    private RecyclerViewItemClickListener mListClickListener;


    public TasksAdapter(List<TaskEntry> data , RecyclerViewItemClickListener clickListener) {
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


    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;

        TasksViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_task_title);
            itemView.setOnClickListener(this);
        }

        void bind (int position){
            mTextView.setText(mData.get(position).getTaskTitle());
        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            mListClickListener.onItemClick(clickedItemIndex);
        }
    }

}


