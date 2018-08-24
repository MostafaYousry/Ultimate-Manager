package com.example.android.employeesmanagementapp.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;

/**
 * a dialog fragment that allows user to pick a time
 * and displays that date in a given view id (TextView).
 */
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final String KEY_DISPLAY_VIEW_ID = "time_view_id";

    private View viewToShowTimeIn;
    private int viewId;
    private Calendar mCalendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(KEY_DISPLAY_VIEW_ID)) {
                viewId = bundle.getInt(KEY_DISPLAY_VIEW_ID);
                viewToShowTimeIn = getActivity().findViewById(viewId);
            }

        }

        //retrieve calendar object from date views
        if (viewId == R.id.task_start_date_time)
            mCalendar = (Calendar) getActivity().findViewById(R.id.task_start_date).getTag();
        else
            mCalendar = (Calendar) getActivity().findViewById(R.id.task_due_date).getTag();


        return new TimePickerDialog(getActivity(), this, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        TextView textView = (TextView) viewToShowTimeIn;
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        textView.setText(AppUtils.getFriendlyTime(mCalendar.getTime()));
    }
}