package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.TextDrawable;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {
    public static final int SELECTION_MODE_SINGLE = 1;

    private Context mContext;


    final private EmployeeItemClickListener mClickListener;
    private List<EmployeeWithExtras> mData;
    public static final int SELECTION_MODE_MULTIPLE = 2;
    private static final String TAG = EmployeesAdapter.class.getSimpleName();
    private static final int HIGHLIGHT_COLOR = 0x999be6ff;
    private int employeesSelectionMode;
    private List<EmployeeWithExtras> mSelectedOnes;

    private EmployeeSelectedStateListener mEmployeeSelectedStateListener;


    public EmployeesAdapter(Context context, @NonNull EmployeeItemClickListener listener, @NonNull EmployeeSelectedStateListener employeeSelectedStateListener) {
        mContext = context;
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
     * used by employees fragment to start or finish a multi selection operation
     *
     * @param selectionMode : Single / Multiple
     */
    public void setEmployeeSelectionMode(int selectionMode) {
        if (selectionMode == SELECTION_MODE_MULTIPLE) {
            if (mSelectedOnes == null)
                mSelectedOnes = new ArrayList<>();
        } else {
            for (EmployeeWithExtras extras : mSelectedOnes) {
                extras.employeeEntry.setChecked(false);
                notifyItemChanged(mData.indexOf(extras));

            }
            mSelectedOnes = null;
        }
        employeesSelectionMode = selectionMode;
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

        void onMultiSelectStart();

        void onMultiSelectFinish();

        void onSelectedNumChanged(int numSelected);

    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        //create object for each view in the item view


        TextView mEmployeeName;
        ImageView mEmployeeImage;
        ImageView mCheckIcon;
        RatingBar mEmployeeRating;
        TextView mNumRunningTasks;
        MaterialCardView mItemView;


        EmployeesViewHolder(View itemView) {
            super(itemView);

            //set the objects by the opposite view by id
            mItemView = (MaterialCardView) itemView;
            mEmployeeName = itemView.findViewById(R.id.employee_name);
            mEmployeeImage = itemView.findViewById(R.id.employee_image);
            mCheckIcon = itemView.findViewById(R.id.check_icon);
            mEmployeeRating = itemView.findViewById(R.id.employee_rating);
            mNumRunningTasks = itemView.findViewById(R.id.employee_has_tasks_runnung);


            // set the item click listener
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(final int position) {

            updateCheckedState(mData.get(position).employeeEntry);


            //change the item data by the position
            EmployeeWithExtras employeeWithExtras = mData.get(position);


            mEmployeeName.setText(mContext.getString(R.string.employee_list_item_name, employeeWithExtras.employeeEntry.getEmployeeFirstName(), employeeWithExtras.employeeEntry.getEmployeeMiddleName()));

            mEmployeeRating.setRating(employeeWithExtras.employeeRating);

            int numberRunningTasks = employeeWithExtras.employeeNumRunningTasks;
            String runningTasksStr = mContext.getResources().getQuantityString(R.plurals.numberOfRunningTasks, numberRunningTasks, numberRunningTasks);
            mNumRunningTasks.setText(runningTasksStr);

            if (employeeWithExtras.employeeEntry.getEmployeeImageUri() == null) {
                Context context = mContext;

                Glide.with(context).clear(mEmployeeImage);

                TextDrawable textDrawable = new TextDrawable(context, employeeWithExtras.employeeEntry, AppUtils.dpToPx(context, 70), AppUtils.dpToPx(context, 70), AppUtils.spToPx(context, 28));
                mEmployeeImage.setImageDrawable(textDrawable);
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .load(Uri.parse(employeeWithExtras.employeeEntry.getEmployeeImageUri()))
                        .into(mEmployeeImage);
            }


            itemView.setTag(employeeWithExtras.employeeEntry.getEmployeeID());


        }


        @Override
        public void onClick(View v) {

            if (employeesSelectionMode == SELECTION_MODE_MULTIPLE) {
                EmployeeWithExtras extras = mData.get(getAdapterPosition());
                extras.employeeEntry.setChecked(!extras.employeeEntry.isChecked());
                updateCheckedState(extras.employeeEntry);


                if (extras.employeeEntry.isChecked()) {
                    mSelectedOnes.add(extras);
                } else {
                    mSelectedOnes.remove(extras);
                }

                if (mSelectedOnes.isEmpty()) {
                    mEmployeeSelectedStateListener.onMultiSelectFinish();
                    employeesSelectionMode = SELECTION_MODE_SINGLE;
                    mSelectedOnes = null;
                } else {
                    mEmployeeSelectedStateListener.onSelectedNumChanged(mSelectedOnes.size());
                }

            } else
                mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (employeesSelectionMode != SELECTION_MODE_MULTIPLE) {
                mEmployeeSelectedStateListener.onMultiSelectStart();

                mData.get(getAdapterPosition()).employeeEntry.setChecked(true);
                updateCheckedState(mData.get(getAdapterPosition()).employeeEntry);

                mSelectedOnes = new ArrayList<>();
                mSelectedOnes.add(mData.get(getAdapterPosition()));
                mEmployeeSelectedStateListener.onSelectedNumChanged(mSelectedOnes.size());

                employeesSelectionMode = SELECTION_MODE_MULTIPLE;
            }
            return true;
        }

        private void updateCheckedState(EmployeeEntry employeeEntry) {

            if (employeeEntry.isChecked()) {
                mCheckIcon.setVisibility(View.VISIBLE);
                mEmployeeImage.setVisibility(View.GONE);
                mItemView.setCardBackgroundColor(HIGHLIGHT_COLOR);
            } else {
                mCheckIcon.setVisibility(View.GONE);
                mEmployeeImage.setVisibility(View.VISIBLE);
                mItemView.setCardBackgroundColor(Color.WHITE);
            }

        }


    }
}
