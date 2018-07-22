package com.example.android.employeesmanagementapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * defines behaviour of the bottom navigation view in
 * coordinator layout
 * <p>
 * handles translating off and to the screen when scrolling.
 * handles displaying the snack bar above the navigation view rather than on top of it.
 */
public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {
    private static final String TAG = BottomNavigationBehavior.class.getSimpleName();


    public BottomNavigationBehavior() {
    }

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);


        //clamping function  0f < child's translation y < child's height
        child.setTranslationY(Math.max(0f, Math.min(child.getHeight(), child.getTranslationY() + dy)));


    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            updateSnackbar(child, (Snackbar.SnackbarLayout) dependency);
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    private void updateSnackbar(View child, Snackbar.SnackbarLayout snackbarLayout) {

        if (snackbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbarLayout.getLayoutParams();

            params.setAnchorId(child.getId());
            params.anchorGravity = Gravity.TOP;
            params.gravity = Gravity.TOP;
            snackbarLayout.setLayoutParams(params);
        }
    }

}