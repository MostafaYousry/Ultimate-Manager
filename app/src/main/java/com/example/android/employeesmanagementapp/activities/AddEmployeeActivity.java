package com.example.android.employeesmanagementapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.employeesmanagementapp.MyAlarmReceiver;
import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.TextDrawable;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.factories.EmpIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewEmployeeViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddEmployeeActivity extends BaseAddActivity implements TasksAdapter.TasksItemClickListener {

    public static final String EMPLOYEE_ID_KEY = "employee_id";
    private static final int DEFAULT_EMPLOYEE_ID = -1;
    public static final String EMPLOYEE_IS_FIRED = "employee_is_fired";
    private static final boolean DEFAULT_EMPLOYEE_IS_FIRED = false;
    private boolean mEmployeeIsFired;

    private int mEmployeeId;
    private EmployeeEntry mOldEmployeeEntry;

    private EditText mEmployeeFirstName;
    private EditText mEmployeeMiddleName;
    private EditText mEmployeeLastName;
    private EditText mEmployeeEmail;
    private EditText mEmployeePhone;
    private EditText mEmployeeNote;
    private EditText mEmployeeSalary;
    private TextView mEmployeeHireDate;
    private Spinner mEmployeeDepartment;
    private ImageView mEmployeeImage;
    private String mEmployeePicturePath = "";
    private RatingBar mEmployeeRating;
    private RecyclerView mEmployeeCompletedTasksRv;
    private RecyclerView mEmployeeRunningTasksRv;


    private DepartmentsArrayAdapter mArrayAdapter;
    private CollapsingToolbarLayout mCollapsingToolbar;

    private boolean departmentsLoaded;
    private int clickedEmployeeDepId = -1;

    private AppDatabase mDb;
    private AddNewEmployeeViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        //create db instance
        mDb = AppDatabase.getInstance(this);


        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mEmployeeId = intent.getIntExtra(EMPLOYEE_ID_KEY, DEFAULT_EMPLOYEE_ID);
            mEmployeeIsFired = intent.getBooleanExtra(EMPLOYEE_IS_FIRED, DEFAULT_EMPLOYEE_IS_FIRED);
        }

        //instantiate AddNewEmployeeViewModel for this employee id
        mViewModel = ViewModelProviders.of(this, new EmpIdFact(mDb, mEmployeeId)).get(AddNewEmployeeViewModel.class);

        //set toolbar as actionbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        //get views
        mEmployeeFirstName = findViewById(R.id.employee_first_name);
        mEmployeeMiddleName = findViewById(R.id.employee_middle_name);
        mEmployeeLastName = findViewById(R.id.employee_last_name);
        mEmployeeEmail = findViewById(R.id.employee_email);
        mEmployeePhone = findViewById(R.id.employee_phone);
        mEmployeeSalary = findViewById(R.id.employee_salary);
        mEmployeeNote = findViewById(R.id.employee_note);
        mEmployeeHireDate = findViewById(R.id.employee_hire_date);

        //set employee hire date view click listener
        mEmployeeHireDate.setOnClickListener(this::showDatePicker);

        mEmployeeDepartment = findViewById(R.id.employee_department);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mEmployeeImage = findViewById(R.id.employee_image);

        //set click Listener to listen for clicks on image view
        mEmployeeImage.setOnClickListener(view -> showPhotoCameraDialog());

        mEmployeeRating = findViewById(R.id.employee_rating);


        //create a new departments array adapter for employee department spinner
        mArrayAdapter = new DepartmentsArrayAdapter(this, AppUtils.dpToPx(this, 12), AppUtils.dpToPx(this, 8), 0, AppUtils.dpToPx(this, 8), R.style.detailActivitiesTextStyle);

        if (!mEmployeeIsFired) {
            mViewModel.allDepartments.observe(this, departmentEntries -> {
                mArrayAdapter.setData(departmentEntries);
                departmentsLoaded = true;
                if (clickedEmployeeDepId != -1) {
                    mEmployeeDepartment.setSelection(mArrayAdapter.getPositionForItemId(clickedEmployeeDepId));
                }
            });
        } else {
            Snackbar.make(findViewById(R.id.coordinator), "Fired employees are view only", Snackbar.LENGTH_LONG).show();
        }

        //set the adapter
        mEmployeeDepartment.setAdapter(mArrayAdapter);


        setUpNameET();
        setUpRunningTasksRV();
        setUpCompletedTasksRV();


        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            clearViews();
        } else {
            final LiveData<EmployeeWithExtras> employee = mViewModel.employeeEntry;
            employee.observe(this, employeeWithExtras -> {
                employee.removeObservers(AddEmployeeActivity.this);
                populateUi(employeeWithExtras);
            });

        }

        ViewCompat.setNestedScrollingEnabled(mEmployeeCompletedTasksRv, false);

    }

    private void setUpRunningTasksRV() {
        mEmployeeRunningTasksRv = findViewById(R.id.employee_running_tasks_rv);
        mEmployeeRunningTasksRv.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeRunningTasksRv.setLayoutManager(layoutManager);

        final TasksAdapter adapter = new TasksAdapter(this, this, true);

        if (mEmployeeId != DEFAULT_EMPLOYEE_ID)
            mViewModel.employeeRunningTasks.observe(this, taskEntries -> {
                if (taskEntries != null && !taskEntries.isEmpty()) {
                    adapter.submitList(taskEntries);
                    mEmployeeRunningTasksRv.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageView9).setVisibility(View.VISIBLE);
                } else {
                    mEmployeeRunningTasksRv.setVisibility(View.GONE);
                    findViewById(R.id.imageView9).setVisibility(View.GONE);
                }
            });

        mEmployeeRunningTasksRv.setAdapter(adapter);
    }

    private void setUpCompletedTasksRV() {
        mEmployeeCompletedTasksRv = findViewById(R.id.employee_completed_tasks_rv);
        mEmployeeCompletedTasksRv.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeCompletedTasksRv.setLayoutManager(layoutManager);

        final TasksAdapter adapter = new TasksAdapter(this, this, true);

        if (mEmployeeId != DEFAULT_EMPLOYEE_ID)
            mViewModel.employeeCompletedTasks.observe(this, taskEntries -> {
                if (taskEntries != null && !taskEntries.isEmpty()) {
                    adapter.submitList(taskEntries);
                    mEmployeeCompletedTasksRv.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageView11).setVisibility(View.VISIBLE);
                } else {
                    mEmployeeCompletedTasksRv.setVisibility(View.GONE);
                    findViewById(R.id.imageView11).setVisibility(View.GONE);
                }
            });

        mEmployeeCompletedTasksRv.setAdapter(adapter);
    }

    /**
     * used to change toolbar text when user changes employee name
     */
    private void setUpNameET() {
        mEmployeeFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCollapsingToolbar.setTitle(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mCollapsingToolbar.setTitle(getString(R.string.add_new_employee));
                }
            }
        });
    }

    private void clearViews() {

        mCollapsingToolbar.setTitle(getString(R.string.add_new_employee));
        mEmployeeDepartment.setSelection(0);
        mEmployeeRating.setVisibility(View.GONE);
        mEmployeeCompletedTasksRv.setVisibility(View.GONE);
        findViewById(R.id.imageView9).setVisibility(View.GONE);

        mEmployeeImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_add));
        mEmployeeImage.setScaleType(ImageView.ScaleType.CENTER);
        mEmployeeImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.secondaryColor, getTheme()));

    }

    private void populateUi(EmployeeWithExtras employeeWithExtras) {
        if (employeeWithExtras == null)
            return;

        mOldEmployeeEntry = employeeWithExtras.employeeEntry;

        clickedEmployeeDepId = employeeWithExtras.employeeEntry.getDepartmentId();

        if (mEmployeeIsFired) {
            mDb.departmentsDao().loadDepartmentById(clickedEmployeeDepId).observe(this, new Observer<DepartmentEntry>() {
                @Override
                public void onChanged(DepartmentEntry departmentEntry) {
                    if (departmentEntry != null) {
                        List<DepartmentEntry> list = new ArrayList<>(1);
                        list.add(departmentEntry);
                        mArrayAdapter.setData(list);
                        mEmployeeDepartment.setSelection(0);
                    }

                }
            });


            disableViewClicks();
        } else {
            if (departmentsLoaded)
                mEmployeeDepartment.setSelection(mArrayAdapter.getPositionForItemId(employeeWithExtras.employeeEntry.getDepartmentId()));

        }

        mEmployeeFirstName.setText(employeeWithExtras.employeeEntry.getEmployeeFirstName());
        mEmployeeMiddleName.setText(employeeWithExtras.employeeEntry.getEmployeeMiddleName());
        mEmployeeLastName.setText(employeeWithExtras.employeeEntry.getEmployeeLastName());
        mEmployeePhone.setText(employeeWithExtras.employeeEntry.getEmployeePhone());
        mEmployeeNote.setText(employeeWithExtras.employeeEntry.getEmployeeNote());
        mEmployeeEmail.setText(employeeWithExtras.employeeEntry.getEmployeeEmail());
        mEmployeeSalary.setText(String.valueOf(employeeWithExtras.employeeEntry.getEmployeeSalary()));
        mEmployeeHireDate.setText(AppUtils.getFriendlyDate(employeeWithExtras.employeeEntry.getEmployeeHireDate().getTime()));
        mEmployeeHireDate.setTag(employeeWithExtras.employeeEntry.getEmployeeHireDate());

        mCollapsingToolbar.setTitle(employeeWithExtras.employeeEntry.getEmployeeFirstName());

        mEmployeeRating.setRating(employeeWithExtras.employeeRating);

        mEmployeePicturePath = employeeWithExtras.employeeEntry.getEmployeeImageUri();
        if (TextUtils.isEmpty(mEmployeePicturePath)) {
            TextDrawable textDrawable = new TextDrawable(this, employeeWithExtras.employeeEntry, AppUtils.dpToPx(this, 70), AppUtils.dpToPx(this, 70), AppUtils.spToPx(this, 28));
            mEmployeeImage.setImageDrawable(textDrawable);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(employeeWithExtras.employeeEntry.getEmployeeImageUri()))
                    .into(mEmployeeImage);
        }


    }

    private void disableViewClicks() {
        mEmployeeImage.setClickable(false);
        mEmployeeFirstName.setEnabled(false);
        mEmployeeFirstName.setFocusable(false);
        mEmployeeMiddleName.setEnabled(false);
        mEmployeeMiddleName.setFocusable(false);
        mEmployeeLastName.setEnabled(false);
        mEmployeeLastName.setFocusable(false);
        mEmployeeEmail.setEnabled(false);
        mEmployeeEmail.setFocusable(false);
        mEmployeePhone.setEnabled(false);
        mEmployeePhone.setFocusable(false);
        mEmployeeSalary.setEnabled(false);
        mEmployeeSalary.setFocusable(false);
        mEmployeeHireDate.setEnabled(false);
        mEmployeeHireDate.setFocusable(false);
        mEmployeeDepartment.setEnabled(false);
        mEmployeeDepartment.setFocusable(false);
        mEmployeeNote.setEnabled(false);
        mEmployeeNote.setFocusable(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_employee_activity, menu);
        if (mEmployeeIsFired)
            menu.removeItem(R.id.action_save);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            case R.id.action_phone:
                dialPhoneNumber();
                break;
            case R.id.action_mail:
                composeEmail();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            mEmployeePicturePath = fullPhotoUri.toString();
            Glide.with(this)
                    .asBitmap()
                    .load(fullPhotoUri)
                    .into(mEmployeeImage);


        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mEmployeePicturePath = cameraImageUri;
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(mEmployeePicturePath))
                    .into(mEmployeeImage);
        }

    }

    private EmployeeEntry getEmployeeEntry() {
        String employeeFirstName = mEmployeeFirstName.getText().toString();
        String employeeMiddleName = mEmployeeMiddleName.getText().toString();
        String employeeLastName = mEmployeeLastName.getText().toString();
        float employeeSalary = Float.parseFloat(mEmployeeSalary.getText().toString());
        String employeeEmail = mEmployeeEmail.getText().toString();
        String employeePhone = mEmployeePhone.getText().toString();
        String employeeNote = mEmployeeNote.getText().toString();
        Calendar employeeHireDate = (Calendar) mEmployeeHireDate.getTag();
        int departmentId = (int) mEmployeeDepartment.getSelectedView().getTag();

        if (mEmployeeId == DEFAULT_EMPLOYEE_ID)
            return new EmployeeEntry(departmentId, employeeFirstName, employeeMiddleName, employeeLastName, employeeSalary, employeeHireDate, employeeEmail, employeePhone, employeeNote, mEmployeePicturePath);
        else
            return new EmployeeEntry(mEmployeeId, departmentId, employeeFirstName, employeeMiddleName, employeeLastName, employeeSalary, employeeHireDate, employeeEmail, employeePhone, employeeNote, mEmployeePicturePath, mOldEmployeeEntry.isEmployeeIsDeleted());
    }

    private void composeEmail() {
        if (TextUtils.isEmpty(mEmployeeEmail.getText()))
            return;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + mEmployeeEmail.getText()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void dialPhoneNumber() {
        if (TextUtils.isEmpty(mEmployeePhone.getText()))
            return;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mEmployeePhone.getText()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onTaskClick(int taskRowID, int taskPosition, boolean taskIsCompleted) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        intent.putExtra(AddTaskActivity.TASK_IS_COMPLETED_KEY, taskIsCompleted);
        startActivity(intent);
    }

    @Override
    protected void save() {
        if (isDataValid()) {

            final EmployeeEntry employeeEntry = getEmployeeEntry();

            if (mEmployeeId != DEFAULT_EMPLOYEE_ID && clickedEmployeeDepId != employeeEntry.getDepartmentId()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Note");
                builder.setMessage("Department changed, employee will be removed from all running tasks. Do you wish to continue ?");
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppExecutor.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.employeesTasksDao().deleteEmployeeFromRunningTasks(mEmployeeId);
                                List<Integer> emptyTasksId = mDb.tasksDao().selectEmptyTasksId();
                                mDb.tasksDao().deleteEmptyTasks();
                                cancelEmptyTasksAlarm(emptyTasksId);

                                employeeEntry.setEmployeeID(mEmployeeId);
                                mDb.employeesDao().updateEmployee(employeeEntry);
                                dialogInterface.dismiss();

                                finish();

                            }
                        });
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        return;
                    }
                });

                builder.show();

            } else {

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mEmployeeId == DEFAULT_EMPLOYEE_ID)
                            mDb.employeesDao().addEmployee(employeeEntry);
                        else {
                            mDb.employeesDao().updateEmployee(employeeEntry);
                        }
                    }
                });
                finish();
            }


        }
    }

    private void cancelEmptyTasksAlarm(List<Integer> emptyTasksId) {
        for (int i = 0; i < emptyTasksId.size(); i++) {
            try {
                Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), emptyTasksId.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Intent serviceIntent = new Intent(getApplicationContext(), NotificationService.class);
                serviceIntent.putExtra("task id", emptyTasksId.get(i));
                getApplicationContext().startService(serviceIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected boolean isDataValid() {

        boolean valid = true;

        if (TextUtils.isEmpty(mEmployeeFirstName.getText())) {
            updateErrorVisibility("firstName", true);
            valid = false;
        } else {
            updateErrorVisibility("firstName", false);
        }

        if (TextUtils.isEmpty(mEmployeeEmail.getText())) {
            updateErrorVisibility("email", true);
            valid = false;
        } else {
            if (!mEmployeeEmail.getText().toString().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                updateErrorVisibility("emailValid", true);
                valid = false;
            } else
                updateErrorVisibility("emailValid", false);
        }

        if (TextUtils.isEmpty(mEmployeePhone.getText())) {
            updateErrorVisibility("phone", true);
            valid = false;
        } else {
            updateErrorVisibility("phone", false);
        }

        if (TextUtils.isEmpty(mEmployeeSalary.getText())) {
            updateErrorVisibility("salary", true);
            valid = false;
        } else {
            updateErrorVisibility("salary", false);
        }

        if (mEmployeeHireDate.getTag() == null) {
            updateErrorVisibility("hireDate", true);
            valid = false;
        } else {
            updateErrorVisibility("hireDate", false);
        }

        return valid;


    }

    @Override
    protected void updateErrorVisibility(String key, boolean show) {
        if (show)
            switch (key) {
                case "firstName":
                    ((TextInputLayout) findViewById(R.id.employee_first_name_TIL)).setError(getString(R.string.required_field));
                    break;
                case "email":
                    ((TextInputLayout) findViewById(R.id.employee_email_TIL)).setError(getString(R.string.required_field));
                    break;
                case "emailValid":
                    ((TextInputLayout) findViewById(R.id.employee_email_TIL)).setError(getString(R.string.error_invalid_email));
                    break;
                case "phone":
                    ((TextInputLayout) findViewById(R.id.employee_phone_TIL)).setError(getString(R.string.required_field));
                    break;
                case "salary":
                    ((TextInputLayout) findViewById(R.id.employee_salary_TIL)).setError(getString(R.string.required_field));
                    break;
                case "hireDate":
                    ((TextInputLayout) findViewById(R.id.employee_hire_date_TIL)).setError(getString(R.string.required_field));
                    break;
            }
        else
            switch (key) {
                case "firstName":
                    ((TextInputLayout) findViewById(R.id.employee_first_name_TIL)).setHelperText(getString(R.string.required_field));
                    break;
                case "emailValid":
                    ((TextInputLayout) findViewById(R.id.employee_email_TIL)).setHelperText(getString(R.string.required_field));
                    break;
                case "phone":
                    ((TextInputLayout) findViewById(R.id.employee_phone_TIL)).setHelperText(getString(R.string.required_field));
                    break;
                case "salary":
                    ((TextInputLayout) findViewById(R.id.employee_salary_TIL)).setHelperText(getString(R.string.required_field));
                    break;
                case "hireDate":
                    ((TextInputLayout) findViewById(R.id.employee_hire_date_TIL)).setHelperText(getString(R.string.required_field));
                    break;
            }
    }


    @Override
    protected boolean fieldsChanged() {
        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            if (!TextUtils.isEmpty(mEmployeeFirstName.getText()))
                return true;

            if (!TextUtils.isEmpty(mEmployeeMiddleName.getText()))
                return true;

            if (!TextUtils.isEmpty(mEmployeeLastName.getText()))
                return true;

            if (!TextUtils.isEmpty(mEmployeeEmail.getText()))
                return true;

            if (!TextUtils.isEmpty(mEmployeePhone.getText()))
                return true;

            if (!TextUtils.isEmpty(mEmployeeSalary.getText()))
                return true;

            if (mEmployeeHireDate.getTag() != null)
                return true;

            if (!TextUtils.isEmpty(mEmployeeNote.getText()))
                return true;

            if (!TextUtils.isEmpty(mEmployeePicturePath))
                return true;

            if (mEmployeeDepartment.getSelectedItemPosition() != 0)
                return true;
        } else
            return !mOldEmployeeEntry.equals(getEmployeeEntry());


        return false;
    }


    @Override
    public void onBackPressed() {
        if (mEmployeeIsFired)
            finish();
        else
            super.onBackPressed();
    }

}
