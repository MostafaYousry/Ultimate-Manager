package com.example.android.employeesmanagementapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.TasksPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class TasksFragment extends Fragment {
    private ViewPager mViewPager;
    private TasksPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private FloatingActionButton mFab;
    private static final String PAGE_INDEX_KEY = "page index";


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mViewPager.setCurrentItem(savedInstanceState.getInt(PAGE_INDEX_KEY), true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_view_pager, container, false);

        mFab = getActivity().findViewById(R.id.fab);
        setUpViewPager(rootView);
        setUpViewPagerOnPageChangeListener();
        return rootView;
    }


    private void setUpViewPager(View rootView) {
        mViewPager = rootView.findViewById(R.id.tasks_view_pager);
        mPagerAdapter = new TasksPagerAdapter(getContext(), getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout = getActivity().findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUpViewPagerOnPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    mFab.show();
                else
                    mFab.hide();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_INDEX_KEY, mViewPager.getCurrentItem());
    }

}
