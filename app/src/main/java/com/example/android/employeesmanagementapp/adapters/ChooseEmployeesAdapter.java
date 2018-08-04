package com.example.android.employeesmanagementapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.AddNewTaskViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseEmployeesAdapter extends RecyclerView.Adapter<ChooseEmployeesAdapter.chooseEmployeeViewHolder> {
    private static ChooseEmployeesAdapter sChooseEmployeesAdapter;
    private List<EmployeeEntry> mEmployeesInDepNotTask;
    private List<EmployeeEntry> chosenEmployees;

    private ChooseEmployeesAdapter() {
    }

    private ChooseEmployeesAdapter(AddNewTaskViewModel viewModel, int depId, int taskId, final LifecycleOwner owner) {
        final LiveData<List<EmployeeEntry>> employees = viewModel.getRestOfEmployeesInDep(depId, taskId);
        employees.observe(owner, new Observer<List<EmployeeEntry>>() {
            @Override
            public void onChanged(List<EmployeeEntry> employeeEntries) {
                employees.removeObservers(owner);
                mEmployeesInDepNotTask = employeeEntries;
            }
        });

        chosenEmployees = new ArrayList<>();
    }

    public static ChooseEmployeesAdapter getInstance(AddNewTaskViewModel viewModel, int depId, int taskId, LifecycleOwner owner) {
        if (sChooseEmployeesAdapter == null) {
            sChooseEmployeesAdapter = new ChooseEmployeesAdapter(viewModel, depId, taskId, owner);
        }

        return sChooseEmployeesAdapter;
    }

    @NonNull
    @Override
    public chooseEmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_employee_item, parent, false);
        chooseEmployeeViewHolder chooseEmployeeViewHolder = new chooseEmployeeViewHolder(rootView);
        return chooseEmployeeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull chooseEmployeeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mEmployeesInDepNotTask == null)
            return 0;
        return mEmployeesInDepNotTask.size();
    }

    public List<EmployeeEntry> getChosenEmployees() {
        return chosenEmployees;
    }

    public void clearChosenEmployees() {
        chosenEmployees.clear();
    }

    public void removeChosenEmployees() {

        System.out.println("Removing chosen Employees");
        mEmployeesInDepNotTask.removeAll(chosenEmployees);
        System.out.println("after removing size = " + mEmployeesInDepNotTask.size());
        notifyDataSetChanged();
    }

    public class chooseEmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView employeeName;
        CheckBox employeeCheckBox;

        public chooseEmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.choose_employee_name);
            employeeCheckBox = itemView.findViewById(R.id.choose_employee_check_box);
            employeeCheckBox.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            employeeName.setText(mEmployeesInDepNotTask.get(position).getEmployeeName());

            if (chosenEmployees.contains(mEmployeesInDepNotTask.get(position))) {
                employeeCheckBox.setChecked(true);
            } else {
                employeeCheckBox.setChecked(false);
            }

        }

        @Override
        public void onClick(View view) {
            EmployeeEntry entry = mEmployeesInDepNotTask.get(getAdapterPosition());
            System.out.println(mEmployeesInDepNotTask.get(getAdapterPosition()).getEmployeeName());
            if (chosenEmployees.contains(entry)) {
                chosenEmployees.remove(entry);
                employeeCheckBox.setChecked(false);
            } else {
                chosenEmployees.add(entry);
                employeeCheckBox.setChecked(true);
            }
        }
    }
}


