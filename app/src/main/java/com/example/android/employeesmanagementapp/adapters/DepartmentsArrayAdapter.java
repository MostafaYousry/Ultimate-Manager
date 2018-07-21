package com.example.android.employeesmanagementapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.HashMap;
import java.util.List;

/**
 * array adapter for spinners and autocomplete text views
 */
public class DepartmentsArrayAdapter extends ArrayAdapter<DepartmentEntry> {
    private List<DepartmentEntry> mDepartmentEntryList;
    private HashMap<Integer, Integer> idToPositionHashMap;

    public DepartmentsArrayAdapter(Context context) {
        super(context, 0);
        idToPositionHashMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mDepartmentEntryList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        TextView departmentName = (TextView) convertView;
        departmentName.setTag(mDepartmentEntryList.get(position).getDepartmentId());
        departmentName.setText(mDepartmentEntryList.get(position).getDepartmentName());

        idToPositionHashMap.put(mDepartmentEntryList.get(position).getDepartmentId(), position);

        return convertView;
    }

    public void setDepartmentEntryList(List<DepartmentEntry> departmentEntryList) {
        mDepartmentEntryList = departmentEntryList;
        notifyDataSetChanged();
    }


    @Override
    public DepartmentEntry getItem(int position) {
        return mDepartmentEntryList.get(position);
    }

    public int getPositionForItemId(int selectedDepartmentId) {

        return idToPositionHashMap.get(selectedDepartmentId);

    }

}
