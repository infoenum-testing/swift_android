package com.swiftdating.app.ui.homeScreen.adapter;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;

import com.swiftdating.app.R;
import com.swiftdating.app.callbacks.OnItemClickListenerType;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.common.swipemenulistview.BaseSwipeListAdapter;
import com.swiftdating.app.data.network.CallServer;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.responsemodel.ChatListModel;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.swiftdating.app.common.AppConstants.SCHEDULE_MATCHES;

public class ScheduleSwipeAdapter extends BaseSwipeListAdapter{

//    public boolean isBlurr;
    private Context mContext;
    private ArrayList<ChatListModel.ChatList> matchList;
    private OnItemClickListenerType onItemClickListener;
    private CommonDialogs.onProductConsume onProductConsume;

    public ScheduleSwipeAdapter(Context mContext, ArrayList<ChatListModel.ChatList> matchList,CommonDialogs.onProductConsume onProductConsume) {
        this.mContext = mContext;
        this.matchList = matchList;
        this.onProductConsume=onProductConsume;
    }

    public void setOnItemClickListener(OnItemClickListenerType onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public ChatListModel.ChatList getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.custom_item_schedule_matches, null);
            new ViewHolder(convertView);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(position, SCHEDULE_MATCHES);
            }
        });
        ViewHolder holder = (ViewHolder) convertView.getTag();

        ChatListModel.ChatList data = getItem(position);
        if (data != null&&data.getImageForUser()!=null&&data.getImageForUser().size()>0)
            CommonUtils.setImageUsingFresco(holder.sdv_picture, CallServer.BaseImage + data.getImageForUser().get(0).getImageUrl(), 3);
        if (data != null&&data.getProfileOfUser()!=null)
            holder.tv_name.setText(data.getProfileOfUser().getName());
        Date convertedDate2 = new Date(), convertedDate = new Date();
        if (data != null && data.getChatByUser() != null) {
            holder.tv_date.setText(data.getChatByUser().getMessage());
            if (data.getChatByUser().getToId().toString().equals(new SharedPreference(mContext).getUserId()) &&
                    data.getChatByUser().getStatus().equalsIgnoreCase("unread")) {
                holder.ivUnread.setVisibility(View.VISIBLE);
                Typeface typeface = ResourcesCompat.getFont(mContext, R.font.bold);
                holder.tv_date.setTypeface(typeface);
            } else {
                holder.ivUnread.setVisibility(View.GONE);
                Typeface typeface = ResourcesCompat.getFont(mContext, R.font.medium);
                holder.tv_date.setTypeface(typeface);
            }

        }
        holder.sdv_picture.setTag(position);
        holder.sdv_picture.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            if (onItemClickListener != null)
                onItemClickListener.OnItemClick(pos, SCHEDULE_MATCHES);
        });
        holder.clView.setTag(position);
        holder.clView.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            if (onItemClickListener != null)
                onItemClickListener.OnItemClick(pos, SCHEDULE_MATCHES);
        });
        //Blurry.with(mContext).sampling(5).radius(10).from(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dummy1)).into(holder.sdv_picture);
        holder.img_blurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Not Deluxe", Toast.LENGTH_SHORT).show();
            }
        });
//        setBlurOnTextView(holder.tv_name, isBlurr);
//        setBlurOnTextView(holder.tv_date, isBlurr);
//        Log.e("TAG", "getView: "+isBlurr);

     /*   if (isBlurr) {
            holder.img_blurr.setVisibility(View.VISIBLE);
            holder.img_blurr.setOnClickListener(v -> CommonDialogs.DeluxePurChaseDialog(mContext,onProductConsume));*/



            /*holder.img_blurr.post(() -> Blurry.with(mContext).color(Color.argb(127, 255, 255, 255)).radius(10).sampling(5).capture(holder.clParent).into(holder.img_blurr));*/
           // Blurry.with(mContext).sampling(5).radius(10).from(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dummy1)).into(holder.sdv_picture_1);

            /* float radius = holder.tv_name.getTextSize() / 3;
            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
            holder. tv_name.getPaint().setMaskFilter(filter);*/


       /* } else {*/


           /* holder.img_blurr.setOnClickListener(v -> {
            });
            holder.sdv_picture_1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dummy1));*/
            holder.img_blurr.setVisibility(View.GONE);


//        }


        return convertView;
    }

    private void setBlurOnTextView(TextView tv_name, boolean blur) {
        if (blur) {
            float radius = tv_name.getTextSize() / 2;
            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
            tv_name.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            tv_name.getPaint().setMaskFilter(filter);
        } else {
            tv_name.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            MaskFilter mm = tv_name.getPaint().setMaskFilter(null);
            tv_name.getPaint().setMaskFilter(null);
        }
    }


    private static class ViewHolder {

        ConstraintLayout clView, clParent;
        ImageView ivUnread, img_blurr;
        private SimpleDraweeView sdv_picture;
        private CircleImageView sdv_picture_1;
        private TextView tv_name, tv_date;

        public ViewHolder(View view) {
            sdv_picture_1 = view.findViewById(R.id.sdv_picture_1);
            sdv_picture = view.findViewById(R.id.sdv_picture);
            tv_name = view.findViewById(R.id.tv_name);
            tv_date = view.findViewById(R.id.tv_date);
            clView = view.findViewById(R.id.clView);
            clParent = view.findViewById(R.id.clParent);
            ivUnread = view.findViewById(R.id.ivUnread);
            img_blurr = view.findViewById(R.id.img_blurr);
            view.setTag(this);
        }

    }
}
