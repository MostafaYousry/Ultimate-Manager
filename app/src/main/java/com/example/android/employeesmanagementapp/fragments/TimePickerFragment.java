package com.example.android.employeesmanagementapp.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
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
        cal.set(Calendar.HOUR, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        Date currentTime = cal.getTime();
        StringBuilder timeFormat = new StringBuilder();
        String midnight;
        if (hourOfDay == 0) {
            timeFormat.append(12);
            midnight = "AM";
        } else if (hourOfDay < 10) {
            timeFormat.append("0" + hourOfDay);
            midnight = "AM";
        } else if (hourOfDay >= 10 && hourOfDay < 12) {
            timeFormat.append(hourOfDay);
            midnight = "AM";
        }
        else if(hourOfDay == 12){
            timeFormat.append(hourOfDay);
            midnight = "PM";
        }
        else if (hourOfDay > 12 && hourOfDay < 22) {
            timeFormat.append("0" + (hourOfDay - 12));
            midnight = "PM";
        } else {
            timeFormat.append(hourOfDay - 12);
            midnight = "PM";
        }
        timeFormat.append(" : ");

        if (minute < 10)
            timeFormat.append("0" + minute);
        else
            timeFormat.append(minute);
        timeFormat.append(" " + midnight);

        textView.setText(timeFormat);
        textView.setTag(currentTime);
    }
}