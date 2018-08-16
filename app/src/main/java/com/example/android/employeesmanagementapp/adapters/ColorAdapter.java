package com.example.android.employeesmanagementapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.employeesmanagementapp.R;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private int[] mColorResources;
    private OnColorSelectedListener mOnColorSelectedListener;


    public ColorAdapter(int[] colorResources, OnColorSelectedListener onColorSelectedListener) {
        mColorResources = colorResources;
        mOnColorSelectedListener = onColorSelectedListener;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View imageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_swatch, parent, false);

        return new ColorViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mColorResources.length;
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int colorRes);
    }

    class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView imageView;

        ColorViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView;
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            imageView.setImageResource(mColorResources[position]);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnColorSelectedListener.onColorSelected(mColorResources[position]);
        }
    }
}