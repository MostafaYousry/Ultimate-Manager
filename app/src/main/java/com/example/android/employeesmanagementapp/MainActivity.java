package com.example.android.employeesmanagementapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String DEPARTMENTS_FRAGMENT_TAG = "departments";
    private static final String TASKS_FRAGMENT_TAG = "tasks";
    private static final String EMPLOYEES_FRAGMENT_TAG = "employees";
    private final String TAG = MainActivity.class.getSimpleName();
    private static int fragmentId = R.id.nav_tasks;
    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = null;
                switch (fragmentId){
                    case  R.id.nav_tasks:
                        intent = new Intent(MainActivity.this, AddTaskActivity.class);
                        break;
                    case R.id.nav_employees:
                        intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                        break;

                    case R.id.nav_departments:
                         intent = new Intent(MainActivity.this, AddDepartmentActivity.class);
                        break;
                  
                }
                startActivity(intent);
            }
        });

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
        fragmentId = item.getItemId();
        switch (fragmentId){
            case  R.id.nav_tasks:
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
