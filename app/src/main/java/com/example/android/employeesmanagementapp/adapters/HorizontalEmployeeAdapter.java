package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.TextDrawable;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalEmployeeAdapter extends PagedListAdapter<EmployeeEntry, HorizontalEmployeeAdapter.EmployeesViewHolder> {
    private Context mContext;
    private List<EmployeeEntry> mAddedEmployees;
    private boolean changeOccurred;
    private static DiffUtil.ItemCallback<EmployeeEntry> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<EmployeeEntry>() {
                @Override
                public boolean areItemsTheSame(EmployeeEntry oldEmployee, EmployeeEntry newEmployee) {
                    return oldEmployee.getEmployeeID() == newEmployee.getEmployeeID();
                }

                @Override
                public boolean areContentsTheSame(EmployeeEntry oldEmployee,
                                                  EmployeeEntry newEmployee) {
                    return oldEmployee.equals(newEmployee);
                }
            };

    private EmployeesAdapter.EmployeeItemClickListener mClickListener;
    private boolean mLongClickEnabled;
    private int mTaskID;

    public HorizontalEmployeeAdapter(Context context, EmployeesAdapter.EmployeeItemClickListener clickListener, boolean longClickEnabled) {
        super(DIFF_CALLBACK);
        mContext = context;
        mClickListener = clickListener;
        mLongClickEnabled = longClickEnabled;
    }


    public HorizontalEmployeeAdapter(Context context, EmployeesAdapter.EmployeeItemClickListener clickListener, boolean longClickEnabled, int taskId) {
        super(DIFF_CALLBACK);
        mContext = context;
        mClickListener = clickListener;
        mLongClickEnabled = longClickEnabled;
        mTaskID = taskId;
    }

    @NonNull
    @Override
    public HorizontalEmployeeAdapter.EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_horizontal_rv, parent, false);
        HorizontalEmployeeAdapter.EmployeesViewHolder employeesViewHolder = new HorizontalEmployeeAdapter.EmployeesViewHolder(rootView);
        return employeesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalEmployeeAdapter.EmployeesViewHolder holder, int position) {

        if (position >= (getCurrentList() == null ? 0 : getCurrentList().size())) {
            if (mAddedEmployees.get(position - (getCurrentList() == null ? 0 : getCurrentList().size())) != null) {
                holder.bind(position);
            } else {
                holder.clear();
            }
        } else {
            if (getItem(position) != null) {
                holder.bind(position);
            } else {
                // Null defines a placeholder item - PagedListAdapter automatically
                // invalidates this row when the actual object is loaded from the
                // database.
                holder.clear();
            }
        }
    }

    @Override
    public int getItemCount() {
        int dataSize = 0, addedSize = 0;
        if (getCurrentList() != null)
            dataSize = getCurrentList().size();
        if (mAddedEmployees != null)
            addedSize = mAddedEmployees.size();


        return dataSize + addedSize;
    }

    public List<EmployeeEntry> getData() {
        return getCurrentList().snapshot();
    }

    public List<EmployeeEntry> getAddedEmployees() {
        return mAddedEmployees;
    }

    public void setAddedEmployees(List<EmployeeEntry> chosenEmployees) {
        if (mAddedEmployees == null)
            mAddedEmployees = chosenEmployees;
        else
            mAddedEmployees.addAll(chosenEmployees);
        notifyDataSetChanged();
//        notifyItemRangeInserted(mData.size() , chosenEmployees.size());

        changeOccurred = true;
    }

    public void clearAdapter() {
        if (getCurrentList() != null)
            getCurrentList().clear();
        if (mAddedEmployees != null)
            mAddedEmployees.clear();
        notifyDataSetChanged();
    }

    public List<EmployeeEntry> getAllEmployees() {
        List<EmployeeEntry> employees = new ArrayList<>();
        if (getCurrentList() != null)
            employees.addAll(getCurrentList().snapshot());
        if (mAddedEmployees != null)
            employees.addAll(mAddedEmployees);

        return employees;
    }

    private void deleteInBackground(EmployeeEntry item, int mTaskID) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(mContext).employeesTasksDao().deleteEmployeeTask(new EmployeesTasksEntry(item.getEmployeeID(), mTaskID));
            }
        });


    }

    public boolean didChangeOccur() {
        return changeOccurred;
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView employeeImage;
        TextView employeeName;
        View mItemView;

        EmployeesViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;

            employeeImage = itemView.findViewById(R.id.employee_image);
            employeeName = itemView.findViewById(R.id.employee_name);


            itemView.setOnClickListener(this);

            if (mLongClickEnabled)
                itemView.setOnLongClickListener(this);

        }

        void bind(int position) {
            if (position >= (getCurrentList() == null ? 0 : getCurrentList().size())) {
                position = position - (getCurrentList() == null ? 0 : getCurrentList().size());

                employeeName.setText(mAddedEmployees.get(position).getEmployeeFirstName());


                if (TextUtils.isEmpty(mAddedEmployees.get(position).getEmployeeImageUri())) {
                    Glide.with(mContext).clear(employeeImage);

                    TextDrawable textDrawable = new TextDrawable(mContext, mAddedEmployees.get(position), AppUtils.dpToPx(mContext, 70), AppUtils.dpToPx(mContext, 70), AppUtils.spToPx(mContext, 28));
                    employeeImage.setImageDrawable(textDrawable);
                } else {
                    Glide.with(mContext)
                            .load(Uri.parse(mAddedEmployees.get(position).getEmployeeImageUri()))
                            .into(employeeImage);
                }

                itemView.setTag(mAddedEmployees.get(position).getEmployeeID());

            } else {
                employeeName.setText(getItem(position).getEmployeeFirstName());


                if (TextUtils.isEmpty(getItem(position).getEmployeeImageUri())) {
                    Glide.with(mContext).clear(employeeImage);

                    TextDrawable textDrawable = new TextDrawable(mContext, getItem(position), AppUtils.dpToPx(mContext, 70), AppUtils.dpToPx(mContext, 70), AppUtils.spToPx(mContext, 28));
                    employeeImage.setImageDrawable(textDrawable);

                } else {
                    Glide.with(mContext)
                            .asBitmap()
                            .load(Uri.parse(getItem(position).getEmployeeImageUri()))
                            .into(employeeImage);
                }

                itemView.setTag(getItem(position).getEmployeeID());
            }
        }

        @Override
        public void onClick(View view) {
            mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.setGravity(Gravity.TOP);
            popup.inflate(R.menu.menu_task_employee_options);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_remove_employee:
                            changeOccurred = true;
                            int deletePosition = getAdapterPosition();
                            if (getCurrentList() == null) {
                                mAddedEmployees.remove(deletePosition);
                                notifyItemRemoved(deletePosition);
                                return true;
                            }

                            if (deletePosition < getCurrentList().size()) {
                                deleteInBackground(getItem(deletePosition), mTaskID);
                            } else {
                                mAddedEmployees.remove(deletePosition - getCurrentList().size());
//                                notifyItemRemoved(deletePosition - getCurrentList().size());
                                notifyDataSetChanged();
                            }
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popup.show();
            return true;
        }

        void clear() {
            Glide.with(mContext).clear(employeeImage);
            employeeName.setText("");
        }
    }
}