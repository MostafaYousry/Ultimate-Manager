package com.example.android.employeesmanagementapp.data.viewmodels;

import android.app.Application;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

/**
 * View Model class
 * <p>
 * caches data for MainActivity
 * ---> runningTasksList for RunningTasksFragment
 * ---> departmentsList for DepartmentsFragment
 * ---> employeesList for EmployeesFragment
 */
public class MainViewModel extends AndroidViewModel {

    private LiveData<List<DepartmentWithExtras>> allDepartmentsWithExtrasList;
    private LiveData<List<DepartmentEntry>> allDepartmentsList;
    private LiveData<List<TaskEntry>> runningTasksList;
    private LiveData<List<TaskEntry>> completedTasksList;
    private LiveData<List<EmployeeWithExtras>> employeesWithExtrasList;


    public MainViewModel(final Application application) {
        super(application);

        allDepartmentsWithExtrasList = AppDatabase.getInstance(application.getApplicationContext()).departmentsDao().loadDepartmentsWithExtras();
        runningTasksList = AppDatabase.getInstance(application.getApplicationContext()).tasksDao().loadRunningTasks();
        completedTasksList = AppDatabase.getInstance(application.getApplicationContext()).tasksDao().loadCompletedTasks();
        employeesWithExtrasList = AppDatabase.getInstance(application.getApplicationContext()).employeesDao().loadEmployees();

        allDepartmentsList = Transformations.map(allDepartmentsWithExtrasList, new Function<List<DepartmentWithExtras>, List<DepartmentEntry>>() {

            @Override
            public List<DepartmentEntry> apply(List<DepartmentWithExtras> input) {
                List<DepartmentEntry> departmentEntries = new ArrayList<>(input.size());
                for (DepartmentWithExtras extras : input) {
                    departmentEntries.add(extras.departmentEntry);
                }

                return departmentEntries;

            }
        });

    }

    public LiveData<List<DepartmentEntry>> getAllDepartmentsList() {
        return allDepartmentsList;
    }

    public LiveData<List<DepartmentWithExtras>> getAllDepartmentsWithExtrasList() {
        return allDepartmentsWithExtrasList;
    }

    public LiveData<List<TaskEntry>> getRunningTasksList() {
        return runningTasksList;
    }

    public LiveData<List<TaskEntry>> getCompletedTasksList() {
        return completedTasksList;
    }

    public LiveData<List<EmployeeWithExtras>> getEmployeesWithExtrasList() {
        return employeesWithExtrasList;
    }
}
