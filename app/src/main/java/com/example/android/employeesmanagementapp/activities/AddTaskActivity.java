package com.example.android.employeesmanagementapp.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.ChooseEmployeesAdapter;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.adapters.HorizontalEmployeeAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.factories.TaskIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewTaskViewModel;
import com.example.android.employeesmanagementapp.fragments.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddTaskActivity extends AppCompatActivity implements EmployeesAdapter.EmployeeItemClickListener, PopupMenu.OnMenuItemClickListener {
    public static final String TASK_VIEW_ONLY = "task_view_only";


    public static final String TASK_ID_KEY = "task_id";
    public static final String TASK_ENABLE_VIEWS_KEY = "enable_views";
    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final boolean DEFAULT_TASK_VIEW_ONLY = false;
    private static final int DEFAULT_TASK_ID = -1;
    private static int tempTaskDepId = -1;
    private int mTaskId;
    private boolean addNewTask = false;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private EmployeesAdapter mEmplyeesAdapter;

    private EditText mTaskTitle;
    private EditText mTaskDescription;

    public HorizontalEmployeeAdapter getHorizontalEmployeeAdapter() {
        return mHorizontalEmployeeAdapter;
    }

    private TextView mTaskStartDate;
    private TextView mTaskDueDate;
    private Spinner mTaskDepartment;
    private Toolbar mToolbar;
    private ImageButton addEmployeesToTaskButton;

    private HorizontalEmployeeAdapter mHorizontalEmployeeAdapter;
    private ChooseEmployeesAdapter mChooseEmployeesAdapter;
    private AppDatabase mDb;
    private AddNewTaskViewModel mViewModel;

    boolean departmentsLoaded = false;
    private int clickedTaskDepId = -1;
    private boolean mEnableViews = true;

    private DepartmentsArrayAdapter mDepartmentsArrayAdapter;
    private RecyclerView mRecyclerView;

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
            mEnableViews = intent.getBooleanExtra(TASK_ENABLE_VIEWS_KEY, DEFAULT_TASK_VIEW_ONLY);
        }
        if (mTaskId == DEFAULT_TASK_ID)
            addNewTask = true;

        mViewModel = ViewModelProviders.of(this, new TaskIdFact(mDb, mTaskId)).get(AddNewTaskViewModel.class);


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
        mTaskDepartment = findViewById(R.id.task_department);
        addEmployeesToTaskButton = findViewById(R.id.add_employees_to_task_button);
        addEmployeesToTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseTaskEmployeesDialog();
            }
        });


        mDepartmentsArrayAdapter = new DepartmentsArrayAdapter(this);
        LiveData<List<DepartmentEntry>> departments = mViewModel.getAllDepartments();


        departments.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                mDepartmentsArrayAdapter.setData(departmentEntries);
                departmentsLoaded = true;
                if (clickedTaskDepId != -1) {
                    mTaskDepartment.setSelection(mDepartmentsArrayAdapter.getPositionForItemId(clickedTaskDepId));
                }
            }
        });


        mTaskDepartment.setAdapter(mDepartmentsArrayAdapter);


        setUpToolBar();

        if (mTaskId == DEFAULT_TASK_ID) {
            clearViews();
        } else {
            final LiveData<TaskEntry> task = mViewModel.getTask();
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

        mTaskDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (tempTaskDepId != (int) view.getTag()) {
                    System.out.println("clear horizontal adapter");
                    setUpEmployeesRV();
                    // or -->
                    // mHorizontalEmployeeAdapter.clearAddedEmployees();
                    AddTaskActivity.tempTaskDepId = (int) view.getTag();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setUpEmployeesRV();

        mTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(view);
            }
        });
    }

    private void showChooseTaskEmployeesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose employees");

        RecyclerView chooseEmployeesRV = new RecyclerView(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chooseEmployeesRV.setLayoutManager(linearLayoutManager);
        chooseEmployeesRV.setHasFixedSize(true);

        int depId;
        if (mTaskId == DEFAULT_TASK_ID) {
            depId = (int) mTaskDepartment.getSelectedView().getTag();
        } else {
            depId = mViewModel.getTask().getValue().getDepartmentID();
        }

        mChooseEmployeesAdapter = ChooseEmployeesAdapter.getInstance(mViewModel, depId, mTaskId, this);
        chooseEmployeesRV.setAdapter(mChooseEmployeesAdapter);

        builder.setView(chooseEmployeesRV);

        builder.setPositiveButton(getString(R.string.choose_department_dialog_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mHorizontalEmployeeAdapter.mergeToAddedEmployees(mChooseEmployeesAdapter.getChosenEmployees());
                mChooseEmployeesAdapter.removeAndClearChosenEmployees();
            }
        });
        builder.setNegativeButton(getString(R.string.choose_department_dialog_cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setUpEmployeesRV() {
        mRecyclerView = findViewById(R.id.task_employees_rv);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(AddTaskActivity.this, view, Gravity.TOP);
                popupMenu.inflate(R.menu.menu_task_employee_options);
                popupMenu.setOnMenuItemClickListener(AddTaskActivity.this);
                popupMenu.show();
                return true;

            }
        };

        mHorizontalEmployeeAdapter = new HorizontalEmployeeAdapter(this, longClickListener);
        if (mTaskId == DEFAULT_TASK_ID) {
            mHorizontalEmployeeAdapter.setData(new ArrayList<EmployeeEntry>());
        } else {
            final LiveData<List<EmployeeEntry>> taskEmployees = mViewModel.getTaskEmployees();
            taskEmployees.observe(this, new Observer<List<EmployeeEntry>>() {
                @Override
                public void onChanged(List<EmployeeEntry> employeeEntries) {
                    taskEmployees.removeObserver(this);
                    mHorizontalEmployeeAdapter.setData(employeeEntries);
                }
            });
        }
        mRecyclerView.setAdapter(mHorizontalEmployeeAdapter);
    }

    private void clearViews() {
        mTaskTitle.setText("");
        mTaskDescription.setText("");
        mTaskStartDate.setText("");
        mTaskDueDate.setText("");
        mTaskDepartment.setSelection(0);

    }

    private void populateUI(TaskEntry taskEntry) {
        if (taskEntry == null)
            return;

        clickedTaskDepId = taskEntry.getDepartmentID();
        mTaskTitle.setText(taskEntry.getTaskTitle());
        mTaskDescription.setText(taskEntry.getTaskDescription());
        mTaskStartDate.setText(taskEntry.getTaskStartDate().toString());
        mTaskDueDate.setText(taskEntry.getTaskDueDate().toString());
        if (departmentsLoaded)
            mTaskDepartment.setSelection(mDepartmentsArrayAdapter.getPositionForItemId(taskEntry.getDepartmentID()));

        mTaskDepartment.setEnabled(false);

        enableViews();

    }

    private void enableViews() {
        mTaskTitle.setEnabled(mEnableViews);
        mTaskDescription.setEnabled(mEnableViews);
        mTaskStartDate.setEnabled(mEnableViews);
        mTaskDueDate.setEnabled(mEnableViews);
        addEmployeesToTaskButton.setEnabled(mEnableViews);
    }


    private void setUpToolBar() {
        if (mTaskId == DEFAULT_TASK_ID) {
            getSupportActionBar().setTitle(getString(R.string.add_new_task));
        } else {
            getSupportActionBar().setTitle(getString(R.string.edit_task));
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
//        Intent intent = new Intent(this, NotificationService.class);
//        // send the due date and the id of the task within the intent
//        Bundle bundle = new Bundle();
//        bundle.putInt("task id",mTaskId);
//        try {
//            bundle.putLong("task due date",FORMAT.parse(mTaskDueDate.toString()).getTime() - new Date().getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        intent.putExtras(bundle);
//        startService(intent);
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
                mChooseEmployeesAdapter.mChosenEmployeesAdapterExecution();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveTask() {
        if (valideData()) {
            int departmentId = (int) mTaskDepartment.getSelectedView().getTag();
            String taskTitle = mTaskTitle.getText().toString();
            String taskDescription = mTaskDescription.getText().toString();
            //todo:change string date to java object date
            Date taskStartDate = new Date();
            Date taskDueDate = new Date();


//            try {
//                taskStartDate = FORMAT.parse(mTaskStartDate.toString());
//                taskDueDate = FORMAT.parse(mTaskDueDate.toString());
//                Log.v("date", "start date " + taskStartDate);
//                Log.v("date", "due date " + taskDueDate);
//                System.out.println("**********************************************************");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


            final TaskEntry newTask = new TaskEntry(departmentId, taskTitle, taskDescription, taskStartDate, taskDueDate);

            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mTaskId == DEFAULT_TASK_ID) {
                        mTaskId = (int) mDb.tasksDao().addTask(newTask);
                    } else {
                        newTask.setTaskId(mTaskId);
                        mDb.tasksDao().updateTask(newTask);
                    }
                }
            });
            final List<EmployeeEntry> addedEmployees = mHorizontalEmployeeAdapter.getAddedEmployees();

            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; addedEmployees != null && i < addedEmployees.size(); i++) {
                        final EmployeesTasksEntry newEmployeeTask = new EmployeesTasksEntry(addedEmployees.get(i).getEmployeeID(), mTaskId);
                        mDb.employeesTasksDao().addEmployeeTask(newEmployeeTask);
                    }
                }
            });

            if (!addNewTask) {
                final List<EmployeeEntry> removedEmployees = mHorizontalEmployeeAdapter.getRemovedEmployees();

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; removedEmployees != null && i < removedEmployees.size(); i++) {
                            final EmployeesTasksEntry deletedEmployeeTask = new EmployeesTasksEntry(removedEmployees.get(i).getEmployeeID(), mTaskId);
                            mDb.employeesTasksDao().deleteEmployeeTask(deletedEmployeeTask);
                        }
                    }
                });
            }

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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_employee:
                if (mChooseEmployeesAdapter == null)
                    mChooseEmployeesAdapter = ChooseEmployeesAdapter.getInstance(mViewModel, mViewModel.getTask().getValue().getDepartmentID(), mTaskId, this);
                mChooseEmployeesAdapter.mergeToEmployeeInDepNotTask(mHorizontalEmployeeAdapter.removeEmployee());
                return true;
            default:
                return false;
        }

    }

    @Override
    public void onEmployeeClick(int employeeRowID, int employeePosition) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_VIEW_ONLY, true);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        startActivity(intent);
    }
}