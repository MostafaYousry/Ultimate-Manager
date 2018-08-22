package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * array adapter for spinners
 * to display department names in a dropdown list
 */
public class DepartmentsArrayAdapter extends ArrayAdapter<String> {

    private List<DepartmentEntry> mDepartmentEntryList;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom, mStyle;

    public DepartmentsArrayAdapter(Context context, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom, int style) {
        super(context, 0);
        mPaddingLeft = paddingLeft;
        mPaddingTop = paddingTop;
        mPaddingRight = paddingRight;
        mPaddingBottom = paddingBottom;
        mStyle = style;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return createView(position, convertView, parent);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }


    private View createView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
        if (convertView == null) {
            TextView textView = new TextView(getContext());
            //apply style to the text view item
            if (Build.VERSION.SDK_INT < 23) {
                textView.setTextAppearance(getContext(), mStyle);
            } else {
                textView.setTextAppearance(mStyle);
            }
            //apply padding to the text as needed
            textView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            convertView = textView;
        }


        convertView.setTag(mDepartmentEntryList.get(position).getDepartmentId());
        ((TextView) convertView).setText(mDepartmentEntryList.get(position).getDepartmentName());

        return convertView;
    }

    @Override
    public int getCount() {
        if (mDepartmentEntryList == null)
            return 0;
        return mDepartmentEntryList.size();
    }


    @Override
    public String getItem(int position) {
        if (mDepartmentEntryList == null)
            return "";
        return mDepartmentEntryList.get(position).getDepartmentName();
    }


    /**
     * used when a department is already assigned to an existing taskEntry
     * its used to select the previously chosen department for that taskEntry
     *
     * @param depId: department id
     * @return its position in the drop down list
     */
    public int getPositionForItemId(int depId) {
        if (mDepartmentEntryList == null)
            return 0;

        //using binary search since list is sorted by ids
        return Collections.binarySearch(mDepartmentEntryList, new DepartmentEntry(depId), (e1, e2)
                -> Integer.compare(e1.getDepartmentId(), e2.getDepartmentId()));
    }

    public void setData(List<DepartmentEntry> departmentEntryList) {
        mDepartmentEntryList = departmentEntryList;
        notifyDataSetChanged();
    }


    public int getDepId(int position) {
        return mDepartmentEntryList.get(position).getDepartmentId();
    }
}
