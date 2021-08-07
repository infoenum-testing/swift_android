package com.swiftdating.app.ui.homeScreen.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonDialogs;
import jp.wasabeef.blurry.Blurry;

public class SearchScreenAdapter extends RecyclerView.Adapter<SearchScreenAdapter.MyViewHolder> implements CommonDialogs.onProductConsume {
    Context context;
    ArrayList<String> list;
    private OnClick onClick;
    private boolean isUnlock;

    public SearchScreenAdapter(Context context, ArrayList<String> list, boolean isUnlock) {
        this.context = context;
        this.list = list;
        this.isUnlock = isUnlock;
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
        return 9;
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {

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
            img.setImageDrawable(position % 2 == 0 ? context.getResources().getDrawable(R.drawable.dummy1) : context.getResources().getDrawable(R.drawable.dummy));
            if (!isUnlock) {
                card_likes.setOnClickListener(v -> CommonDialogs.DeluxePurChaseDialog(context, SearchScreenAdapter.this));
                float radius = tv_name.getTextSize() / 3;
                BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                tv_name.getPaint().setMaskFilter(filter);
                Blurry.with(context).sampling(5).radius(10).from(BitmapFactory.decodeResource(context.getResources(), position % 2 == 0 ? R.drawable.dummy1 : R.drawable.dummy)).into(img);
            } else {
                card_likes.setOnClickListener(v -> {});
                tv_name.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                MaskFilter mm = tv_name.getPaint().setMaskFilter(null);
                tv_name.getPaint().setMaskFilter(mm);
            }
        }
    }
}
