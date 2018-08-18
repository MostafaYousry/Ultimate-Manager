package com.example.android.employeesmanagementapp.adapters;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


public class TasksAdapter extends PagedListAdapter<TaskEntry, TasksAdapter.TasksViewHolder> {

    private Context mContext;
    private TasksItemClickListener mTaskClickListener;
    private boolean mTasksAreCompleted;


    private static DiffUtil.ItemCallback<TaskEntry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TaskEntry>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(TaskEntry oldTask, TaskEntry newTask) {
                    return oldTask.getTaskId() == newTask.getTaskId();
                }

                @Override
                public boolean areContentsTheSame(TaskEntry oldTask,
                                                  TaskEntry newTask) {
                    return oldTask.equals(newTask);
                }
            };


    public TasksAdapter(Context context, TasksItemClickListener clickListener, boolean tasksAreCompleted) {
        super(DIFF_CALLBACK);
        mContext = context;
        mTaskClickListener = clickListener;
        mTasksAreCompleted = tasksAreCompleted;
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
        if (getItem(position) != null) {
            holder.bind(position);
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }

    }


    /**
     * interface to handle click events done on a recycler view item
     */
    public interface TasksItemClickListener {
        void onTaskClick(int taskRowID, int taskPosition, boolean taskIsCompleted);
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialCardView mItemView;
        TextView mTaskTitle;
        TextView mTaskDates;
        ImageButton mTaskOptions;

        TasksViewHolder(final View itemView) {
            super(itemView);
            mItemView = (MaterialCardView) itemView;
            mTaskTitle = itemView.findViewById(R.id.task_title);
            mTaskDates = itemView.findViewById(R.id.task_dates);
            mTaskOptions = itemView.findViewById(R.id.task_options_button);
            mTaskOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(view.getContext(), mTaskOptions);
                    popup.inflate(R.menu.menu_task_options);

                    if (mTasksAreCompleted) {
                        Menu menu = popup.getMenu();
                        menu.removeItem(R.id.action_mark_as_done);
                        menu.removeItem(R.id.action_delete_task);
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_mark_as_done:
                                    AppUtils.showRateTaskDialog(mContext, (int) itemView.getTag());
                                    return true;
                                case R.id.action_delete_task:

                                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppDatabase.getInstance(mContext).employeesTasksDao().deleteTaskJoinRecords(getItem(getAdapterPosition()).getTaskId());
                                            AppDatabase.getInstance(mContext).tasksDao().deleteTask(getItem(getAdapterPosition()));
                                        }
                                    });

                                    return true;
                                case R.id.action_color_task:
                                    AppUtils.showColorPicker(mContext, (int) itemView.getTag());
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

            mTaskTitle.setText(getItem(position).getTaskTitle());

            mTaskDates.setText(mContext.getString(R.string.task_list_item_name_dates, AppUtils.getFriendlyDate(getItem(position).getTaskStartDate()), AppUtils.getFriendlyDate(getItem(position).getTaskDueDate())));

            int taskColor = ResourcesCompat.getColor(itemView.getResources(), getItem(position).getTaskColorResource(), mContext.getTheme());

            mItemView.setCardBackgroundColor(taskColor);

            mItemView.setTag(getItem(position).getTaskId());
        }

        @Override
        public void onClick(View v) {
            mTaskClickListener.onTaskClick((int) mItemView.getTag(), getAdapterPosition(), getItem(getAdapterPosition()).isTaskIsCompleted());
        }

        void clear() {
            mTaskTitle.setText("");

            mTaskDates.setText("");

            mItemView.setCardBackgroundColor(Color.WHITE);
        }
    }

}


