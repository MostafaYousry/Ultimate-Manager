package com.example.android.employeesmanagementapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BottomNavigationFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {


    public BottomNavigationFabBehavior() {
    }

    public BottomNavigationFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return updateFab(child, dependency);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        child.setTranslationY(0f);
    }


    private boolean updateFab(View child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            float oldTranslation = child.getTranslationY();
            float height = dependency.getHeight();
            float newTranslation = dependency.getTranslationY() - height;
            child.setTranslationY(newTranslation);

            return oldTranslation != newTranslation;
        }
        return false;
    }

}
