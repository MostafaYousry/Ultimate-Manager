package com.example.android.employeesmanagementapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<TaskEntry> mData;
    private RecyclerViewItemClickListener mListClickListener;

    public TasksAdapter(RecyclerViewItemClickListener clickListener) {
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
    public void onBindViewHolder(@NonNull final TasksViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    /**
     * used to update adapters data if any change occurs
     *
     * @param tasks : new tasks list
     */
    public void setData(List<TaskEntry> tasks) {
        mData = tasks;
        notifyDataSetChanged();
    }

    public List<TaskEntry> getData() {
        return mData;
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mItemView;
        TextView mTextView;

        TasksViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTextView = itemView.findViewById(R.id.item_task_title);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            mTextView.setText(mData.get(position).getTaskTitle());
            mItemView.setTag(mData.get(position).getTaskId());
        }

        @Override
        public void onClick(View v) {
            mListClickListener.onItemClick((int) mItemView.getTag(), getAdapterPosition());
        }

    }

}


