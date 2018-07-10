package com.example.android.employeesmanagementapp;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set hamburger icon in the action bar to open the drawer
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_drawer);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //setup navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //when app starts we show the tasks fragment
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container , new TasksFragment()).commit();
        }
    }

    /**
     * used to handle switching between fragments when a new navigation item is selected
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //in case item is already selected do nothing
        if (item.isChecked()){
            mDrawerLayout.closeDrawers();
            return true;
        }


        Log.d(TAG , "New Item is selected");

        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()){
            case R.id.nav_tasks:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container , new TasksFragment()).commit();
                mToolbar.setTitle(getString(R.string.tasks));
                break;
            case R.id.nav_employees:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container , new EmployeesFragment()).commit();
                mToolbar.setTitle(getString(R.string.employees));
                break;
            case R.id.nav_departments:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container , new DepartmentsFragment()).commit();
                mToolbar.setTitle(getString(R.string.departments));
                break;

        }

        item.setChecked(true);
        mDrawerLayout.closeDrawers();

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
