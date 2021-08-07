package com.swiftdating.app.ui.homeScreen.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.swiftdating.app.R;
import com.swiftdating.app.model.DetailTagModel;

public class DetailTagAdapter extends RecyclerView.Adapter<DetailTagAdapter.MyViewHolder> {

    private Context mContext;
    private List<DetailTagModel> dataList;

    public DetailTagAdapter(Context mContext, List<DetailTagModel> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @Override
    public DetailTagAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_tags,
                parent, false);
        return new DetailTagAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        try {

            DetailTagModel data = dataList.get(position);
            if (!TextUtils.isEmpty(data.getName())) {
                holder.cl_item.setVisibility(View.VISIBLE);
                holder.rb_item.setText(data.getName());
                holder.ivView.setImageDrawable(mContext.getResources().getDrawable(data.getImage()));
            } else {
                holder.cl_item.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                holder.cl_item.setMaxWidth(0);
                holder.rb_item.setText("");
                holder.ivView.setImageDrawable(null);
                holder.cl_item.setMaxHeight(0);
                holder.cl_item.setMinHeight(0);
                holder.cl_item.setMinWidth(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout cl_item;
        private TextView rb_item;
        ImageView ivView;


        private MyViewHolder(View view) {
            super(view);
            cl_item = view.findViewById(R.id.cl_item);
            rb_item = view.findViewById(R.id.rb_item);
            ivView = view.findViewById(R.id.ivView);
        }
    }
}

