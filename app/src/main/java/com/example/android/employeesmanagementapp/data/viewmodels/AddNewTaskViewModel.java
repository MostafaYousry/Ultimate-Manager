package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class AddNewTaskViewModel extends ViewModel {
    public LiveData<TaskEntry> taskEntry;
    public LiveData<List<DepartmentEntry>> allDepartments;

    public LiveData<PagedList<EmployeeEntry>> taskEmployees;


    private AppDatabase mAppDatabase;
    private int mTaskId;

    public AddNewTaskViewModel(AppDatabase appDatabase, int taskId) {
        mAppDatabase = appDatabase;
        mTaskId = taskId;

        allDepartments = appDatabase.departmentsDao().loadDepartments();

        if (taskId != -1) {
            taskEntry = appDatabase.tasksDao().loadTaskById(taskId);

            taskEmployees = new LivePagedListBuilder<>(appDatabase.employeesTasksDao().getEmployeesForTask(taskId), /* page size */ 20).build();
        }

    }


    public LiveData<List<EmployeeEntry>> getRestOfEmployeesInDep(int selectedDepartmentId, List<EmployeeEntry> exceptThese) {

        LiveData<List<EmployeeEntry>> restOfEmployeesInDep;
        if (mTaskId == -1 && exceptThese == null)
            restOfEmployeesInDep = mAppDatabase.employeesDao().loadEmployeesInDep(selectedDepartmentId, new ArrayList<>());
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
