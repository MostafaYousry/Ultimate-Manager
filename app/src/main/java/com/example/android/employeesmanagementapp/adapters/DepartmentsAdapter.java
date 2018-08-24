package com.example.android.employeesmanagementapp.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.employeesmanagementapp.MyAlarmReceiver;
import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.utils.ColorUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.paging.PagedListAdapter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Recycler view adapter for
 * displaying list of departments
 * extends paged list adapter class
 * to allow list paging
 */
public class DepartmentsAdapter extends PagedListAdapter<DepartmentWithExtras, DepartmentsAdapter.DepartmentsViewHolder> {

    private Context mContext;

    private DepartmentItemClickListener mDepartmentItemClickListener;

    //the diff that is called by the paged list adapter
    //adapter uses this diff to know what changes occurred
    //between the current list and a new list
    //it handles notifyItemRemoved() , inserted , changed ,... automatically
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
        return new DepartmentsViewHolder(rootView);
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

        //filter to palette colors to reject white colors as our text color is white
        Palette.Filter paletteFilter = (rgb, hsl) -> rgb != Color.WHITE;

        DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = (MaterialCardView) itemView;
            mDepartmentName = itemView.findViewById(R.id.department_name);
            mDepartmentImage = itemView.findViewById(R.id.department_image);
            mDepartmentSummary = itemView.findViewById(R.id.department_summary);

