package com.example.android.employeesmanagementapp.data.factories;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewTaskViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DepIdFact extends ViewModelProvider.NewInstanceFactory {
    private int mDepId;
    private AppDatabase mDatabase;

    public DepIdFact(AppDatabase database, int depId) {

        mDatabase = database;
        mDepId = depId;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.getName().equals(AddNewTaskViewModel.class.getName()))
            return (T) new AddNewTaskViewModel(mDatabase, mDepId);
        else
            return (T) new AddNewDepViewModel(mDatabase, mDepId);

    }
}
