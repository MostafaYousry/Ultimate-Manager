package com.example.android.employeesmanagementapp.adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalEmployeeAdapter extends RecyclerView.Adapter<HorizontalEmployeeAdapter.EmployeesViewHolder> {
    private List<EmployeeEntry> mData;
    final private RecyclerViewItemClickListener mClickListener;
    private boolean cancelIconIsVisible;
    private boolean isOnCLick;
    private final SparseBooleanArray array=new SparseBooleanArray();

    public HorizontalEmployeeAdapter(RecyclerViewItemClickListener clickListener, boolean cancelIconIsVisible) {
        mClickListener = clickListener;
        this.cancelIconIsVisible = cancelIconIsVisible;
        isOnCLick = !(cancelIconIsVisible);
    }

    @NonNull
    @Override

    public HorizontalEmployeeAdapter.EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_horizonatl_rv_item, parent, false);
        HorizontalEmployeeAdapter.EmployeesViewHolder employeesViewHolder = new HorizontalEmployeeAdapter.EmployeesViewHolder(rootView);
        return employeesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalEmployeeAdapter.EmployeesViewHolder holder, int position) {
        if(array.get(position)){
            holder.cancelEmployee.setVisibility(View.VISIBLE);
        }else{
            holder.cancelEmployee.setVisibility(View.GONE);
        }
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<EmployeeEntry> data) {
        mData = data;

    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView employeeImage;
        ImageView cancelEmployee;
        TextView employeeName;
        View mItemView;

        public EmployeesViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            employeeImage = itemView.findViewById(R.id.employee_horizontal_rv_image);
            employeeName = itemView.findViewById(R.id.employee_horizontal_rv_name);
            cancelEmployee = itemView.findViewById(R.id.cancel_employee_ic);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public void bind(int position) {
            employeeImage.setImageResource(AppUtils.getRandomEmployeeImage());
            employeeName.setText(mData.get(position).getEmployeeName());
            cancelEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(getAdapterPosition());
                }
            });
            itemView.setTag(mData.get(position).getEmployeeID());
        }

        @Override
        public void onClick(View view) {
            if (isOnCLick)
                mClickListener.onItemClick((int) mItemView.getTag(), getAdapterPosition());
        }

        public void removeAt(int position) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
        }

        @Override
        public boolean onLongClick(View view) {
            if (cancelIconIsVisible) {
                cancelEmployee.setVisibility(View.VISIBLE);
                array.put(getAdapterPosition(),true);
            }
            return true;
        }
    }
}