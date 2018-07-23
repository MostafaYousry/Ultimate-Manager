package com.example.android.employeesmanagementapp.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.fragments.DepartmentsFragment;
import com.example.android.employeesmanagementapp.fragments.EmployeesFragment;
import com.example.android.employeesmanagementapp.fragments.TasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private int mSelectedFragmentId;
    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpFab();

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        //setup navigation view
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


        //when app starts we show the tasks fragment
        if (savedInstanceState == null) {
            mBottomNavigationView.setSelectedItemId(R.id.nav_tasks);
            loadFragment(new TasksFragment());
            getSupportActionBar().setTitle(getString(R.string.tasks));
            mSelectedFragmentId = R.id.nav_tasks;
        }


    }

    void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_manger_options, menu);
        return true;
    }


    private void setUpFab() {

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (mSelectedFragmentId) {
                    case R.id.nav_tasks:
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
    }

    /**
     * used to handle switching between fragments when a new navigation item is selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //in case item is already selected do nothing
        if (item.isChecked()) {
            return true;
        }

        Log.d(TAG, "New Item is selected");

        mSelectedFragmentId = item.getItemId();
        switch (mSelectedFragmentId) {
            case R.id.nav_tasks:
                loadFragment(new TasksFragment());
                mToolbar.setTitle(getString(R.string.tasks));
                break;
            case R.id.nav_employees:
                loadFragment(new EmployeesFragment());
                mToolbar.setTitle(getString(R.string.employees));
                break;
            case R.id.nav_departments:
                loadFragment(new DepartmentsFragment());
                mToolbar.setTitle(getString(R.string.departments));
                break;

        }

        item.setChecked(true);

        return true;
    }


}
