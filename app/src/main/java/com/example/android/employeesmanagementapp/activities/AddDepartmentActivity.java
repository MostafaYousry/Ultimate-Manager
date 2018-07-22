package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.fragments.EmployeeBottomSheetFragment;
import com.example.android.employeesmanagementapp.fragments.TaskBottomSheetFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


public class AddDepartmentActivity extends AppCompatActivity {
    public static final String DEPARTMENT_ID_KEY = "department_id";

    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private static final String TAG = AddDepartmentActivity.class.getSimpleName();

    private int mDepartmentId;
    private EditText mDepartmentName;
    private Toolbar mToolbar;
    private Button mShowEmployeesBottomSheet, mAddEmployeesBottomSheet, mShowCompletedTasksBottomSheet;

    private AppDatabase mDb;

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
        mShowEmployeesBottomSheet = findViewById(R.id.show_employees_bottom_sheet);
        mAddEmployeesBottomSheet = findViewById(R.id.add_employee_bottom_sheet);
        mShowCompletedTasksBottomSheet = findViewById(R.id.completed_tasks_bottom_sheet);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        setUpToolBar();
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
        } else {
            final LiveData<DepartmentEntry> department = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class).getDepartment();
            department.observe(this, new Observer<DepartmentEntry>() {
                @Override
                public void onChanged(DepartmentEntry departmentEntry) {
                    department.removeObservers(AddDepartmentActivity.this);
                    populateUi(departmentEntry);
                }
            });
        }


        setUpBottomSheetButtons();


    }


    private void populateUi(DepartmentEntry departmentEntry) {
        if (departmentEntry == null)
            return;
        mDepartmentName.setText(departmentEntry.getDepartmentName());


    }


    //bottom sheets to show department's employees and completed tasks
    private void setUpBottomSheetButtons() {

        mAddEmployeesBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeBottomSheetFragment employeesFragment = new EmployeeBottomSheetFragment();
                employeesFragment.show(getSupportFragmentManager(), employeesFragment.getTag());
            }
        });


        mShowCompletedTasksBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskBottomSheetFragment taskFragment = new TaskBottomSheetFragment();
                taskFragment.show(getSupportFragmentManager(), taskFragment.getTag());
            }
        });
    }

    private void clearViews() {
        mDepartmentName.setText("");
    }

    private void setUpToolBar() {
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            getSupportActionBar().setTitle("Add new department");
        } else {
            getSupportActionBar().setTitle("Update department");
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
                saveDepartment();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveDepartment() {
        //todo:insert/update new data into db
        if (valideData()) {
            final String departmentName = mDepartmentName.getText().toString();

            final DepartmentEntry newDepartment = new DepartmentEntry(departmentName);

            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.departmentsDao().addDepartment(newDepartment);
                }
            });
        }
        finish();
    }

    private boolean valideData() {
        return true;
    }
}
