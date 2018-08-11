package com.example.android.employeesmanagementapp.adapters;


import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<TaskEntry> mData;
    private TasksItemClickListener mTaskClickListener;


    public TasksAdapter(TasksItemClickListener clickListener) {
        mTaskClickListener = clickListener;
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

    public TaskEntry getItem(int position) {
        return mData.get(position);
    }

    /**
     * interface to handle click events done on a recycler view item
     */
    public interface TasksItemClickListener {
        void onTaskClick(int taskRowID, int taskPosition);
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mItemView;
        TextView mTaskTitle;
        TextView mTaskStartDate;
        TextView mTaskDueDate;
        ImageButton mTaskOptions;

        TasksViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTaskTitle = itemView.findViewById(R.id.item_task_title);
            mTaskStartDate = itemView.findViewById(R.id.task_start_date);
            mTaskDueDate = itemView.findViewById(R.id.task_due_date);
            mTaskOptions = itemView.findViewById(R.id.task_options_button);
            mTaskOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(view.getContext(), mTaskOptions);
                    popup.inflate(R.menu.menu_task_options);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_mark_as_done:
                                    //handle menu1 click
                                    return true;
                                case R.id.action_delete_task:
                                    //handle menu2 click
                                    return true;
                                case R.id.action_color_task:
                                    //handle menu3 click
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
            });
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            mTaskTitle.setText(mData.get(position).getTaskTitle());
            mTaskStartDate.setText(AppUtils.getFriendlyDate(mData.get(position).getTaskStartDate()));
            mTaskDueDate.setText(AppUtils.getFriendlyDate(mData.get(position).getTaskDueDate()));
            mItemView.setTag(mData.get(position).getTaskId());
        }

        @Override
        public void onClick(View v) {
            mTaskClickListener.onTaskClick((int) mItemView.getTag(), getAdapterPosition());
        }

    }

}


