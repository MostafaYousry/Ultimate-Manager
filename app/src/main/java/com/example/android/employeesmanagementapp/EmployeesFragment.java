package com.example.android.employeesmanagementapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeesFragment extends Fragment implements RecyclerViewItemClickListener{

    private static final String TAG = EmployeesFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private EmployeesAdapter mEmployeesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

        // method to setup employees recycler view
        setupRecyclerView(rootView);

        return rootView;
    }

    private void setupRecyclerView(View rootView) {

        //access the employee recycler view
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //set the employee recycler view layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //create object of EmployeesAdapter and send data
        mEmployeesAdapter = new EmployeesAdapter(AppUtils.getEmployeesFakeData(), this);

        //set the employee recycler view adapter
        mRecyclerView.setAdapter(mEmployeesAdapter);
    }



    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemIndex) {
        //todo:open employee details

        Log.d(TAG,"Item at index " + clickedItemIndex + " is clicked");
        Snackbar.make(getView(), "Item at index " + clickedItemIndex + " is clicked", Snackbar.LENGTH_SHORT)
                .show();
    }


    public static class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeesViewHolder> {
        final private RecyclerViewItemClickListener mClickListener;
        private List<EmployeeEntry> mData;

        EmployeesAdapter(List<EmployeeEntry> data, RecyclerViewItemClickListener listener) {
            mData = data;
            mClickListener = listener;
        }

        @NonNull
        @Override
        public EmployeesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //inflate item layout for the view holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employees_rv, parent, false);

            EmployeesViewHolder employeesViewHolder = new EmployeesViewHolder(v);

            return employeesViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeesViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public class EmployeesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //create object for each view in the item view
            TextView employeeName;
            ImageView employeeImage;

            EmployeesViewHolder(View itemView) {
                super(itemView);

                //set the objects by the opposite view by id
                employeeName = itemView.findViewById(R.id.employee_name);
                employeeImage = itemView.findViewById(R.id.employee_image);

                // set the item click listener
                itemView.setOnClickListener(this);
            }

            void bind(int position) {

                //change the item data by the position
                employeeName.setText(mData.get(position).getEmployeeName());
                employeeImage.setImageResource(AppUtils.getRandomEmployeeImage());
            }

            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}