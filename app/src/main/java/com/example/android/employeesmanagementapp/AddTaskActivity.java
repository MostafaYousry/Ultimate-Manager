package com.example.android.employeesmanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity  {

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private static final int DEFAULT_TASK_ID = -1;
    public static final String TASK_ID_KEY = "task_id";

    private int mTaskId;
    private double mTaskIdDoubleMode;

    private EditText mTaskTitle;
    private EditText mTaskDescription;
    private TextView mTaskStartDate;
    private TextView mTaskDueDate;
    private RatingBar mTaskRatingBar;
    private Spinner mTaskDepartmentSpinner;
    private Spinner mTaskEmployeesSpinner;
    private Toolbar mToolbar;
    private Button showEmployeesBottomSheet, addEmployeesBottomSheet;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mTaskIdDoubleMode = intent.getDoubleExtra(TASK_ID_KEY, DEFAULT_TASK_ID);
            mTaskId = (int) mTaskIdDoubleMode;
        }


        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        //get views
        mTaskTitle = findViewById(R.id.task_title);
        mTaskDescription = findViewById(R.id.task_description);
        mTaskStartDate = findViewById(R.id.task_start_date);
        mTaskDueDate = findViewById(R.id.task_due_date);
        mTaskRatingBar = findViewById(R.id.task_rating);
        mTaskDepartmentSpinner = findViewById(R.id.task_department);
        mTaskEmployeesSpinner = findViewById(R.id.task_employees);


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

        setUpToolBar();
        setUpDepartmentSpinner();
        setUpEmployeesSpinner();
        setUpRatingBar();


        if (mTaskId == DEFAULT_TASK_ID){
            clearViews();
        }else {
            loadTaskData();
            Log.v(TAG,mTaskId + "        " + mTaskIdDoubleMode);
            if(mTaskId !=  mTaskIdDoubleMode){
                mTaskTitle.setEnabled(false);
                mTaskDescription.setEnabled(false);
                mTaskStartDate.setClickable(false);
                mTaskDueDate.setClickable(false);
                mTaskRatingBar.setEnabled(false);
            }
        }


        //allow scrolling of edit text content when it is inside a scroll view
        mTaskDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

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


    private void clearViews(){
        mTaskTitle.setText("");
        mTaskDescription.setText("");
        mTaskStartDate.setText("");
        mTaskDueDate.setText("");
        mTaskRatingBar.setRating(0);

    }

    private void loadTaskData(){
        //todo:fill with task data
    }


    private void setUpDepartmentSpinner(){
        //todo:replace with data from db
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departments_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mTaskDepartmentSpinner.setAdapter(adapter);

        if (mTaskId == DEFAULT_TASK_ID){
            mTaskDepartmentSpinner.setSelection(0);
        }else {
            //todo:select this tasks department
            //mTaskDepartmentSpinner.setSelection();
        }
    }

    private void setUpEmployeesSpinner(){
        //todo:replace with data from db
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.employees_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mTaskEmployeesSpinner.setAdapter(adapter);

        if (mTaskId == DEFAULT_TASK_ID){
            mTaskEmployeesSpinner.setSelection(0);
        }else {
            //todo:select this tasks employees
            //mTaskDepartmentSpinner.setSelection();
        }
    }

    private void setUpToolBar(){
        if (mTaskId == DEFAULT_TASK_ID){
            getSupportActionBar().setTitle(getString(R.string.add_new_task));
        }else {
            getSupportActionBar().setTitle(getString(R.string.update_task));
        }
    }

    private void setUpRatingBar(){
        mTaskRatingBar.setNumStars(5);
        mTaskRatingBar.setMax(5);
        mTaskRatingBar.setStepSize(0.5f);

        if (mTaskId == DEFAULT_TASK_ID){
            mTaskRatingBar.setRating(0);
            mTaskRatingBar.setIsIndicator(false);
        }else {
            mToolbar.setTitle(getString(R.string.update_task));
//            mTaskRatingBar.setIsIndicator(true);
            //todo:set task rating
        }
    }


    @Override
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



