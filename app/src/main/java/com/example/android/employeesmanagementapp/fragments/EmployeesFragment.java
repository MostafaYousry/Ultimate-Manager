package com.example.android.employeesmanagementapp.fragments;


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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * fragment that shows a list of all employees in company
 */
public class EmployeesFragment extends Fragment implements EmployeesAdapter.EmployeeItemClickListener, EmployeesAdapter.EmployeeSelectedStateListener {


    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;

    private AppDatabase mDb;

    private LinearLayout emptyView;
    private TextView emptyViewTextView;
    private ImageView emptyViewImageView;

    private boolean mIsInMultiSelectMode;

    private Toolbar mActivityToolBar;
    private TextView mActivityToolBarText;
    private FloatingActionButton mActivityFab;
    private boolean selectOptionPressed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());

        //observes employees list in main view model
        ViewModelProviders.of(getActivity()).get(MainViewModel.class).employeesWithExtrasList
                .observe(this, employeeWithExtras -> {
                    if (employeeWithExtras != null) {
                        if (employeeWithExtras.isEmpty()) {
                            showEmptyView();
                        } else {
                            mEmployeesAdapter.submitList(employeeWithExtras);
                            showRecyclerView();
                        }
                    }
                });

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivityToolBar = getActivity().findViewById(R.id.toolbar);
        mActivityToolBarText = getActivity().findViewById(R.id.custom_title);
        mActivityToolBar.setNavigationOnClickListener(view -> abortMultiSelection());
        mActivityFab = getActivity().findViewById(R.id.fab);
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

    /**
     * cancels multi selection operation on list
     */
    public void abortMultiSelection() {
        onMultiSelectFinish();
        mEmployeesAdapter.setEmployeeSelectionMode(EmployeesAdapter.SELECTION_MODE_SINGLE);
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


        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeesAdapter);


    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        emptyViewTextView.setText(R.string.empty_view_message_employees);
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


    /**
     * creates menu for employees fragment
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_employees_fragment, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * handles choosing a menu option
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_select: //starts a multi select operation on list
                selectOptionPressed = true;
                onMultiSelectStart();
                mEmployeesAdapter.setEmployeeSelectionMode(EmployeesAdapter.SELECTION_MODE_MULTIPLE);
                return true;
            case R.id.action_delete_employees:

                showDeleteEmployeesDialogue();
                return true;

            case R.id.action_move_employees:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.dialog_message_move_employees));
                builder.setPositiveButton(getString(R.string.dialog_positive_btn_continue), (dialogInterface, i) -> showChooseDepDialog());

                builder.setNegativeButton(getString(R.string.dialog_negative_btn_cancel), (dialog, which) -> dialog.dismiss());
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showDeleteEmployeesDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_title_fire_employees));
        builder.setMessage(getString(R.string.dialog_message_fire_employees));

        builder.setPositiveButton(getString(R.string.dialog_positive_btn_delete), (dialog, which) -> AppExecutor.getInstance().diskIO().execute(() -> {

            for (EmployeeWithExtras employeeWithExtras : mEmployeesAdapter.getSelectedOnes()) {

                int empID = employeeWithExtras.employeeEntry.getEmployeeID();

                //if employee has completed tasks only mark employee as deleted only
                if (employeeWithExtras.employeeNumRunningTasks == 0 && mDb.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                    mDb.employeesDao().markEmployeeAsDeleted(empID);

                    //if employee has running tasks only remove employee from them then delete employee record
                } else if (employeeWithExtras.employeeNumRunningTasks > 0 && mDb.employeesTasksDao().getNumCompletedTasksEmployee(empID) == 0) {
                    mDb.employeesTasksDao().deleteEmployeeJoinRecords(empID);
                    mDb.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);

                    //if employee has both so remove him from running tasks only
                    //then mark him as deleted
                } else if (employeeWithExtras.employeeNumRunningTasks > 0 && mDb.employeesTasksDao().getNumCompletedTasksEmployee(empID) > 0) {
                    mDb.employeesTasksDao().deleteEmployeeFromRunningTasks(empID);
                    mDb.employeesDao().markEmployeeAsDeleted(empID);
                } else {
                    //if he has nothing delete employee record
                    mDb.employeesDao().deleteEmployee(employeeWithExtras.employeeEntry);
                }

            }

            //finish multi selection
            getActivity().runOnUiThread(this::abortMultiSelection);

            //check if tasks with no employees have appeared
            //due to the above employees deletion
            mDb.tasksDao().deleteTasksWithNoEmployees();

        }));
        builder.setNegativeButton(getString(R.string.dialog_negative_btn_cancel), (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showChooseDepDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_title_choose_department));

        final DepartmentsArrayAdapter departmentsArrayAdapter = new DepartmentsArrayAdapter(getActivity(), AppUtils.dpToPx(getActivity(), 24), AppUtils.dpToPx(getActivity(), 8), AppUtils.dpToPx(getActivity(), 0), AppUtils.dpToPx(getActivity(), 8), R.style.mainTextStyle);

        //load all departments
        final LiveData<List<DepartmentEntry>> departments = mDb.departmentsDao().loadDepartments();
        departments.observe(this, departmentEntries -> {
            departments.removeObservers(getActivity());
            departmentsArrayAdapter.setData(departmentEntries);
        });

        //make dialog appear as a single choice list
        builder.setSingleChoiceItems(departmentsArrayAdapter, 0, (dialogInterface, i) -> {
            AppExecutor.getInstance().diskIO().execute(() -> {
                int selectedDepartmentId = departmentsArrayAdapter.getDepId(i);
                for (EmployeeWithExtras employeeWithExtras : mEmployeesAdapter.getSelectedOnes()) {
                    mDb.employeesTasksDao().deleteEmployeeFromRunningTasks(employeeWithExtras.employeeEntry.getEmployeeID());
                    employeeWithExtras.employeeEntry.setDepartmentId(selectedDepartmentId);
                    mDb.employeesDao().updateEmployee(employeeWithExtras.employeeEntry);
                }

                mDb.tasksDao().deleteTasksWithNoEmployees();

                getActivity().runOnUiThread(this::abortMultiSelection);
            });
            dialogInterface.dismiss();
        });

        builder.setNegativeButton(getString(R.string.dialog_negative_btn_cancel), (dialog, which) -> dialog.dismiss());

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
        mActivityToolBarText.setText(getString(R.string.employees));
        mActivityFab.show();
    }

    @Override
    public void onSelectedNumChanged(int numSelected) {
        if (selectOptionPressed) {
            selectOptionPressed = false;
            getActivity().invalidateOptionsMenu();
        }
        mActivityToolBarText.setText(String.valueOf(numSelected));
    }


}
