package com.example.android.employeesmanagementapp.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final String KEY_DISPLAY_VIEW_ID = "time_view_id";
    public static final String KEY_DISPLAY_TIME = "time to be displayed";

    private View viewToShowTimeIn;
    private Long mTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle bundle = getArguments();


        if (bundle != null) {
            if (bundle.containsKey(KEY_DISPLAY_VIEW_ID))
                viewToShowTimeIn = getActivity().findViewById(bundle.getInt(KEY_DISPLAY_VIEW_ID));
            if (bundle.containsKey(KEY_DISPLAY_TIME))
                mTime = bundle.getLong(KEY_DISPLAY_TIME);
        }


        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
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