            //sets a click listener on the three dots to show a popup menu with department options
            itemView.findViewById(R.id.pop_up_menu).setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(mContext, view);
                popup.setGravity(Gravity.TOP);
                popup.inflate(R.menu.menu_department_options);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.action_delete_department:

                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setMessage(mContext.getString(R.string.dialog_message_delete_department));
                            alertDialog.setPositiveButton(mContext.getString(R.string.dialog_positive_btn_continue), (dialogInterface, i) ->
                                    AppExecutor.getInstance().diskIO().execute(() -> {
                                        AppDatabase db = AppDatabase.getInstance(mContext);
                                        DepartmentEntry departmentEntry = getItem(getAdapterPosition()).departmentEntry;

                                        //first delete all employees in department
                                        for (EmployeeWithExtras employeeWithExtras : db.employeesDao().loadEmployeesExtrasInDep(departmentEntry.getDepartmentId())) {

                                            int empID = employeeWithExtras.employeeEntry.getEmployeeID();

                                            //if employee has completed tasks only mark employee as deleted only
                                            if (employeeWithExtras.employeeNumRunningTasks == 0 && db.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                                                db.employeesDao().markEmployeeAsDeleted(empID);

                                                //if employee has running tasks only remove employee from them then delete employee record
                                            } else if (employeeWithExtras.employeeNumRunningTasks > 0 && db.employeesTasksDao().getNumCompletedTasksEmployee(empID) == 0) {
                                                db.employeesTasksDao().deleteEmployeeJoinRecords(empID);
                                                db.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);

                                                //if employee has both so remove him from running tasks only
                                                //then mark him as deleted
                                            } else if (employeeWithExtras.employeeNumRunningTasks > 0 && db.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                                                db.employeesTasksDao().deleteEmployeeFromRunningTasks(empID);
                                                db.employeesDao().markEmployeeAsDeleted(empID);
                                            } else {
                                                //if he has nothing delete employee record
                                                db.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);
                                            }

                                        }

                                        //then check if tasks with no employees have appeared
                                        //due to the above employees deletion
                                        db.tasksDao().deleteTasksWithNoEmployees();
                                                    List<Integer> emptyTasksId = db.tasksDao().selectEmptyTasksId();
                                                    db.tasksDao().deleteEmptyTasks();
                                                    cancelEmptyTasksAlarm(emptyTasksId);

                                        //then delete department
                                        //if department has no completed tasks
                                        //delete department record
                                        if (db.employeesTasksDao().getNumCompletedTasksDepartment(departmentEntry.getDepartmentId()) == 0) {
                                            db.departmentsDao().deleteDepartment(departmentEntry);
                                        } else {
                                            //if it has then mark department as deleted
                                            departmentEntry.setDepartmentIsDeleted(true);
                                            db.departmentsDao().updateDepartment(departmentEntry);
                                        }

                                    }));

                            alertDialog.setNegativeButton(mContext.getString(R.string.dialog_negative_btn_cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                            alertDialog.show();
                            return true;
                        default:
                            return false;
                    }
                });


                popup.show();
            });

            // set the item click listener
            itemView.setOnClickListener(this);
        }

        //binds this item view with it's corresponding data
        void bind(int position) {

            mDepartmentName.setText(getItem(position).departmentEntry.getDepartmentName());

            //gets summary quantity strings
            String runningTasksSummary = mContext.getResources().getQuantityString(R.plurals.numberOfRunningTasks, getItem(position).numRunningTasks, getItem(position).numRunningTasks);
            String completedTasksSummary = mContext.getResources().getQuantityString(R.plurals.numberOfCompletedTasks, getItem(position).numCompletedTasks, getItem(position).numCompletedTasks);
            String numberOfEmployees = mContext.getResources().getQuantityString(R.plurals.numberOfEmployees, getItem(position).numOfEmployees, getItem(position).numOfEmployees);

            mDepartmentSummary.setText(mContext.getString(R.string.department_summary, numberOfEmployees, runningTasksSummary, completedTasksSummary));


            //if department has no image then use default
            //which is a department icon in center and unique background color depending
            //on object's hashcode , the background of the summary text is grey
            if (TextUtils.isEmpty(getItem(position).departmentEntry.getDepartmentImageUri())) {
                Glide.with(mContext).clear(mDepartmentImage);
                mDepartmentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_departments));
                mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);

                mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), ColorUtils.getDepartmentBackgroundColor(getItem(position).departmentEntry.getDepartmentName()), mContext.getTheme()));

                mItemView.setCardBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.department_fallback_color, mContext.getTheme()));
            } else {
                //if department has an image then load it using Glide
                Glide.with(mContext)
                        .asBitmap()
                        .load(Uri.parse(getItem(position).departmentEntry.getDepartmentImageUri()))
                        //listener that notifies us when the image is ready
                        //to extract a palette for it in order to correctly choose
                        //a vibrant color as the background of summary text
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

            //sets id of the department record as the tag for this view
            //to be retrieved later when a click occurs
            mItemView.setTag(getItem(position).departmentEntry.getDepartmentId());
        }

        /**
         * callback that notifies the fragment when a click on rv item occurs
         *
         * @param v : view
         */
        @Override
        public void onClick(View v) {
            mDepartmentItemClickListener.onDepartmentClick((int) mItemView.getTag(), getAdapterPosition());
        }

        /**
         * creates a palette in a background thread and when finished it registers the
         * resulting color to the background color of the department's summary text
         *
         * @param bitmap
         */
        void createPaletteAsync(Bitmap bitmap) {
            Palette.Builder builder = new Palette.Builder(bitmap);
            builder.addFilter(paletteFilter);
            builder.generate(palette -> {

                //choose a fallback color as the dominant color in image
                //to set this color if no vibrant colors exist
                int fallbackColor = palette.getDominantColor(0);

                //get the most vibrant swatch in image
                Palette.Swatch vibrantColorSwatch = palette.getVibrantSwatch();
                if (vibrantColorSwatch != null) {
                    //if it exists set it
                    int vibrantColor = vibrantColorSwatch.getRgb();
                    mItemView.setCardBackgroundColor(vibrantColor);
                } else {
                    //if it doesn't then set the fallback color
                    mItemView.setCardBackgroundColor(fallbackColor);
                }
            });
        }

        /**
         * clears view holder to default values
         * used for place holders in paged list adapter
         */
        void clear() {
            mDepartmentName.setText("");
            mDepartmentSummary.setText("");

            Glide.with(mContext).clear(mDepartmentImage);
            mItemView.setCardBackgroundColor(Color.WHITE);

        }
    }

    private void cancelEmptyTasksAlarm(List<Integer> emptyTasksId) {
        for (int i = 0; i < emptyTasksId.size(); i++) {
            try {
                Intent intent = new Intent(mContext, MyAlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, emptyTasksId.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Intent serviceIntent = new Intent(mContext, NotificationService.class);
                serviceIntent.putExtra("task id", emptyTasksId.get(i));
                mContext.startService(serviceIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}