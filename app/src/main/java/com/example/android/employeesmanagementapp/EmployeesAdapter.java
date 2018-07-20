package com.example.android.employeesmanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {
    final private RecyclerViewItemClickListener mClickListener;
    private List<EmployeeEntry> mData;

    EmployeesAdapter(List<EmployeeEntry> data, RecyclerViewItemClickListener listener) {
        mData = data;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item layout for the view holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employees_rv, parent, false);

        EmployeesViewHolder employeesViewHolder = new EmployeesViewHolder(v);

        return employeesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //create object for each view in the item view
        TextView employeeName;
        ImageView employeeImage;

        EmployeesViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            employeeName = itemView.findViewById(R.id.employee_name);
            employeeImage = itemView.findViewById(R.id.employee_image);

            // set the item click listener
            itemView.setOnClickListener(this);
        }

        void bind(int position) {

            //change the item data by the position
            employeeName.setText(mData.get(position).getEmployeeName());
            employeeImage.setImageResource(AppUtils.getRandomEmployeeImage());
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(getAdapterPosition());
        }
    }
}