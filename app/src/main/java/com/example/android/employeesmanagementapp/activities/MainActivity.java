package com.example.android.employeesmanagementapp.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private Toolbar mToolbar;
    private TextView mToolbarText;
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
        cancelNotification(getApplicationContext(), 22327);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        mToolbarText = findViewById(R.id.custom_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(null);


        //setup navigation view
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


        mTabLayout = findViewById(R.id.tab_layout);
        mFab = findViewById(R.id.fab);

        setUpFabClickListeners();

        ViewModelProviders.of(this).get(MainViewModel.class).numOfCompanyDepartments.observe(this, numOfDepartments -> {
            if (numOfDepartments != null) {
                mNumberOfDepartments = numOfDepartments;
                adjustFabListeners();
            }
        });

        if (savedInstanceState == null) {
            tasksFragment = new TasksFragment();
            employeesFragment = new EmployeesFragment();
            departmentsFragment = new DepartmentsFragment();
            activeFragment = tasksFragment;

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, departmentsFragment, "departments").hide(departmentsFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, employeesFragment, "employees").hide(employeesFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tasksFragment, "tasks").commit();
            mBottomNavigationView.setSelectedItemId(R.id.nav_tasks);

        } else {
            tasksFragment = (TasksFragment) getSupportFragmentManager().getFragment(savedInstanceState, "tasks_fragment");
            employeesFragment = (EmployeesFragment) getSupportFragmentManager().getFragment(savedInstanceState, "employees_fragment");
            departmentsFragment = (DepartmentsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "departments_fragment");

            mBottomNavigationView.setSelectedItemId(savedInstanceState.getInt("selected_fragment_id"));

        }
    }

    private void cancelNotification(Context applicationContext, int notificationId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) applicationContext.getSystemService(ns);
        nMgr.cancel(notificationId);
    }

    private void setUpFabClickListeners() {

        mFabClickListenerNoDeps = view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No Departments");
            builder.setMessage("Please create a department first");
            builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, AddDepartmentActivity.class);
                    startActivity(intent);
                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.show();
        };

        mFabClickListenerEmployees = view -> {
            Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        };

        mFabClickListenerTasks = view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        };

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
        mFabClickListenerNoDeps = null;
        mFabClickListenerTasks = null;
        mFabClickListenerEmployees = null;
        mFabClickListenerDepartments = null;

    }

    void loadFragment(Fragment fragment) {
        if (activeFragment == null || activeFragment == fragment)
            return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(activeFragment).show(fragment);
        activeFragment = fragment;
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.commit();
    }


    /**
     * used to handle switching between fragments when a new navigation item is selected
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "tasks_fragment", tasksFragment);
        getSupportFragmentManager().putFragment(outState, "employees_fragment", employeesFragment);
        getSupportFragmentManager().putFragment(outState, "departments_fragment", departmentsFragment);
        outState.putInt("selected_fragment_id", mBottomNavigationView.getSelectedItemId());
    }


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

    private void adjustAppBar(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_tasks:
                mTabLayout.setVisibility(View.VISIBLE);
                mToolbarText.setText(getString(R.string.tasks));
                break;
            case R.id.nav_employees:
                mTabLayout.setVisibility(View.GONE);
                mToolbarText.setText(getString(R.string.employees));
                break;
            case R.id.nav_departments:
                mTabLayout.setVisibility(View.GONE);
                mToolbarText.setText(getString(R.string.departments));
                break;
        }
    }

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

