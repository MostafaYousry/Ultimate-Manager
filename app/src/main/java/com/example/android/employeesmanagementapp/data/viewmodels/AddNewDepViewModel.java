package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * View Model class
 * <p>
 * caches data for AddDepartmentActivity
 * ---> department entry record
 * ---> all department's employees
 * ---> all department's running tasks
 * ---> all department's completed tasks
 */
public class AddNewDepViewModel extends ViewModel {
    public LiveData<DepartmentEntry> departmentEntry;

    public LiveData<PagedList<EmployeeEntry>> departmentEmployees;
    public LiveData<PagedList<TaskEntry>> departmentCompletedTasks;
    public LiveData<PagedList<TaskEntry>> departmentRunningTasks;

    public AddNewDepViewModel(AppDatabase database, int depId) {
        if (depId > 0) {
            departmentEntry = database.departmentsDao().loadDepartmentById(depId);

            //configurations for pagedListAdapter
            PagedList.Config config = new PagedList.Config.Builder()
                    .setPageSize(10)
                    .build();

            departmentEmployees = new LivePagedListBuilder<>(database.employeesDao().loadEmployeesInDep(depId), config).build();
            departmentCompletedTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForDepartment(depId, true), config).build();
            departmentRunningTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForDepartment(depId, false), config).build();
        }
    }

}
