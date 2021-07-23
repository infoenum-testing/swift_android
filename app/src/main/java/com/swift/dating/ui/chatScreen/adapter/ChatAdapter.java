package com.swift.dating.ui.chatScreen.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import com.swift.dating.R;
import com.swift.dating.callbacks.OnItemClickListener;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatModel> getDataChatLists;
    private LayoutInflater mInflater;
    private int check_view;
    private SharedPreference sp;
    private String imgUrl;
    OnItemClickListener clickListener;
    private HashMap<Integer, String> checkDate = new HashMap<>();

    public ChatAdapter(Activity mActivity, ArrayList<ChatModel> getDataChatLists, String imgUrl, OnItemClickListener clickListener) {
        mInflater = LayoutInflater.from(mActivity);
        this.getDataChatLists = getDataChatLists;
        this.imgUrl = imgUrl;
        generateIndexing();
        this.clickListener = clickListener;
        sp = new SharedPreference(mActivity);
    }

    public void updateReceiptsList(ArrayList<ChatModel> getDataChatLists) {
        this.getDataChatLists = getDataChatLists;
        generateIndexing();
        this.notifyDataSetChanged();
    }


    private void generateIndexing() {
        for (int i = 0; i < getDataChatLists.size(); i++) {
            if (i == 0) {
                checkDate.put(i, getDataChatLists.get(i).getDate_created());
            } else {
                String date1 = getDataChatLists.get(i).getDate_created().substring(0, 10);
                String date2 = getDataChatLists.get(i - 1).getDate_created().substring(0, 10);

                if (!date1.equalsIgnoreCase(date2)) {
                    checkDate.put(i, getDataChatLists.get(i).getDate_created());
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataChatLists.get(position).getFrom_id().equalsIgnoreCase(sp.getUserId()))
            check_view = 0;
        else
            check_view = 1;
        return position;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (check_view == 0)
            view = mInflater.inflate(R.layout.custom_item_chat_right, parent, false);
        else
            view = mInflater.inflate(R.layout.custom_item_chat_left, parent, false);

        return new FindHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        final FindHolder viewHolder = (FindHolder) holder;
        ChatModel getDataChatList = getDataChatLists.get(position);

        try {
            if (!getDataChatList.getMessage().equalsIgnoreCase("")) {
                viewHolder.msg.setText(getDataChatList.getMessage());
                CommonUtils.setImageUsingFresco(viewHolder.iv_picture,imgUrl,3);
            }

            if (!TextUtils.isEmpty(getDataChatList.getDate_created())) {
                if(getDataChatList.getDate_created().contains("T"))
                CommonUtils.setTimeInChat(viewHolder.post_time, CommonUtils.deviceTime(getDataChatList.getDate_created().replace("T"," ").split("\\.")[0]));
                else{
                    CommonUtils.setTimeInChat(viewHolder.post_time, CommonUtils.deviceTime(getDataChatList.getDate_created()));
                }
                if (checkDate.containsKey(position)) {
                    Log.e("value", "is " + checkDate.get(position));
                    if(getDataChatList.getDate_created().contains("T"))
                        CommonUtils.setDateInChat(viewHolder.msg_date, checkDate.get(position).split("\\.")[0].replace("T"," "));
                    else{
                        CommonUtils.setDateInChat(viewHolder.msg_date, checkDate.get(position));
                    }
                    viewHolder.msg_date.setVisibility(View.VISIBLE);
                } else
                    viewHolder.msg_date.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnItemClick((int)viewHolder.iv_picture.getTag());
            }
        });
    }



    @Override
    public int getItemCount() {
        if (getDataChatLists != null) {
            return getDataChatLists.size();
        } else {
            return 0;
        }
    }

    public static class FindHolder extends RecyclerView.ViewHolder {

        TextView msg, msg_date, post_time;
        SimpleDraweeView iv_picture;
        FindHolder(View v) {
            super(v);
            msg =  v.findViewById(R.id.tv_message);
            post_time =  v.findViewById(R.id.tv_time);
            msg_date = v.findViewById(R.id.msg_date);
            iv_picture = v.findViewById(R.id.iv_picture);
            iv_picture.setTag(getAdapterPosition());
        }
    }
}
