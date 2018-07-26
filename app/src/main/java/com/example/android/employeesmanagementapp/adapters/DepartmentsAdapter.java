package com.example.android.employeesmanagementapp.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder> {

    private List<DepartmentEntry> mDepartments;
    private RecyclerViewItemClickListener mGridItemClickListener;
    public static final int SELECTION_MODE_SINGLE = 1;
    public static final int SELECTION_MODE_MULTIPLE = 2;
    private static final String TAG = DepartmentsAdapter.class.getSimpleName();
    private int departmentsSelectionMode;
    private DepartmentSelectedStateListener mDepartmentSelectedStateListener;


    public DepartmentsAdapter(@NonNull RecyclerViewItemClickListener gridItemClickListener) {
        mGridItemClickListener = gridItemClickListener;
        departmentsSelectionMode= SELECTION_MODE_SINGLE;
    }

    public DepartmentsAdapter(@NonNull RecyclerViewItemClickListener gridItemClickListener, @NonNull DepartmentSelectedStateListener departmentSelectedStateListener) {
        mGridItemClickListener = gridItemClickListener;
        mDepartmentSelectedStateListener = departmentSelectedStateListener;
        departmentsSelectionMode= SELECTION_MODE_SINGLE;
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
     * @return : current selectionMode : Single / Multiple
     */
    public int getDepartmentSelectionMode() {
        return departmentsSelectionMode;
    }

    /**
     * used by departments fragment to start or finish a multi selection operation
     *
     * @param selectionMode : Single / Multiple
     */
    public void setDepartmentSelectionMode(int selectionMode) {
        departmentsSelectionMode = selectionMode;
        notifyDataSetChanged();
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

    public interface DepartmentSelectedStateListener {
        void onDepartmentSelected(DepartmentEntry departmentEntry);

        void onDepartmentDeselected(DepartmentEntry departmentEntry);
    }

    public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener {
        public View mItemView;
        public TextView mDepartmentName;
        boolean mIsItemSelected = false;

        public DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mDepartmentName = itemView.findViewById(R.id.item_department_name);

            // set the item click listener
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(int position) {
            if (departmentsSelectionMode == SELECTION_MODE_SINGLE && mIsItemSelected) {
                mIsItemSelected = false;
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            mDepartmentName.setText(mDepartments.get(position).getDepartmentName());
            mItemView.setTag(mDepartments.get(position).getDepartmentId());
        }

        @Override
        public void onClick(View v) {
            //if at least one of the items has a long click on it, its color will be grey
            //and for that, onClick will behave like onLongClick "select items"
            //if the item is selected and click on it again "long or normal click", its background will return white and will not be selected
            if (departmentsSelectionMode == SELECTION_MODE_MULTIPLE)
                changeItemSelectedState();
             else
            mGridItemClickListener.onItemClick((int) mItemView.getTag(), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (departmentsSelectionMode != SELECTION_MODE_MULTIPLE) {
                changeItemSelectedState();
                departmentsSelectionMode = SELECTION_MODE_MULTIPLE;
            }
            return true;
        }

        private void changeItemSelectedState() {

            if (!mIsItemSelected) {
                mItemView.setBackgroundColor(Color.parseColor("#888888"));
                mIsItemSelected = true;
                mDepartmentSelectedStateListener.onDepartmentSelected(mDepartments.get(getAdapterPosition()));
            } else {
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
                mIsItemSelected = false;
                mDepartmentSelectedStateListener.onDepartmentDeselected(mDepartments.get(getAdapterPosition()));
            }
        }
    }
}