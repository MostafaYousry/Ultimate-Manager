package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddDepartmentActivity extends AppCompatActivity implements RecyclerViewItemClickListener {
    public static final String DEPARTMENT_ID_KEY = "department_id";

    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private static final String TAG = AddDepartmentActivity.class.getSimpleName();

    private BottomSheetBehavior mSheetBehavior;

    private int mDepartmentId;

    private RecyclerView mRecyclerView;
    private EditText mDepartmentName;
    private Toolbar mToolbar;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_department);
        super.onCreate(savedInstanceState);


        mDb = AppDatabase.getInstance(this);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mDepartmentId = intent.getIntExtra(DEPARTMENT_ID_KEY, DEFAULT_DEPARTMENT_ID);
        }

        //find views
        mDepartmentName = findViewById(R.id.department_name);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        setUpToolBar();
      //  setUpEmployeesBS();


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


        // Lookup the recyclerview in activity layout
        mRecyclerView= (RecyclerView) findViewById(R.id.rvTasks);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        // Create adapter passing in the sample user data
        TasksAdapter mTadapter = new TasksAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        mRecyclerView.setAdapter(mTadapter);
        mTadapter.setData(AppUtils.getTasksFakeData());
        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // That's all!

    }


    private void populateUi(DepartmentEntry departmentEntry) {
        if (departmentEntry == null)
            return;
        mDepartmentName.setText(departmentEntry.getDepartmentName());


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

/*
    private void setUpEmployeesBS() {

        mSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_root));

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.show_dep_emp_rv);
        final EmployeesAdapter employeesAdapter = new EmployeesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(employeesAdapter);

        LiveData<List<EmployeeEntry>> employeesInDepartment = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class).getEmployees();
        employeesInDepartment.observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employeeEntries) {
                employeesAdapter.setData(employeeEntries);
            }
        });


        Button showDepEmpButton = findViewById(R.id.show_employees_button);
        showDepEmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }
*/


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
        if (valideData()) {
            String departmentName = mDepartmentName.getText().toString();


            final DepartmentEntry newDepartment = new DepartmentEntry(departmentName);

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

    private boolean valideData() {
        return true;
    }


    @Override
    public void onItemClick(int clickedItemRowID, int clickedItemPosition) {
        Log.d(TAG, "item in bottom sheet is clicked");

        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_VIEW_ONLY, true);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, clickedItemRowID);
        startActivity(intent);
    }
}


