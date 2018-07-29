package com.example.android.employeesmanagementapp.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.UndoDeleteAction;
import com.example.android.employeesmanagementapp.activities.AddEmployeeActivity;
import com.example.android.employeesmanagementapp.activities.MainActivity;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements RecyclerViewItemClickListener, EmployeesAdapter.EmployeeSelectedStateListener {

    public static final String TAG = EmployeesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;

    private AppDatabase mDb;

    private List<EmployeeEntry> mSelectedEmployees = new ArrayList<>();

    private LinearLayout emptyView;
    private TextView emptyViewTextView;

    private Snackbar mSnackbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());
        setHasOptionsMenu(true);
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

        setFabActivation();
        setUpOnSwipe();

        return rootView;
    }

    private void setUpOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                int entryPosition = viewHolder.getAdapterPosition();
                EmployeeEntry employeeEntry = mEmployeesAdapter.getData().get(entryPosition);
                UndoDeleteAction mUndoDeleteAction = new UndoDeleteAction( employeeEntry,null, getContext());
                Snackbar.make(getActivity().findViewById(android.R.id.content), employeeEntry.getEmployeeName()+" will be deleted", Snackbar.LENGTH_LONG).setAction("Undo", mUndoDeleteAction).show();

                    System.out.println("deleting");
                    AppExecutor.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            int position = viewHolder.getAdapterPosition();
                            mDb.employeesDao().deleteEmployee(mEmployeesAdapter.getData().get(position));
                        }
                    });

            }
        }).attachToRecyclerView(mRecyclerView);


    }

    @Override
    public void onStop() {
        super.onStop();

        if (mSnackbar != null)
            mSnackbar.dismiss();
    }


    private void setFabActivation() {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final int depNum = mDb.departmentsDao().getNumDepartments();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (depNum == 0) {
                            getActivity().findViewById(R.id.fab).setEnabled(false);
                            mSnackbar = Snackbar.make(getView(), "please add department first", Snackbar.LENGTH_INDEFINITE);
                            mSnackbar.show();
                        } else {
                            getActivity().findViewById(R.id.fab).setEnabled(true);
                            if (mSnackbar != null)
                                mSnackbar.dismiss();
                        }

                    }
                });
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
        mEmployeesAdapter = new EmployeesAdapter(this, this);

        final LiveData<List<EmployeeEntry>> employeesList = ViewModelProviders.of(this).get(MainViewModel.class).getAllEmployeesList();
        employeesList.observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employeeEntries) {
                if (employeeEntries != null) {
                    if (employeeEntries.isEmpty())
                        showEmptyView();
                    else {
                        mEmployeesAdapter.setData(employeeEntries);
                        showRecyclerView();
                    }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_employees_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if (mSelectedEmployees.isEmpty()) {
            Toast.makeText(getContext(), "No employees selected", Toast.LENGTH_LONG).show();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.delete_employees:
                Toast.makeText(getContext(), "Deleting " + mSelectedEmployees.size() + " employees", Toast.LENGTH_LONG).show();
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mSelectedEmployees.size(); i++) {
                            mDb.employeesDao().deleteEmployee(mSelectedEmployees.get(i));
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resetSelection();
                            }
                        });
                    }
                });

                return true;

            case R.id.move_employees:
                showChooseDepDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChooseDepDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.choose_department_dialog_title));

        final Spinner spinner = new Spinner(getContext());
        spinner.setPaddingRelative(dpToPx(16), 0, dpToPx(16), 0);
        spinner.setAdapter(new DepartmentsArrayAdapter(getContext(), this));
        spinner.setSelection(0);
        builder.setView(spinner);

        builder.setPositiveButton(getString(R.string.choose_department_dialog_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "moving " + mSelectedEmployees.size() + " employees", Toast.LENGTH_LONG).show();
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mSelectedEmployees.size(); i++) {
                            EmployeeEntry employeeEntry = mSelectedEmployees.get(i);
                            employeeEntry.setDepartmentId((int) spinner.getSelectedView().getTag());
                            mDb.employeesDao().updateEmployee(employeeEntry);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resetSelection();
                            }
                        });
                    }
                });
            }
        });
        builder.setNegativeButton(getString(R.string.choose_department_dialog_cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                resetSelection();
            }
        });

        builder.show();
    }

    /**
     * Converts dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void resetSelection() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.employees));
        mEmployeesAdapter.setEmployeeSelectionMode(EmployeesAdapter.SELECTION_MODE_SINGLE);
        mSelectedEmployees.clear();
    }

    public boolean isInMultiSelectionMode() {
        if (mEmployeesAdapter.getEmployeeSelectionMode() == EmployeesAdapter.SELECTION_MODE_MULTIPLE) {
            resetSelection();
            return true;
        }
        return false;
    }

    @Override
    public void onEmployeeSelected(EmployeeEntry employeeEntry) {
        //add employee to selected list
        mSelectedEmployees.add(employeeEntry);
        Toast.makeText(getContext(), "employee with id " + employeeEntry.getEmployeeID() + " is added", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mSelectedEmployees.size() + " selected");
    }

    @Override
    public void onEmployeeDeselected(EmployeeEntry employeeEntry) {
        //remove employee from selected list
        mSelectedEmployees.remove(employeeEntry);
        Toast.makeText(getContext(), "employee with id " + employeeEntry.getEmployeeID() + " is removed", Toast.LENGTH_SHORT).show();
        if (mSelectedEmployees.isEmpty()) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.employees));
            mEmployeesAdapter.setEmployeeSelectionMode(EmployeesAdapter.SELECTION_MODE_SINGLE);
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(mSelectedEmployees.size() + " selected");
        }
    }
}
