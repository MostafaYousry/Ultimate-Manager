package com.example.android.employeesmanagementapp.data.factories;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewTaskViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * factory used to supply view model
 * with extra parameters used in view model creation
 * it supplies AddNewTaskViewModel with task id to
 * load any needed data related to this task
 */
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
