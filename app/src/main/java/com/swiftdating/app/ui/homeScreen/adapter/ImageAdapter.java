package com.swiftdating.app.ui.homeScreen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collections;
import java.util.List;

import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.common.ScreenUtils;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.model.ImageModel;
import com.swiftdating.app.ui.base.SwipeAndDragHelper;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> implements SwipeAndDragHelper.ActionCompletionContract {
    private static final String TAG = "ImageAdapter";
    private Context mContext;
    private List<ImageModel> matchList;
    private OnClickListener onClickListener;

    public ImageAdapter(Context mContext, List<ImageModel> matchList, OnClickListener onClickListener) {
        this.mContext = mContext;
        this.matchList = matchList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_edit_profile,
                parent, false);
        return new ImageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.iv_photo2.setMinimumHeight((ScreenUtils.getScreenWidth(mContext) / 3) - 20);
        holder.iv_photo2.setMinimumWidth((ScreenUtils.getScreenWidth(mContext) / 3) - 20);
        holder.iv_photo2.setMaxHeight((ScreenUtils.getScreenWidth(mContext) / 3) - 20);
        holder.iv_photo2.setMaxWidth((ScreenUtils.getScreenWidth(mContext) / 3) - 20);

        if (matchList.size() > position) {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivAdd.setVisibility(View.GONE);
            if (matchList.size() < 2) {
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivEdit.setVisibility(View.VISIBLE);
            } else {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivEdit.setVisibility(View.GONE);
            }


            CommonUtils.setImageUsingFresco(holder.ivPhoto, CallServer.BaseImage + matchList.get(position).getImageUrl(), 3);
        }
        if (matchList.size() < position) {
            holder.ivPhoto.setOnDragListener(null);
            holder.ivPhoto.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public void onViewMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            if (toPosition < matchList.size())
                for (int i = fromPosition; i < toPosition; i++) {
                    if (toPosition < matchList.size())
                        Collections.swap(matchList, i, i + 1);
                }
        } else {
            if (fromPosition < matchList.size())
                for (int i = fromPosition; i > toPosition; i--) {
                    if (toPosition < matchList.size())
                        Collections.swap(matchList, i, i - 1);
                }
        }
        Log.e(TAG, "onViewMoved: " + fromPosition + "  " + toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onViewSwiped(int position) {

    }

    @Override
    public void onMoveComplete(int fromPosition, int toPosition) {
        Log.e(TAG, "onMoveComplete: " + fromPosition + "  " + toPosition);
        notifyDataSetChanged();
        onClickListener.onItemMoved(matchList);
    }


    public interface OnClickListener {
        void onItemClick(int pos);

        void onDeleteClick(int pos);

        void onReplaceClick(int pos);

        void onItemMoved(List<ImageModel> list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivDelete, ivAdd, ivEdit;
        SimpleDraweeView ivPhoto;
        ConstraintLayout clView;
        ImageView iv_photo2;
        View rowView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            ivDelete = itemView.findViewById(R.id.iv_deletePhoto);
            ivAdd = itemView.findViewById(R.id.iv_addPhoto);
            ivEdit = itemView.findViewById(R.id.iv_editPhoto);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            iv_photo2 = itemView.findViewById(R.id.iv_photo2);
            clView = itemView.findViewById(R.id.clView);

            ivAdd.setOnClickListener(this);
            ivDelete.setOnClickListener(v -> onClickListener.onDeleteClick(getAdapterPosition()));
            ivEdit.setOnClickListener(v -> onClickListener.onReplaceClick(getAdapterPosition()));
            ivPhoto.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_addPhoto || v.getId() == R.id.iv_photo)
                onClickListener.onItemClick(getAdapterPosition());
        }
    }


}
