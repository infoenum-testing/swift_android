package com.swift.dating.ui.homeScreen.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import com.swift.dating.R;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.data.network.CallServer;
import com.swift.dating.model.responsemodel.ImageForUser;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.model.responsemodel.User;
import com.swift.dating.ui.homeScreen.HomeActivity;
import com.swift.dating.ui.userCardScreen.UserCardActivity;
import jp.wasabeef.blurry.Blurry;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {
    private static final String TAG = "LikesImagesAdapter";
    Context context;
    List<User> list;
    private OnClick onClick;
    private boolean isUnlock;
    private CommonDialogs.onProductConsume clickListener;

    public SearchUserAdapter(Context context, List<User> list, boolean isUnlock, CommonDialogs.onProductConsume clickListener) {
        this.context = context;
        this.list = list;
        this.isUnlock = isUnlock;
        this.clickListener = clickListener;
    }

    public void setUnlock(boolean unlock) {
        isUnlock = unlock;
    }

    public void setOnClicklistner(OnClick onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.like_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface OnClick {
        void Listeners();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv_name;
        CardView card_likes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            card_likes = itemView.findViewById(R.id.card_likes);
        }

        public void bind(int position) {
            //img.setImageDrawable(position % 2 == 0 ? context.getResources().getDrawable(R.drawable.dummy1) : context.getResources().getDrawable(R.drawable.dummy));
            ProfileOfUser user = list.get(position).getProfileOfUser();
            List<ImageForUser> imageForUser = list.get(position).getImageForUser();
            if (!isUnlock) {
                Blurry.with(context).sampling(5).radius(10).from(BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_unselected)).into(img);
            } else {
                img.setImageDrawable(context.getDrawable(R.drawable.profile_unselected));
            }
            if (imageForUser != null && imageForUser.size() > 0) {
                Glide.with(context)
                        .asBitmap()
                        .load(CallServer.BaseImage + imageForUser.get(0).getImageUrl())
                        .placeholder(R.drawable.profile_unselected).error(R.drawable.profile_unselected)
                        .into(new CustomTarget<Bitmap>(130, 160) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                img.setImageBitmap(resource);
                                if (!isUnlock)
                                    Blurry.with(context).sampling(5).radius(10).from(resource).into(img);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
            if (user != null)
                tv_name.setText(user.getName() + ", " + CommonUtils.getAge(user.getDob()));
            if (!isUnlock) {
                card_likes.setOnClickListener(v -> CommonDialogs.DeluxePurChaseDialog(context, clickListener));
                float radius = tv_name.getTextSize() / 3;
                BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                tv_name.getPaint().setMaskFilter(filter);
                //  Blurry.with(context).sampling(5).radius(10).from(BitmapFactory.decodeResource(context.getResources(), position % 2 == 0 ? R.drawable.dummy1 : R.drawable.dummy)).into(img);
            } else {
                card_likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, UserCardActivity.class).putExtra("userid", list.get(position).getId()).putExtra("tabPos", 2).putExtra("isFromSearch", true));
                        ((HomeActivity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
                tv_name.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                MaskFilter mm = tv_name.getPaint().setMaskFilter(null);
                tv_name.getPaint().setMaskFilter(mm);
            }
        }
    }

}
