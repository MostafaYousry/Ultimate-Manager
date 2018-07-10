package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment {
    String [] s = {"taaa","taaaa","taaaaa","taaaaaa","taaaaaaa","taaaaaa","taaaaa","taaaa","taaa","taa","ta","t"};
    private RecyclerView mRecyclerView;
    private EmployeeAdapter mEmployeeAdapter;
    private LinearLayoutManager mLinearLayoutManager ;


    // Required empty public constructor
    public EmployeesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_employees, container, false);
        setupRecyclerView(rootView);

        return rootView;
    }

    private void setupRecyclerView(View rootview) {
        mRecyclerView = rootview.findViewById(R.id.employees_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mEmployeeAdapter = new EmployeeAdapter(s);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mEmployeeAdapter);
    }
}