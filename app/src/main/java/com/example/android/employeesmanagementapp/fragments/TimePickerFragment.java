package com.example.android.employeesmanagementapp.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private View viewToShowTimeIn;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("time_view_id"))
            viewToShowTimeIn = getActivity().findViewById(bundle.getInt("time_view_id"));

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        TextView textView = (TextView) viewToShowTimeIn;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        Date currentTime = cal.getTime();
        textView.setText(AppUtils.getFriendlyTime(currentTime));
        textView.setTag(currentTime);
    }
}