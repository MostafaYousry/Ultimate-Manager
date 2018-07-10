package com.example.android.employeesmanagementapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private String [] mData;

    public EmployeeAdapter(String[] data) {
        mData = data;
    }


    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_layout,parent,false);
        EmployeeViewHolder employeeViewHolder = new EmployeeViewHolder(v);
        return employeeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName;
        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employee_name);
        }
        void bind (int position){
            employeeName.setText(mData[position]);
        }
    }
}

