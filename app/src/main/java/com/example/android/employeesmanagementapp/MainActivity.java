package com.example.android.employeesmanagementapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DEPARTMENTS_FRAGMENT_TAG = "departments";
    private static final String TASKS_FRAGMENT_TAG = "tasks";
    private static final String EMPLOYEES_FRAGMENT_TAG = "employees";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        //setup navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        //when app starts we show the tasks fragment
        if (savedInstanceState == null){
            bottomNavigationView.setSelectedItemId(R.id.nav_tasks);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container , new TasksFragment(),TASKS_FRAGMENT_TAG).commit();
        }

    }

    /**
     * used to handle switching between fragments when a new navigation item is selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //in case item is already selected do nothing
        if (item.isChecked()){
            return true;
        }


        Log.d(TAG , "New Item is selected");

        //todo: add to back stack to be able to return to previous fragment when back is pressed
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()){
            case R.id.nav_tasks:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container , new TasksFragment() , TASKS_FRAGMENT_TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                mToolbar.setTitle(getString(R.string.tasks));
                break;
            case R.id.nav_employees:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container , new EmployeesFragment() , EMPLOYEES_FRAGMENT_TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                mToolbar.setTitle(getString(R.string.employees));
                break;
            case R.id.nav_departments:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container , new DepartmentsFragment() , DEPARTMENTS_FRAGMENT_TAG)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                mToolbar.setTitle(getString(R.string.departments));
                break;

        }

        item.setChecked(true);

        return true;
    }


}
