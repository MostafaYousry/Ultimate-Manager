package com.example.android.employeesmanagementapp.data.factories;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewDepViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * factory used to supply view model
 * with extra parameters used in view model creation
 * it supplies AddNewDepViewModel with department id to
 * load any needed data related to this deparmtent
 */
public class DepIdFact extends ViewModelProvider.NewInstanceFactory {
    private int mDepId;
    private AppDatabase mDatabase;

    public DepIdFact(AppDatabase database, int depId) {

        mDatabase = database;
        mDepId = depId;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddNewDepViewModel(mDatabase, mDepId);

    }
}
