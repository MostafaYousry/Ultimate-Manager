package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.adapters.HorizontalEmployeeAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddDepartmentActivity extends AppCompatActivity implements RecyclerViewItemClickListener {
    private static final String TAG = AddDepartmentActivity.class.getSimpleName();

    public static final String DEPARTMENT_ID_KEY = "department_id";
    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private int mDepartmentId;

    private RecyclerView mDepCompletedTasksRV;
    private EditText mDepartmentName;
    private Toolbar mToolbar;
    private AppDatabase mDb;
    private RecyclerView mDepEmployeesRV;



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

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        setUpToolBar();
        setUpEmployeesRV();

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


        // Lookup the recycler view in activity layout
        mDepCompletedTasksRV = findViewById(R.id.department_tasks_rv);
        mDepCompletedTasksRV.setNestedScrollingEnabled(false);
        mDepCompletedTasksRV.setHasFixedSize(true);
        // Create adapter passing in the sample user data
        TasksAdapter mTadapter = new TasksAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        mDepCompletedTasksRV.setAdapter(mTadapter);
        mTadapter.setData(AppUtils.getTasksFakeData());
        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mDepCompletedTasksRV.setLayoutManager(linearLayoutManager);
        // That's all!

    }


    private void setUpEmployeesRV() {
        mDepEmployeesRV = findViewById(R.id.department_employees_rv);

        mDepEmployeesRV.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mDepEmployeesRV.setLayoutManager(layoutManager);

        HorizontalEmployeeAdapter mAdapter = new HorizontalEmployeeAdapter(this, false);
        mAdapter.setData(AppUtils.getEmployeesFakeData());
        mDepEmployeesRV.setAdapter(mAdapter);

    }

    protected void onStop() {
        super.onStop();
//
//        Intent intent = new Intent(this, NotificationService.class);
//        // send the due date and the id of the task within the intent
//        //intent.putExtra("task due date", taskDueDate.getTime() - taskStartDAte.getTime())'
//        //intent.putExtra("task id",mTaskId);
//
//        //just for experiment until tasks are done
//        Bundle bundle = new Bundle();
//        System.out.println(mDepartmentId);
//        bundle.putInt("task id", 38);
//        bundle.putLong("task due date", 30);
//        intent.putExtras(bundle);
//        startService(intent);
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
