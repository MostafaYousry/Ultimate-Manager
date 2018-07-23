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
public class DepartmentsArrayAdapter extends ArrayAdapter<String> {

    private List<DepartmentEntry> mDepartmentEntryList;
    private HashMap<Integer, Integer> idToPositionHashMap;

    public DepartmentsArrayAdapter(Context context) {
        super(context, 0);
        idToPositionHashMap = new HashMap<>();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        TextView departmentName = (TextView) convertView;
        convertView.setTag(mDepartmentEntryList.get(position).getDepartmentId());
        departmentName.setText(mDepartmentEntryList.get(position).getDepartmentName());

        idToPositionHashMap.put(mDepartmentEntryList.get(position).getDepartmentId(), position);

        return convertView;
    }

    @Override
    public int getCount() {
        return mDepartmentEntryList.size();
    }


    @Override
    public String getItem(int position) {
        if (mDepartmentEntryList == null)
            return "";
        return mDepartmentEntryList.get(position).getDepartmentName();
    }





    public void setDepartmentEntryList(List<DepartmentEntry> departmentEntryList) {
        mDepartmentEntryList = departmentEntryList;
        notifyDataSetChanged();
    }


    /**
     * used when a department is already assigned to an existing task
     * its used to select the previously chosen department for that task
     *
     * @param oldTaskDepartmentId :
     * @return its position in the drop down list
     */
    public int getPositionForItemId(int oldTaskDepartmentId) {
        if (mDepartmentEntryList == null || idToPositionHashMap.size() == 0)
            return 0;
        return idToPositionHashMap.get(oldTaskDepartmentId);

    }

}
