package com.example.android.employeesmanagementapp;

/**
 * interface to handle click events done on a recycler view item
 */
public interface RecyclerViewItemClickListener {
    void onItemClick(int clickedItemRowID, int clickedItemRowPostition);

    boolean onItemLongCLick(int longClickedItemRowId, int longcClickedItemPostition);
}
