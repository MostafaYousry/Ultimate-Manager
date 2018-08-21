package com.example.android.employeesmanagementapp.adapters;


import android.content.Context;
import android.os.CountDownTimer;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private Context mContext;

    private List<TaskEntry> mData;
    private TasksItemClickListener mTaskClickListener;
    private boolean mTasksAreCompleted;


    public TasksAdapter(Context context, TasksItemClickListener clickListener, boolean tasksAreCompleted) {
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
        MaterialCardView mItemView;
        TextView mTaskTitle;
        TextView mTaskDates;
        ImageButton mTaskOptions;
        CountDownTimer mCountDownTimer;

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
                                            AppDatabase.getInstance(mContext).employeesTasksDao().deleteTaskJoinRecords(mData.get(getAdapterPosition()).getTaskId());
                                            AppDatabase.getInstance(mContext).tasksDao().deleteTask(mData.get(getAdapterPosition()));
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

            mTaskTitle.setText(mData.get(position).getTaskTitle());

            getRemainingTime(mData.get(position).getTaskDueDate());

            int taskColor = ResourcesCompat.getColor(itemView.getResources(), mData.get(position).getTaskColorResource(), mContext.getTheme());

            mItemView.setCardBackgroundColor(taskColor);

            mItemView.setTag(mData.get(position).getTaskId());
        }

        private void getRemainingTime(Date taskDueDate) {
            if (mCountDownTimer != null)
                mCountDownTimer.cancel();
            mCountDownTimer = new CountDownTimer(taskDueDate.getTime() - new Date().getTime(), 1000) {
                long years, months, days, hours, minutes, seconds;
                String[] chosenFields = new String[3];
                int indexOfFields = 0;
                Calendar cal = Calendar.getInstance();

                @Override
                public void onTick(long millisUntilFinished) {
                    indexOfFields = 0;
                    cal.setTimeInMillis(millisUntilFinished);
                    seconds = (millisUntilFinished / 1000) % 60;
                    minutes = (millisUntilFinished / (1000 * 60)) % 60;
                    hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                    years = cal.get(Calendar.YEAR) - 1970;
                    months = cal.get(Calendar.MONTH);
                    days = cal.get(Calendar.DAY_OF_MONTH) - 2;

                    if (years > 0 && indexOfFields + 1 != 4) {
                        if (years == 1)
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_single_year, years);
                        else
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_multi_years, years);
                    }
                    if (months > 0 && indexOfFields + 1 != 4) {

                        if (months == 1)
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_single_month, months);
                        else
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_multi_moths ,months);

                    }
                    if (days > 0 && indexOfFields + 1 != 4) {

                        if (days == 1)
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_single_day, days);
                        else
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_multi_days, days);

                    }
                    if (hours > 0 && indexOfFields + 1 != 4) {

                        if (hours == 1)
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_single_hour, hours);
                        else
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_multi_hours, hours);

                    }
                    if (minutes > 0 && indexOfFields + 1 != 4) {

                        if (minutes == 1)
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_single_minute, minutes);
                        else
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_multi_minutes, minutes);

                    }
                    if (seconds > 0 && indexOfFields + 1 != 4) {

                        if (seconds == 1)
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_single_second, seconds);
                        else
                            chosenFields[indexOfFields++] = mContext.getString(R.string.remaining_multi_seconds, seconds);
                    }

                    if (indexOfFields == 3)
                        mTaskDates.setText(mContext.getString(R.string.date_three_fields, chosenFields[0], chosenFields[1], chosenFields[2]));

                    else if (indexOfFields == 2)
                        mTaskDates.setText(mContext.getString(R.string.date_two_fields, chosenFields[0], chosenFields[1]));

                    else if (indexOfFields == 1)
                        mTaskDates.setText(mContext.getString(R.string.date_one_fields, chosenFields[0]));

                    System.out.println(mTaskDates.getText());
                    mTaskDates.setTextColor(mContext.getResources().getColor(R.color.primaryTextColor));
                }

                @Override
                public void onFinish() {
                    mTaskDates.setText(R.string.overdue);
                    mTaskDates.setTextColor(mContext.getResources().getColor(R.color.A700_16));
                }
            }.start();
        }

        @Override
        public void onClick(View v) {
            mTaskClickListener.onTaskClick((int) mItemView.getTag(), getAdapterPosition());
        }
    }
}


