package com.example.android.employeesmanagementapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * a dialog fragment that pops to allow user to choose a date
 * and displays that date in a given view id (TextView).
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_DISPLAY_VIEW_ID = "date_view_id";

    private View mViewToShowDateIn;
    private Calendar mCalendar;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey(KEY_DISPLAY_VIEW_ID))
                mViewToShowDateIn = getActivity().findViewById(bundle.getInt(KEY_DISPLAY_VIEW_ID));
        }

        if (mViewToShowDateIn.getTag() != null)
            mCalendar = (Calendar) mViewToShowDateIn.getTag();
        else {
            mCalendar = Calendar.getInstance();
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND,0);
        }


        return new DatePickerDialog(getActivity(), this, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView textView = (TextView) mViewToShowDateIn;

        Calendar calendar = (Calendar) mCalendar.clone();

        calendar.set(year, month, dayOfMonth);

        textView.setText(AppUtils.getFriendlyDate(calendar.getTime()));
        textView.setTag(calendar);
    }

}
