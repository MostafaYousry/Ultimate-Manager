package com.example.android.employeesmanagementapp.data.viewmodels;

import android.app.Application;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * View Model class
 * <p>
 * caches data for MainActivity
 * ---> runningTasksList for RunningTasksFragment
 * ---> departmentsList for DepartmentsFragment
 * ---> employeesList for EmployeesFragment
 */
public class MainViewModel extends AndroidViewModel {

    public LiveData<Integer> numOfCompanyDepartments;

    public final LiveData<PagedList<DepartmentWithExtras>> allDepartmentsWithExtrasList;
    public final LiveData<PagedList<TaskEntry>> runningTasksList;
    public final LiveData<PagedList<TaskEntry>> completedTasksList;
    public final LiveData<PagedList<EmployeeWithExtras>> employeesWithExtrasList;


    public MainViewModel(final Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        PagedList.Config config = new PagedList.Config.Builder()
                .setPrefetchDistance(50)
                .setPageSize(10)
                .build();

        runningTasksList = new LivePagedListBuilder<>(db.tasksDao().loadRunningTasks(), config).build();
        completedTasksList = new LivePagedListBuilder<>(db.tasksDao().loadCompletedTasks(), config).build();
        employeesWithExtrasList = new LivePagedListBuilder<>(db.employeesDao().loadEmployees(), config).build();
        allDepartmentsWithExtrasList = new LivePagedListBuilder<>(db.departmentsDao().loadDepartmentsWithExtras(), config).build();

        numOfCompanyDepartments = db.departmentsDao().getNumDepartments();
    }

}
