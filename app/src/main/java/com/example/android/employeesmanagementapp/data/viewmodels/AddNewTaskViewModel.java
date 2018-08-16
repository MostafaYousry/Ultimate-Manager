package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewTaskViewModel extends ViewModel {
    private LiveData<TaskEntry> task;
    private LiveData<List<EmployeeEntry>> taskEmployees;
    private LiveData<List<DepartmentEntry>> allDepartments;

    private AppDatabase mAppDatabase;
    private int mTaskId;

    public AddNewTaskViewModel(AppDatabase appDatabase, int taskId) {
        mAppDatabase = appDatabase;
        mTaskId = taskId;

        allDepartments = appDatabase.departmentsDao().loadDepartments();

        if (taskId != -1) {
            task = appDatabase.tasksDao().loadTaskById(taskId);
            taskEmployees = appDatabase.employeesTasksDao().getEmployeesForTask(taskId);
        }

    }


    public LiveData<TaskEntry> getTask() {
        return task;
    }

    public LiveData<List<EmployeeEntry>> getTaskEmployees() {
        return taskEmployees;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments() {
        return allDepartments;
    }

    public LiveData<List<EmployeeEntry>> getRestOfEmployeesInDep(int selectedDepartmentId, List<EmployeeEntry> exceptThese) {

        LiveData<List<EmployeeEntry>> restOfEmployeesInDep;
        if (mTaskId == -1 && exceptThese == null)
            restOfEmployeesInDep = mAppDatabase.employeesDao().loadEmployeesInDep(selectedDepartmentId);
        else
            restOfEmployeesInDep = mAppDatabase.employeesDao().loadEmployeesInDep(selectedDepartmentId, getIds(exceptThese));


        return restOfEmployeesInDep;
    }

    private List<Integer> getIds(List<EmployeeEntry> exceptThese) {
        List<Integer> ids = new ArrayList<>();
        for (EmployeeEntry entry : exceptThese) {
            ids.add(entry.getEmployeeID());
        }
        return ids;
    }
}
