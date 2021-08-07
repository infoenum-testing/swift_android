package com.swiftdating.app.ui.addImageScreen.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;

import com.swiftdating.app.R;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyHolder> {

    private ArrayList<String> listImage;
    private LayoutInflater mInflater;
    private OnClickListener onClickListener;

    public PhotoAdapter(Context context, ArrayList<String> list, OnClickListener onClickListener) {
        this.listImage = list;
        mInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_add_photos, parent, false);
        PhotoAdapter.MyHolder holder = new PhotoAdapter.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if (listImage.size() > position) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivAdd.setVisibility(View.GONE);
            holder.ivPhoto.setImageURI(Uri.fromFile(new File(listImage.get(position))));
        }

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivDelete, ivAdd;
        SimpleDraweeView ivPhoto;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            ivDelete = itemView.findViewById(R.id.iv_deletePhoto);
            ivAdd = itemView.findViewById(R.id.iv_addPhoto);
            ivAdd.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_addPhoto)
                onClickListener.onItemClick(getAdapterPosition());
            else if (v.getId() == R.id.iv_deletePhoto) {
                onClickListener.onDeleteClick(getAdapterPosition());
            }
        }
    }

    public interface OnClickListener {
        void onItemClick(int pos);

        void onDeleteClick(int pos);
    }
}