package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.adapters.HorizontalEmployeeAdapter;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddDepartmentActivity extends AppCompatActivity implements EmployeesAdapter.EmployeeItemClickListener, TasksAdapter.TasksItemClickListener {
    public static final String DEPARTMENT_ID_KEY = "department_id";
    private static final String TAG = AddDepartmentActivity.class.getSimpleName();
    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private int mDepartmentId;

    private RecyclerView mDepCompletedTasksRV;
    private RecyclerView mDepEmployeesRV;
    private EditText mDepartmentName;
    private Toolbar mToolbar;

    private AppDatabase mDb;
    private AddNewDepViewModel mViewModel;


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


        mViewModel = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class);

        setUpEmployeesRV();
        setUpTasksRV();

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
        } else {
            final LiveData<DepartmentEntry> department = mViewModel.getDepartment();
            department.observe(this, new Observer<DepartmentEntry>() {
                @Override
                public void onChanged(DepartmentEntry departmentEntry) {
                    department.removeObservers(AddDepartmentActivity.this);
                    populateUi(departmentEntry);
                }
            });

        }

    }

    private void setUpTasksRV() {

        mDepCompletedTasksRV = findViewById(R.id.department_tasks_rv);
        mDepCompletedTasksRV.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDepCompletedTasksRV.setLayoutManager(linearLayoutManager);
        final TasksAdapter adapter = new TasksAdapter(this);

        if(mDepartmentId == DEFAULT_DEPARTMENT_ID)
            adapter.setData(new ArrayList<TaskEntry>());
        else {
            final LiveData<List<TaskEntry>> depCompletedTasks = mViewModel.getCompletedTasks();
            depCompletedTasks.observe(this, new Observer<List<TaskEntry>>() {
                @Override
                public void onChanged(List<TaskEntry> tasks) {
                    depCompletedTasks.removeObservers(AddDepartmentActivity.this);
                    adapter.setData(tasks);
                }
            });
        }
        mDepCompletedTasksRV.setAdapter(adapter);
    }


    private void setUpEmployeesRV() {

        mDepEmployeesRV = findViewById(R.id.task_employees_rv);
        mDepEmployeesRV.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mDepEmployeesRV.setLayoutManager(layoutManager);

        final HorizontalEmployeeAdapter adapter = new HorizontalEmployeeAdapter(this);

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID)
            adapter.setData(new ArrayList<EmployeeEntry>());
        else {
            final LiveData<List<EmployeeEntry>> depEmployees = mViewModel.getEmployees();
            depEmployees.observe(this, new Observer<List<EmployeeEntry>>() {
                @Override
                public void onChanged(List<EmployeeEntry> employees) {
                    depEmployees.removeObservers(AddDepartmentActivity.this);
                    adapter.setData(employees);
                }
            });
        }
        mDepEmployeesRV.setAdapter(adapter);

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
        getSupportActionBar().setTitle(departmentEntry.getDepartmentName());
        mDepCompletedTasksRV.setVisibility(View.VISIBLE);
        mDepEmployeesRV.setVisibility(View.VISIBLE);
        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        findViewById(R.id.department_employees_text_view).setVisibility(View.VISIBLE);


    }

    private void clearViews() {
        getSupportActionBar().setTitle("Add new department");
        mDepartmentName.setText("");
        mDepCompletedTasksRV.setVisibility(View.GONE);
        mDepEmployeesRV.setVisibility(View.GONE);
        findViewById(R.id.textView2).setVisibility(View.GONE);
        findViewById(R.id.department_employees_text_view).setVisibility(View.GONE);
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


            final DepartmentEntry newDepartment = new DepartmentEntry(departmentName,false);

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
    public void onEmployeeClick(int employeeRowID, int employeePosition) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_VIEW_ONLY, true);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        startActivity(intent);
    }

    @Override
    public void onTaskClick(int taskRowID, int taskPosition) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_VIEW_ONLY, true);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        startActivity(intent);
    }
}
