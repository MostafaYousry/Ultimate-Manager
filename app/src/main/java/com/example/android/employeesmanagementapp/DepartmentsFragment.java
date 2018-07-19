package com.example.android.employeesmanagementapp;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment implements RecyclerViewItemClickListener {
    private final String TAG = DepartmentsFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private DepartmentsAdapter mAdapter;

    public DepartmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragments_rv, container, false);

        //get recycler view
        mRecyclerView = rootView.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //initialise recycler view adapter
        mAdapter = new DepartmentsAdapter(AppUtils.getDepartmentsFakeData(), this);
        mRecyclerView.setAdapter(mAdapter);

        //hooking recycler view with grid layout manager (2 columns)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //adding spacing between grid items
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(8)));

        //applying default rv animations
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;

    }


    /**
     * called when a grid item is clicked
     */
    @Override
    public void onItemClick(int clickedItemIndex) {
        //todo: open department detail activity
        Log.d(TAG, "Item at index " + clickedItemIndex + " is clicked");

        Snackbar.make(getView(), "Item at index " + clickedItemIndex + " is clicked", Snackbar.LENGTH_SHORT)
                .show();
    }

    /**
     * Converts dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        public GridSpacingItemDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position


            if (position % 2 == 0) {
                //if left row
                outRect.left = spacing;
                outRect.right = spacing / 2;
            } else {
                //if right row
                outRect.right = spacing;
                outRect.left = spacing / 2;
            }

            //if top row
            if (position == 0 || position == 1) {
                outRect.top = spacing;
            }


            outRect.bottom = spacing;

        }
    }

    public static class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder> {

        private List<DepartmentEntry> mDepartments;
        private RecyclerViewItemClickListener mGridItemClickListener;


        public DepartmentsAdapter(List<DepartmentEntry> data, RecyclerViewItemClickListener gridItemClickListener) {
            mDepartments = data;
            mGridItemClickListener = gridItemClickListener;
        }

        @NonNull
        @Override
        public DepartmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_departments_rv, parent, false);
            DepartmentsViewHolder departmentsViewHolder = new DepartmentsViewHolder(rootView);

            return departmentsViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DepartmentsViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mDepartments.size();
        }

        /**
         * used to update adapters data if any change occurs
         *
         * @param departments new departments list
         */
        public void setData(List<DepartmentEntry> departments) {
            mDepartments = departments;
            notifyDataSetChanged();
        }

        public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public View mItemView;
            public TextView mDepartmentName;

            public DepartmentsViewHolder(View itemView) {
                super(itemView);
                mItemView = itemView;
                mDepartmentName = itemView.findViewById(R.id.item_department_name);
                mItemView.setOnClickListener(this);
            }

            public void bind(int position) {
                mDepartmentName.setText(mDepartments.get(position).getDepartmentName());
            }

            @Override
            public void onClick(View v) {
                int clickedItemIndex = getAdapterPosition();
                mGridItemClickListener.onItemClick(clickedItemIndex);
            }
        }
    }
}
