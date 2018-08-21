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
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.HorizontalAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.example.android.employeesmanagementapp.utils.ColorUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * class for handling adding and editing departments
 * <p>
 * it shows department details
 * --name
 * --date created
 * --employees
 * --running tasks
 * --completed tasks
 */
public class AddDepartmentActivity extends BaseAddActivity implements HorizontalAdapter.HorizontalEmployeeItemClickListener, TasksAdapter.TasksItemClickListener {

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
        // if id = -1 then it is a new department else it is an old department
        Intent intent = getIntent();
        if (intent != null) {
            mDepartmentId = intent.getIntExtra(DEPARTMENT_ID_KEY, DEFAULT_DEPARTMENT_ID);
        }

        //find views
        mDepartmentName = findViewById(R.id.department_name);
        mDepartmentImage = findViewById(R.id.department_image);
        mDepartmentDateCreated = findViewById(R.id.department_date_created);

        //set click listener for department date created to show date picker dialog
        mDepartmentDateCreated.setOnClickListener(this::showDatePicker);

        mDepEmployeesRV = findViewById(R.id.department_employees_rv);
        mDepRunningTasksRV = findViewById(R.id.department_running_tasks_rv);
        mDepCompletedTasksRV = findViewById(R.id.department_completed_tasks_rv);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);

        //set on click listener for the image view to show dialog for choosing
        //either take a camera picture or loading one from device
        mDepartmentImage.setOnClickListener(view -> showPhotoCameraDialog());

        //set toolbar as actionbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        //create instance of this activities view model (AddNewDepViewModel)
        mViewModel = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class);


        //decide weather to load old department or create a new one
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
        } else {
            //load department entry from db
            final LiveData<DepartmentEntry> department = mViewModel.departmentEntry;
            department.observe(this, departmentEntry -> {
                department.removeObservers(AddDepartmentActivity.this);
                populateUi(departmentEntry);
            });

            //setup lists for department's (employees , running tasks,completed tasks)
            setUpEmployeesRV();
            setUpDepRunningTasksRV();
            setUpDepCompletedTasksRV();

            //disable nested scrolling to avoid sluggish scrolling
            ViewCompat.setNestedScrollingEnabled(mDepEmployeesRV, false);
            ViewCompat.setNestedScrollingEnabled(mDepCompletedTasksRV, false);
            ViewCompat.setNestedScrollingEnabled(mDepRunningTasksRV, false);

        }

    }


    /**
     * gets called after loading a picture or taking a camera picture
     * it displays the loaded image in the image view (in appbar layout)
     * and saves the ure in mDepartmentPicturePath variable to be later stored in save()
     *
     * @param requestCode : unique code assigned when calling start activity for result
     * @param resultCode  : operation is successful or not
     * @param data        : intent returned from camera or file chooset
     */
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

    /**
     * shows list of all department's employees in
     * a recycler view that uses horizontal linear layout manager
     * to display list as a horizontally scrolling list
     */
    private void setUpEmployeesRV() {

        mDepEmployeesRV.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mDepEmployeesRV.setLayoutManager(layoutManager);

        final HorizontalAdapter adapter = new HorizontalAdapter(this, true, false, 0, this);
        mDepEmployeesRV.setAdapter(adapter);


        mViewModel.departmentEmployees.observe(this, employeeEntries -> {
            if (employeeEntries != null && !employeeEntries.isEmpty()) {
                adapter.submitList(employeeEntries);
                mDepEmployeesRV.setVisibility(View.VISIBLE);
                findViewById(R.id.imageView9).setVisibility(View.VISIBLE);
            } else {
                mDepEmployeesRV.setVisibility(View.GONE);
                findViewById(R.id.imageView9).setVisibility(View.GONE);
            }
        });


    }

    /**
     * shows list of all department's running tasks
     */
    private void setUpDepRunningTasksRV() {

        mDepRunningTasksRV.setHasFixedSize(false);

        mDepRunningTasksRV.setLayoutManager(new LinearLayoutManager(this));
        TasksAdapter adapter = new TasksAdapter(this, this, false);
        mDepRunningTasksRV.setAdapter(adapter);


        mViewModel.departmentRunningTasks.observe(this, taskEntries -> {
            if (taskEntries != null && !taskEntries.isEmpty()) {
                adapter.submitList(taskEntries);
                mDepRunningTasksRV.setVisibility(View.VISIBLE);
                findViewById(R.id.imageView10).setVisibility(View.VISIBLE);
            } else {
                mDepRunningTasksRV.setVisibility(View.GONE);
                findViewById(R.id.imageView10).setVisibility(View.GONE);
            }
        });
    }

    /**
     * shows list of all department's completed tasks
     */
    private void setUpDepCompletedTasksRV() {

        mDepCompletedTasksRV.setHasFixedSize(false);

        mDepCompletedTasksRV.setLayoutManager(new LinearLayoutManager(this));
        final TasksAdapter adapter = new TasksAdapter(this, this, true);
        mDepCompletedTasksRV.setAdapter(adapter);


        mViewModel.departmentCompletedTasks.observe(this, taskEntries -> {
            if (taskEntries != null && !taskEntries.isEmpty()) {
                adapter.submitList(taskEntries);
                mDepCompletedTasksRV.setVisibility(View.VISIBLE);
                findViewById(R.id.imageView11).setVisibility(View.VISIBLE);
            } else {
                mDepCompletedTasksRV.setVisibility(View.GONE);
                findViewById(R.id.imageView11).setVisibility(View.GONE);
            }
        });

    }

    /**
     * displays all this dentr's data in relevant views
     *
     * @param departmentEntry
     */
    private void populateUi(DepartmentEntry departmentEntry) {
        if (departmentEntry == null)
            return;

        //save an old version of the department entry to be diffed (using equals)
        //later on to decide weather to show discard changes dialog or not
        mOldDepartmentEntry = departmentEntry;

        mDepartmentName.setText(departmentEntry.getDepartmentName());
        mCollapsingToolbar.setTitle(departmentEntry.getDepartmentName());

        mDepartmentPicturePath = departmentEntry.getDepartmentImageUri();

        //if uri of department's image is not set
        //show default image implementation
        //that is department icon in center and a unique background color
        //that depends of this object(departmentEntry) hashcode
        if (TextUtils.isEmpty(mDepartmentPicturePath)) {
            mDepartmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_departments));
            mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);
            mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), ColorUtils.getDepartmentBackgroundColor(departmentEntry), getTheme()));
        } else {
            //loads the department image with it's uri using Glide
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(mDepartmentPicturePath))
                    .into(mDepartmentImage);
        }

        mDepartmentDateCreated.setText(AppUtils.getFriendlyDate(departmentEntry.getDepartmentDateCreated().getTime()));

        //set tag on this view to be used in date picker fragment and saved in save()
        mDepartmentDateCreated.setTag(departmentEntry.getDepartmentDateCreated());
    }

    /**
     * restore state of views to adding a new department
     */
    private void clearViews() {

        getSupportActionBar().setTitle(getString(R.string.toolbar_add_department));

        //hide recycler views as they are empty
        mDepCompletedTasksRV.setVisibility(View.GONE);
        mDepRunningTasksRV.setVisibility(View.GONE);
        mDepEmployeesRV.setVisibility(View.GONE);

        //sets camera add icon and background to the image view to
        //show that clicking it chooses a picture for department
        mDepartmentImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_add));
        mDepartmentImage.setScaleType(ImageView.ScaleType.CENTER);
        mDepartmentImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.secondaryColor, getTheme()));

        //hide recycler views helper icons (department employees icon , department running tasks icon , department completed tasks icon)
        findViewById(R.id.imageView9).setVisibility(View.GONE);
        findViewById(R.id.imageView10).setVisibility(View.GONE);
        findViewById(R.id.imageView11).setVisibility(View.GONE);
    }

    /**
     * inflate toolbar menu for AddDepartmentActivity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * handles choosing menu item from toolbar
     */
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

    /**
     * creates a department entry with the data in the views
     */
    private DepartmentEntry getDepartmentEntry() {
        String departmentName = mDepartmentName.getText().toString();
        Calendar departmentCreatedDate = (Calendar) mDepartmentDateCreated.getTag();

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID)
            return new DepartmentEntry(departmentName, departmentCreatedDate, mDepartmentPicturePath);
        else
            return new DepartmentEntry(mDepartmentId, departmentName, departmentCreatedDate, mDepartmentPicturePath, mOldDepartmentEntry.isDepartmentIsDeleted());
    }

    /**
     * notifies the activity when a department employee is clicked
     * to open AddEmployeeActivity to edit/view this employee
     *
     * @param employeeRowID    : employee record id
     * @param employeePosition : adapter position
     * @param isFired          : weather employee is fired(deleted) or not
     */
    @Override
    public void onEmployeeClick(int employeeRowID, int employeePosition, boolean isFired) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_IS_FIRED, isFired);
        startActivity(intent);
    }

    /**
     * notifies the activity when a department task is clicked
     * to open AddTaskActivity to view/edit this task
     *
     * @param taskRowID       : task record id
     * @param taskPosition    : adapter position
     * @param taskIsCompleted : running/completed task
     */
    @Override
    public void onTaskClick(int taskRowID, int taskPosition, boolean taskIsCompleted) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        intent.putExtra(AddTaskActivity.TASK_IS_COMPLETED_KEY, taskIsCompleted);
        startActivity(intent);
    }

    /**
     * used to check if all the data of department is valid
     * and shows/hides error and helper messages accordingly
     *
     * @return : if data is valid true else false
     */
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

    /**
     * shows or hides error and helper messages for each field
     *
     * @param key  : field name
     * @param show : to show or hide the error
     */
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

    /**
     * save (insert/update) the department entry
     */
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


    /**
     * checks if any data is changed without saving
     *
     * @return
     */
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

}
