package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.TextDrawable;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalEmployeeAdapter extends RecyclerView.Adapter<HorizontalEmployeeAdapter.EmployeesViewHolder> {
    private List<EmployeeEntry> mData;
    private List<EmployeeEntry> mAddedEmployees;
    private List<EmployeeEntry> mRemovedEmployees;

    private EmployeesAdapter.EmployeeItemClickListener mClickListener;
    private boolean mLongClickEnabled;

    public HorizontalEmployeeAdapter(EmployeesAdapter.EmployeeItemClickListener clickListener, boolean longClickEnabled) {
        mClickListener = clickListener;
        mLongClickEnabled = longClickEnabled;
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
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        int dataSize = 0, addedSize = 0;
        if (mData != null)
            dataSize = mData.size();
        if (mAddedEmployees != null)
            addedSize = mAddedEmployees.size();


        return dataSize + addedSize;
    }

    public List<EmployeeEntry> getData() {
        return mData;
    }

    public void setData(List<EmployeeEntry> data) {
        mData = data;
        notifyDataSetChanged();
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
    }

    public void clearAdapter() {
        if (mData != null)
            mData.clear();
        if (mAddedEmployees != null)
            mAddedEmployees.clear();
    }

    public List<EmployeeEntry> getAllEmployees() {
        List<EmployeeEntry> employees = new ArrayList<>();
        if (mData != null)
            employees.addAll(mData);
        if (mAddedEmployees != null)
            employees.addAll(mAddedEmployees);

        return employees;
    }

    public List<EmployeeEntry> getRemovedEmployees() {
        return mRemovedEmployees;
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
            if (position >= mData.size()) {
                int addedEmployeeIndex = position - mData.size();

                employeeName.setText(mAddedEmployees.get(addedEmployeeIndex).getEmployeeName());

                if (mData.get(position).getEmployeeImageUri() == null) {
                    Context context = itemView.getContext();

                    TextDrawable textDrawable = new TextDrawable(context, mData.get(position), AppUtils.dpToPx(context, 70), AppUtils.dpToPx(context, 70), AppUtils.spToPx(context, 28));
                    employeeImage.setImageDrawable(textDrawable);
                } else {
//                Glide.with(mEmployeeImage.getContext())
//                        .load()
//                        .apply(RequestOptions.fitCenterTransform())
//                        .into(mEmployeeImage);
                }

                itemView.setTag(mAddedEmployees.get(addedEmployeeIndex).getEmployeeID());

            } else {
                employeeName.setText(mData.get(position).getEmployeeName());

                if (mData.get(position).getEmployeeImageUri() == null) {
                    Context context = itemView.getContext();

                    TextDrawable textDrawable = new TextDrawable(context, mData.get(position), AppUtils.dpToPx(context, 70), AppUtils.dpToPx(context, 70), AppUtils.spToPx(context, 28));
                    employeeImage.setImageDrawable(textDrawable);
                } else {
//                Glide.with(mEmployeeImage.getContext())
//                        .load()
//                        .apply(RequestOptions.fitCenterTransform())
//                        .into(mEmployeeImage);
                }

                itemView.setTag(mData.get(position).getEmployeeID());
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
                            int deletePosition = getAdapterPosition();
                            if (deletePosition < mData.size()) {
                                if (mRemovedEmployees == null)
                                    mRemovedEmployees = new ArrayList<>();
                                mRemovedEmployees.add(mData.remove(deletePosition));
                                notifyItemRemoved(deletePosition);
                            } else {
                                mAddedEmployees.remove(deletePosition - mData.size());
                                notifyItemRemoved(deletePosition - mData.size());
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
    }
}