package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import androidx.appcompat.widget.PopupMenu;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalAdapter extends PagedListAdapter<EmployeeEntry, HorizontalAdapter.MyAdapterVH> {

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
    private Context mContext;
    private List<EmployeeEntry> mAddedEmployees;
    private boolean changeOccurred;
    private HorizontalEmployeeItemClickListener mClickListener;
    private boolean mUseLoadedData;
    private boolean mUseAddedData;
    private int mTaskID;


    public HorizontalAdapter(Context context, boolean useLoadedData, boolean useAddedData, int taskID, HorizontalEmployeeItemClickListener clickListener) {
        super(DIFF_CALLBACK);
        mContext = context;
        mTaskID = taskID;
        mClickListener = clickListener;
        mUseLoadedData = useLoadedData;
        mUseAddedData = useAddedData;

        mAddedEmployees = new ArrayList<>();
    }

    @Override
    public MyAdapterVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_horizontal_rv, parent, false);
        return new MyAdapterVH(rootView);
    }

    @Override
    public void onBindViewHolder(MyAdapterVH holder, int position) {
        if (mUseLoadedData && !mUseAddedData) {
            if (getItem(position) == null)
                holder.clear();
            else
                holder.bindDepEmployee(position);
        } else if (mUseAddedData && !mUseLoadedData) {
            if (mAddedEmployees.get(position) == null)
                holder.clear();
            else
                holder.bindNewTask(position);
        } else if (mUseLoadedData && mUseAddedData) {
            if (getItem(position) == null && mAddedEmployees.get(position) == null)
                holder.clear();
            else
                holder.bindOldTask(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mUseLoadedData && !mUseAddedData) {
            return super.getItemCount();
        } else if (mUseAddedData && !mUseLoadedData) {
            return mAddedEmployees.size();
        } else if (mUseLoadedData && mUseAddedData) {
            return super.getItemCount() + mAddedEmployees.size();
        }

        return -1;
    }

    public List<EmployeeEntry> getAddedEmployees() {
        return mAddedEmployees;
    }

    public void setAddedEmployees(List<EmployeeEntry> chosenEmployees) {
        if (mUseAddedData && !mUseLoadedData) {
            mAddedEmployees.addAll(chosenEmployees);
            notifyItemRangeInserted(mAddedEmployees.size(), chosenEmployees.size());
        } else if (mUseLoadedData && mUseAddedData) {
            insertInBackGround(chosenEmployees);
        }

        changeOccurred = true;
    }

    public void clearAdapter() {
        int addedSize = mAddedEmployees.size();
        mAddedEmployees = null;
        mAddedEmployees = new ArrayList<>();
        notifyItemRangeRemoved(0, addedSize);

    }

    public List<EmployeeEntry> getAllEmployees() {
        List<EmployeeEntry> employees = new ArrayList<>();
        if (getCurrentList() != null)
            employees.addAll(getCurrentList().snapshot());
        if (mAddedEmployees != null)
            employees.addAll(mAddedEmployees);

        return employees;
    }

    private void deleteInBackground(EmployeeEntry employeeEntry) {
        AppExecutor.getInstance().diskIO().execute(() -> AppDatabase.getInstance(mContext).employeesTasksDao().deleteEmployeeTask(new EmployeesTasksEntry(employeeEntry.getEmployeeID(), mTaskID)));
    }

    private void insertInBackGround(List<EmployeeEntry> chosenEmployees) {
        AppExecutor.getInstance().diskIO().execute(() -> {
            AppDatabase database = AppDatabase.getInstance(mContext);

            for (EmployeeEntry e : chosenEmployees) {
                database.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(e.getEmployeeID(), mTaskID));
            }

        });

    }

    public boolean didChangeOccur() {
        return changeOccurred;
    }

    public interface HorizontalEmployeeItemClickListener {
        void onEmployeeClick(int employeeRowID, int employeePosition, boolean isFired);
    }

    class MyAdapterVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView employeeImage;
        TextView employeeName;
        View mItemView;

        MyAdapterVH(View itemView) {
            super(itemView);
            mItemView = itemView;

            employeeImage = itemView.findViewById(R.id.employee_image);
            employeeName = itemView.findViewById(R.id.employee_name);


            itemView.setOnClickListener(this);

            if (mUseAddedData)
                itemView.setOnLongClickListener(this);
        }

        void bindDepEmployee(int position) {
            employeeName.setText(getItem(position).getEmployeeFirstName());

            if (TextUtils.isEmpty(getItem(position).getEmployeeImageUri())) {
                Glide.with(mContext).clear(employeeImage);

                TextDrawable textDrawable = new TextDrawable(mContext, getItem(position), AppUtils.dpToPx(mContext, 70), AppUtils.dpToPx(mContext, 70), AppUtils.spToPx(mContext, 28));
                employeeImage.setImageDrawable(textDrawable);
            } else {
                Glide.with(mContext)
                        .load(Uri.parse(getItem(position).getEmployeeImageUri()))
                        .into(employeeImage);
            }

            itemView.setTag(getItem(position).getEmployeeID());

        }

        void bindNewTask(int position) {
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
        }

        void bindOldTask(int position) {

            if (getItem(position) != null) {
                bindDepEmployee(position);
            } else {
                bindNewTask(position - HorizontalAdapter.super.getItemCount());
            }

        }

        @Override
        public void onClick(View view) {
            if (getItem(getAdapterPosition()) != null) {
                mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition(), getItem(getAdapterPosition()).isEmployeeIsDeleted());
            } else {
                mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition(), mAddedEmployees.get(getAdapterPosition()).isEmployeeIsDeleted());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.setGravity(Gravity.TOP);
            popup.inflate(R.menu.menu_task_employee_options);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_remove_employee:
                        changeOccurred = true;

                        int deletePosition = getAdapterPosition();

                        if (mUseAddedData && !mUseLoadedData) {
                            mAddedEmployees.remove(deletePosition);
                        } else if (mUseLoadedData && mUseAddedData) {
                            if (getItem(deletePosition) != null) {
                                deleteInBackground(getItem(deletePosition));
                            } else {
                                mAddedEmployees.remove(deletePosition - HorizontalAdapter.super.getItemCount());
                                notifyItemRemoved(deletePosition - HorizontalAdapter.super.getItemCount());
                            }
                        }

                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
            return true;
        }

        void clear() {
            Glide.with(mContext).clear(employeeImage);
            employeeImage.setImageDrawable(null);
            employeeImage.setImageBitmap(null);
            employeeImage.setImageResource(android.R.color.transparent);
            employeeName.setText("");
        }
    }

}
