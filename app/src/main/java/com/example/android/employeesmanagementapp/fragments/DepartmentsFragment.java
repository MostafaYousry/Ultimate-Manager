package com.example.android.employeesmanagementapp.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.activities.AddDepartmentActivity;
import com.example.android.employeesmanagementapp.activities.MainActivity;
import com.example.android.employeesmanagementapp.adapters.DepartmentsAdapter;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

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
public class DepartmentsFragment extends Fragment implements RecyclerViewItemClickListener , DepartmentsAdapter.DepartmentSelectedStateListener {
    private final String TAG = DepartmentsFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private DepartmentsAdapter mAdapter;
    private AppDatabase mDb;
    private LinearLayout emptyView;
    private TextView emptyViewTextView;
    private List<DepartmentEntry> mSelectedDepartments = new ArrayList<>();
    private LiveData<List<EmployeeEntry>> mhasEmployees ;


    public DepartmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.fab).setEnabled(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

        //get recycler view
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);

        emptyView = rootView.findViewById(R.id.empty_view);
        emptyViewTextView = rootView.findViewById(R.id.empty_view_message_text_view);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //initialise recycler view adapter
        mAdapter = new DepartmentsAdapter(this,this);

        LiveData<List<DepartmentEntry>> departmentsList = ViewModelProviders.of(this).get(MainViewModel.class).getAllDepartmentsList();
        departmentsList.observe(this, new Observer<List<DepartmentEntry>>() {
            @Override
            public void onChanged(List<DepartmentEntry> departmentEntries) {
                if (departmentEntries != null) {
                    mAdapter.setData(departmentEntries);
                    if (mAdapter.getItemCount() == 0)
                        showEmptyView();
                    else
                        showRecyclerView();


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
        emptyViewTextView.setText(R.string.department_empty_view_message);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }


    /**
     * called when a grid item is clicked
     */
    @Override
    public void onItemClick(int clickedItemRowID, int clickedItemPosition) {
        Intent intent = new Intent(getActivity(), AddDepartmentActivity.class);
        intent.putExtra(AddDepartmentActivity.DEPARTMENT_ID_KEY, clickedItemRowID);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_departments_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        if (mSelectedDepartments.isEmpty()) {
            Toast.makeText(getContext(), "No departments selected", Toast.LENGTH_LONG).show();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.delete_departments:
                Toast.makeText(getContext(), "Deleting " + mSelectedDepartments.size() + " departments", Toast.LENGTH_LONG).show();
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mSelectedDepartments.size(); i++) {
                           //todo: make sure not to delete a full department
                            //mhasEmployees=  mDb.employeesDao().loadEmployees(mSelectedDepartments.get(i).getDepartmentId());
                            //if(mhasEmployees != null)
                             // Toast.makeText(getContext(),"Can't delete this department because it has employees please move them or delete them first", Toast.LENGTH_LONG).show();
                            mDb.departmentsDao().deleteDepartment(mSelectedDepartments.get(i));
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

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Converts dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void resetSelection() {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.departments));
        mAdapter.setDepartmentSelectionMode(DepartmentsAdapter.SELECTION_MODE_SINGLE);
        mSelectedDepartments.clear();
    }

    public boolean isInMultiSelectionMode() {
        if (mAdapter.getDepartmentSelectionMode() == DepartmentsAdapter.SELECTION_MODE_MULTIPLE) {
            resetSelection();
            return true;
        }
        return false;
    }

    @Override
    public void onDepartmentSelected(DepartmentEntry departmentEntry) {
        //add department to selected list
        mSelectedDepartments.add(departmentEntry);
        Toast.makeText(getContext(), "department with id " + departmentEntry.getDepartmentId() + " is added", Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mSelectedDepartments.size() + " selected");
    }

    @Override
    public void onDepartmentDeselected(DepartmentEntry departmentEntry) {
        //remove department from selected list
        mSelectedDepartments.remove(departmentEntry);
        Toast.makeText(getContext(), "department with id " + departmentEntry.getDepartmentId() + " is removed", Toast.LENGTH_SHORT).show();
        if (mSelectedDepartments.isEmpty()) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.departments));
            mAdapter.setDepartmentSelectionMode(DepartmentsAdapter.SELECTION_MODE_SINGLE);
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(mSelectedDepartments.size() + " selected");
        }
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
