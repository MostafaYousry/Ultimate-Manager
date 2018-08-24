package com.example.android.employeesmanagementapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.MyAlarmReceiver;
import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.adapters.HorizontalAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.factories.TaskIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewTaskViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.example.android.employeesmanagementapp.utils.NotificationUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddTaskActivity extends BaseAddActivity implements HorizontalAdapter.HorizontalEmployeeItemClickListener {

    public static final String TASK_ID_KEY = "task_id";
    public static final String TASK_IS_COMPLETED_KEY = "task_is_completed";
    private static final boolean DEFAULT_TASK_IS_COMPLETED = false;
    private static final int DEFAULT_TASK_ID = -1;

    private TaskEntry mOldTaskEntry;

    private boolean mTaskIsCompleted;

    boolean departmentsLoaded = false;
    private int clickedTaskDepId = -1;

    private int mTaskId;

    private EditText mTaskTitle;
    private EditText mTaskDescription;
    private TextView mTaskStartDate;
    private TextView mTaskDueDate;
    private TextView mTaskStartTime;
    private TextView mTaskDueTime;
    private Spinner mTaskDepartment;
    private RecyclerView mTaskEmployeesRV;

    private HorizontalAdapter mHorizontalEmployeeAdapter;
    private DepartmentsArrayAdapter mDepartmentsArrayAdapter;

    private AppDatabase mDb;
    private AddNewTaskViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //create db instance
        mDb = AppDatabase.getInstance(this);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mTaskId = intent.getIntExtra(TASK_ID_KEY, DEFAULT_TASK_ID);

            //boolean to decide weather to lock views (as task is completed)
            //or to enable them
            mTaskIsCompleted = intent.getBooleanExtra(TASK_IS_COMPLETED_KEY, DEFAULT_TASK_IS_COMPLETED);
        }

        //find views
        mTaskTitle = findViewById(R.id.task_title);
        mTaskDescription = findViewById(R.id.task_description);
        mTaskStartDate = findViewById(R.id.task_start_date);
        mTaskDueDate = findViewById(R.id.task_due_date);
        mTaskStartTime = findViewById(R.id.task_start_date_time);
        mTaskDueTime = findViewById(R.id.task_due_date_time);
        mTaskDepartment = findViewById(R.id.task_department);
        mTaskEmployeesRV = findViewById(R.id.task_employees_rv);


        //instantiate AddNewTaskViewModel for this task id
        mViewModel = ViewModelProviders.of(this, new TaskIdFact(mDb, mTaskId)).get(AddNewTaskViewModel.class);


        //set toolbar as actionbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //create a new departments array adapter for employee department spinner
        mDepartmentsArrayAdapter = new DepartmentsArrayAdapter(this, AppUtils.dpToPx(this, 12), AppUtils.dpToPx(this, 8), AppUtils.dpToPx(this, 0), AppUtils.dpToPx(this, 8), R.style.detailActivitiesTextStyle);


        //if task is completed don't load all departments (unnecessary)
        if (!mTaskIsCompleted) {
            mViewModel.allDepartments.observe(this, departmentEntries -> {
                mDepartmentsArrayAdapter.setData(departmentEntries);
                departmentsLoaded = true;
                if (clickedTaskDepId != -1) {
                    mTaskDepartment.setSelection(mDepartmentsArrayAdapter.getPositionForItemId(clickedTaskDepId));
                }
            });
        } else {
            //display a snackbar to show that this task is completed
            //and cannot be edited
            Snackbar.make(findViewById(R.id.coordinator), getString(R.string.snackbar_completed_task_hint), Snackbar.LENGTH_LONG).show();
        }


        //set the adapter for departments spinner
        mTaskDepartment.setAdapter(mDepartmentsArrayAdapter);


        setUpToolBarTitle();

        //decide weather to load old task or create a new one
        if (mTaskId == DEFAULT_TASK_ID) {
            clearViews();
        } else {
            //load task entry from db
            final LiveData<TaskEntry> task = mViewModel.taskEntry;
            task.observe(this, new Observer<TaskEntry>() {
                @Override
                public void onChanged(@Nullable TaskEntry taskEntry) {
                    task.removeObserver(this);
                    populateUI(taskEntry);
                }
            });
        }


        setUpTaskEmployeesRV();

        //set click listeners to open date picker / time picker dialog
        mTaskDueDate.setOnClickListener(this::showDatePicker);
        mTaskStartDate.setOnClickListener(this::showDatePicker);
        mTaskStartTime.setOnClickListener(this::showTimePicker);
        mTaskDueTime.setOnClickListener(this::showTimePicker);

        //disable nested scrolling to avoid sluggish scrolling
        //because recycler view is inside nested scroll view
        ViewCompat.setNestedScrollingEnabled(mTaskEmployeesRV, false);

    }

    /**
     * set toolbar title accordingly
     * add new task/view completed task/edit running task
     */
    private void setUpToolBarTitle() {
        if (!mTaskIsCompleted)
            if (mTaskId == DEFAULT_TASK_ID) {
                getSupportActionBar().setTitle(getString(R.string.toolbar_add_task));
            } else {
                getSupportActionBar().setTitle(getString(R.string.toolbar_edit_task));
            }
        else
            getSupportActionBar().setTitle(getString(R.string.toolbar_view_task));
    }


    /**
     * shows dialog with all available employees in the selected department
     * that can be assigned to this running/new task
     * <p>
     * triggered when user click on employee with + icon
     *
     * @param view
     */
    public void showChooseTaskEmployeesDialog(View view) {

        int depId;
        List<EmployeeEntry> exceptThese;

        //get the department id for this task and the employees previously selected for this task
        //to get all employees in department except them
        if (mTaskId == DEFAULT_TASK_ID) {
            depId = (int) mTaskDepartment.getSelectedView().getTag();
            if (mHorizontalEmployeeAdapter.getItemCount() == 0)
                exceptThese = null;
            else
                exceptThese = mHorizontalEmployeeAdapter.getAddedEmployees();
        } else {
            depId = mViewModel.taskEntry.getValue().getDepartmentID();
            exceptThese = mHorizontalEmployeeAdapter.getAllEmployees();
        }

        //load all available employees in this department
        final LiveData<List<EmployeeEntry>> restOfEmployees = mViewModel.getRestOfEmployeesInDep(depId, exceptThese);
        restOfEmployees.observe(this, employeeEntries -> {
            restOfEmployees.removeObservers(AddTaskActivity.this);
            if (employeeEntries != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);

                //if no available employees exist show a dialog displaying so
                if (employeeEntries.isEmpty()) {
                    builder.setMessage(getString(R.string.dialog_message_no_available_employees));
                    builder.setPositiveButton(getString(R.string.dialog_positive_btn_ok), (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.show();

                } else {
                    builder.setTitle(getString(R.string.dialog_title_available_employees));
                    builder.setCancelable(false);

                    final List<EmployeeEntry> chosenOnes = new ArrayList<>();
                    final boolean[] isChecked = new boolean[employeeEntries.size()];

                    builder.setMultiChoiceItems(getEmployeeNames(employeeEntries), isChecked, (dialogInterface, i, checked) -> {
                        isChecked[i] = checked;

                        if (checked) {
                            chosenOnes.add(employeeEntries.get(i));
                        } else {
                            chosenOnes.remove(employeeEntries.get(i));
                        }
                    });

                    //if ok is chosen pass the added list to horizontal adapter to display it
                    builder.setPositiveButton(getString(R.string.dialog_positive_btn_ok), (dialog, which) -> {
                        if (!chosenOnes.isEmpty())
                            mHorizontalEmployeeAdapter.setAddedEmployees(chosenOnes);
                        dialog.dismiss();
                    });

                    builder.setNegativeButton(getString(R.string.dialog_negative_btn_cancel), (dialog, which) -> dialog.dismiss());

                    builder.show();
                }

            }

        });

    }

    /**
     * maps employee entries to list of employee names to be displayed in choose employees dialog
     *
     * @param employeeEntries
     * @return
     */
    private String[] getEmployeeNames(List<EmployeeEntry> employeeEntries) {

        String[] arr = new String[employeeEntries.size()];
        for (int i = 0; i < employeeEntries.size(); i++) {
            arr[i] = employeeEntries.get(i).getFullName();
        }

        return arr;
    }


    /**
     * shows list of all task's employees in
     * a recycler view that uses horizontal linear layout manager
     * to display list as a horizontally scrolling list
     */
    private void setUpTaskEmployeesRV() {

        mTaskEmployeesRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mTaskEmployeesRV.setLayoutManager(layoutManager);

        if (mTaskId != DEFAULT_TASK_ID) {
            mHorizontalEmployeeAdapter = new HorizontalAdapter(this, true, true, mTaskId, this);
            mViewModel.taskEmployees.observe(this, mHorizontalEmployeeAdapter::submitList);
        } else
            mHorizontalEmployeeAdapter = new HorizontalAdapter(this, false, true, mTaskId, this);

        mTaskEmployeesRV.setAdapter(mHorizontalEmployeeAdapter);
    }

    /**
     * restore state of views to adding a new task
     */
    private void clearViews() {

        mTaskDepartment.setSelection(0);

        //listener for task department changes to clear selected employees list
        //triggered only for new tasks
        //as we don't allow changes to task's department after it is created and assigned to employees
        mTaskDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mHorizontalEmployeeAdapter.clearAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    /**
     * displays all this task's data in relevant views
     *
     * @param taskEntry
     */
    private void populateUI(TaskEntry taskEntry) {
        if (taskEntry == null)
            return;

        //save an old version of the task entry to be diffed (using equals)
        //later on to decide weather to show discard changes dialog or not
        mOldTaskEntry = taskEntry;

        //the task department id is saved to select this department later in spinner
        //if department's are not yet loaded if they are loaded we select it now
        clickedTaskDepId = taskEntry.getDepartmentID();

        //if task is completed w load only it's department and not the whole list
        //and disable views(click and focus)
        if (mTaskIsCompleted) {
            mDb.departmentsDao().loadDepartmentById(clickedTaskDepId).observe(this, departmentEntry -> {
                if (departmentEntry != null) {
                    List<DepartmentEntry> list = new ArrayList<>(1);
                    list.add(departmentEntry);
                    mDepartmentsArrayAdapter.setData(list);
                    mTaskDepartment.setSelection(0);
                }

            });
            disableViewClicks();
        } else {

            if (departmentsLoaded)
                mTaskDepartment.setSelection(mDepartmentsArrayAdapter.getPositionForItemId(taskEntry.getDepartmentID()));

            //disable changing task's department after being created and assigned to employees
            mTaskDepartment.setEnabled(false);

            // TODO: 8/21/18
//        mTaskDepartment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
//                builder.setMessage("Can't change department of an already running taskEntry.If you want you can delete this taskEntry and create a new one.");
//                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.show();
//            }
//        });
        }

        mTaskTitle.setText(taskEntry.getTaskTitle());
        mTaskDescription.setText(taskEntry.getTaskDescription());

        mTaskStartDate.setText(AppUtils.getFriendlyDate(taskEntry.getTaskStartDate().getTime()));

        //set tag on this view to be used in date and time picker fragment and saved in save()
        mTaskStartDate.setTag(taskEntry.getTaskStartDate());

        mTaskDueDate.setText(AppUtils.getFriendlyDate(taskEntry.getTaskDueDate().getTime()));
        //set tag on this view to be used in date and time picker fragment and saved in save()
        mTaskDueDate.setTag(taskEntry.getTaskDueDate());

        mTaskStartTime.setText(AppUtils.getFriendlyTime(taskEntry.getTaskStartDate().getTime()));

        mTaskDueTime.setText(AppUtils.getFriendlyTime(taskEntry.getTaskDueDate().getTime()));


    }

    /**
     * disables clicking and focusing in the layout
     */
    private void disableViewClicks() {


        mTaskTitle.setEnabled(false);
        mTaskTitle.setFocusable(false);

        mTaskDescription.setEnabled(false);
        mTaskDescription.setFocusable(false);

        mTaskStartDate.setEnabled(false);
        mTaskStartDate.setFocusable(false);

        mTaskDueDate.setEnabled(false);
        mTaskDueDate.setFocusable(false);

        mTaskStartTime.setEnabled(false);
        mTaskStartTime.setFocusable(false);

        mTaskDueTime.setEnabled(false);
        mTaskDueTime.setFocusable(false);

        mTaskDepartment.setEnabled(false);
        mTaskDepartment.setFocusable(false);

        findViewById(R.id.add_employees_to_task_button).setClickable(false);

    }


    @Override
    protected void onStop() {
        super.onStop();
        // if there is a previous alarm manager is created stop it and create a new one with the new taskDueDate
        NotificationUtils.cancelAlarmManager(this, mTaskId);
        NotificationUtils.createNewAlarm(this, (Calendar) mTaskDueDate.getTag(), mTaskId);

    }


    /**
     * inflate toolbar menu for AddTaskActivity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        if (mTaskIsCompleted)
            menu.removeItem(R.id.action_save);
        return true;
    }


    /**
     * handles choosing menu item from toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * used to check if all the data of task is valid
     * and shows/hides error and helper messages accordingly
     *
     * @return : if data is valid true else false
     */
    @Override
    protected boolean isDataValid() {

        boolean valid = true;

        if (TextUtils.isEmpty(mTaskTitle.getText())) {
            updateErrorVisibility("title", true);
            valid = false;
        } else {
            updateErrorVisibility("title", false);
        }

        if (mTaskTitle.getText().length() > 40) {
            valid = false;
        }


        if (TextUtils.isEmpty(mTaskStartDate.getText())) {
            updateErrorVisibility("startDate", true);
            valid = false;
        } else {
            updateErrorVisibility("startDate", false);
        }

        if (TextUtils.isEmpty(mTaskDueDate.getText())) {
            updateErrorVisibility("dueDate", true);
            valid = false;
        } else {
            updateErrorVisibility("dueDate", false);
        }

        if (mHorizontalEmployeeAdapter.getItemCount() == 0) {
            updateErrorVisibility("employees", true);
            valid = false;
        }

        return valid;
    }

    /**
     * shows or hides error and helper messages for each field
     *
     * @param key  : field name
     * @param show : to show or hide the error
     */
    @Override
    protected void updateErrorVisibility(String key, boolean show) {
        if (show)
            switch (key) {
                case "title":
                    ((TextInputLayout) findViewById(R.id.task_title_TIL)).setError(getString(R.string.required_field));
                    break;
                case "startDate":
                    ((TextInputLayout) findViewById(R.id.task_start_date_TIL)).setError(getString(R.string.required_field));
                    break;
                case "dueDate":
                    ((TextInputLayout) findViewById(R.id.task_due_date_TIL)).setError(getString(R.string.required_field));
                    break;
                case "employees":
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
                    builder.setMessage("There must be at least one employee assigned to this task.");
                    builder.setPositiveButton("ok", (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.show();
            }
        else
            switch (key) {
                case "title":
                    ((TextInputLayout) findViewById(R.id.task_title_TIL)).setHelperText(getString(R.string.required_field));

                    break;
                case "startDate":
                    ((TextInputLayout) findViewById(R.id.task_start_date_TIL)).setHelperText(getString(R.string.required_field));
                    break;
                case "dueDate":
                    ((TextInputLayout) findViewById(R.id.task_due_date_TIL)).setHelperText(getString(R.string.required_field));
                    break;
            }
    }

    /**
     * saves (insert / update) the task entry
     */
    @Override
    protected void save() {
        if (isDataValid()) {
            final TaskEntry newTask = getTaskEntry();

            AppExecutor.getInstance().diskIO().execute(() -> {
                if (mTaskId == DEFAULT_TASK_ID) {
                    //returns newly inserted task record id
                    //to be used to assign employees to it in join table(employees tasks join table)
                    mTaskId = (int) mDb.tasksDao().addTask(newTask);
                } else {
                    mDb.tasksDao().updateTask(newTask);
                }
            });

            //in case if it is a new task insert the added employees
            //other cases are handled in horizontal adapter
            if (mTaskId == DEFAULT_TASK_ID) {
                final List<EmployeeEntry> addedEmployees = mHorizontalEmployeeAdapter.getAddedEmployees();
                if (!addedEmployees.isEmpty())
                    AppExecutor.getInstance().diskIO().execute(() -> {
                        for (int i = 0; i < addedEmployees.size(); i++) {
                            EmployeesTasksEntry newEmployeeTask = new EmployeesTasksEntry(addedEmployees.get(i).getEmployeeID(), mTaskId);
                            mDb.employeesTasksDao().addEmployeeTask(newEmployeeTask);
                        }
                    });
            }
            finish();
        }
    }


    /**
     * notifies the activity when a task employee is clicked
     * to open AddEmployeeActivity to edit/view this employee
     *
     * @param employeeRowID    : employee record id
     * @param employeePosition : adapter position
     * @param isFired          : weather employee is fired(deleted) or not
     */
    @Override
    public void onEmployeeClick(int employeeRowID, int employeePosition, boolean isFired) {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_IS_FIRED, isFired);
        startActivity(intent);
    }

    /**
     * creates a task entry with the data in the views
     */
    private TaskEntry getTaskEntry() {
        int departmentId = (int) mTaskDepartment.getSelectedView().getTag();
        String taskTitle = mTaskTitle.getText().toString();
        String taskDescription = mTaskDescription.getText().toString();
        Calendar taskDueDate = (Calendar) mTaskDueDate.getTag();
        Calendar taskStartDate = (Calendar) mTaskStartDate.getTag();

        if (mTaskId == DEFAULT_TASK_ID)
            return new TaskEntry(departmentId, taskTitle, taskDescription, taskStartDate, taskDueDate);
        else
            return new TaskEntry(mTaskId, departmentId, taskTitle, taskDescription, taskStartDate, taskDueDate, mOldTaskEntry.getTaskRating(), mOldTaskEntry.isTaskIsCompleted(), mOldTaskEntry.getTaskColorResource());
    }

    /**
     * checks if any data is changed without saving
     *
     * @return
     */
    @Override
    protected boolean fieldsChanged() {
        if (mTaskId == DEFAULT_TASK_ID) {
            if (!TextUtils.isEmpty(mTaskTitle.getText()))
                return true;

            if (!TextUtils.isEmpty(mTaskDescription.getText()))
                return true;

            if (!TextUtils.isEmpty(mTaskStartDate.getText()))
                return true;

            if (!TextUtils.isEmpty(mTaskDueDate.getText()))
                return true;

            if (!TextUtils.isEmpty(mTaskStartTime.getText()))
                return true;

            if (!TextUtils.isEmpty(mTaskDueTime.getText()))
                return true;

            if (mTaskDepartment.getSelectedItemPosition() != 0)
                return true;

            if (mHorizontalEmployeeAdapter.didChangeOccur())
                return true;
        } else
            return !mOldTaskEntry.equals(getTaskEntry()) || mHorizontalEmployeeAdapter.didChangeOccur();


        return false;
    }

    @Override
    protected void showDiscardChangesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_discard_changes));
        builder.setMessage(getString(R.string.dialog_message_discard));
        builder.setNegativeButton(getString(R.string.dialog_negative_btn_discard), (dialogInterface, i) -> {
            mHorizontalEmployeeAdapter.discardImmediateChanges();
            dialogInterface.dismiss();
            finish();
        });

        builder.setPositiveButton(getString(R.string.dialog_positive_btn_save), (dialogInterface, i) -> {
            save();
            dialogInterface.dismiss();
        });
        builder.show();
    }

    //if task is completed then there is no data changed
    //so finish the activity
    @Override
    public void onBackPressed() {
        if (mTaskIsCompleted)
            finish();
        else
            super.onBackPressed();
    }
}