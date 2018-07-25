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
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.factories.EmpIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewEmployeeViewModel;
import com.example.android.employeesmanagementapp.fragments.DatePickerFragment;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class AddEmployeeActivity extends AppCompatActivity {

    public static final String EMPLOYEE_ID_KEY = "employee_id";
    public static final String EMPLOYEE_VIEW_ONLY = "employee_view_only";
    private static final String TAG = AddEmployeeActivity.class.getSimpleName();
    private static final int DEFAULT_EMPLOYEE_ID = -1;
    private static final boolean DEFAULT_EMPLOYEE_VIEW_ONLY = false;

    private int mEmployeeId;
    private boolean mIsViewOnly;

    private EditText mEmployeeName;
    private EditText mEmployeeSalary;
    private TextView mEmployeeHireDate;
    private Spinner mEmployeeDepartment;
    private ImageView mEmployeeImage;

    private DepartmentsArrayAdapter mArrayAdapter;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;


    private AppDatabase mDb;


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
            mIsViewOnly = intent.getBooleanExtra(EMPLOYEE_VIEW_ONLY, DEFAULT_EMPLOYEE_VIEW_ONLY);
        }

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

        mArrayAdapter = new DepartmentsArrayAdapter(this, this);
        mEmployeeDepartment.setAdapter(mArrayAdapter);


        setUpNameET();


        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            clearViews();
        } else {
            final LiveData<EmployeeEntry> employee = ViewModelProviders.of(this, new EmpIdFact(mDb, mEmployeeId)).get(AddNewEmployeeViewModel.class).getEmployee();
            employee.observe(this, new Observer<EmployeeEntry>() {
                @Override
                public void onChanged(EmployeeEntry employeeEntry) {
                    employee.removeObservers(AddEmployeeActivity.this);
                    populateUi(employeeEntry);
                }
            });
        }

        if (mIsViewOnly) {
            deactivateViews();
        }

    }


    public void pickDate(View view) {
        //create a bundle containing id of clicked text view (startDateTextView or dueDateTextView)
        Bundle bundle = new Bundle();
        bundle.putInt("date_view_id", view.getId());

        //instantiate a DatePickerFragment to show date picker dialog
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);

        //show th dialog
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
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

    }

    private void populateUi(EmployeeEntry employeeEntry) {
        if (employeeEntry == null)
            return;

        mEmployeeName.setText(employeeEntry.getEmployeeName());
        mEmployeeSalary.setText(String.valueOf(employeeEntry.getEmployeeSalary()));
        mEmployeeHireDate.setText(employeeEntry.getEmployeeHireDate().toString());
        mCollapsingToolbar.setTitle(employeeEntry.getEmployeeName());

        final LiveData<DepartmentEntry> employeeDep = ViewModelProviders.of(this, new DepIdFact(mDb, employeeEntry.getDepartmentId())).get(AddNewDepViewModel.class).getDepartment();
        employeeDep.observe(this, new Observer<DepartmentEntry>() {
            @Override
            public void onChanged(DepartmentEntry departmentEntry) {
                employeeDep.removeObserver(this);
                mEmployeeDepartment.setSelection(mArrayAdapter.getPositionForItemId(departmentEntry));
            }
        });


        Glide.with(this).load(AppUtils.getRandomEmployeeImage()).apply(RequestOptions.centerCropTransform()).into(mEmployeeImage);
    }


    private void deactivateViews() {
        mEmployeeName.setEnabled(false);
        mEmployeeSalary.setEnabled(false);
        mEmployeeHireDate.setEnabled(false);
        mEmployeeDepartment.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
        if (valideData()) {
            final int departmentId = (int) mEmployeeDepartment.getSelectedView().getTag();
            Log.d(TAG, "departmentId = " + departmentId);

            final String employeeName = mEmployeeName.getText().toString();
            final int employeeSalary = Integer.parseInt(mEmployeeSalary.getText().toString());
            //todo:convert string date to object Date
            final Date employeeHireDate = new Date();

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

    private boolean valideData() {
        return true;
    }
}
