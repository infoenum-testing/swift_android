package com.swiftdating.app.ui.homeScreen.adapter;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
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

public class ScheduleSwipeAdapter extends RecyclerView.Adapter<ScheduleSwipeAdapter.MyViewHolder> {

    //    public boolean isBlurr;
    private Context mContext;
    private ArrayList<ChatListModel.ChatList> matchList;
    private OnItemClickListenerType onItemClickListener;
    private ListenerSwipe listenerSwipe;
    private CommonDialogs.onProductConsume onProductConsume;
    private ViewBinderHelper viewBinderHelper;

    public ScheduleSwipeAdapter(Context mContext, ArrayList<ChatListModel.ChatList> matchList, CommonDialogs.onProductConsume onProductConsume) {
        this.mContext = mContext;
        this.matchList = matchList;
        this.onProductConsume = onProductConsume;
        viewBinderHelper = new ViewBinderHelper();
        viewBinderHelper.setOpenOnlyOne(true);
    }

    public void setListenerSwipe(ListenerSwipe listenerSwipe) {
        this.listenerSwipe = listenerSwipe;
    }

    public void setOnItemClickListener(OnItemClickListenerType onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_item_schedule_matches, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(matchList.get(position));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public interface ListenerSwipe {
        void OnClickSwipeView(int pos, boolean isReport);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout clView, clParent;
        ImageView ivUnread, /*img_blurr*/
                ivStar, ivVerifyProfile;
        ProgressBar pb_time_left;
        SimpleDraweeView sdv_picture;
        CircleImageView sdv_picture_1;
        TextView tv_name, tv_date, tvYourTurn, tv_report, tv_delete;
        SwipeRevealLayout swipe_reveal;

        public MyViewHolder(@NonNull View view) {
            super(view);
            swipe_reveal = view.findViewById(R.id.swipe_reveal);
            sdv_picture_1 = view.findViewById(R.id.sdv_picture_1);
            sdv_picture = view.findViewById(R.id.sdv_picture);
            tv_name = view.findViewById(R.id.tv_name);
            tv_date = view.findViewById(R.id.tv_date);
            clView = view.findViewById(R.id.clView);
            clParent = view.findViewById(R.id.clParent);
            ivUnread = view.findViewById(R.id.ivUnread);
//            img_blurr = view.findViewById(R.id.img_blurr);
            tvYourTurn = view.findViewById(R.id.tvYourTurn);
            pb_time_left = view.findViewById(R.id.pb_time_left);
            tv_report = view.findViewById(R.id.tv_report);
            tv_delete = view.findViewById(R.id.tv_delete);
            view.setTag(this);

        }

        public void bind(ChatListModel.ChatList data) {
            viewBinderHelper.bind(swipe_reveal, "" + getAbsoluteAdapterPosition());
            tv_delete.setText(!TextUtils.isEmpty(data.getIsDirectChat()) && data.getIsDirectChat().equalsIgnoreCase("YES") ? "Block" : "Unmatch");
            tv_delete.setOnClickListener(view -> {
                if (listenerSwipe != null) {
                    listenerSwipe.OnClickSwipeView(getAbsoluteAdapterPosition(), false);
                }
            });
            tv_report.setOnClickListener(view -> {
                if (listenerSwipe != null) {
                    listenerSwipe.OnClickSwipeView(getAbsoluteAdapterPosition(), true);
                }
            });
            if (data.getImageForUser() != null && data.getImageForUser().size() > 0)
                CommonUtils.setImageUsingFresco(sdv_picture, CallServer.BaseImage + data.getImageForUser().get(0).getImageUrl(), 3);
            if (data.getProfileOfUser() != null)
                tv_name.setText(data.getProfileOfUser().getName());
//            Date convertedDate2 = new Date(), convertedDate = new Date();
            if (data.getChatByUser() != null) {
                tv_date.setText(data.getChatByUser().getMessage());
                if (data.getChatByUser().getToId().toString().equals(new SharedPreference(mContext).getUserId()) && data.getChatByUser().getStatus().equalsIgnoreCase("unread")) {
                    ivUnread.setVisibility(View.VISIBLE);
                    Typeface typeface = ResourcesCompat.getFont(mContext, R.font.bold);
                    tv_date.setTypeface(typeface);
                } else {
                    ivUnread.setVisibility(View.GONE);
                    Typeface typeface = ResourcesCompat.getFont(mContext, R.font.medium);
                    tv_date.setTypeface(typeface);
                }
                if (data.getChatByUser().getToId().toString().equals(new SharedPreference(mContext).getUserId())){
                    if (data.getUnreadMessages() == 0 && data.getIsChatStarted() == 1)
                        tvYourTurn.setVisibility(View.GONE);
                    else
                        tvYourTurn.setVisibility(View.VISIBLE);
                }
            }
            sdv_picture.setTag(getAbsoluteAdapterPosition());
            sdv_picture.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(pos, SCHEDULE_MATCHES);
            });
            clView.setTag(getAbsoluteAdapterPosition());
            clView.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(pos, SCHEDULE_MATCHES);
            });
            //Blurry.with(mContext).sampling(5).radius(10).from(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dummy1)).into(holder.sdv_picture);
           /* img_blurr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Not Premium", Toast.LENGTH_SHORT).show();
                }
            });*/
            if (data.getMatchOfUser() != null && !TextUtils.isEmpty(data.getMatchOfUser().getCalltimerExpiry())) {
                long expire = CommonUtils.stringToDate(data.getMatchOfUser().getCalltimerExpiry().replace("T", " ").split("\\.")[0]);
                long server = CommonUtils.stringToDate(data.getChatByUser().getServerTime().replace("T", " ").split("\\.")[0]);
                /*long created = CommonUtils.stringToDate(data.getMatchOfUser().getCreatedAt().replace("T", " ").split("\\.")[0]);
                if (data.getMatchOfUser().getTimetokenAppliedOn() != null) {
                    created = CommonUtils.stringToDate(data.getMatchOfUser().getTimetokenAppliedOn().replace("T", " ").split("\\.")[0]);
                }*/
                pb_time_left.setProgress((int) ((expire - server) * 100 / (72 * 60)));
            }
