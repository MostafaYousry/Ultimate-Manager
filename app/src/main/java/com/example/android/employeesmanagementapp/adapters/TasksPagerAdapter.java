package com.example.android.employeesmanagementapp.adapters;

import com.example.android.employeesmanagementapp.fragments.CompletedTasksFragment;
import com.example.android.employeesmanagementapp.fragments.RunningTasksFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TasksPagerAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 2;

    public TasksPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new RunningTasksFragment();
        return new CompletedTasksFragment();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Running";
        else
            return "Completed";
    }
}
