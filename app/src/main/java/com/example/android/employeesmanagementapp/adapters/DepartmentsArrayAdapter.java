package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * array adapter for spinners and autocomplete text views
 */
public class DepartmentsArrayAdapter extends ArrayAdapter<String> {

    private List<DepartmentEntry> mDepartmentEntryList;


    public DepartmentsArrayAdapter(Context context) {

        super(context, 0);
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
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        TextView departmentName = (TextView) convertView;
        convertView.setTag(mDepartmentEntryList.get(position).getDepartmentId());
        departmentName.setText(mDepartmentEntryList.get(position).getDepartmentName());

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
     * used when a department is already assigned to an existing task
     * its used to select the previously chosen department for that task
     *
     * @param depId:
     * @return its position in the drop down list
     */
    public int getPositionForItemId(int depId) {
        if (mDepartmentEntryList == null)
            return 0;


        return mDepartmentEntryList.indexOf(new DepartmentEntry(depId));

    }

    public void setData(List<DepartmentEntry> departmentEntryList) {
        mDepartmentEntryList = departmentEntryList;
        notifyDataSetChanged();
    }
}
