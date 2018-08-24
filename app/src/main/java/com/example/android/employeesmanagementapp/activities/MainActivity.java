package com.example.android.employeesmanagementapp.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.example.android.employeesmanagementapp.fragments.DepartmentsFragment;
import com.example.android.employeesmanagementapp.fragments.EmployeesFragment;
import com.example.android.employeesmanagementapp.fragments.TasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

/**
 * main app activity that shows all 3 main
 * --company tasks(running,completed)
 * --company employees
 * --company departments
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //used to display title in toolbar with applied font
    private TextView mToolbarTitleTextView;

    private BottomNavigationView mBottomNavigationView;

    private TabLayout mTabLayout;

    private FloatingActionButton mFab;

    private TasksFragment tasksFragment;
    private EmployeesFragment employeesFragment;
    private DepartmentsFragment departmentsFragment;

    private Fragment activeFragment;

    private View.OnClickListener mFabClickListenerNoDeps;
    private View.OnClickListener mFabClickListenerTasks;
    private View.OnClickListener mFabClickListenerEmployees;
    private View.OnClickListener mFabClickListenerDepartments;

    private int mNumberOfDepartments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationService.setBadge(getApplicationContext(), 0);
        NotificationService.setTasksCount(0);
        if (activeFragment instanceof TasksFragment)
            cancelNotification(getApplicationContext(), 22327);

        //set toolbar as actionbar
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(null);

        //find views
        mTabLayout = findViewById(R.id.tab_layout);
        mFab = findViewById(R.id.fab);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        //toolbar text view to show title in with applied font
        mToolbarTitleTextView = findViewById(R.id.custom_title);

        //setup bottom navigation view's actions when changing between navigation items
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


        setUpFabClickListeners();

        //observe number of departments in company
        //to decide weather to allow adding employees/tasks or not
        //as in order to create a task/employee they should be assigned to a department
        ViewModelProviders.of(this).get(MainViewModel.class).numOfCompanyDepartments.observe(this, numOfDepartments -> {
            if (numOfDepartments != null) {
                mNumberOfDepartments = numOfDepartments;
                adjustFabListeners();
            }
        });


        if (savedInstanceState == null) {//first time the activity is created

            //create instance of each fragment to be saved
            //later in onSaveInstanceState()
            //and to prevent unneeded fragment recreations
            tasksFragment = new TasksFragment();
            employeesFragment = new EmployeesFragment();
            departmentsFragment = new DepartmentsFragment();

            //active fragment will be tasks fragment (home fragment)
            activeFragment = tasksFragment;

            //adds all 3 fragments to the fragment manager
            //with employees and department fragments hidden
            //while task fragment is shown
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, departmentsFragment, "departments").hide(departmentsFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, employeesFragment, "employees").hide(employeesFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tasksFragment, "tasks").commit();

            //sets the selected navigation item to be tasks
            mBottomNavigationView.setSelectedItemId(R.id.nav_tasks);

        } else {//activity is recreated

            //restore fragments instances from bundle
            tasksFragment = (TasksFragment) getSupportFragmentManager().getFragment(savedInstanceState, "tasks_fragment");
            employeesFragment = (EmployeesFragment) getSupportFragmentManager().getFragment(savedInstanceState, "employees_fragment");
            departmentsFragment = (DepartmentsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "departments_fragment");

            //restore selected active fragment from bundle
            int selectedFragmentId = savedInstanceState.getInt("selected_fragment_id");
            switch (selectedFragmentId) {
                case R.id.nav_tasks:
                    activeFragment = tasksFragment;
                    break;
                case R.id.nav_employees:
                    activeFragment = employeesFragment;
                    break;
                case R.id.nav_departments:
                    activeFragment = departmentsFragment;
                    break;

            }

            mBottomNavigationView.setSelectedItemId(selectedFragmentId);
        }
    }

    private void cancelNotification(Context applicationContext, int notificationId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) applicationContext.getSystemService(ns);
        nMgr.cancel(notificationId);
    }

    /**
     * creates and caches fab click listeners
     * each listener should work when certain conditions are met
     */
    private void setUpFabClickListeners() {

        //click listener to be applied when there are no departments in the company
        //shows a dialog to either create a department or dismiss dialog
        mFabClickListenerNoDeps = view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.dialog_title_no_departments));
            builder.setMessage(getString(R.string.dialog_message_no_departments));
            builder.setPositiveButton(getString(R.string.dialog_positive_btn_create), (dialogInterface, i) -> {
                Intent intent = new Intent(MainActivity.this, AddDepartmentActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();
            });

            builder.setNegativeButton(getString(R.string.dialog_negative_btn_not_now), (dialogInterface, i) -> dialogInterface.dismiss());

            builder.show();
        };

        //click listener that launches AddEmployeeActivity
        mFabClickListenerEmployees = view -> {
            Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        };

        //click listener that launches AddTaskActivity
        mFabClickListenerTasks = view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        };

        //click listener that launches AddDepartmentActivity
        mFabClickListenerDepartments = view -> {
            Intent intent = new Intent(MainActivity.this, AddDepartmentActivity.class);
            startActivity(intent);
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activeFragment instanceof TasksFragment) {
            NotificationService.setBadge(getApplicationContext(), 0);
            NotificationService.setTasksCount(0);
        }

        //clear all fab listeners
        mFabClickListenerNoDeps = null;
        mFabClickListenerTasks = null;
        mFabClickListenerEmployees = null;
        mFabClickListenerDepartments = null;

    }

    /**
     * used to switch between active fragment
     * and the provided fragment
     *
     * @param fragment : fragment to be shown
     */
    private void loadFragment(Fragment fragment) {
        if (activeFragment == fragment)
            return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.hide(activeFragment).show(fragment);

        activeFragment = fragment;

        transaction.commit();
    }


    /**
     * used to handle switching between fragments when a new navigation item is selected
     * handles appbar/fab changes for each fragment
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_tasks:
                adjustBottomNavigationIcons(item);
                loadFragment(tasksFragment);
                adjustAppBar(item);
                adjustFabListeners();
                if (mTabLayout.getSelectedTabPosition() == 1)
                    mFab.hide();
                else mFab.show();
                cancelNotification(getApplicationContext(), 22327);
                break;
            case R.id.nav_employees:
                adjustBottomNavigationIcons(item);
                loadFragment(employeesFragment);
                adjustAppBar(item);
                adjustFabListeners();
                mFab.show();
                break;
            case R.id.nav_departments:
                adjustBottomNavigationIcons(item);
                loadFragment(departmentsFragment);
                adjustAppBar(item);
                adjustFabListeners();
                mFab.show();
                break;

        }

        item.setChecked(true);

        return true;
    }

    /**
     * decides what listener to use depending on
     * active fragment and number of company departments
     */
    private void adjustFabListeners() {
        if (mNumberOfDepartments == 0) {
            if (activeFragment instanceof TasksFragment || activeFragment instanceof EmployeesFragment)
                mFab.setOnClickListener(mFabClickListenerNoDeps);
            else
                mFab.setOnClickListener(mFabClickListenerDepartments);
        } else {
            if (activeFragment instanceof TasksFragment)
                mFab.setOnClickListener(mFabClickListenerTasks);
            else if (activeFragment instanceof EmployeesFragment)
                mFab.setOnClickListener(mFabClickListenerEmployees);
            else
                mFab.setOnClickListener(mFabClickListenerDepartments);
        }
    }

    /**
     * saves instances of fragments and id of selected fragment
     * to be later restored if activity
     * is recreated
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "tasks_fragment", tasksFragment);
        getSupportFragmentManager().putFragment(outState, "employees_fragment", employeesFragment);
        getSupportFragmentManager().putFragment(outState, "departments_fragment", departmentsFragment);
        outState.putInt("selected_fragment_id", mBottomNavigationView.getSelectedItemId());
    }


    /**
     * handles flipping icons of bottom navigation menu items
     * from outlined icons to filled icons
     * according to selected state of each menu item
     *
     * @param item
     */
    private void adjustBottomNavigationIcons(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_tasks:
                item.setIcon(R.drawable.ic_tasks_filled);
                mBottomNavigationView.getMenu().findItem(R.id.nav_employees).setIcon(R.drawable.ic_employees);
                mBottomNavigationView.getMenu().findItem(R.id.nav_departments).setIcon(R.drawable.ic_departments);
                break;
            case R.id.nav_employees:
                item.setIcon(R.drawable.ic_employee_filled);
                mBottomNavigationView.getMenu().findItem(R.id.nav_tasks).setIcon(R.drawable.ic_tasks);
                mBottomNavigationView.getMenu().findItem(R.id.nav_departments).setIcon(R.drawable.ic_departments);
                break;
            case R.id.nav_departments:
                item.setIcon(R.drawable.ic_departments_filled);
                mBottomNavigationView.getMenu().findItem(R.id.nav_tasks).setIcon(R.drawable.ic_tasks);
                mBottomNavigationView.getMenu().findItem(R.id.nav_employees).setIcon(R.drawable.ic_employees);
                break;
        }

        mBottomNavigationView.setItemIconTintList(null);

    }

    /**
     * handles assigning titles to toolbar
     * according to which fragment is currently visible
     * and shows tab layout if active fragment is tasks
     * hides it otherwise
     *
     * @param item
     */
    private void adjustAppBar(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_tasks:
                mTabLayout.setVisibility(View.VISIBLE);
                mToolbarTitleTextView.setText(getString(R.string.tasks));
                break;
            case R.id.nav_employees:
                mTabLayout.setVisibility(View.GONE);
                mToolbarTitleTextView.setText(getString(R.string.employees));
                break;
            case R.id.nav_departments:
                mTabLayout.setVisibility(View.GONE);
                mToolbarTitleTextView.setText(getString(R.string.departments));
                break;
        }
    }

    /**
     * checks first if there is a multi selection happening
     * in employees fragment and ends it first then returns to home fragment(tasks) if
     * active fragment is not tasks
     */
    @Override
    public void onBackPressed() {

        if (!(activeFragment instanceof TasksFragment)) {
            if (activeFragment instanceof EmployeesFragment && ((EmployeesFragment) activeFragment).isInMultiSelectMode()) {
                ((EmployeesFragment) activeFragment).abortMultiSelection();
                return;
            }
            mBottomNavigationView.setSelectedItemId(R.id.nav_tasks);
        } else {
            super.onBackPressed();
        }
    }

}

