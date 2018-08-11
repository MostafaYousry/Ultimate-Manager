package com.example.android.employeesmanagementapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * a dialog fragment that pops to allow user to choose a date
 * and displays that date in a given view id (TextView).
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_ALLOW_PAST_DATES = "allow_past_dates";
    public static final String KEY_DISPLAY_VIEW_ID = "date_view_id";

    private View mViewToShowDateIn;
    private boolean mAllowPastDates;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(KEY_DISPLAY_VIEW_ID))
                mViewToShowDateIn = getActivity().findViewById(bundle.getInt(KEY_DISPLAY_VIEW_ID));

            if (bundle.containsKey(KEY_ALLOW_PAST_DATES))
                mAllowPastDates = bundle.getBoolean(KEY_ALLOW_PAST_DATES);
        }


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
        TextView textView = (TextView) mViewToShowDateIn;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, dayOfMonth);
        Date currentDate = cal.getTime();
        textView.setText(AppUtils.getFriendlyDate(currentDate));
        textView.setTag(currentDate);
    }


    public class MyDatePickerDialog extends DatePickerDialog {

        private Date minDate;

        MyDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            Calendar cal = Calendar.getInstance();
            minDate = cal.getTime();
        }


        public void onDateChanged(final DatePicker view, int year, int month, int day) {

            if (mAllowPastDates)
                return;

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
