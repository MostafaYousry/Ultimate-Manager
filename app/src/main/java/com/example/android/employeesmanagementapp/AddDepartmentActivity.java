package com.example.android.employeesmanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class AddDepartmentActivity extends AppCompatActivity {
    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private static final int DEFAULT_DEPARTMENT_ID = -1;
    public static final String DEPARTMENT_ID_KEY = "task_id";

    private int mDepartmentId;
    private EditText mDepartmentName , mRunningtask;
    private Spinner mDepartmentEmployees, mDepartmentCompletedTasks;
    private Toolbar mToolbar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mDepartmentId= intent.getIntExtra(DEPARTMENT_ID_KEY, DEFAULT_DEPARTMENT_ID);
        }

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.department_toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        mDepartmentName =  findViewById(R.id.department_name_edit_text);
        mDepartmentEmployees = findViewById(R.id.department_employees_spinner);
        mRunningtask = findViewById(R.id.department_running_task_edit_text);
        mDepartmentCompletedTasks = findViewById(R.id.department_completed_tasks_spinner);

        setUpToolBar();
        setUpCompletedTasksSpinner();
        setUpEmployeesSpinner();

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID){
            clearViews();
        }else {
            loadTaskData();
        }

    }

    private void loadTaskData() {
        //todo:fill with task data
    }

    private void clearViews() {
        mDepartmentName.setText("");
        mRunningtask.setText("");
    }

    private void setUpEmployeesSpinner() {
        //todo:replace with data from db
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.employees_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mDepartmentEmployees.setAdapter(adapter);

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID){
            mDepartmentEmployees.setSelection(0);
        }else {
            //todo:select this tasks employees
            //mTaskDepartmentSpinner.setSelection();
        }
    }

    private void setUpCompletedTasksSpinner() {
        //todo:replace with data from db
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mDepartmentCompletedTasks.setAdapter(adapter);

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID){
            mDepartmentCompletedTasks.setSelection(0);
        }else {
            //todo:select this tasks employees
            //mTaskDepartmentSpinner.setSelection();
        }

    }

    private void setUpToolBar() {
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID){
            getSupportActionBar().setTitle(getString(R.string.add_new_department));
        }else {
            getSupportActionBar().setTitle(getString(R.string.update_department));
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveTask();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveTask(){
        //todo:insert/update new data into db
        finish();
    }
}
