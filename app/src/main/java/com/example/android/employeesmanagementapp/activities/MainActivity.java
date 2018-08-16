package com.example.android.employeesmanagementapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.example.android.employeesmanagementapp.fragments.DepartmentsFragment;
import com.example.android.employeesmanagementapp.fragments.EmployeesFragment;
import com.example.android.employeesmanagementapp.fragments.TasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private Toolbar mToolbar;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationService.setBadge(getApplicationContext(),0);
        NotificationService.setTasksCount(0);


        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        //setup navigation view
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


        mTabLayout = findViewById(R.id.tab_layout);
        mFab = findViewById(R.id.fab);

        setUpFabClickListeners();

        ViewModelProviders.of(this).get(MainViewModel.class).getAllDepartmentsList().observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                if (departmentEntries != null) {

                    if (activeFragment instanceof TasksFragment) {
                        if (departmentEntries.isEmpty())
                            mFab.setOnClickListener(mFabClickListenerNoDeps);
                        else mFab.setOnClickListener(mFabClickListenerTasks);

                    } else if (activeFragment instanceof EmployeesFragment) {
                        if (departmentEntries.isEmpty())
                            mFab.setOnClickListener(mFabClickListenerNoDeps);
                        else mFab.setOnClickListener(mFabClickListenerEmployees);
                    } else {
                        mFab.setOnClickListener(mFabClickListenerDepartments);
                    }

                }
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

    private void setUpFabClickListeners() {

        mFabClickListenerNoDeps = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        };

        mFabClickListenerEmployees = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                startActivity(intent);
            }
        };

        mFabClickListenerTasks = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        };

        mFabClickListenerDepartments = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddDepartmentActivity.class);
                startActivity(intent);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationService.setBadge(getApplicationContext(),0);
        NotificationService.setTasksCount(0);
        mFabClickListenerNoDeps = null;
        mFabClickListenerTasks = null;
        mFabClickListenerEmployees = null;
        mFabClickListenerDepartments = null;
    }

    void loadFragment(Fragment fragment) {
        if (activeFragment == fragment)
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
                item.setIcon(R.drawable.ic_tasks_filled);
                mBottomNavigationView.getMenu().findItem(R.id.nav_employees).setIcon(R.drawable.ic_employees);
                mBottomNavigationView.getMenu().findItem(R.id.nav_departments).setIcon(R.drawable.ic_departments);
                mBottomNavigationView.setItemIconTintList(null);
                loadFragment(tasksFragment);
                mTabLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(getString(R.string.tasks));
                mFab.setOnClickListener(mFabClickListenerTasks);
                if (mTabLayout.getSelectedTabPosition() == 1)
                    mFab.hide();
                else mFab.show();
                break;
            case R.id.nav_employees:
                item.setIcon(R.drawable.ic_employee_filled);
                mBottomNavigationView.getMenu().findItem(R.id.nav_tasks).setIcon(R.drawable.ic_tasks);
                mBottomNavigationView.getMenu().findItem(R.id.nav_departments).setIcon(R.drawable.ic_departments);
                mBottomNavigationView.setItemIconTintList(null);
                loadFragment(employeesFragment);
                mTabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.employees));
                mFab.setOnClickListener(mFabClickListenerEmployees);
                mFab.show();
                break;
            case R.id.nav_departments:
                item.setIcon(R.drawable.ic_departments_filled);
                mBottomNavigationView.getMenu().findItem(R.id.nav_tasks).setIcon(R.drawable.ic_tasks);
                mBottomNavigationView.getMenu().findItem(R.id.nav_employees).setIcon(R.drawable.ic_employees);
                mBottomNavigationView.setItemIconTintList(null);
                loadFragment(departmentsFragment);
                mTabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.departments));
                mFab.setOnClickListener(mFabClickListenerDepartments);
                mFab.show();
                break;

        }

        item.setChecked(true);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "tasks_fragment", tasksFragment);
        getSupportFragmentManager().putFragment(outState, "employees_fragment", employeesFragment);
        getSupportFragmentManager().putFragment(outState, "departments_fragment", departmentsFragment);
        outState.putInt("selected_fragment_id", mBottomNavigationView.getSelectedItemId());
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

