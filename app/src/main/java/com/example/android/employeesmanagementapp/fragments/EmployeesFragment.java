package com.example.android.employeesmanagementapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.RecyclerViewItemLongClickListener;
import com.example.android.employeesmanagementapp.activities.AddEmployeeActivity;
import com.example.android.employeesmanagementapp.activities.MainActivity;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements RecyclerViewItemClickListener, RecyclerViewItemLongClickListener {

    public static final String TAG = EmployeesFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;
    private AppDatabase mDb;
    private ArrayList<EmployeeEntry> selectedEmployees = new ArrayList<EmployeeEntry>();
    private List<EmployeeEntry> employeeList = new ArrayList<EmployeeEntry>();
    EmployeeSelection mEmployeeSelection;

    public interface EmployeeSelection {
         void getSelectedEmployees(ArrayList<EmployeeEntry> selectedEmployeesId );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the interface. If not, it throws an exception
        try {
            mEmployeeSelection = (EmployeeSelection) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EmployeeSelection");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

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
        mEmployeesAdapter = new EmployeesAdapter(this, true, this);

        final LiveData<List<EmployeeEntry>> employeesList = ViewModelProviders.of(this).get(MainViewModel.class).getAllEmployeesList();
        employeesList.observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employeeEntries) {
                mEmployeesAdapter.setData(employeeEntries);
                employeeList = employeeEntries;
            }
        });


        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeesAdapter);
    }


    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemRowID, int clickedPosition) {
        //todo:open employee details

        Log.d(TAG, "Item with ID =  " + clickedItemRowID + " is clicked");
        Snackbar.make(getView(), "Item with ID =  " + clickedItemRowID + " is clicked", Snackbar.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(getContext(),AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY,clickedItemRowID);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongCLick(int longClickedItemRowId, int clickedPosition) {
        //if employeeId doesn't exist in  the array list --> add it
        if (!selectedEmployees.contains(employeeList.get(clickedPosition))) {
            selectedEmployees.add(employeeList.get(clickedPosition));
            Toast.makeText(getContext(), "employee long click listener with id " + longClickedItemRowId, Toast.LENGTH_LONG).show();
        }
        //if employeeId exists in  the array list --> remove it
        else{
            selectedEmployees.remove(employeeList.get(clickedPosition));
            Toast.makeText(getContext(), "Remove employee with id " + longClickedItemRowId, Toast.LENGTH_LONG).show();
        }
        mEmployeeSelection.getSelectedEmployees(selectedEmployees);
        return true;
    }
}