package com.example.android.employeesmanagementapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder> {

    private List<DepartmentEntry> mDepartments;
    private RecyclerViewItemClickListener mGridItemClickListener;


    public DepartmentsAdapter(RecyclerViewItemClickListener gridItemClickListener) {
        mGridItemClickListener = gridItemClickListener;
    }

    @NonNull
    @Override
    public DepartmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_departments_rv, parent, false);
        DepartmentsViewHolder departmentsViewHolder = new DepartmentsViewHolder(rootView);

        return departmentsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mDepartments == null)
            return 0;
        return mDepartments.size();
    }

    /**
     * used to update adapters data if any change occurs
     *
     * @param departments new departments list
     */
    public void setData(List<DepartmentEntry> departments) {
        mDepartments = departments;
        notifyDataSetChanged();
    }

    public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mItemView;
        public TextView mDepartmentName;

        public DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mDepartmentName = itemView.findViewById(R.id.item_department_name);
            mItemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mDepartmentName.setText(mDepartments.get(position).getDepartmentName());
            mItemView.setTag(mDepartments.get(position).getDepartmentId());
        }

        @Override
        public void onClick(View v) {
            mGridItemClickListener.onItemClick((int) mItemView.getTag(),getAdapterPosition());
        }
    }
}