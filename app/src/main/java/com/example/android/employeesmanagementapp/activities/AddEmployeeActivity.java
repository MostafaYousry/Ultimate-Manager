package com.example.android.employeesmanagementapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class AddEmployeeActivity extends AppCompatActivity implements TasksAdapter.TasksItemClickListener {

    public static final String EMPLOYEE_ID_KEY = "employee_id";
    private static final String TAG = AddEmployeeActivity.class.getSimpleName();
    private static final int DEFAULT_EMPLOYEE_ID = -1;

    private int mEmployeeId;

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
    private RatingBar mEmployeeRating;
    private RecyclerView mEmployeeCompletedTasks;
    private String mEmployeePicturePath;

    private DepartmentsArrayAdapter mArrayAdapter;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;

    private boolean departmentsLoaded;
    private int clickedEmployeeDepId = -1;

    private AppDatabase mDb;
    private AddNewEmployeeViewModel mViewModel;


    @SuppressLint("ClickableViewAccessibility")
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
        }

        mViewModel = ViewModelProviders.of(this, new EmpIdFact(mDb, mEmployeeId)).get(AddNewEmployeeViewModel.class);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
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
        mEmployeeDepartment = findViewById(R.id.employee_department);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);

        mEmployeeImage = findViewById(R.id.employee_image);
        mEmployeeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.showPhotoCameraDialog(AddEmployeeActivity.this);
            }
        });
        mEmployeeRating = findViewById(R.id.employee_rating);


        mArrayAdapter = new DepartmentsArrayAdapter(this, AppUtils.dpToPx(this, 12), AppUtils.dpToPx(this, 8), 0, AppUtils.dpToPx(this, 8), R.style.detailActivitiesTextStyle);

        LiveData<List<DepartmentEntry>> departments = mViewModel.allDepartments;
        departments.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                mArrayAdapter.setData(departmentEntries);
                departmentsLoaded = true;
                if (clickedEmployeeDepId != -1) {
                    mEmployeeDepartment.setSelection(mArrayAdapter.getPositionForItemId(clickedEmployeeDepId));
                }
            }
        });
        mEmployeeDepartment.setAdapter(mArrayAdapter);


        setUpNameET();
        setUpCompletedTasksRV();


        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            clearViews();
        } else {
            final LiveData<EmployeeWithExtras> employee = mViewModel.employeeEntry;
            employee.observe(this, new Observer<EmployeeWithExtras>() {
                @Override
                public void onChanged(EmployeeWithExtras employeeWithExtras) {
                    employee.removeObservers(AddEmployeeActivity.this);
                    populateUi(employeeWithExtras);
                }
            });

        }

        ViewCompat.setNestedScrollingEnabled(mEmployeeCompletedTasks, false);

    }

    private void setUpCompletedTasksRV() {
        mEmployeeCompletedTasks = findViewById(R.id.employee_tasks_rv);
        mEmployeeCompletedTasks.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeCompletedTasks.setLayoutManager(layoutManager);

        final TasksAdapter adapter = new TasksAdapter(this, this, true);

//        if (mEmployeeId == DEFAULT_EMPLOYEE_ID)
//            adapter.setData(new ArrayList<TaskEntry>());
//        else {
        mViewModel.employeeCompletedTasks.observe(this, adapter::submitList);
//        }
        mEmployeeCompletedTasks.setAdapter(adapter);
    }


    public void pickDate(View view) {
        AppUtils.showDatePicker(this, view);
    }


    private void setUpNameET() {
        mEmployeeFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "Name is changing");
                mCollapsingToolbar.setTitle(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    getSupportActionBar().setTitle(getString(R.string.add_new_employee));
                }
            }
        });
    }

    private void clearViews() {

        mCollapsingToolbar.setTitle(getString(R.string.add_new_employee));
        mEmployeeDepartment.setSelection(0);
        mEmployeeRating.setVisibility(View.GONE);
        mEmployeeCompletedTasks.setVisibility(View.GONE);
        findViewById(R.id.imageView9).setVisibility(View.GONE);

        mEmployeeImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_add));
        mEmployeeImage.setScaleType(ImageView.ScaleType.CENTER);
        mEmployeeImage.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.secondaryColor, getTheme()));

    }

    private void populateUi(EmployeeWithExtras employeeWithExtras) {
        if (employeeWithExtras == null)
            return;

        clickedEmployeeDepId = employeeWithExtras.employeeEntry.getDepartmentId();

        mEmployeeFirstName.setText(employeeWithExtras.employeeEntry.getEmployeeFirstName());
        mEmployeeMiddleName.setText(employeeWithExtras.employeeEntry.getEmployeeMiddleName());
        mEmployeeLastName.setText(employeeWithExtras.employeeEntry.getEmployeeLastName());
        mEmployeePhone.setText(employeeWithExtras.employeeEntry.getEmployeePhone());
        mEmployeeNote.setText(employeeWithExtras.employeeEntry.getEmployeeNote());
        mEmployeeEmail.setText(employeeWithExtras.employeeEntry.getEmployeeEmail());
        mEmployeeSalary.setText(String.valueOf(employeeWithExtras.employeeEntry.getEmployeeSalary()));
        mEmployeeHireDate.setText(AppUtils.getFriendlyDate(employeeWithExtras.employeeEntry.getEmployeeHireDate()));
        mEmployeeHireDate.setTag(employeeWithExtras.employeeEntry.getEmployeeHireDate());

        mCollapsingToolbar.setTitle(employeeWithExtras.employeeEntry.getEmployeeFirstName());
        if (departmentsLoaded)
            mEmployeeDepartment.setSelection(mArrayAdapter.getPositionForItemId(employeeWithExtras.employeeEntry.getDepartmentId()));
        mEmployeeRating.setRating(employeeWithExtras.employeeRating);

        if (employeeWithExtras.employeeEntry.getEmployeeImageUri() == null) {
            TextDrawable textDrawable = new TextDrawable(this, employeeWithExtras.employeeEntry, AppUtils.dpToPx(this, 70), AppUtils.dpToPx(this, 70), AppUtils.spToPx(this, 28));
            mEmployeeImage.setImageDrawable(textDrawable);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(employeeWithExtras.employeeEntry.getEmployeeImageUri()))
                    .into(mEmployeeImage);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveEmployee();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageUtils.REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            Glide.with(this)
                    .asBitmap()
                    .load(fullPhotoUri)
                    .into(mEmployeeImage);

            ImageUtils.importCopy(this, fullPhotoUri);
            mEmployeePicturePath = ImageUtils.sImageURI;


        } else if (requestCode == ImageUtils.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mEmployeePicturePath = ImageUtils.sImageURI;
            Glide.with(this)
                    .asBitmap()
                    .load(Uri.parse(mEmployeePicturePath))
                    .into(mEmployeeImage);
        }

    }

    private void saveEmployee() {
        if (isDataValid()) {

            String employeeFirstName = mEmployeeFirstName.getText().toString();
            String employeeMiddleName = mEmployeeMiddleName.getText().toString();
            String employeeLastName = mEmployeeLastName.getText().toString();
            float employeeSalary = Float.parseFloat(mEmployeeSalary.getText().toString());
            String employeeEmail = mEmployeeEmail.getText().toString();
            String employeePhone = mEmployeePhone.getText().toString();
            String employeeNote = mEmployeeNote.getText().toString();
            Date employeeHireDate = (Date) mEmployeeHireDate.getTag();
            int departmentId = (int) mEmployeeDepartment.getSelectedView().getTag();

            final EmployeeEntry newEmployee = new EmployeeEntry(departmentId, employeeFirstName, employeeMiddleName, employeeLastName, employeeSalary, employeeHireDate, employeeEmail, employeePhone, employeeNote, mEmployeePicturePath);

            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mEmployeeId == DEFAULT_EMPLOYEE_ID)
                        mDb.employeesDao().addEmployee(newEmployee);
                    else {
                        newEmployee.setEmployeeID(mEmployeeId);
                        mDb.employeesDao().updateEmployee(newEmployee);
                    }
                }
            });
            finish();
        }

    }

    private boolean isDataValid() {

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

    private void updateErrorVisibility(String key, boolean show) {
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
    public void onTaskClick(int taskRowID, int taskPosition, boolean taskIsCompleted) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        intent.putExtra(AddTaskActivity.TASK_IS_COMPLETED_KEY, taskIsCompleted);
        startActivity(intent);
    }
}
