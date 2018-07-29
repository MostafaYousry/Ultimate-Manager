package com.example.android.employeesmanagementapp.adapters;

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
    public static final int SELECTION_MODE_SINGLE = 1;
    public static final int SELECTION_MODE_MULTIPLE = 2;
    private static final String TAG = EmployeesAdapter.class.getSimpleName();
    final private RecyclerViewItemClickListener mClickListener;
    private List<EmployeeEntry> mData;

    public List<EmployeeEntry> getData() {
        return mData;
    }

    private CheckBoxClickListener mCheckBoxClickListener;
    private int employeesSelectionMode;
    private EmployeeSelectedStateListener mEmployeeSelectedStateListener;

    public EmployeesAdapter(@NonNull RecyclerViewItemClickListener listener) {
        mClickListener = listener;
        employeesSelectionMode = SELECTION_MODE_SINGLE;
    }


    public EmployeesAdapter(@NonNull RecyclerViewItemClickListener listener, @NonNull CheckBoxClickListener checkBoxClickListener) {
        mClickListener = listener;
        mCheckBoxClickListener = checkBoxClickListener;
        employeesSelectionMode = SELECTION_MODE_SINGLE;
    }

    public EmployeesAdapter(@NonNull RecyclerViewItemClickListener listener, @NonNull EmployeeSelectedStateListener employeeSelectedStateListener) {
        mClickListener = listener;
        mEmployeeSelectedStateListener = employeeSelectedStateListener;
        employeesSelectionMode = SELECTION_MODE_SINGLE;
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

        if (mData == null)
            return 0;
        return mData.size();
    }

    /**
     * @return : current selectionMode : Single / Multiple
     */
    public int getEmployeeSelectionMode() {
        return employeesSelectionMode;
    }

    /**
     * used by employees fragment to start or finish a multi selection operation
     *
     * @param selectionMode : Single / Multiple
     */
    public void setEmployeeSelectionMode(int selectionMode) {
        employeesSelectionMode = selectionMode;
        notifyDataSetChanged();
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

    public interface EmployeeSelectedStateListener {
        void onEmployeeSelected(EmployeeEntry employeeEntry);

        void onEmployeeDeselected(EmployeeEntry employeeEntry);
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
        boolean mIsItemSelected = false;


        EmployeesViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            mItemView = itemView;
            mEmployeeName = itemView.findViewById(R.id.employee_name);
            mEmployeeImage = itemView.findViewById(R.id.employee_image);
            mEmployeeCheckBox = itemView.findViewById(R.id.employee_check_box);

            if (mCheckBoxClickListener != null)
                mEmployeeCheckBox.setVisibility(View.VISIBLE);
            else
                mEmployeeCheckBox.setVisibility(View.GONE);


            // set the item click listener
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(final int position) {

            if (employeesSelectionMode == SELECTION_MODE_SINGLE && mIsItemSelected) {
                mIsItemSelected = false;
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            //change the item data by the position
            mEmployeeName.setText(mData.get(position).getEmployeeName());

            Glide.with(mEmployeeImage.getContext())
                    .load(AppUtils.getRandomEmployeeImage())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(mEmployeeImage);

            itemView.setTag(mData.get(position).getEmployeeID());


        }


        @Override
        public void onClick(View v) {
            if (v instanceof CheckBox) {
                mCheckBoxClickListener.onCheckBoxClicked((int) mItemView.getTag());
            }

            //if at least one of the items has a long click on it, its color will be grey
            //and for that, onClick will behave like onLongClick "select items"
            //if the item is selected and click on it again "long or normal click", its background will return white and will not be selected
            if (employeesSelectionMode == SELECTION_MODE_MULTIPLE) {
                changeItemSelectedState();
            } else
                mClickListener.onItemClick((int) mItemView.getTag(), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (employeesSelectionMode != SELECTION_MODE_MULTIPLE) {
                changeItemSelectedState();
                employeesSelectionMode = SELECTION_MODE_MULTIPLE;
            }
            return true;
        }

        private void changeItemSelectedState() {

            if (!mIsItemSelected) {
                mItemView.setBackgroundColor(Color.parseColor("#888888"));
                mIsItemSelected = true;
                mEmployeeSelectedStateListener.onEmployeeSelected(mData.get(getAdapterPosition()));
            } else {
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
                mIsItemSelected = false;
                mEmployeeSelectedStateListener.onEmployeeDeselected(mData.get(getAdapterPosition()));
            }
        }


    }
}
