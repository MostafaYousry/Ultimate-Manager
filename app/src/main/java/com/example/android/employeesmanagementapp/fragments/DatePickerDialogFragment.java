package com.example.android.employeesmanagementapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_DISPLAY_VIEW_ID = "date_view_id";

    private View mViewToShowDateIn;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(KEY_DISPLAY_VIEW_ID))
                mViewToShowDateIn = getActivity().findViewById(bundle.getInt(KEY_DISPLAY_VIEW_ID));

        }


        // populate with current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instancMyDatePickerDialoge of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
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

}
