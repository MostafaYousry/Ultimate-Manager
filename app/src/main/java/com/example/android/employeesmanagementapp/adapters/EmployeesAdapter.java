package com.example.android.employeesmanagementapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.RecyclerViewItemLongClickListener;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {
    private static final String TAG = EmployeesAdapter.class.getSimpleName();
    private List<EmployeeEntry> mData;
    final private RecyclerViewItemClickListener mClickListener;
    final private RecyclerViewItemLongClickListener mLongClickListener;
    private boolean visible = false;
    private boolean mUseCheckBoxLayout;
    private int numOfSelected = 0;

    public EmployeesAdapter(RecyclerViewItemClickListener listener, boolean useCheckBoxLayout, RecyclerViewItemLongClickListener longClickListener) {
        mClickListener = listener;
        mUseCheckBoxLayout = useCheckBoxLayout;
        mLongClickListener = longClickListener;
    }

    @NonNull
    @Override
    public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //todo:check which item layout
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

        if (mData == null)
            return 0;
        return mData.size();
    }


    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //create object for each view in the item view
        View mItemView;
        TextView employeeName;
        ImageView employeeImage;
        CheckBox employeeCheckBox;
        boolean isSelected = false;

        EmployeesViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            employeeName = itemView.findViewById(R.id.employee_name);
            employeeImage = itemView.findViewById(R.id.employee_image);
            employeeCheckBox = itemView.findViewById(R.id.employee_check_box);
            mItemView = itemView;
            //todo:check which layout's viewholder

            // set the item click listener
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(final int position) {

            //change the item data by the position
            employeeName.setText(mData.get(position).getEmployeeName());
            employeeImage.setImageResource(AppUtils.getRandomEmployeeImage());
            mItemView.setTag(mData.get(position).getEmployeeID());


        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            if (numOfSelected > 0) {
                if (!isSelected) {
                    numOfSelected++;
                    mItemView.setBackgroundColor(Color.parseColor("#888888"));
                    isSelected = true;
                } else {
                    numOfSelected--;
                    mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
                    isSelected = false;
                }
                mLongClickListener.onItemLongCLick((int) mItemView.getTag());
            } else
                mClickListener.onItemClick((int) mItemView.getTag());
        }

        @Override
        public boolean onLongClick(View view) {
            System.out.println(isSelected + "   " + (int) mItemView.getTag());
            if (!isSelected) {
                numOfSelected++;
                mItemView.setBackgroundColor(Color.parseColor("#888888"));
                isSelected = true;
            } else {
                numOfSelected--;
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
                isSelected = false;
            }
            mLongClickListener.onItemLongCLick((int) mItemView.getTag());
            return true;
        }

        private void setItemViewBackGround(int color) {
            mItemView.setBackgroundColor(color);
        }
    }

    /**
     * used to update adapters data if any change occurs
     *
     * @param employees new employees list
     */
    public void setData(List<EmployeeEntry> employees) {
        mData = employees;
        notifyDataSetChanged();
    }
}