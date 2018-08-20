package com.example.android.employeesmanagementapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.HorizontalEmployeeAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.example.android.employeesmanagementapp.utils.ColorUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddDepartmentActivity extends BaseAddActivity implements HorizontalEmployeeAdapter.HorizontalEmployeeItemClickListener, TasksAdapter.TasksItemClickListener {

    public static final String DEPARTMENT_ID_KEY = "department_id";
    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private int mDepartmentId;
    private DepartmentEntry mOldDepartmentEntry;

    private RecyclerView mDepCompletedTasksRV;
    private RecyclerView mDepRunningTasksRV;
    private RecyclerView mDepEmployeesRV;

    private ImageView mDepartmentImage;
    private EditText mDepartmentName;
    private String mDepartmentPicturePath = "";
    private EditText mDepartmentDateCreated;

    private CollapsingToolbarLayout mCollapsingToolbar;

    private AppDatabase mDb;
    private AddNewDepViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        mDb = AppDatabase.getInstance(this);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mDepartmentId = intent.getIntExtra(DEPARTMENT_ID_KEY, DEFAULT_DEPARTMENT_ID);
        }

        //find views
        mDepartmentName = findViewById(R.id.department_name);
        mDepartmentImage = findViewById(R.id.department_image);
        mDepartmentDateCreated = findViewById(R.id.department_date_created);
        mDepartmentDateCreated.setOnClickListener(this::showDatePicker);

        mDepEmployeesRV = findViewById(R.id.department_employees_rv);
        mDepRunningTasksRV = findViewById(R.id.department_running_tasks_rv);
        mDepCompletedTasksRV = findViewById(R.id.department_completed_tasks_rv);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);

        mDepartmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoCameraDialog();
            }
        });

        //set toolbar as actionbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        mViewModel = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class);


        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
        } else {
            final LiveData<DepartmentEntry> department = mViewModel.departmentEntry;
            department.observe(this, departmentEntry -> {
                department.removeObservers(AddDepartmentActivity.this);
                populateUi(departmentEntry);
            });

            setUpEmployeesRV();
            setUpDepRunningTasksRV();
            setUpDepCompletedTasksRV();

            ViewCompat.setNestedScrollingEnabled(mDepEmployeesRV, false);
            ViewCompat.setNestedScrollingEnabled(mDepCompletedTasksRV, false);
            ViewCompat.setNestedScrollingEnabled(mDepRunningTasksRV, false);

        }

        mDepartmentDateCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(view);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            mDepartmentPicturePath = fullPhotoUri.toString();
            Glide.with(this)
                    .asBitmap()
                    .load(fullPhotoUri)
                    .into(mDepartmentImage);


        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mDepartmentPicturePath = cameraImageUri;
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(mDepartmentPicturePath))
                    .into(mDepartmentImage);
        }

    }

    private void setUpEmployeesRV() {

        mDepEmployeesRV.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mDepEmployeesRV.setLayoutManager(layoutManager);

        final HorizontalEmployeeAdapter adapter = new HorizontalEmployeeAdapter(this, this, false);
        mDepEmployeesRV.setAdapter(adapter);


        mViewModel.departmentEmployees.observe(this, new Observer<PagedList<EmployeeEntry>>() {
            @Override
            public void onChanged(PagedList<EmployeeEntry> employeeEntries) {
                if (employeeEntries != null && !employeeEntries.isEmpty()) {
                    adapter.submitList(employeeEntries);
                    mDepEmployeesRV.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageView9).setVisibility(View.VISIBLE);
                } else {
                    mDepEmployeesRV.setVisibility(View.GONE);
                    findViewById(R.id.imageView9).setVisibility(View.GONE);
                }
            }
        });


    }

    private void setUpDepRunningTasksRV() {

        mDepRunningTasksRV.setHasFixedSize(false);

        mDepRunningTasksRV.setLayoutManager(new LinearLayoutManager(this));
        TasksAdapter adapter = new TasksAdapter(this, this, false);
        mDepRunningTasksRV.setAdapter(adapter);


        mViewModel.departmentRunningTasks.observe(this, new Observer<PagedList<TaskEntry>>() {
            @Override
            public void onChanged(PagedList<TaskEntry> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()) {
                    adapter.submitList(taskEntries);
                    mDepRunningTasksRV.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageView10).setVisibility(View.VISIBLE);
                } else {
                    mDepRunningTasksRV.setVisibility(View.GONE);
                    findViewById(R.id.imageView10).setVisibility(View.GONE);
                }
            }
        });
    }

    private void setUpDepCompletedTasksRV() {

        mDepCompletedTasksRV.setHasFixedSize(false);

        mDepCompletedTasksRV.setLayoutManager(new LinearLayoutManager(this));
        final TasksAdapter adapter = new TasksAdapter(this, this, true);
        mDepCompletedTasksRV.setAdapter(adapter);


        mViewModel.departmentCompletedTasks.observe(this, new Observer<PagedList<TaskEntry>>() {
            @Override
            public void onChanged(PagedList<TaskEntry> taskEntries) {
                if (taskEntries != null && !taskEntries.isEmpty()) {
                    adapter.submitList(taskEntries);
                    mDepCompletedTasksRV.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageView11).setVisibility(View.VISIBLE);
                } else {
                    mDepCompletedTasksRV.setVisibility(View.GONE);
                    findViewById(R.id.imageView11).setVisibility(View.GONE);
                }
            }
        });

    }

    private void populateUi(DepartmentEntry departmentEntry) {
        if (departmentEntry == null)
            return;

        mOldDepartmentEntry = departmentEntry;

        mDepartmentName.setText(departmentEntry.getDepartmentName());
        mCollapsingToolbar.setTitle(departmentEntry.getDepartmentName());

        mDepartmentPicturePath = departmentEntry.getDepartmentImageUri();

        if (TextUtils.isEmpty(mDepartmentPicturePath)) {
            mDepartmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_departments));
            mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);
            mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), ColorUtils.getDepartmentBackgroundColor(departmentEntry), getTheme()));
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(mDepartmentPicturePath))
                    .into(mDepartmentImage);
        }

        mDepartmentDateCreated.setText(AppUtils.getFriendlyDate(departmentEntry.getDepartmentDateCreated()));
        mDepartmentDateCreated.setTag(departmentEntry.getDepartmentDateCreated());
    }

    private void clearViews() {
        getSupportActionBar().setTitle("Add department");
        mDepartmentName.setText("");

        mDepCompletedTasksRV.setVisibility(View.GONE);
        mDepRunningTasksRV.setVisibility(View.GONE);
        mDepEmployeesRV.setVisibility(View.GONE);

        mDepartmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_add));
        mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);
        mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.secondaryColor, getTheme()));

        findViewById(R.id.imageView9).setVisibility(View.GONE);
        findViewById(R.id.imageView10).setVisibility(View.GONE);
        findViewById(R.id.imageView11).setVisibility(View.GONE);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private DepartmentEntry getDepartmentEntry() {
        String departmentName = mDepartmentName.getText().toString();
        Date departmentCreatedDate = (Date) mDepartmentDateCreated.getTag();

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID)
            return new DepartmentEntry(departmentName, departmentCreatedDate, mDepartmentPicturePath);
        else
            return new DepartmentEntry(mDepartmentId, departmentName, departmentCreatedDate, mDepartmentPicturePath, mOldDepartmentEntry.isDepartmentIsDeleted());
    }

    @Override
    public void onEmployeeClick(int employeeRowID, int employeePosition, boolean isFired) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_IS_FIRED, isFired);
        startActivity(intent);
    }

    @Override
    public void onTaskClick(int taskRowID, int taskPosition, boolean taskIsCompleted) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        intent.putExtra(AddTaskActivity.TASK_IS_COMPLETED_KEY, taskIsCompleted);
        startActivity(intent);
    }

    @Override
    protected boolean isDataValid() {

        boolean valid = true;

        if (TextUtils.isEmpty(mDepartmentName.getText())) {
            updateErrorVisibility("departmentName", true);
            valid = false;
        } else {
            updateErrorVisibility("departmentName", false);
        }


        if (mDepartmentDateCreated.getTag() == null) {
            updateErrorVisibility("departmentDateCreated", true);
            valid = false;
        } else {
            updateErrorVisibility("departmentDateCreated", false);
        }

        return valid;


    }

    @Override
    protected void updateErrorVisibility(String key, boolean show) {
        if (show)
            switch (key) {
                case "departmentName":
                    ((TextInputLayout) findViewById(R.id.department_name_TIL)).setError(getString(R.string.required_field));
                    break;
                case "departmentDateCreated":
                    ((TextInputLayout) findViewById(R.id.department_date_created_TIL)).setError(getString(R.string.required_field));
                    break;
            }
        else
            switch (key) {
                case "departmentName":
                    ((TextInputLayout) findViewById(R.id.department_name_TIL)).setHelperText(getString(R.string.required_field));
                    break;
                case "departmentDateCreated":
                    ((TextInputLayout) findViewById(R.id.department_date_created_TIL)).setHelperText(getString(R.string.required_field));
                    break;
            }
    }

    @Override
    protected void save() {
        if (isDataValid()) {

            final DepartmentEntry newDepartment = getDepartmentEntry();

            AppExecutor.getInstance().diskIO().execute(() -> {
                if (mDepartmentId == DEFAULT_DEPARTMENT_ID)
                    mDb.departmentsDao().addDepartment(newDepartment);
                else {
                    mDb.departmentsDao().updateDepartment(newDepartment);
                }

            });

            finish();
        }
    }


    @Override
    protected boolean fieldsChanged() {
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            if (!TextUtils.isEmpty(mDepartmentName.getText()))
                return true;

            if (mDepartmentDateCreated.getTag() != null)
                return true;

            if (!TextUtils.isEmpty(mDepartmentPicturePath))
                return true;

        } else
            return !mOldDepartmentEntry.equals(getDepartmentEntry());


        return false;
    }

    @Override
    protected void showDiscardChangesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard changes");
        builder.setMessage("All changes will be discarded.");
        builder.setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                save();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
