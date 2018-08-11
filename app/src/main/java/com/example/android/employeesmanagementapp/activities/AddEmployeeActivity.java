package com.example.android.employeesmanagementapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.factories.EmpIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewEmployeeViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    private EditText mEmployeeName;
    private EditText mEmployeeSalary;
    private TextView mEmployeeHireDate;
    private Spinner mEmployeeDepartment;
    private ImageView mEmployeeImage;
    private RatingBar mEmployeeRating;
    private RecyclerView mEmployeeCompletedTasks;

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
        mEmployeeName = findViewById(R.id.employee_name);
        mEmployeeSalary = findViewById(R.id.employee_salary);
        mEmployeeHireDate = findViewById(R.id.employee_hire_date);
        mEmployeeDepartment = findViewById(R.id.employee_department);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mEmployeeImage = findViewById(R.id.employee_image);
        mEmployeeRating = findViewById(R.id.employee_rating);


        mArrayAdapter = new DepartmentsArrayAdapter(this);
        LiveData<List<DepartmentEntry>> departments = mViewModel.getAllDepartments();
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
        setUpTasksRV();


        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            clearViews();
        } else {
            final LiveData<EmployeeWithExtras> employee = mViewModel.getEmployee();
            employee.observe(this, new Observer<EmployeeWithExtras>() {
                @Override
                public void onChanged(EmployeeWithExtras employeeWithExtras) {
                    employee.removeObservers(AddEmployeeActivity.this);
                    populateUi(employeeWithExtras);
                }
            });

        }

    }

    private void setUpTasksRV() {
        mEmployeeCompletedTasks = findViewById(R.id.employee_tasks_rv);
        mEmployeeCompletedTasks.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEmployeeCompletedTasks.setLayoutManager(layoutManager);

        final TasksAdapter adapter = new TasksAdapter(this);

        if (mEmployeeId == DEFAULT_EMPLOYEE_ID)
            adapter.setData(new ArrayList<TaskEntry>());
        else {
            final LiveData<List<TaskEntry>> employeeCompletedTasks = mViewModel.getEmployeeCompletedTasks();
            employeeCompletedTasks.observe(this, new Observer<List<TaskEntry>>() {
                @Override
                public void onChanged(List<TaskEntry> tasks) {
                    employeeCompletedTasks.removeObservers(AddEmployeeActivity.this);
                    adapter.setData(tasks);
                }
            });
        }
        mEmployeeCompletedTasks.setAdapter(adapter);
    }


    public void pickDate(View view) {
        AppUtils.showDatePicker(this, view, true);
    }


    private void setUpNameET() {
        mEmployeeName.addTextChangedListener(new TextWatcher() {
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
        mEmployeeName.setText("");
        mEmployeeSalary.setText("");
        mEmployeeHireDate.setText("");
        mCollapsingToolbar.setTitle(getString(R.string.add_new_employee));
        mEmployeeDepartment.setSelection(0);
        mEmployeeRating.setVisibility(View.GONE);
        mEmployeeCompletedTasks.setVisibility(View.GONE);
        findViewById(R.id.textView3).setVisibility(View.GONE);
        findViewById(R.id.textView4).setVisibility(View.GONE);

    }

    private void populateUi(EmployeeWithExtras employeeWithExtras) {
        if (employeeWithExtras == null)
            return;

        clickedEmployeeDepId = employeeWithExtras.employeeEntry.getDepartmentId();

        mEmployeeName.setText(employeeWithExtras.employeeEntry.getEmployeeName());
        mEmployeeSalary.setText(String.valueOf(employeeWithExtras.employeeEntry.getEmployeeSalary()));
        mEmployeeHireDate.setText(AppUtils.getFriendlyDate(employeeWithExtras.employeeEntry.getEmployeeHireDate()));
        mEmployeeHireDate.setTag(employeeWithExtras.employeeEntry.getEmployeeHireDate());
        mCollapsingToolbar.setTitle(employeeWithExtras.employeeEntry.getEmployeeName());
        if (departmentsLoaded)
            mEmployeeDepartment.setSelection(mArrayAdapter.getPositionForItemId(employeeWithExtras.employeeEntry.getDepartmentId()));
        mEmployeeRating.setRating(employeeWithExtras.employeeRating);

        Glide.with(this).load(AppUtils.getRandomEmployeeImage()).apply(RequestOptions.centerCropTransform()).into(mEmployeeImage);
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


    private void saveEmployee() {
        if (isDataValid()) {
            String employeeName = mEmployeeName.getText().toString();
            int employeeSalary = Integer.parseInt(mEmployeeSalary.getText().toString());
            Date employeeHireDate = (Date) mEmployeeHireDate.getTag();
            int departmentId = (int) mEmployeeDepartment.getSelectedView().getTag();

            final EmployeeEntry newEmployee = new EmployeeEntry(departmentId, employeeName, employeeSalary, employeeHireDate);

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

        if (true)
            return true;

        if (TextUtils.isEmpty(mEmployeeName.getText())) {
            showError("name");
            return false;
        }
        if (!TextUtils.isDigitsOnly(mEmployeeSalary.getText())) {
            showError("salary");
            return false;
        }
        if (mEmployeeHireDate.getTag() == null) {
            showError("hireDate");
            return false;
        }

        return true;


    }

    private void showError(String error) {
        switch (error) {
            case "name":
                break;
            case "salary":
                break;
            case "hireDate":
                break;
        }
    }

    @Override
    public void onTaskClick(int taskRowID, int taskPosition) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        startActivity(intent);
    }
}
