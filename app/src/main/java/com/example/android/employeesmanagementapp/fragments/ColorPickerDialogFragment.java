package com.example.android.employeesmanagementapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.adapters.ColorAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.utils.ColorUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * dialog that shows a list of all available task colors
 * to allow the user to choose one
 */
public class ColorPickerDialogFragment extends DialogFragment implements ColorAdapter.OnColorSelectedListener {

    public static final String KEY_TASK_ID = "task_id";
    private int mTaskId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mTaskId = getArguments().getInt(KEY_TASK_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Task color");

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.color_dialog_view, null, false);

        RecyclerView mGrid = dialogView.findViewById(R.id.color_swatches_rv);
        mGrid.setLayoutManager(new GridLayoutManager(getActivity(), 4));


        ColorAdapter colorAdapter = new ColorAdapter(ColorUtils.getTaskColorResources(), this);
        mGrid.setAdapter(colorAdapter);

        builder.setCancelable(true);

        builder.setView(dialogView);

        return builder.create();
    }


    /**
     * when a color is selected update record of task in database
     *
     * @param colorRes
     */
    @Override
    public void onColorSelected(final int colorRes) {
        AppExecutor.getInstance().diskIO().execute(() -> AppDatabase.getInstance(getActivity()).tasksDao().updateTaskColor(colorRes, mTaskId));
        dismiss();
    }
}
