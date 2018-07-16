package com.example.android.employeesmanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddDepartmentActivity extends AppCompatActivity {
    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private static final int DEFAULT_DEPARTMENT_ID = -1;
    public static final String DEPARTMENT_ID_KEY = "task_id";

    private int mDepartmentId;
    private EditText mDepartmentName, mRunningtask;
    private Toolbar mToolbar;
    private Button showEmployeesBottomSheet, addEmployeesBottomSheet, showCompletedTasksBottomSheet;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mDepartmentId = intent.getIntExtra(DEPARTMENT_ID_KEY, DEFAULT_DEPARTMENT_ID);
        }

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.department_toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setUpToolBar();
        setBottomSheetButtons();

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
        } else {
            loadTaskData();
        }

    }

    //make the bottom sheet shows its content for employees and completed tasks
    private void setBottomSheetButtons() {

        mDepartmentName = findViewById(R.id.department_name_edit_text);
        mRunningtask = findViewById(R.id.department_running_task_edit_text);

        showEmployeesBottomSheet = findViewById(R.id.show_employees_bottom_sheet);
        showEmployeesBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeBottomSheetFragment employeesFragment = new EmployeeBottomSheetFragment();
                employeesFragment.show(getSupportFragmentManager(), employeesFragment.getTag());
            }
        });


        addEmployeesBottomSheet = findViewById(R.id.add_employee_bottom_sheet);
        addEmployeesBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeBottomSheetFragment employeesFragment = new EmployeeBottomSheetFragment();
                employeesFragment.show(getSupportFragmentManager(), employeesFragment.getTag());
                employeesFragment.getEmployeesAdapter().setCheckBoxVisibility(1);
            }
        });


        showCompletedTasksBottomSheet = findViewById(R.id.completed_tasks_bottom_sheet);
        showCompletedTasksBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskBottomSheetFragment taskFragment = new TaskBottomSheetFragment();
                taskFragment.show(getSupportFragmentManager(), taskFragment.getTag());
            }
        });
    }

    private void loadTaskData() {
        //todo:fill with task data
    }

    private void clearViews() {
        mDepartmentName.setText("");
        mRunningtask.setText("");
    }

    private void setUpToolBar() {
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            getSupportActionBar().setTitle(getString(R.string.add_new_department));
        } else {
            getSupportActionBar().setTitle(getString(R.string.update_department));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveTask();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveTask() {
        //todo:insert/update new data into db
        finish();
    }
}
