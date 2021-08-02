package com.swift.dating.ui.settingScreen;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swift.dating.R;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderHolder> {
    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SliderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderHolder(LayoutInflater.from(context).inflate(R.layout.slider_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderHolder holder, int position) {
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.arr_premium_images);
        TypedArray title = context.getResources().obtainTypedArray(R.array.arr_premium_txt);
        holder.image.setImageResource(imgs.getResourceId(position, -1));
//        holder.tvTitle.setText(title.getResourceId(position, -1));
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    static class SliderHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvTitle;

        public SliderHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
