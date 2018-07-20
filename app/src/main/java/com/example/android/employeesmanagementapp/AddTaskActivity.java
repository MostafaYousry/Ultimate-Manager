package com.example.android.employeesmanagementapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.AppDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class AddTaskActivity extends AppCompatActivity  {

    private static final String TAG = AddTaskActivity.class.getSimpleName();

    private static final int DEFAULT_TASK_ID = -1;
    public static final String TASK_ID_KEY = "task_id";

    private int mTaskId;

    private EditText mTaskTitle;
    private EditText mTaskDescription;
    private TextView mTaskStartDate;
    private TextView mTaskDueDate;
    private RatingBar mTaskRatingBar;
    private AutoCompleteTextView mTaskDepartment;
    private Toolbar mToolbar;

    private AppDatabase mDb;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mDb = AppDatabase.getInstance(this);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mTaskId = intent.getIntExtra(TASK_ID_KEY, DEFAULT_TASK_ID);
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
        mTaskDepartment = findViewById(R.id.task_department);



        setUpToolBar();
        setUpDepartmentDropDown();
        setUpRatingBar();


        if (mTaskId == DEFAULT_TASK_ID){
            clearViews();
        }else {
            loadTaskData();
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


    private void setUpDepartmentDropDown() {
        //todo:replace with data from db
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departments_array, android.R.layout.simple_dropdown_item_1line);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        mTaskDepartment.setAdapter(adapter);

        mTaskDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTaskDepartment.showDropDown();
            }
        });

        if (mTaskId == DEFAULT_TASK_ID){
            mTaskDepartment.setSelection(0);
        }else {
            //todo:select this tasks department
            //mTaskDepartmentSpinner.setSelection();
        }
    }


    private void setUpToolBar(){
        if (mTaskId == DEFAULT_TASK_ID){
            getSupportActionBar().setTitle(getString(R.string.add_new_task));
        }else {
            getSupportActionBar().setTitle(getString(R.string.edit_task));
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
//        AppExecutor.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                TaskEntry taskEntry = new TaskEntry();
//                mDb.tasksDao().addTask(taskEntry);
//
//            }
//        });
        finish();
    }
}