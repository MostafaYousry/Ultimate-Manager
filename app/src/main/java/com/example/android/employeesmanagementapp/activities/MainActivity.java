package com.example.android.employeesmanagementapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.fragments.DepartmentsFragment;
import com.example.android.employeesmanagementapp.fragments.EmployeesFragment;
import com.example.android.employeesmanagementapp.fragments.TasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;
    private TabLayout mTabLayout;

    private TasksFragment tasksFragment;
    private EmployeesFragment employeesFragment;
    private DepartmentsFragment departmentsFragment;

    private Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        //setup navigation view
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


        mTabLayout = findViewById(R.id.tab_layout);

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

    void loadFragment(Fragment fragment) {
        if (activeFragment == fragment)
            return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(activeFragment).show(fragment);
        activeFragment = fragment;
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.addToBackStack(null);
        transaction.commit();
    }


    /**
     * used to handle switching between fragments when a new navigation item is selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "Item is selected");

        switch (item.getItemId()) {
            case R.id.nav_tasks:
                loadFragment(tasksFragment);
                mTabLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(getString(R.string.tasks));
                break;
            case R.id.nav_employees:
                loadFragment(employeesFragment);
                mTabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.employees));
                break;
            case R.id.nav_departments:
                loadFragment(departmentsFragment);
                mTabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.departments));
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
}

