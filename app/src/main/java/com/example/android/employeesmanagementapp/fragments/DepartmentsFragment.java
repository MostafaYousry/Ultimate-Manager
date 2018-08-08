package com.example.android.employeesmanagementapp.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.activities.AddDepartmentActivity;
import com.example.android.employeesmanagementapp.activities.MainActivity;
import com.example.android.employeesmanagementapp.adapters.DepartmentsAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment implements DepartmentsAdapter.DepartmentItemClickListener, PopupMenu.OnMenuItemClickListener {

    private final String TAG = DepartmentsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private DepartmentsAdapter mAdapter;
    private AppDatabase mDb;
    private LinearLayout mEmptyView;
    private TextView mEmptyViewTextView;
    private List<DepartmentEntry> mSelectedDepartments = new ArrayList<>();
    private AddNewDepViewModel mAddNewDepViewModel;

    public DepartmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //enable add departments button
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.fab).setEnabled(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

        //get views
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);
        mEmptyView = rootView.findViewById(R.id.empty_view);
        mEmptyViewTextView = rootView.findViewById(R.id.empty_view_message_text_view);
        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //define a click listener to show popup menu with department options
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), view, Gravity.TOP);

                // This activity implements OnMenuItemClickListener
                popup.setOnMenuItemClickListener(DepartmentsFragment.this);
                popup.inflate(R.menu.menu_department_options);
                popup.show();
            }
        };

        //initialise recycler view adapter
        mAdapter = new DepartmentsAdapter(this, clickListener);

        LiveData<List<DepartmentEntry>> departmentsList = ViewModelProviders.of(getActivity()).get(MainViewModel.class).getAllDepartmentsList();
        departmentsList.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                if (departmentEntries != null) {
                    if (departmentEntries.isEmpty())
                        showEmptyView();
                    else {
                        mAdapter.setData(departmentEntries);
                        showRecyclerView();
                    }
                }
            }
        });


        mRecyclerView.setAdapter(mAdapter);

        //hooking recycler view with grid layout manager (2 columns)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //adding spacing between grid items
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(8)));

        //applying default rv animations
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;

    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyViewTextView.setText(R.string.department_empty_view_message);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }


    /**
     * Converts dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_department:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("if you deleted this Department all its employees and tasks will be deleted");
                        Log.i("test","yes");
                        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAddNewDepViewModel = ViewModelProviders.of(DepartmentsFragment.this, new DepIdFact(mDb, mAdapter.getClickedDepartment().getDepartmentId())).get(AddNewDepViewModel.class);
                                final LiveData<List<TaskEntry>> depRunningTasks = mAddNewDepViewModel.getRunningTasks();
                                depRunningTasks.observe(DepartmentsFragment.this, new Observer<List<TaskEntry>>() {
                                    @Override
                                    public void onChanged(final List<TaskEntry> taskEntries) {
                                        depRunningTasks.removeObserver(this);
                                        AppExecutor.getInstance().diskIO().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (TaskEntry taskEntry : taskEntries) {
                                                    Log.i("test",taskEntry.getTaskTitle());
                                                    mDb.employeesTasksDao().deleteDepartmentRunningTasks(taskEntry.getTaskId());
                                                    mDb.tasksDao().deleteTask(taskEntry);
                                                }
                                            }
                                        });
                                    }
                                });
                                final LiveData<List<EmployeeEntry>> depEmployees = mAddNewDepViewModel.getEmployees();
                                depEmployees.observe(DepartmentsFragment.this, new Observer<List<EmployeeEntry>>() {
                                    @Override
                                    public void onChanged(final List<EmployeeEntry> employeeEntries) {
                                        depEmployees.removeObserver(this);
                                        AppExecutor.getInstance().diskIO().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                for (EmployeeEntry employeeEntry : employeeEntries) {
                                                    Log.i("test",employeeEntry.getEmployeeName());
                                                    mDb.employeesDao().deleteEmployeeFromDepartmentTask(employeeEntry.getEmployeeID());
                                                }
                                            }
                                        });
                                    }
                                });

                            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.getClickedDepartment().setDepartmentIsDeleted(true);
                                    Log.i("test", mAdapter.getClickedDepartment().getDepartmentName());
                                    mDb.departmentsDao().updateDepartment(mAdapter.getClickedDepartment());
                                }
                            });
                            }
                        });
                        alertDialog.show();
                    }
                });
                //todo: make sure not to delete a full department
                //hasEmployees=  mDb.employeesDao().loadEmployees(mSelectedDepartments.get(i).getDepartmentId());
                //if(mhasEmployees != null)
                // Toast.makeText(getContext(),"Can't delete this department because it has employees please move them or delete them first", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDepartmentClick(int departmentRowID, int departmentPosition) {
        Intent intent = new Intent(getActivity(), AddDepartmentActivity.class);
        intent.putExtra(AddDepartmentActivity.DEPARTMENT_ID_KEY, departmentRowID);
        startActivity(intent);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        public GridSpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position


            if (position % 2 == 0) {
                //if left row
                outRect.left = spacing;
                outRect.right = spacing / 2;
            } else {
                //if right row
                outRect.right = spacing;
                outRect.left = spacing / 2;
            }

            //if top row
            if (position == 0 || position == 1) {
                outRect.top = spacing;
            }


            outRect.bottom = spacing;

        }
    }


}
