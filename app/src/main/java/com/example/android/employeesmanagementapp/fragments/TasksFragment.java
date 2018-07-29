package com.example.android.employeesmanagementapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.RecyclerViewItemClickListener;
import com.example.android.employeesmanagementapp.UndoDeleteAction;
import com.example.android.employeesmanagementapp.activities.AddTaskActivity;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment implements RecyclerViewItemClickListener {

    private final String TAG = TasksFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;
    private AppDatabase mDb;
    private LinearLayout emptyView;
    private TextView emptyViewTextView;
    private Snackbar mSnackbar;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mDb = AppDatabase.getInstance(getContext());

        View view = inflater.inflate(R.layout.fragments_rv, container, false);

        emptyView = view.findViewById(R.id.empty_view);
        emptyViewTextView = view.findViewById(R.id.empty_view_message_text_view);

        // Inflate the layout for this fragment
        mRecyclerView = view.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new TasksAdapter(this);

        LiveData<List<TaskEntry>> tasksList = ViewModelProviders.of(this).get(MainViewModel.class).getTasksList();
        tasksList.observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> taskEntries) {
                mAdapter.setData(taskEntries);
                if (mAdapter.getItemCount() == 0)
                    showEmptyView();
                else
                    showRecyclerView();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        setFabActivation();
        setUpOnSwipe();

        return view;
    }

    private void setUpOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                int taskPosition = viewHolder.getAdapterPosition();
                TaskEntry taskEntry = mAdapter.getData().get(taskPosition);
                UndoDeleteAction mUndoDeleteAction = new UndoDeleteAction( null,taskEntry, getContext());
                Snackbar.make(getActivity().findViewById(android.R.id.content), taskEntry.getTaskTitle()+" will be deleted", Snackbar.LENGTH_LONG).setAction("Undo", mUndoDeleteAction).show();

                System.out.println("deleting");
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        mDb.tasksDao().deleteTask(mAdapter.getData().get(position));
                    }
                });

            }
        }).attachToRecyclerView(mRecyclerView);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSnackbar != null)
            mSnackbar.dismiss();
    }

    private void setFabActivation() {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final int depNum = mDb.departmentsDao().getNumDepartments();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (depNum == 0) {
                            getActivity().findViewById(R.id.fab).setEnabled(false);
                            mSnackbar = Snackbar.make(getView(), "please add department first", Snackbar.LENGTH_INDEFINITE);
                            mSnackbar.show();
                        } else {
                            getActivity().findViewById(R.id.fab).setEnabled(true);
                            if (mSnackbar != null)
                                mSnackbar.dismiss();
                        }

                    }
                });
            }
        });
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        emptyViewTextView.setText(R.string.task_empty_view_message);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }


    /**
     * called when a list item is clicked
     */
    @Override
    public void onItemClick(int clickedItemRowID, int clickedItemPosition) {

        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, clickedItemRowID);
        startActivity(intent);
    }

}

