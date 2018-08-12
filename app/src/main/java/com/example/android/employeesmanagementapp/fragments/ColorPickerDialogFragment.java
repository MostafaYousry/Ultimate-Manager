package com.example.android.employeesmanagementapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.ColorAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ColorPickerDialogFragment extends DialogFragment implements ColorAdapter.OnColorSelectedListener {
    public static final String KEY_TASK_ID = "task_id";
    private RecyclerView mGrid;
    private int mTaskId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mTaskId = getArguments().getInt(KEY_TASK_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Task color");

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.color_dialog_view, null, false);

        mGrid = dialogView.findViewById(R.id.color_swatches_rv);
        mGrid.setLayoutManager(new GridLayoutManager(getActivity(), 4));


        ColorAdapter colorAdapter = new ColorAdapter(AppUtils.getColorResources(), this);
        mGrid.setAdapter(colorAdapter);

        builder.setCancelable(true);

        builder.setView(dialogView);

        return builder.create();
    }


    @Override
    public void onColorSelected(final int colorRes) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getActivity()).tasksDao().updateTaskColor(colorRes, mTaskId);
            }
        });
        dismiss();
    }
}
