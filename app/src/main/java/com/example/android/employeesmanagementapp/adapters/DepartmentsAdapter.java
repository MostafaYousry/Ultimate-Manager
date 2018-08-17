package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.utils.ColorUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder> {
    private static final String TAG = DepartmentsAdapter.class.getSimpleName();

    private Context mContext;

    private List<DepartmentWithExtras> mDepartments;
    private DepartmentItemClickListener mDepartmentItemClickListener;

    public DepartmentsAdapter(Context context, @NonNull DepartmentItemClickListener gridItemClickListener) {
        mContext = context;
        mDepartmentItemClickListener = gridItemClickListener;
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
    public void setData(List<DepartmentWithExtras> departments) {
        mDepartments = departments;
        notifyDataSetChanged();
    }

    /**
     * interface to handle click events done on a recycler view item
     */
    public interface DepartmentItemClickListener {
        void onDepartmentClick(int departmentRowID, int departmentPosition);
    }

    public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mItemView;
        TextView mDepartmentName;
        TextView mDepartmentSummary;
        ImageView mDepartmentImage;

        DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mDepartmentName = itemView.findViewById(R.id.department_name);
            mDepartmentImage = itemView.findViewById(R.id.department_image);
            mDepartmentSummary = itemView.findViewById(R.id.department_summary);

            itemView.findViewById(R.id.pop_up_menu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(mContext, view);
                    popup.setGravity(Gravity.TOP);
                    popup.inflate(R.menu.menu_department_options);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_delete_department:

//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//                                            alertDialog.setTitle("Alert");
//                                            alertDialog.setMessage("if you deleted this Department all its employees and tasks will be deleted");
//                                            Log.i("test", "yes");
//                                            alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    mAddNewDepViewModel = ViewModelProviders.of(DepartmentsFragment.this, new DepIdFact(mDb, mAdapter.getClickedDepartment().getDepartmentId())).get(AddNewDepViewModel.class);
//                                                    final LiveData<List<TaskEntry>> depRunningTasks = mAddNewDepViewModel.getRunningTasks();
//                                                    depRunningTasks.observe(DepartmentsFragment.this, new Observer<List<TaskEntry>>() {
//                                                        @Override
//                                                        public void onChanged(final List<TaskEntry> taskEntries) {
//                                                            depRunningTasks.removeObserver(this);
//                                                            AppExecutor.getInstance().diskIO().execute(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    for (TaskEntry taskEntry : taskEntries) {
//                                                                        Log.i("test", taskEntry.getTaskTitle());
//                                                                        mDb.employeesTasksDao().deleteTaskJoinRecords(taskEntry.getTaskId());
//                                                                        mDb.tasksDao().deleteTask(taskEntry);
//                                                                    }
//                                                                }
//                                                            });
//                                                        }
//                                                    });
//                                                    final LiveData<List<EmployeeEntry>> depEmployees = mAddNewDepViewModel.getEmployees();
//                                                    depEmployees.observe(DepartmentsFragment.this, new Observer<List<EmployeeEntry>>() {
//                                                        @Override
//                                                        public void onChanged(final List<EmployeeEntry> employeeEntries) {
//                                                            depEmployees.removeObserver(this);
//                                                            AppExecutor.getInstance().diskIO().execute(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//                                                                    for (EmployeeEntry employeeEntry : employeeEntries) {
//                                                                        mDb.employeesDao().deleteEmployeeFromDepartmentTask(employeeEntry.getEmployeeID());
//                                                                    }
//                                                                }
//                                                            });
//                                                        }
//                                                    });
//
//                                                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            mAdapter.getClickedDepartment().setDepartmentIsDeleted(true);
//                                                            Log.i("test", mAdapter.getClickedDepartment().getDepartmentName());
//                                                            mDb.departmentsDao().updateDepartment(mAdapter.getClickedDepartment());
//                                                        }
//                                                    });
//                                                }
//                                            });
//                                            alertDialog.show();
//                                        }
//                                    });
//                                    //todo: make sure not to delete a full department
//                                    //hasEmployees=  mDb.employeesDao().loadEmployees(mSelectedDepartments.get(i).getDepartmentId());
//                                    //if(mhasEmployees != null)
//                                    // Toast.makeText(getContext(),"Can't delete this department because it has employees please move them or delete them first", Toast.LENGTH_LONG).show();
//
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });


            // set the item click listener
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mDepartmentName.setText(mDepartments.get(position).departmentEntry.getDepartmentName());

            String runningTasksSummary = mContext.getResources().getQuantityString(R.plurals.numberOfRunningTasks, mDepartments.get(position).numRunningTasks, mDepartments.get(position).numRunningTasks);
            String completedTasksSummary = mContext.getResources().getQuantityString(R.plurals.numberOfCompletedTasks, mDepartments.get(position).numCompletedTasks, mDepartments.get(position).numCompletedTasks);
            String numberOfEmployees = mContext.getResources().getQuantityString(R.plurals.numberOfEmployees, mDepartments.get(position).numOfEmployees, mDepartments.get(position).numOfEmployees);

            mDepartmentSummary.setText(mContext.getString(R.string.department_summary, runningTasksSummary, completedTasksSummary, numberOfEmployees));


            if (mDepartments.get(position).departmentEntry.getDepartmentImageUri() == null) {
                mDepartmentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_departments));
                mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);
                mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), ColorUtils.getDepartmentBackgroundColor(mDepartments.get(position).departmentEntry), mContext.getTheme()));
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .load(Uri.parse(mDepartments.get(position).departmentEntry.getDepartmentImageUri()))
                        .into(mDepartmentImage);
            }

            mItemView.setTag(mDepartments.get(position).departmentEntry.getDepartmentId());
        }

        @Override
        public void onClick(View v) {
            mDepartmentItemClickListener.onDepartmentClick((int) mItemView.getTag(), getAdapterPosition());
        }

    }
}