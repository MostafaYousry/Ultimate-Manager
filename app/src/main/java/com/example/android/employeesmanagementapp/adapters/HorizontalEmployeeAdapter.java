package com.example.android.employeesmanagementapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalEmployeeAdapter extends RecyclerView.Adapter<HorizontalEmployeeAdapter.EmployeesViewHolder> {
    private List<EmployeeEntry> mData;
    private List<EmployeeEntry> mAddedEmployees;
    private EmployeesAdapter.EmployeeItemClickListener mClickListener;
    private View.OnLongClickListener mOnEmployeeLongClicked;

    public HorizontalEmployeeAdapter(EmployeesAdapter.EmployeeItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public HorizontalEmployeeAdapter(EmployeesAdapter.EmployeeItemClickListener clickListener, View.OnLongClickListener onEmployeeLongClicked) {
        this(clickListener);
        mOnEmployeeLongClicked = onEmployeeLongClicked;
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

    public List<EmployeeEntry> getData(){
        return mData;
    }

    @Override
    public int getItemCount() {
        if (mData == null && mAddedEmployees != null)
            return mAddedEmployees.size();
        else if (mAddedEmployees == null && mData != null)
            return mData.size();
        else if (mData == null && mAddedEmployees == null)
            return 0;
        return mData.size() + mAddedEmployees.size();
    }

    public void setData(List<EmployeeEntry> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void mergeToAddedEmployees(List<EmployeeEntry> chosenEmployees) {
        if (mAddedEmployees == null)
            mAddedEmployees = new ArrayList<>();
        mAddedEmployees.addAll(chosenEmployees);
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView employeeImage;
        TextView employeeName;
        View mItemView;

        public EmployeesViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            employeeImage = itemView.findViewById(R.id.employee_image);
            employeeName = itemView.findViewById(R.id.employee_name);

            //todo move this to bind when loading different images
            Glide.with(itemView.getContext()).load(AppUtils.getRandomEmployeeImage())
                    .into(employeeImage);

            itemView.setOnClickListener(this);
            if (mOnEmployeeLongClicked != null)
                itemView.setOnLongClickListener(mOnEmployeeLongClicked);

        }

        public void bind(int position) {
            if (position >= mData.size()) {
                employeeName.setText(mAddedEmployees.get(position).getEmployeeName());

                itemView.setTag(mAddedEmployees.get(position).getEmployeeID());
            }

            employeeName.setText(mData.get(position).getEmployeeName());

            itemView.setTag(mData.get(position).getEmployeeID());
        }

        @Override
        public void onClick(View view) {
            mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition());
        }

    }
}