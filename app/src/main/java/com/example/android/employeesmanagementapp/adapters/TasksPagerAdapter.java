package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.fragments.CompletedTasksFragment;
import com.example.android.employeesmanagementapp.fragments.RunningTasksFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * adapter for displaying fragments as pages
 * displays two fragments (running tasks fragment , completed tasks fragment)
 */
public class TasksPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 2;
    private Context mContext;

    public TasksPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
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
            return mContext.getString(R.string.tab_layout_title_running);
        else
            return mContext.getString(R.string.tab_layout_title_completed);
    }
}
