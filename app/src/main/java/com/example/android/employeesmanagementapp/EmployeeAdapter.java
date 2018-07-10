package com.example.android.employeesmanagementapp;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private ArrayList<EmployeeData>  mData;
    final private EmployeeOnClickListener mEmployeeOnClickListener;

    public EmployeeAdapter(ArrayList<EmployeeData> data, EmployeeOnClickListener listener) {
        mData = data;
        mEmployeeOnClickListener =  listener;
    }

    //interface to be implemented by Employee Fragment
    public interface EmployeeOnClickListener{
        void onListItemCLicked(int clickedItemIndex);
    }


    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item layout for the view holder
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
        return mData.size();
    }


    public class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //create object for each view in the item view
        TextView employeeName;
        ImageView employeeImage;

        public EmployeeViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            employeeName = itemView.findViewById(R.id.employee_name);
            employeeImage = itemView.findViewById(R.id.employee_image);

            // set the item click listener
            itemView.setOnClickListener(this);
        }
        void bind (int position){

            //change the item data by the position
            employeeName.setText(mData.get(position).getEmployeeName());
            employeeImage.setImageResource(mData.get(position).getEmployeeImageSac());
        }

        @Override
        public void onClick(View v) {
            mEmployeeOnClickListener.onListItemCLicked(getAdapterPosition());
        }
    }
}