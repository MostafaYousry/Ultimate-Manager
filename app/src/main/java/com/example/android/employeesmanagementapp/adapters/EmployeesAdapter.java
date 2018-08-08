package com.example.android.employeesmanagementapp.adapters;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {
    public static final int SELECTION_MODE_SINGLE = 1;
    public static final int SELECTION_MODE_MULTIPLE = 2;
    private static final String TAG = EmployeesAdapter.class.getSimpleName();
    final private EmployeeItemClickListener mClickListener;
    private List<EmployeeWithExtras> mData;


    private int employeesSelectionMode;
    private EmployeeSelectedStateListener mEmployeeSelectedStateListener;
    private SparseBooleanArray mSelectedEmployees = new SparseBooleanArray();


    public EmployeesAdapter(@NonNull EmployeeItemClickListener listener, @NonNull EmployeeSelectedStateListener employeeSelectedStateListener) {
        mClickListener = listener;
        mEmployeeSelectedStateListener = employeeSelectedStateListener;
        employeesSelectionMode = SELECTION_MODE_SINGLE;
    }

    public EmployeeEntry getItem(int position) {
        return mData.get(position).employeeEntry;
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
        if(mSelectedEmployees.get(position) == true){
            holder.itemView.setBackgroundColor(Color.parseColor("#888888"));
        }
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
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
    public void setData(List<EmployeeWithExtras> employees) {
        mData = employees;
        notifyDataSetChanged();
    }

    /**
     * interface to handle click events done on a recycler view item
     */
    public interface EmployeeItemClickListener {
        void onEmployeeClick(int employeeRowID, int employeePosition);
    }

    public interface EmployeeSelectedStateListener {
        //void onEmployeeSelected(EmployeeEntry employeeEntry);

        void onEmployeeSelected(EmployeeWithExtras employeeEntry);

        void onEmployeeDeselected(EmployeeWithExtras employeeEntry);
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //create object for each view in the item view
        TextView mEmployeeName;
        ImageView mEmployeeImage;
        RatingBar mEmployeeRating;
        TextView mNumRunningTasks;
        View mItemView;
        boolean mIsItemSelected = false;


        EmployeesViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            mItemView = itemView;
            mEmployeeName = itemView.findViewById(R.id.employee_name);
            mEmployeeImage = itemView.findViewById(R.id.employee_image);
            mEmployeeRating = itemView.findViewById(R.id.employee_rating);
            mNumRunningTasks = itemView.findViewById(R.id.employee_has_tasks_runnung);


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
            mEmployeeName.setText(mData.get(position).employeeEntry.getEmployeeName());

            mEmployeeRating.setRating(mData.get(position).employeeRating);

            mNumRunningTasks.setText(mData.get(position).employeeNumRunningTasks + " running tasks");

            Glide.with(mEmployeeImage.getContext())
                    .load(AppUtils.getRandomEmployeeImage())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(mEmployeeImage);

            itemView.setTag(mData.get(position).employeeEntry.getEmployeeID());


        }


        @Override
        public void onClick(View v) {
            //if at least one of the items has a long click on it, its color will be grey
            //and for that, onClick will behave like onLongClick "select items"
            //if the item is selected and click on it again "long or normal click", its background will return white and will not be selected
            if (employeesSelectionMode == SELECTION_MODE_MULTIPLE) {
                changeItemSelectedState();
            } else
                mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition());
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

            if (!mSelectedEmployees.get(getAdapterPosition())) {
                mItemView.setBackgroundColor(Color.parseColor("#888888"));
                mIsItemSelected = true;
                mSelectedEmployees.append(getAdapterPosition(),true);
                Log.v("employees" , "adding");
                mEmployeeSelectedStateListener.onEmployeeSelected(mData.get(getAdapterPosition()));
            } else {
                mItemView.setBackgroundColor(Color.parseColor("#ffffff"));
                mIsItemSelected = false;
                mSelectedEmployees.delete(getAdapterPosition());
                Log.v("employees" , "removing");
                mEmployeeSelectedStateListener.onEmployeeDeselected(mData.get(getAdapterPosition()));
            }
        }


    }
}
