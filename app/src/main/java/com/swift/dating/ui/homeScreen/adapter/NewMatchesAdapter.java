package com.swift.dating.ui.homeScreen.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.swift.dating.R;
import com.swift.dating.callbacks.OnItemClickListenerType;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.data.network.CallServer;
import com.swift.dating.model.responsemodel.MatchListResponseModel;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.swift.dating.common.AppConstants.NEW_MATCHES;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MatchListResponseModel.Match> matchList;
    private OnItemClickListenerType onItemClickListener;

    public NewMatchesAdapter(Context mContext, ArrayList<MatchListResponseModel.Match> matchList) {
        this.matchList = matchList;
        this.mContext = mContext;

    }

    public void setOnItemClickListener(OnItemClickListenerType onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_new_matches, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            MatchListResponseModel.Match data = matchList.get(position);
            Glide.with(holder.sdv_picture).load(CallServer.BaseImage + data.getImageForUserImageUrl()).into(holder.sdv_picture);
            holder.tv_name.setText(data.getProfileOfUserName());
            long expire = CommonUtils.stringToDate(data.getMatchForUserCalltimerExpiry().replace("T", " ").split("\\.")[0]);
            long server = CommonUtils.stringToDate(data.getMatchForUserServerTime().replace("T", " ").split("\\.")[0]);
            long created = CommonUtils.stringToDate(data.getMatchForUserCreatedAt().replace("T", " ").split("\\.")[0]);
            if (data.getTimetokenAppliedOn() != null) {
                created = CommonUtils.stringToDate(data.getTimetokenAppliedOn().replace("T", " ").split("\\.")[0]);
            }
            holder.pb_time_left.setProgress((int) ((expire - server) * 100 / (expire - created)));
            if (server >= expire) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.sdv_picture.setForeground(mContext.getDrawable(R.drawable.transblack));
                }
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.sdv_picture.setForeground(mContext.getDrawable(R.drawable.transparent_circle));
                }
            }
            holder.sdv_picture.setTag(position);
            holder.sdv_picture.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(pos, NEW_MATCHES);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar pb_time_left;
        private CircleImageView sdv_picture;
        private TextView tv_name;

        public MyViewHolder(View view) {
            super(view);
            pb_time_left = view.findViewById(R.id.pb_time_left);
            sdv_picture = view.findViewById(R.id.sdv_picture);
            tv_name = view.findViewById(R.id.tv_name);
        }
    }
}
