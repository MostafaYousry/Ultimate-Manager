package com.example.android.employeesmanagementapp;

import android.text.Editable;
import android.text.TextWatcher;
import com.example.android.employeesmanagementapp.utils.AppUtils;

public class MyTextWatcher implements TextWatcher {
    private String mMainText;
    private boolean mIsChanged = false;

    public MyTextWatcher(String oldText) {
        this.mMainText = oldText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().equals(mMainText) && !mIsChanged) {
            AppUtils.setNumOfChangedFiled(1);
            mIsChanged = true;
        } else if (editable.toString().equals(mMainText) && mIsChanged) {
            AppUtils.setNumOfChangedFiled(-1);
            mIsChanged = false;
        }
    }
}
