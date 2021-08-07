package com.swiftdating.app.ui.createAccountScreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.swiftdating.app.model.FlexModel;
import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnItemClickListener;

public class FlexAdapter extends RecyclerView.Adapter<FlexAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<FlexModel> dataList;
    private OnItemClickListener onItemClickListener;
    int type = 0;

    public FlexAdapter(Context mContext, ArrayList<FlexModel> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_flex,
                parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        try {

            FlexModel data = dataList.get(position);


            holder.rb_item.setText(data.getName());
            holder.rb_item.setChecked(data.isChecked());

            holder.cl_item.setTag(position);
            holder.cl_item.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(pos);
            });

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
        private RadioButton rb_item;

        private MyViewHolder(View view) {
            super(view);
            cl_item = view.findViewById(R.id.cl_item);
            rb_item = view.findViewById(R.id.rb_item);
        }
    }
}
