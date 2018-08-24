package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * View Model class
 * <p>
 * caches data for AddEmployeeActivity
 * ---> employee entry record
 * ---> all company departments for spinner that selects employee's department
 * ---> all employee's running tasks
 * ---> all employee's completed tasks
 */
public class AddNewEmployeeViewModel extends ViewModel {
    public LiveData<EmployeeWithExtras> employeeEntry;

    public LiveData<List<DepartmentEntry>> allDepartments;

    public LiveData<PagedList<TaskEntry>> employeeCompletedTasks;
    public LiveData<PagedList<TaskEntry>> employeeRunningTasks;


    public AddNewEmployeeViewModel(AppDatabase database, int empID) {
        allDepartments = database.departmentsDao().loadDepartments();

        if (empID != -1) {

            employeeEntry = database.employeesDao().loadEmployeeById(empID);

            //configurations for pagedListAdapter
            PagedList.Config config = new PagedList.Config.Builder()
                    .setPageSize(10)
                    .build();

            employeeCompletedTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForEmployee(empID, true), config).build();
            employeeRunningTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForEmployee(empID, false), config).build();


        }
    }

}
