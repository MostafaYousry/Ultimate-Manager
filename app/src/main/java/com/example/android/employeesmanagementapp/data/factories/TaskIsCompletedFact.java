package com.example.android.employeesmanagementapp.data.factories;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TaskIsCompletedFact extends ViewModelProvider.NewInstanceFactory {
    private AppDatabase mDatabase;
    private boolean mTaskIsCompleted;

    public TaskIsCompletedFact(AppDatabase database, boolean taskIsCompleted) {

        mDatabase = database;
        mTaskIsCompleted = taskIsCompleted;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainViewModel(mDatabase, mTaskIsCompleted);
    }
}
