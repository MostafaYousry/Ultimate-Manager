package com.example.android.employeesmanagementapp.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TasksScreenSlidePagerFragment extends Fragment {
    private static final int NUM_PAGES = 2;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks_view_pager, container,false);

        setUpViewPager(rootView);
        setUpViewPagerOnPageChangeListener();
        return rootView;
    }

    private void setUpViewPagerOnPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setUpFab(position);
            }

            @Override
            public void onPageSelected(int position) {
                setUpFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpFab(int position) {
        if(position == 0)
            getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        else getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }


    private void setUpViewPager(View rootView) {
        mViewPager =  rootView.findViewById(R.id.tasks_view_pager);
        mPagerAdapter = new ScreenPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private class ScreenPagerAdapter extends FragmentStatePagerAdapter {

        public ScreenPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return new RunningTasksFragment();

            return new CompletedTasksFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0)
                return "Running tasks";
            else
                return "Completed tasks";
        }
    }
}
