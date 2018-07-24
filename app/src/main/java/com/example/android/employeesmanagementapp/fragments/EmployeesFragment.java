package com.example.android.employeesmanagementapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.activities.AddEmployeeActivity;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements RecyclerViewItemClickListener {

    public static final String TAG = EmployeesFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;
    private AppDatabase mDb;
    private ArrayList<EmployeeEntry> selectedEmployees = new ArrayList<EmployeeEntry>();
    private List<EmployeeEntry> employeeList = new ArrayList<EmployeeEntry>();
    EmployeeSelection mEmployeeSelection;
    private LinearLayout emptyView;
    private TextView emptyViewTextView;
    private Snackbar mSnackbar;

    public interface EmployeeSelection {
        void getSelectedEmployees(ArrayList<EmployeeEntry> selectedEmployeesId);
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

        emptyView = rootView.findViewById(R.id.empty_view);
        emptyViewTextView = rootView.findViewById(R.id.empty_view_message_text_view);

        setFabEnabled();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSnackbar != null)
            mSnackbar.dismiss();
    }

    private void setFabEnabled() {
        LiveData<List<DepartmentEntry>> departmentList = ViewModelProviders.of(getActivity()).get(MainViewModel.class).getAllDepartmentsList();
        departmentList.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                if (departmentEntries != null) {
                    if (departmentEntries.size() == 0) {
                        getActivity().findViewById(R.id.fab).setEnabled(false);
                        mSnackbar = Snackbar.make(getView(), "please add department first", Snackbar.LENGTH_INDEFINITE);
                        mSnackbar.show();
                    } else {
                        getActivity().findViewById(R.id.fab).setEnabled(true);
                        if (mSnackbar != null)
                            mSnackbar.dismiss();
                    }
                }
            }
        });
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
        mEmployeesAdapter = new EmployeesAdapter(this, false, null);

        final LiveData<List<EmployeeEntry>> employeesList = ViewModelProviders.of(this).get(MainViewModel.class).getAllEmployeesList();
        employeesList.observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employeeEntries) {
                if (employeeEntries != null) {
                    mEmployeesAdapter.setData(employeeEntries);
                    employeeList = employeeEntries;
                    if (mEmployeesAdapter.getItemCount() == 0)
                        showEmptyView();
                    else
                        showRecyclerView();

                }
            }
        });


        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeesAdapter);
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        emptyViewTextView.setText(R.string.employee_empty_view_message);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }


    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemId, int clickedItemPosition) {

        Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, clickedItemId);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_VIEW_ONLY, false);
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
        else {
            selectedEmployees.remove(employeeList.get(clickedPosition));
            Toast.makeText(getContext(), "Remove employee with id " + longClickedItemRowId, Toast.LENGTH_LONG).show();
        }
        mEmployeeSelection.getSelectedEmployees(selectedEmployees);
        return true;
    }
}