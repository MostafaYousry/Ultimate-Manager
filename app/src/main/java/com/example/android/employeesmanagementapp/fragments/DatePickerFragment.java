package com.example.android.employeesmanagementapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * a dialog fragment that pops to allow user to choose a date
 * and displays that date in a given view id (TextView).
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private View viewToShowDateIn;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey("date_view_id"))
            viewToShowDateIn = getActivity().findViewById(bundle.getInt("date_view_id"));

        // populate with current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new MyDatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView textView = (TextView) viewToShowDateIn;
        textView.setText(dayOfMonth + "/" + month + "/" + year);
    }


    public class MyDatePickerDialog extends DatePickerDialog {

        private Date minDate;

        MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            Calendar cal = Calendar.getInstance();
            minDate = cal.getTime();
        }


        public void onDateChanged(final DatePicker view, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            Date currentDate = cal.getTime();

            //prevent user from entering past date
            if (!minDate.before(currentDate)) {
                cal.setTime(minDate);
                view.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            }
        }


    }

}
