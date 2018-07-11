package com.example.android.employeesmanagementapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder> {

    private List<DepartmentEntry> mDepartments;
    private GridItemClickListener mGridItemClickListener;

    //interface to handle click events
    public interface GridItemClickListener{
        void onGridItemClick(int clickedItemIndex);
    }


    public DepartmentsAdapter(List<DepartmentEntry> data , GridItemClickListener gridItemClickListener){
        mDepartments = data;
        mGridItemClickListener = gridItemClickListener;
    }

    @NonNull
    @Override
    public DepartmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_departments_rv,parent,false);
        DepartmentsViewHolder departmentsViewHolder = new DepartmentsViewHolder(rootView);

        return departmentsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mDepartments.size();
    }


    public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View mItemView;
        public TextView mDepartmentName;

        public DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mDepartmentName = itemView.findViewById(R.id.tv_department_name);
            mItemView.setOnClickListener(this);
        }

        public void bind(int position){
            mDepartmentName.setText(mDepartments.get(position).getDepartmentName());
        }

        @Override
        public void onClick(View v) {
            int clickedItemIndex = getAdapterPosition();
            mGridItemClickListener.onGridItemClick(clickedItemIndex);
        }
    }

    /**
     * used to update adapters data if any change occurs
     *
     * @param  departments new departments list
     */
    public void setData(List<DepartmentEntry> departments){
        mDepartments = departments;
        notifyDataSetChanged();
    }
}
