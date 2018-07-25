package com.example.android.employeesmanagementapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class EmployeeBottomSheetFragment extends BottomSheetDialogFragment implements RecyclerViewItemClickListener {

    public static final String TAG = EmployeeBottomSheetFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private View mRootView;
    private Toolbar mToolbar;
    private static final String EMPLOYEES_IN_OUT_DEPARTMENT_KEY = "dep_emp_key";
    private EmployeesAdapter mEmployeesAdapter;
    private boolean mInDepartmentEmployees;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(EMPLOYEES_IN_OUT_DEPARTMENT_KEY))
                mInDepartmentEmployees = bundle.getBoolean(EMPLOYEES_IN_OUT_DEPARTMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);
        mRootView = rootView;
        // method to setup employees recycler view
        setupRecyclerView(rootView);


        return rootView;
    }


    private void setupRecyclerView(View rootView) {

        //access the employee recycler view
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //set the employee recycler view layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //create object of EmployeesAdapter and send data
        mEmployeesAdapter = new EmployeesAdapter(this);

        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeesAdapter);
    }

    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemIndex) {
        //todo:open employee details

        Log.d(TAG, "Item at index " + clickedItemIndex + " is clicked in bottom sheet");
        Snackbar.make(getView(), "Item at index " + clickedItemIndex + " is clicked", Snackbar.LENGTH_SHORT)
                .show();
    }

    public EmployeesAdapter getEmployeesAdapter() {
        return mEmployeesAdapter;
    }

}