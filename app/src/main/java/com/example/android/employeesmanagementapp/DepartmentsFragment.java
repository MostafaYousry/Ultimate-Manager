package com.example.android.employeesmanagementapp;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.utils.AppUtils;


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

}
