package com.example.android.employeesmanagementapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.example.android.employeesmanagementapp.fragments.DatePickerFragment;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class AddTaskActivity extends AppCompatActivity {

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

    private DepartmentsArrayAdapter mDepartmentsArrayAdapter;
    private int mSelectedDepartmentID;


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

        if (mTaskId == DEFAULT_TASK_ID) {
            clearViews();
        } else {
            final LiveData<TaskEntry> task = mDb.tasksDao().loadTaskById(mTaskId);
            task.observe(this, new Observer<TaskEntry>() {
                @Override
                public void onChanged(@Nullable TaskEntry taskEntry) {
                    task.removeObserver(this);
                    populateUI(taskEntry);
                }
            });
        }


        //allow scrolling of edit text content when it is inside a scroll view
        mTaskDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

    }


    private void clearViews() {
        mTaskTitle.setText("");
        mTaskDescription.setText("");
        mTaskStartDate.setText("");
        mTaskDueDate.setText("");
        mTaskDepartment.setText(mDepartmentsArrayAdapter.getItem(0));
        mTaskRatingBar.setRating(0);
    }

    private void populateUI(TaskEntry taskEntry) {
        if (taskEntry == null)
            return;
        System.out.println(mTaskId + "  " + taskEntry.getTaskTitle() + " " + taskEntry.getTaskDescription());
        mTaskTitle.setText(taskEntry.getTaskTitle());
        mTaskDescription.setText(taskEntry.getTaskDescription());
        mTaskStartDate.setText(taskEntry.getTaskDueDate().toString());
        mTaskDueDate.setText(taskEntry.getTaskDueDate().toString());
        mTaskDepartment.setText(mDepartmentsArrayAdapter.getItem(mDepartmentsArrayAdapter.getPositionForItemId(taskEntry.getDepartmentID())));
        mSelectedDepartmentID = taskEntry.getDepartmentID();
        mTaskRatingBar.setRating(taskEntry.getTaskRating());


    }


    private void setUpDepartmentDropDown() {

        mDepartmentsArrayAdapter = new DepartmentsArrayAdapter(AddTaskActivity.this);


        LiveData<List<DepartmentEntry>> departments = ViewModelProviders.of(this).get(MainViewModel.class).getAllDepartmentsList();
        departments.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                mDepartmentsArrayAdapter.setDepartmentEntryList(departmentEntries);
            }
        });


        mTaskDepartment.setAdapter(mDepartmentsArrayAdapter);

        mTaskDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedDepartmentID = (int) view.getTag();
            }
        });


        mTaskDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTaskDepartment.showDropDown();
            }
        });

    }


    private void setUpToolBar() {
        if (mTaskId == DEFAULT_TASK_ID) {
            getSupportActionBar().setTitle(getString(R.string.add_new_task));
        } else {
            getSupportActionBar().setTitle(getString(R.string.edit_task));
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
        if (valideData()) {
            int departmentId = mSelectedDepartmentID;
            String taskTitle = mTaskTitle.getText().toString();
            String taskDescription = mTaskDescription.getText().toString();
            //todo:change string date to java object date
            Date taskStartDate = new Date();
            Date taskDueDate = new Date();


            final TaskEntry newTask = new TaskEntry(departmentId, taskTitle, taskDescription, taskStartDate, taskDueDate);

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mTaskId == DEFAULT_TASK_ID) {
                            mDb.tasksDao().addTask(newTask);
                            System.out.println("new task");
                        }
                        else {
                            newTask.setTaskId(mTaskId);
                            mDb.tasksDao().updateTask(newTask);
                            System.out.println("update task");
                        }
                    }
                });
        }
        finish();
    }

    private boolean valideData() {
        return true;
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

}