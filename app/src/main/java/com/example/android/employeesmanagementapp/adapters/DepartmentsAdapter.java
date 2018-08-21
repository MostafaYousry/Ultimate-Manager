package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.utils.ColorUtils;
import com.google.android.material.card.MaterialCardView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.paging.PagedListAdapter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class DepartmentsAdapter extends PagedListAdapter<DepartmentWithExtras, DepartmentsAdapter.DepartmentsViewHolder> {
    private static final String TAG = DepartmentsAdapter.class.getSimpleName();

    private Context mContext;

    private DepartmentItemClickListener mDepartmentItemClickListener;

    private static DiffUtil.ItemCallback<DepartmentWithExtras> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DepartmentWithExtras>() {

                @Override
                public boolean areItemsTheSame(DepartmentWithExtras oldDepartment, DepartmentWithExtras newDepartment) {
                    return oldDepartment.departmentEntry.getDepartmentId() == newDepartment.departmentEntry.getDepartmentId();
                }

                @Override
                public boolean areContentsTheSame(DepartmentWithExtras oldDepartment,
                                                  DepartmentWithExtras newDepartment) {
                    return oldDepartment.equals(newDepartment);
                }


            };


    public DepartmentsAdapter(Context context, @NonNull DepartmentItemClickListener gridItemClickListener) {
        super(DIFF_CALLBACK);
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
     * interface to handle click events done on a recycler view item
     */
    public interface DepartmentItemClickListener {
        void onDepartmentClick(int departmentRowID, int departmentPosition);
    }

    public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialCardView mItemView;
        TextView mDepartmentName;
        TextView mDepartmentSummary;
        ImageView mDepartmentImage;
        Palette.Filter paletteFilter = new Palette.Filter() {
            @Override
            public boolean isAllowed(int rgb, float[] hsl) {
                if (rgb == Color.WHITE)
                    return false;
                return true;
            }
        };

        DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = (MaterialCardView) itemView;
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

                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                                    alertDialog.setTitle("Note");
                                    alertDialog.setMessage("All running tasks and employees are about to be deleted, completed tasks will be saved.\nDo you wish to continue ?");
                                    alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AppDatabase db = AppDatabase.getInstance(mContext);
                                                    DepartmentEntry departmentEntry = getItem(getAdapterPosition()).departmentEntry;

                                                    for (EmployeeWithExtras employeeWithExtras : db.employeesDao().loadEmployeesExtrasInDep(departmentEntry.getDepartmentId())) {

                                                        int empID = employeeWithExtras.employeeEntry.getEmployeeID();

                                                        if (employeeWithExtras.employeeNumRunningTasks == 0 && db.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                                                            db.employeesDao().deleteEmployee(empID);
                                                        } else if (employeeWithExtras.employeeNumRunningTasks > 0 && db.employeesTasksDao().getNumCompletedTasksEmployee(empID) == 0) {
                                                            db.employeesTasksDao().deleteEmployeeJoinRecords(empID);
                                                            db.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);
                                                        } else if (employeeWithExtras.employeeNumRunningTasks > 0 && db.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                                                            db.employeesTasksDao().deleteEmployeeFromRunningTasks(empID);
                                                            db.employeesDao().deleteEmployee(empID);
                                                        } else {
                                                            db.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);
                                                        }

                                                    }

                                                    db.tasksDao().deleteEmptyTasks();

                                                    if (db.departmentsDao().getNumCompletedTasksDepartment(departmentEntry.getDepartmentId()) == 0) {
                                                        db.departmentsDao().deleteDepartment(departmentEntry);
                                                    } else {
                                                        departmentEntry.setDepartmentIsDeleted(true);
                                                        db.departmentsDao().updateDepartment(departmentEntry);
                                                    }

                                                }
                                            });

                                        }


                                    });

                                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });


                    popup.show();
                }

            });
            // set the item click listener
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mDepartmentName.setText(getItem(position).departmentEntry.getDepartmentName());

            String runningTasksSummary = mContext.getResources().getQuantityString(R.plurals.numberOfRunningTasks, getItem(position).numRunningTasks, getItem(position).numRunningTasks);
            String completedTasksSummary = mContext.getResources().getQuantityString(R.plurals.numberOfCompletedTasks, getItem(position).numCompletedTasks, getItem(position).numCompletedTasks);
            String numberOfEmployees = mContext.getResources().getQuantityString(R.plurals.numberOfEmployees, getItem(position).numOfEmployees, getItem(position).numOfEmployees);

            mDepartmentSummary.setText(mContext.getString(R.string.department_summary, numberOfEmployees, runningTasksSummary, completedTasksSummary));


            if (TextUtils.isEmpty(getItem(position).departmentEntry.getDepartmentImageUri())) {
                Glide.with(mContext).clear(mDepartmentImage);
                mDepartmentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_departments));
                mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);

                mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), ColorUtils.getDepartmentBackgroundColor(getItem(position).departmentEntry), mContext.getTheme()));

                mItemView.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.department_fallback_color, mContext.getTheme()));
            } else {
                Glide.with(mContext)
                        .asBitmap()
                        .load(Uri.parse(getItem(position).departmentEntry.getDepartmentImageUri()))
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                createPaletteAsync(resource);
                                return false;
                            }
                        })
                        .into(mDepartmentImage);
            }

            mItemView.setTag(getItem(position).departmentEntry.getDepartmentId());
        }

        @Override
        public void onClick(View v) {
            mDepartmentItemClickListener.onDepartmentClick((int) mItemView.getTag(), getAdapterPosition());
        }

        void createPaletteAsync(Bitmap bitmap) {
            Palette.Builder builder = new Palette.Builder(bitmap);
            builder.addFilter(paletteFilter);
            builder.generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {

                    int fallbackColor = palette.getDominantColor(0);

                    Palette.Swatch vibrantColorSwatch = palette.getVibrantSwatch();
                    if (vibrantColorSwatch != null) {
                        int vibrantColor = vibrantColorSwatch.getRgb();
                        mItemView.setCardBackgroundColor(vibrantColor);
                    } else {
                        mItemView.setCardBackgroundColor(fallbackColor);
                    }
                }
            });
        }

        void clear() {
            mDepartmentName.setText("");
            mDepartmentSummary.setText("");

            Glide.with(mContext).clear(mDepartmentImage);
            mItemView.setCardBackgroundColor(Color.WHITE);

        }
    }
}