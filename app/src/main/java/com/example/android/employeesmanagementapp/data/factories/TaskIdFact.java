package com.example.android.employeesmanagementapp.data.factories;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewTaskViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TaskIdFact extends ViewModelProvider.NewInstanceFactory {
    private int mTaskId;
    private AppDatabase mDatabase;

    public TaskIdFact(AppDatabase database, int taskId) {

        mDatabase = database;
        mTaskId = taskId;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddNewTaskViewModel(mDatabase, mTaskId);
    }
}
