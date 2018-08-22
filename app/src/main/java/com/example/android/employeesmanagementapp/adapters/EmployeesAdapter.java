package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
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
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Recycler view adapter for
 * displaying list of employees
 * extends paged list adapter class
 * to allow list paging
 */
public class EmployeesAdapter extends PagedListAdapter<EmployeeWithExtras, EmployeesAdapter.EmployeesViewHolder> {

    private Context mContext;

    public static final int SELECTION_MODE_SINGLE = 1;
    public static final int SELECTION_MODE_MULTIPLE = 2;
    //the diff that is called by the paged list adapter
    //adapter uses this diff to know what changes occurred
    //between the current list and a new list
    //it handles notifyItemRemoved() , inserted , changed ,... automatically
    private static DiffUtil.ItemCallback<EmployeeWithExtras> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<EmployeeWithExtras>() {
                @Override
                public boolean areItemsTheSame(EmployeeWithExtras oldEmployee, EmployeeWithExtras newEmployee) {
                    return oldEmployee.employeeEntry.getEmployeeID() == newEmployee.employeeEntry.getEmployeeID();
                }

                @Override
                public boolean areContentsTheSame(EmployeeWithExtras oldEmployee,
                                                  EmployeeWithExtras newEmployee) {
                    return oldEmployee.equals(newEmployee);
                }
            };
    final private EmployeeItemClickListener mClickListener;

    private static final int HIGHLIGHT_COLOR = 0x999be6ff;

    private List<EmployeeWithExtras> mSelectedOnes;

    private EmployeeSelectedStateListener mEmployeeSelectedStateListener;
    private int employeesSelectionMode;


    public EmployeesAdapter(Context context, @NonNull EmployeeItemClickListener listener, @NonNull EmployeeSelectedStateListener employeeSelectedStateListener) {
        super(DIFF_CALLBACK);
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

        return new EmployeesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesViewHolder holder, int position) {
        if (getItem(position) != null) {
            holder.bind(position);
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }

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
                notifyItemChanged(getCurrentList().indexOf(extras));

            }
            mSelectedOnes = null;
        }
        employeesSelectionMode = selectionMode;
    }

    /**
     * @return all selected employees
     */
    public List<EmployeeWithExtras> getSelectedOnes() {
        return mSelectedOnes;
    }

    /**
     * interface to handle click events done on a recycler view item
     */
    public interface EmployeeItemClickListener {
        void onEmployeeClick(int employeeRowID, int employeePosition);
    }

    /**
     * interface to handle multi selection operation
     */
    public interface EmployeeSelectedStateListener {

        void onMultiSelectStart();

        void onMultiSelectFinish();

        void onSelectedNumChanged(int numSelected);
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mEmployeeName;
        ImageView mEmployeeImage;
        ImageView mCheckIcon;
        RatingBar mEmployeeRating;
        TextView mNumRunningTasks;
        MaterialCardView mItemView;

        EmployeesViewHolder(View itemView) {
            super(itemView);
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

        //binds this item view with it's corresponding data
        void bind(final int position) {

            EmployeeWithExtras employeeWithExtras = getItem(position);

            updateCheckedState(employeeWithExtras.employeeEntry);

            mEmployeeName.setText(mContext.getString(R.string.employee_list_item_name, employeeWithExtras.employeeEntry.getEmployeeFirstName(), employeeWithExtras.employeeEntry.getEmployeeMiddleName() != null ? employeeWithExtras.employeeEntry.getEmployeeMiddleName() : ""));

            mEmployeeRating.setRating(employeeWithExtras.employeeRating);

            //quantity string for number of running tasks
            int numberRunningTasks = employeeWithExtras.employeeNumRunningTasks;
            String runningTasksStr = mContext.getResources().getQuantityString(R.plurals.numberOfRunningTasks, numberRunningTasks, numberRunningTasks);
            mNumRunningTasks.setText(runningTasksStr);

            //if employee has no image then use default
            //which is a first letter of his first name in a700 color
            //and it's background is corresponding a100 color colors are unique
            //as the depend on object's hashcode
            if (TextUtils.isEmpty(employeeWithExtras.employeeEntry.getEmployeeImageUri())) {
                Glide.with(mContext).clear(mEmployeeImage);

                TextDrawable textDrawable = new TextDrawable(mContext, employeeWithExtras.employeeEntry, AppUtils.dpToPx(mContext, 70), AppUtils.dpToPx(mContext, 70), AppUtils.spToPx(mContext, 28));
                mEmployeeImage.setImageDrawable(textDrawable);
            } else {
                //if employee has an image then load it using Glide
                Glide.with(mContext)
                        .asBitmap()
                        .load(Uri.parse(employeeWithExtras.employeeEntry.getEmployeeImageUri()))
                        .into(mEmployeeImage);
            }

            //sets id of the employee record as the tag for this view
            //to be retrieved later when a click occurs
            itemView.setTag(employeeWithExtras.employeeEntry.getEmployeeID());

        }


        /**
         * callback that notifies the fragment when a click on rv item occurs
         * handles multi/single selection mode
         *
         * @param v : view
         */
        @Override
        public void onClick(View v) {

            //if selection mode is multiple then
            //clicking will select the item
            if (employeesSelectionMode == SELECTION_MODE_MULTIPLE) {
                //update item to be checked
                EmployeeWithExtras extras = getItem(getAdapterPosition());
                extras.employeeEntry.setChecked(!extras.employeeEntry.isChecked());
                updateCheckedState(extras.employeeEntry);

                //add or remove it from selected list
                if (extras.employeeEntry.isChecked()) {
                    mSelectedOnes.add(extras);
                } else {
                    mSelectedOnes.remove(extras);
                }

                //update selection mode if selected list becomes empty
                //then selection mode must rollback to single
                if (mSelectedOnes.isEmpty()) {
                    mEmployeeSelectedStateListener.onMultiSelectFinish();
                    employeesSelectionMode = SELECTION_MODE_SINGLE;
                    mSelectedOnes = null;
                } else {
                    //update toolbar counter for selected items
                    mEmployeeSelectedStateListener.onSelectedNumChanged(mSelectedOnes.size());
                }

            } else // normal click on rv item
                mClickListener.onEmployeeClick((int) mItemView.getTag(), getAdapterPosition());
        }

        /**
         * starts a multi selection mode operation
         *
         * @param view
         * @return
         */
        @Override
        public boolean onLongClick(View view) {
            if (employeesSelectionMode != SELECTION_MODE_MULTIPLE) {
                mEmployeeSelectedStateListener.onMultiSelectStart();

                getItem(getAdapterPosition()).employeeEntry.setChecked(true);
                updateCheckedState(getItem(getAdapterPosition()).employeeEntry);

                mSelectedOnes = new ArrayList<>();
                mSelectedOnes.add(getItem(getAdapterPosition()));
                mEmployeeSelectedStateListener.onSelectedNumChanged(mSelectedOnes.size());

                employeesSelectionMode = SELECTION_MODE_MULTIPLE;
                return true;
            }
            return false;
        }

        /**
         * update changes in item layout to
         * display to the user that the item is selected
         * or deselected
         *
         * @param employeeEntry
         */
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


        /**
         * clears view holder to default values
         * used for place holders in paged list adapter
         */
        void clear() {

//            updateCheckedState(mData.get(position).employeeEntry);

            mEmployeeName.setText("");

            mEmployeeRating.setRating(0);

            mNumRunningTasks.setText("");

            Glide.with(mContext).clear(mEmployeeImage);


        }
    }
}
