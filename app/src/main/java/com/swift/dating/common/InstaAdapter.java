package com.swift.dating.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.swift.dating.R;
import com.swift.dating.model.responsemodel.InstagramImageModel;
import com.swift.dating.ui.viewpagerScreen.ViewPagerActivity;

public class InstaAdapter extends RecyclerView.Adapter<InstaAdapter.ViewHolder> {
    Context mContext;
    List<InstagramImageModel.Datum> imageList = new ArrayList<>();
    int page;

    public InstaAdapter(Context context, List<InstagramImageModel.Datum> instaList, int page) {
        mContext = context;
        imageList = instaList;
        this.page = page;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.insta_photo_grid_item,
                parent, false);
        return new InstaAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("Test", position + " ");
        if (imageList != null && imageList.size() > 0 && imageList.size() > ((page * 6) + position) &&
                imageList.get((page * 6) + position).getMediaUrl() != null)
            Glide.with(mContext).load(Uri.parse(imageList.get((page * 6) + position).getMediaUrl())).centerCrop().into(holder.ivInstaPic);

        holder.ivInstaPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewPagerActivity.class);
                intent.putExtra("images", new Gson().toJson(imageList));
                intent.putExtra("position", (page * 6) + position);
                intent.putExtra("isInstaImage", true);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivInstaPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivInstaPic = itemView.findViewById(R.id.ivInstaPic);
        }
    }
}
