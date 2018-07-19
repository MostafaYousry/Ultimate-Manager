package com.example.android.employeesmanagementapp;

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
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class AddEmployeeActivity extends AppCompatActivity {

    public static final String EMPLOYEE_ID_KEY = "employee_id";
    private static final String TAG = AddEmployeeActivity.class.getSimpleName();
    private static final int DEFAULT_EMPLOYEE_ID = -1;
    private int mEmployeeId;

    private EditText mEmployeeName;
    private EditText mEmployeeSalary;
    private TextView mEmployeeHireDate;
    private TextView mEmployeeDepartment;
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


        setUpToolBar();
        setUpNameET();


        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            clearViews();
        } else {
            loadEmployeeData();
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
        mEmployeeDepartment.setText("");

    }

    private void loadEmployeeData() {
        //todo:fill with task data
    }


    private void setUpToolBar() {
        if (mEmployeeId == DEFAULT_EMPLOYEE_ID) {
            mCollapsingToolbar.setTitle(getString(R.string.add_new_employee));
        } else {
            //todo:set toolbar title with employee name
        }
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
        //todo:insert/update new data into db
//        AppExecutor.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        finish();
    }
}
