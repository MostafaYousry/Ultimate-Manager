package com.example.android.employeesmanagementapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {
    private static final String TAG = EmployeesAdapter.class.getSimpleName();
    private List<EmployeeEntry> mData;
    final private RecyclerViewItemClickListener mClickListener;
    private boolean visible = false;
    private boolean mUseCheckBoxLayout;
    private int numOfSelected = 0;
    private CheckBoxClickListener mCheckBoxClickListener;
    private boolean mShowCheckBoxes;

    public EmployeesAdapter(RecyclerViewItemClickListener listener, boolean showCheckBoxes, CheckBoxClickListener checkBoxClickListener) {
        mClickListener = listener;
        mShowCheckBoxes = showCheckBoxes;
        mCheckBoxClickListener = checkBoxClickListener;
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


    public interface CheckBoxClickListener {
        void onCheckBoxClicked(int employeeID);
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //create object for each view in the item view
        TextView mEmployeeName;
        ImageView mEmployeeImage;
        CheckBox mEmployeeCheckBox;
        View mItemView;
        boolean isSelected = false;


        EmployeesViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            mItemView = itemView;
            mEmployeeName = itemView.findViewById(R.id.employee_name);
            mEmployeeImage = itemView.findViewById(R.id.employee_image);
            mEmployeeCheckBox = itemView.findViewById(R.id.employee_check_box);
            if (mShowCheckBoxes)
                mEmployeeCheckBox.setVisibility(View.VISIBLE);
            else
                mEmployeeCheckBox.setVisibility(View.GONE);


            // set the item click listener
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(final int position) {

            //change the item data by the position
            mEmployeeName.setText(mData.get(position).getEmployeeName());

            Glide.with(mEmployeeImage.getContext())
                    .load(AppUtils.getRandomEmployeeImage())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(mEmployeeImage);

            itemView.setTag(mData.get(position).getEmployeeID());


        }

        //if at least one of the items has a long click on it, its color will be grey
        //and for that, onClick will behave like onLongClick "select items"
        //if the item is selected and click on it again "long or normal click", its background will return white and will not be selected

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                mCheckBoxClickListener.onCheckBoxClicked((int) mItemView.getTag());
            }

            if (numOfSelected > 0) {
                changeItemMode();
            } else
                mClickListener.onItemClick((int) mItemView.getTag(), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            changeItemMode();
            return true;
        }

        private void changeItemMode() {
            if (!isSelected) {
                numOfSelected++;
                mItemView.setBackgroundColor(Color.parseColor("#888888"));
                isSelected = true;
            } else {
                numOfSelected--;
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
                isSelected = false;
            }
            mClickListener.onItemLongCLick((int) mItemView.getTag(), getAdapterPosition());
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
