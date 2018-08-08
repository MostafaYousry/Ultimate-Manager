package com.example.android.employeesmanagementapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder> {
    private static final String TAG = DepartmentsAdapter.class.getSimpleName();

    private List<DepartmentEntry> mDepartments;
    private DepartmentItemClickListener mDepartmentItemClickListener;
    private View.OnClickListener mPopupMenuClickListener;
    private MultiTransformation mImageTransformations;
    private int clickedPosition=0;

    public DepartmentEntry getClickedDepartment(){
        return mDepartments.get(clickedPosition);
    }

    public DepartmentsAdapter(@NonNull DepartmentItemClickListener gridItemClickListener, @NonNull View.OnClickListener popupMenuClickListener) {
        mDepartmentItemClickListener = gridItemClickListener;
        mPopupMenuClickListener = popupMenuClickListener;
        mImageTransformations = new MultiTransformation(
                new RoundedCornersTransformation(6, 0, RoundedCornersTransformation.CornerType.TOP_LEFT),
                new RoundedCornersTransformation(6, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT));
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
        if (mDepartments == null)
            return 0;
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

    /**
     * interface to handle click events done on a recycler view item
     */
    public interface DepartmentItemClickListener {
        void onDepartmentClick(int departmentRowID, int departmentPosition);
    }

    public class DepartmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mItemView;
        public TextView mDepartmentName;
        public ImageView mDepartmentImage;

        public DepartmentsViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mDepartmentName = itemView.findViewById(R.id.item_department_name);
            mDepartmentImage = itemView.findViewById(R.id.department_image);
            itemView.findViewById(R.id.pop_up_menu).setOnClickListener(mPopupMenuClickListener);

            //todo move this to bind when loading different images
            Glide.with(itemView.getContext()).load(R.drawable.google)
                    .apply(bitmapTransform(mImageTransformations))
                    .into(mDepartmentImage);

            // set the item click listener
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mDepartmentName.setText(mDepartments.get(position).getDepartmentName());
            mItemView.setTag(mDepartments.get(position).getDepartmentId());
        }

        @Override
        public void onClick(View v) {
            mDepartmentItemClickListener.onDepartmentClick((int) mItemView.getTag(), getAdapterPosition());
            clickedPosition = getAdapterPosition();
        }

    }
}