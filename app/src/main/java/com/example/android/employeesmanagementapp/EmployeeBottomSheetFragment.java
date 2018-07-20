package com.example.android.employeesmanagementapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


@SuppressLint("ValidFragment")
public class EmployeeBottomSheetFragment extends BottomSheetDialogFragment implements RecyclerViewItemClickListener {

    public static final String TAG = EmployeesFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private View mRootView;
    private Toolbar mToolbar;
    private EmployeesAdapter mEmployeesAdapter = new EmployeesAdapter(AppUtils.getEmployeesFakeData(), this);
    private boolean toolBarVisibility;

    @SuppressLint("ValidFragment")
    public EmployeeBottomSheetFragment(boolean toolBarVisibility) {
        this.toolBarVisibility = toolBarVisibility;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);
        mRootView = rootView;
        // method to setup employees recycler view
        setupRecyclerView(rootView);
        if (toolBarVisibility)
            setUpToolBar();
        return rootView;
    }

    private void setUpToolBar() {
        mToolbar = mRootView.findViewById(R.id.fragment_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Available Employees");
        mToolbar.setVisibility(View.VISIBLE);
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
//        mEmployeesAdapter = new EmployeesAdapter(AppUtils.getEmployeesFakeData(), this);

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