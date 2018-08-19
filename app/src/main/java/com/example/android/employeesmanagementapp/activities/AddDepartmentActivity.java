package com.example.android.employeesmanagementapp.activities;

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
import com.example.android.employeesmanagementapp.MyTextWatcher;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
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
import com.example.android.employeesmanagementapp.utils.ImageUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddDepartmentActivity extends AppCompatActivity implements EmployeesAdapter.EmployeeItemClickListener, TasksAdapter.TasksItemClickListener {

    public static final String DEPARTMENT_ID_KEY = "department_id";
    private static final String TAG = AddDepartmentActivity.class.getSimpleName();
    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private int mDepartmentId;

    private RecyclerView mDepCompletedTasksRV;
    private RecyclerView mDepRunningTasksRV;
    private RecyclerView mDepEmployeesRV;

    private ImageView mDepartmentImage;
    private EditText mDepartmentName;
    private String mDepartmentPicturePath;
    private String mMainDepartmentPicturePath;

    private EditText mDepartmentDateCreated;

    private Toolbar mToolbar;
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
        mDepEmployeesRV = findViewById(R.id.department_employees_rv);
        mDepRunningTasksRV = findViewById(R.id.department_running_tasks_rv);
        mDepCompletedTasksRV = findViewById(R.id.department_completed_tasks_rv);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);

        mDepartmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.showPhotoCameraDialog(AddDepartmentActivity.this);
            }
        });

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        mViewModel = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class);


        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
            setUpTextWatcher();
        } else {
            final LiveData<DepartmentEntry> department = mViewModel.getDepartment();
            department.observe(this, new Observer<DepartmentEntry>() {
                @Override
                public void onChanged(DepartmentEntry departmentEntry) {
                    department.removeObservers(AddDepartmentActivity.this);
                    populateUi(departmentEntry);
                    setUpTextWatcher();
                }
            });

            setUpEmployeesRV();
            setUpDepRunningTasksRV();
            setUpDepCompletedTasksRV();

            ViewCompat.setNestedScrollingEnabled(mDepEmployeesRV, false);
            ViewCompat.setNestedScrollingEnabled(mDepCompletedTasksRV, false);
            ViewCompat.setNestedScrollingEnabled(mDepRunningTasksRV, false);

        }

    }

    private void setUpTextWatcher() {
        mDepartmentName.addTextChangedListener(new MyTextWatcher(mDepartmentName.getText().toString()));
        mDepartmentDateCreated.addTextChangedListener(new MyTextWatcher(mDepartmentDateCreated.getText().toString()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtils.REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            Glide.with(this)
                    .asBitmap()
                    .load(fullPhotoUri)
                    .into(mDepartmentImage);

            ImageUtils.importCopy(this, fullPhotoUri);
            mDepartmentPicturePath = ImageUtils.sImageURI;


        } else if (requestCode == ImageUtils.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mDepartmentPicturePath = ImageUtils.sImageURI;
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


        final LiveData<List<EmployeeEntry>> depEmployees = mViewModel.getEmployees();
        depEmployees.observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employees) {
                if (employees != null && !employees.isEmpty())
                    adapter.setData(employees);
                else {
                    findViewById(R.id.imageView9).setVisibility(View.GONE);
                    mDepEmployeesRV.setVisibility(View.GONE);
                }
            }
        });


    }

    private void setUpDepRunningTasksRV() {

        mDepRunningTasksRV.setHasFixedSize(true);

        mDepRunningTasksRV.setLayoutManager(new LinearLayoutManager(this));
        final TasksAdapter adapter = new TasksAdapter(this, this, false);
        mDepRunningTasksRV.setAdapter(adapter);


        final LiveData<List<TaskEntry>> depCompletedTasks = mViewModel.getRunningTasks();
        depCompletedTasks.observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> tasks) {
                if (tasks != null && !tasks.isEmpty())
                    adapter.setData(tasks);
                else {
                    findViewById(R.id.imageView10).setVisibility(View.GONE);
                    mDepRunningTasksRV.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setUpDepCompletedTasksRV() {

        mDepCompletedTasksRV.setHasFixedSize(true);

        mDepCompletedTasksRV.setLayoutManager(new LinearLayoutManager(this));
        final TasksAdapter adapter = new TasksAdapter(this, this, true);
        mDepCompletedTasksRV.setAdapter(adapter);


        final LiveData<List<TaskEntry>> depCompletedTasks = mViewModel.getCompletedTasks();
        depCompletedTasks.observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> tasks) {
                if (tasks != null && !tasks.isEmpty())
                    adapter.setData(tasks);
                else {
                    findViewById(R.id.imageView11).setVisibility(View.GONE);
                    mDepCompletedTasksRV.setVisibility(View.GONE);
                }
            }
        });

    }

    private void populateUi(DepartmentEntry departmentEntry) {
        if (departmentEntry == null)
            return;

        mDepartmentName.setText(departmentEntry.getDepartmentName());
        mCollapsingToolbar.setTitle(departmentEntry.getDepartmentName());

        if (departmentEntry.getDepartmentImageUri() == null) {
            mDepartmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_departments));
            mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);
            mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), ColorUtils.getDepartmentBackgroundColor(departmentEntry), getTheme()));
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(departmentEntry.getDepartmentImageUri()))
                    .into(mDepartmentImage);

            mMainDepartmentPicturePath = departmentEntry.getDepartmentImageUri();
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

    public void pickDate(View view) {
        AppUtils.showDatePicker(this, view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveDepartment();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDepartment() {
        if (isDataValid()) {
            String departmentName = mDepartmentName.getText().toString();
            Date departmentCreatedDate = (Date) mDepartmentDateCreated.getTag();

            final DepartmentEntry newDepartment = new DepartmentEntry(departmentName, departmentCreatedDate, mDepartmentPicturePath);

            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDepartmentId == DEFAULT_DEPARTMENT_ID)
                        mDb.departmentsDao().addDepartment(newDepartment);
                    else {
                        newDepartment.setDepartmentId(mDepartmentId);
                        mDb.departmentsDao().updateDepartment(newDepartment);
                    }

                }
            });

            finish();
        }

    }

    private boolean isDataValid() {

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

    private void updateErrorVisibility(String key, boolean show) {
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
    public void onEmployeeClick(int employeeRowID, int employeePosition) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        startActivity(intent);
    }

    @Override
    public void onTaskClick(int taskRowID, int taskPosition) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (AppUtils.getNumOfChangedFiled() > 0 || (mDepartmentPicturePath != null && !mMainDepartmentPicturePath.equals(mDepartmentPicturePath))) {
            AppUtils.showDiscardChangesDialog(AddDepartmentActivity.this);
        }
        else super.onBackPressed();
    }
}
