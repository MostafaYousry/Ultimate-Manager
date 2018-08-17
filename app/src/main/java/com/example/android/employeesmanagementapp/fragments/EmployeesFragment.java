package com.example.android.employeesmanagementapp.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.activities.AddEmployeeActivity;
import com.example.android.employeesmanagementapp.adapters.DepartmentsArrayAdapter;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements EmployeesAdapter.EmployeeItemClickListener, EmployeesAdapter.EmployeeSelectedStateListener {

    public static final String TAG = EmployeesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;

    private AppDatabase mDb;

    private LinearLayout emptyView;
    private TextView emptyViewTextView;
    private ImageView emptyViewImageView;

    private boolean mIsInMultiSelectMode;

    private Toolbar mActivityToolBar;
    private FloatingActionButton mActivityFab;
    private boolean selectOptionPressed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivityToolBar = getActivity().findViewById(R.id.toolbar);
        mActivityToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abortMultiSelection();
            }
        });
        mActivityFab = getActivity().findViewById(R.id.fab);
    }

    public void abortMultiSelection() {
        onMultiSelectFinish();
        mEmployeesAdapter.setEmployeeSelectionMode(EmployeesAdapter.SELECTION_MODE_SINGLE);
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
        emptyViewImageView = rootView.findViewById(R.id.empty_view_message_image_view);

        return rootView;
    }


    public boolean isInMultiSelectMode() {
        return mIsInMultiSelectMode;
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
        mEmployeesAdapter = new EmployeesAdapter(getContext(), this, this);

        final MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        final LiveData<List<EmployeeWithExtras>> employeesList = mainViewModel.getEmployeesWithExtrasList();
        employeesList.observe(this, new Observer<List<EmployeeWithExtras>>() {
            @Override
            public void onChanged(List<EmployeeWithExtras> employeeEntries) {
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
        emptyViewImageView.setImageResource(R.drawable.ic_no_employee);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (selectOptionPressed) {
            menu.findItem(R.id.action_delete_employees).setVisible(false);
            menu.findItem(R.id.action_move_employees).setVisible(false);
            menu.findItem(R.id.action_select).setVisible(false);
            return;
        }


        if (mIsInMultiSelectMode) {
            menu.findItem(R.id.action_delete_employees).setVisible(true);
            menu.findItem(R.id.action_move_employees).setVisible(true);
            menu.findItem(R.id.action_select).setVisible(false);
        } else {
            menu.findItem(R.id.action_delete_employees).setVisible(false);
            menu.findItem(R.id.action_move_employees).setVisible(false);
            menu.findItem(R.id.action_select).setVisible(true);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_employees_fragment, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_select:
                selectOptionPressed = true;
                onMultiSelectStart();
                mEmployeesAdapter.setEmployeeSelectionMode(EmployeesAdapter.SELECTION_MODE_MULTIPLE);
                return true;
            case R.id.action_delete_employees:

                showDeleteEmployeesDialogue();
                return true;

            case R.id.action_move_employees:

                showChooseDepDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showDeleteEmployeesDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.title_dialog_delete_employees));
        builder.setMessage(getString(R.string.message_dialog_delete_employees));

        builder.setPositiveButton(getString(R.string.delete_employees), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (EmployeeWithExtras employeeWithExtras : mEmployeesAdapter.getSelectedOnes()) {

                            int empID = employeeWithExtras.employeeEntry.getEmployeeID();

                            if (employeeWithExtras.employeeNumRunningTasks == 0 && mDb.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                                mDb.employeesDao().deleteEmployee(empID);
                            } else if (employeeWithExtras.employeeNumRunningTasks > 0 && mDb.employeesTasksDao().getNumCompletedTasksEmployee(empID) == 0) {
                                mDb.employeesTasksDao().deleteEmployeeJoinRecords(empID);
                                mDb.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);
                            } else if (employeeWithExtras.employeeNumRunningTasks > 0 && mDb.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                                mDb.employeesTasksDao().deleteEmployeeFromRunningTasks(empID);
                                mDb.employeesDao().deleteEmployee(empID);
                            } else {
                                mDb.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);
                            }

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                abortMultiSelection();
                            }
                        });
                            mDb.tasksDao().deleteEmptyTasks();

                    }

                });


            }
        });
        builder.setNegativeButton(getString(R.string.cancel_delete_employee), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.show();
    }

    private void showChooseDepDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.choose_department_dialog_title));

        final DepartmentsArrayAdapter departmentsArrayAdapter = new DepartmentsArrayAdapter(getActivity(), AppUtils.dpToPx(getActivity(), 24), AppUtils.dpToPx(getActivity(), 8), AppUtils.dpToPx(getActivity(), 0), AppUtils.dpToPx(getActivity(), 8), R.style.mainTextStyle);

        final LiveData<List<DepartmentEntry>> departments = ViewModelProviders.of(getActivity()).get(MainViewModel.class).getAllDepartmentsList();
        departments.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                departments.removeObservers(getActivity());
                departmentsArrayAdapter.setData(departmentEntries);
            }
        });

        builder.setSingleChoiceItems(departmentsArrayAdapter, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int selectedDepartmentId = departmentsArrayAdapter.getDepId(i);
                        for (EmployeeWithExtras employeeWithExtras : mEmployeesAdapter.getSelectedOnes()) {
                            employeeWithExtras.employeeEntry.setDepartmentId(selectedDepartmentId);
                            mDb.employeesDao().updateEmployee(employeeWithExtras.employeeEntry);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                abortMultiSelection();
                            }
                        });
                    }
                });
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.choose_department_dialog_cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onEmployeeClick(int employeeRowID, int employeePosition) {
        Intent intent = new Intent(getActivity(), AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, employeeRowID);
        startActivity(intent);
    }

    @Override
    public void onMultiSelectStart() {
        mIsInMultiSelectMode = true;
        getActivity().invalidateOptionsMenu();
        mActivityToolBar.setNavigationIcon(R.drawable.ic_close);
        mActivityFab.hide();
    }

    @Override
    public void onMultiSelectFinish() {
        mIsInMultiSelectMode = false;
        getActivity().invalidateOptionsMenu();
        mActivityToolBar.setNavigationIcon(null);
        mActivityToolBar.setTitle(R.string.employees);
        mActivityFab.show();
    }

    @Override
    public void onSelectedNumChanged(int numSelected) {
        if (selectOptionPressed) {
            selectOptionPressed = false;
            getActivity().invalidateOptionsMenu();
        }
        mActivityToolBar.setTitle(String.valueOf(numSelected));
    }


}
