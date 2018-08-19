package com.example.android.employeesmanagementapp.fragments;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.activities.AddDepartmentActivity;
import com.example.android.employeesmanagementapp.adapters.DepartmentsAdapter;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.example.android.employeesmanagementapp.utils.AppUtils;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment implements DepartmentsAdapter.DepartmentItemClickListener {

    private final String TAG = DepartmentsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private DepartmentsAdapter mAdapter;
    private LinearLayout mEmptyView;
    private TextView mEmptyViewTextView;
    private ImageView mEmptyViewImageView;


    public DepartmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //enable add departments button
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.fab).setEnabled(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

        //get views
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);
        mEmptyView = rootView.findViewById(R.id.empty_view);
        mEmptyViewTextView = rootView.findViewById(R.id.empty_view_message_text_view);
        mEmptyViewImageView = rootView.findViewById(R.id.empty_view_message_image_view);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //initialise recycler view adapter
        mAdapter = new DepartmentsAdapter(getContext(), this);

        ViewModelProviders.of(getActivity()).get(MainViewModel.class)
                .allDepartmentsWithExtrasList
                .observe(this, new Observer<PagedList<DepartmentWithExtras>>() {
                    @Override
                    public void onChanged(PagedList<DepartmentWithExtras> departmentWithExtras) {
                        if (departmentWithExtras != null)
                            if (departmentWithExtras.isEmpty())
                                showEmptyView();
                            else {
                                mAdapter.submitList(departmentWithExtras);
                                showRecyclerView();
                            }
                    }
                });


        mRecyclerView.setAdapter(mAdapter);

        //hooking recycler view with grid layout manager (2 columns)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //adding spacing between grid items
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration());

        //applying default rv animations
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        return rootView;

    }


    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyViewTextView.setText(R.string.department_empty_view_message);
        mEmptyViewImageView.setImageResource(R.drawable.ic_no_dep);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onDepartmentClick(int departmentRowID, int departmentPosition) {
        Intent intent = new Intent(getActivity(), AddDepartmentActivity.class);
        intent.putExtra(AddDepartmentActivity.DEPARTMENT_ID_KEY, departmentRowID);
        startActivity(intent);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spacingLeftRight = AppUtils.dpToPx(getContext(), 3);
        private int spacingCenter = AppUtils.dpToPx(getContext(), 5);
        private int spacingTopBottom = AppUtils.dpToPx(getContext(), 8);

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position


            if (position % 2 == 0) {
                //if left row
                outRect.left = spacingLeftRight;
                outRect.right = spacingCenter / 2;
            } else {
                //if right row
                outRect.right = spacingLeftRight;
                outRect.left = spacingCenter / 2;
            }

            //if top row
            if (position == 0 || position == 1) {
                outRect.top = spacingTopBottom;
            }


            outRect.bottom = spacingTopBottom;

        }
    }


}
