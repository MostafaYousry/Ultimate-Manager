package com.example.android.employeesmanagementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.NotificationService;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.adapters.EmployeesAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.factories.DepIdFact;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AddDepartmentActivity extends AppCompatActivity implements RecyclerViewItemClickListener {
    public static final String DEPARTMENT_ID_KEY = "department_id";

    private static final int DEFAULT_DEPARTMENT_ID = -1;
    private static final String TAG = AddDepartmentActivity.class.getSimpleName();

    private BottomSheetBehavior mSheetBehavior;

    private int mDepartmentId;

    private EditText mDepartmentName;
    private Toolbar mToolbar;
    private AppDatabase mDb;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        mDb = AppDatabase.getInstance(this);

        //check if activity was opened from a click on rv item or from the fab
        Intent intent = getIntent();
        if (intent != null) {
            mDepartmentId = intent.getIntExtra(DEPARTMENT_ID_KEY, DEFAULT_DEPARTMENT_ID);
        }

        //find views
        mDepartmentName = findViewById(R.id.department_name);

        //set toolbar as actionbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //set toolbar home icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        setUpToolBar();
        //setUpEmployeesBS();
        setUpEmployeesRV();


        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            clearViews();
        } else {
            final LiveData<DepartmentEntry> department = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class).getDepartment();
            department.observe(this, new Observer<DepartmentEntry>() {
                @Override
                public void onChanged(DepartmentEntry departmentEntry) {
                    department.removeObservers(AddDepartmentActivity.this);
                    populateUi(departmentEntry);
                }
            });
        }


    }

    private void setUpEmployeesRV() {
        mRecyclerView = findViewById(R.id.department_employees_rv);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        HorizontalEmployeeAdapter mAdapter = new HorizontalEmployeeAdapter(this);
        mAdapter.setData(AppUtils.getEmployeesFakeData(), false);
        mRecyclerView.setAdapter(mAdapter);

    }

    protected void onStop() {
        super.onStop();

        Intent intent = new Intent(this, NotificationService.class);
        // send the due date and the id of the task within the intent
        //intent.putExtra("task due date", taskDueDate.getTime() - taskStartDAte.getTime())'
        //intent.putExtra("task id",mTaskId);

        //just for experiment until tasks are done
        Bundle bundle = new Bundle();
        System.out.println(mDepartmentId);
        bundle.putInt("task id", 38);
        bundle.putLong("task due date", 30);
        intent.putExtras(bundle);
        startService(intent);
    }


    private void populateUi(DepartmentEntry departmentEntry) {
        if (departmentEntry == null)
            return;
        mDepartmentName.setText(departmentEntry.getDepartmentName());


    }

    private void clearViews() {
        mDepartmentName.setText("");
    }

    private void setUpToolBar() {
        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            getSupportActionBar().setTitle("Add new department");
        } else {
            getSupportActionBar().setTitle("Update department");
        }
    }

    private void setUpEmployeesBS() {

        mSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_root));

        if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
            mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.show_dep_emp_rv);
        final EmployeesAdapter employeesAdapter = new EmployeesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(employeesAdapter);

        LiveData<List<EmployeeEntry>> employeesInDepartment = ViewModelProviders.of(this, new DepIdFact(mDb, mDepartmentId)).get(AddNewDepViewModel.class).getEmployees();
        employeesInDepartment.observe(this, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employeeEntries) {
                employeesAdapter.setData(employeeEntries);
            }
        });


        Button showDepEmpButton = findViewById(R.id.show_employees_button);
        showDepEmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveDepartment();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveDepartment() {
        if (valideData()) {
            String departmentName = mDepartmentName.getText().toString();


            final DepartmentEntry newDepartment = new DepartmentEntry(departmentName);

            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDepartmentId == DEFAULT_DEPARTMENT_ID) {
                        mDb.departmentsDao().addDepartment(newDepartment);
                    } else {
                        newDepartment.setDepartmentId(mDepartmentId);
                        mDb.departmentsDao().updateDepartment(newDepartment);
                    }

                }
            });

            finish();
        }

    }

    private boolean valideData() {
        return true;
    }


    @Override
    public void onItemClick(int clickedItemRowID, int clickedItemPosition) {
        Log.d(TAG, "item in bottom sheet is clicked");
        System.out.println("fyffyfu " + clickedItemRowID + "    " + clickedItemPosition);
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_VIEW_ONLY, true);
        intent.putExtra(AddEmployeeActivity.EMPLOYEE_ID_KEY, clickedItemRowID);
        startActivity(intent);
    }
}

class HorizontalEmployeeAdapter extends RecyclerView.Adapter<HorizontalEmployeeAdapter.EmployeesViewHolder> {
    private List<EmployeeEntry> mData;
    final private RecyclerViewItemClickListener mClickListener;
    private boolean cancelIconIsVisible;

    public HorizontalEmployeeAdapter(RecyclerViewItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @NonNull
    @Override

    public HorizontalEmployeeAdapter.EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_horizonatl_rv_item, parent, false);
        HorizontalEmployeeAdapter.EmployeesViewHolder employeesViewHolder = new HorizontalEmployeeAdapter.EmployeesViewHolder(rootView);

        return employeesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalEmployeeAdapter.EmployeesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<EmployeeEntry> data, boolean cancelIconIsVisible) {
        mData = data;
        this.cancelIconIsVisible = cancelIconIsVisible;
    }

    public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView employeeImage;
        ImageView cancelEmployee;
        TextView employeeName;
        View mItemView;

        public EmployeesViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            employeeImage = itemView.findViewById(R.id.employee_horizontal_rv_image);
            employeeName = itemView.findViewById(R.id.employee_horizontal_rv_name);
            cancelEmployee = itemView.findViewById(R.id.cancel_employee_ic);
            if(cancelIconIsVisible) {
                cancelEmployee.setVisibility(View.VISIBLE);
                cancelEmployee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeAt(getAdapterPosition());
                    }
                });
            }

            itemView.setOnClickListener(this);

        }

        public void bind(int position) {
            employeeImage.setImageResource(AppUtils.getRandomEmployeeImage());
            employeeName.setText(mData.get(position).getEmployeeName());

            itemView.setTag(mData.get(position).getEmployeeID());
        }

        @Override
        public void onClick(View view) {
            mClickListener.onItemClick((int) mItemView.getTag(), getAdapterPosition());
        }
        public void removeAt(int position) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
        }
    }
}