//            img_blurr.setVisibility(View.GONE);
        }
    }

   /* @Override
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
        ViewHolder holder=null;
        if (convertView == null) {
            LayoutInflater layoutInflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.custom_item_schedule_matches, parent, false);
            holder = new ViewHolder(convertView);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.OnItemClick(position, SCHEDULE_MATCHES);
            }
        });
        if (holder == null)
            holder = (ViewHolder) convertView.getTag();

        ChatListModel.ChatList data = getItem(position);
        Log.e("TAG", "getView: " + position + "  " + data.getProfileOfUser());

        if (data != null && data.getImageForUser() != null && data.getImageForUser().size() > 0)
            CommonUtils.setImageUsingFresco(holder.sdv_picture, CallServer.BaseImage + data.getImageForUser().get(0).getImageUrl(), 3);
        if (data != null && data.getProfileOfUser() != null)
            holder.tv_name.setText(data.getProfileOfUser().getName());
        Date convertedDate2 = new Date(), convertedDate = new Date();
        if (data != null && data.getChatByUser() != null) {
            holder.tv_date.setText(data.getChatByUser().getMessage());
            if (data.getChatByUser().getToId().toString().equals(new SharedPreference(mContext).getUserId()) &&
                    data.getChatByUser().getStatus().equalsIgnoreCase("unread")) {
                holder.ivUnread.setVisibility(View.VISIBLE);
                holder.tvYourTurn.setVisibility(View.VISIBLE);
                Typeface typeface = ResourcesCompat.getFont(mContext, R.font.bold);
                holder.tv_date.setTypeface(typeface);
            } else {
                holder.ivUnread.setVisibility(View.GONE);
                holder.tvYourTurn.setVisibility(View.GONE);
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
        holder.img_blurr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Not Premium", Toast.LENGTH_SHORT).show();
            }
        });
        if (data != null && data.getMatchOfUser() != null && !TextUtils.isEmpty(data.getMatchOfUser().getCalltimerExpiry())) {
            long expire = CommonUtils.stringToDate(data.getMatchOfUser().getCalltimerExpiry().replace("T", " ").split("\\.")[0]);
            long server = CommonUtils.stringToDate(data.getChatByUser().getServerTime().replace("T", " ").split("\\.")[0]);

            holder.pb_time_left.setProgress((int) ((expire - server) * 100 / (72 * 60)));
        }
        holder.img_blurr.setVisibility(View.GONE);

 *//* long created = CommonUtils.stringToDate(data.getMatchOfUser().getCreatedAt().replace("T", " ").split("\\.")[0]);
        if (data.getMatchOfUser().getTimetokenAppliedOn() != null) {
            created = CommonUtils.stringToDate(data.getMatchOfUser().getTimetokenAppliedOn().replace("T", " ").split("\\.")[0]);
        }*//*
//        setBlurOnTextView(holder.tv_name, isBlurr);
//        setBlurOnTextView(holder.tv_date, isBlurr);
//        Log.e("TAG", "getView: "+isBlurr);

     *//*   if (isBlurr) {
            holder.img_blurr.setVisibility(View.VISIBLE);
            holder.img_blurr.setOnClickListener(v -> CommonDialogs.DeluxePurChaseDialog(mContext,onProductConsume));*//*



     *//*holder.img_blurr.post(() -> Blurry.with(mContext).color(Color.argb(127, 255, 255, 255)).radius(10).sampling(5).capture(holder.clParent).into(holder.img_blurr));*//*
        // Blurry.with(mContext).sampling(5).radius(10).from(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dummy1)).into(holder.sdv_picture_1);

            *//* float radius = holder.tv_name.getTextSize() / 3;
            BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
            holder. tv_name.getPaint().setMaskFilter(filter);*//*


     *//* } else {*//*


     *//* holder.img_blurr.setOnClickListener(v -> {
            });
            holder.sdv_picture_1.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dummy1));*//*


        ///////////////////////////  setting verified tick star image and your turn
        // if (data.getProfileOfUser())
        // holder.ivVerifyProfile.setVisibility();


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


    class ViewHolder {

        ConstraintLayout clView, clParent;
        ImageView ivUnread, img_blurr, ivStar, ivVerifyProfile;
        ProgressBar pb_time_left;
        SimpleDraweeView sdv_picture;
        CircleImageView sdv_picture_1;
        TextView tv_name, tv_date, tvYourTurn;

        public ViewHolder(View view) {
            sdv_picture_1 = view.findViewById(R.id.sdv_picture_1);
            sdv_picture = view.findViewById(R.id.sdv_picture);
            tv_name = view.findViewById(R.id.tv_name);
            tv_date = view.findViewById(R.id.tv_date);
            clView = view.findViewById(R.id.clView);
            clParent = view.findViewById(R.id.clParent);
            ivUnread = view.findViewById(R.id.ivUnread);
            img_blurr = view.findViewById(R.id.img_blurr);
            tvYourTurn = view.findViewById(R.id.tvYourTurn);
            pb_time_left = view.findViewById(R.id.pb_time_left);
            view.setTag(this);
        }

    }*/
}
