package com.swiftdating.app.ui.settingScreen;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.swiftdating.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    List<String> titleList;
    private LayoutInflater mLayoutInflater;
    OnItemClicked clicked;

    public SliderAdapter(Context context, List<String> titleList, OnItemClicked clicked) {
        this.context = context;
        this.titleList = titleList;
        this.clicked = clicked;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NotNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.slider_items, container, false);
        ImageView image;
        TextView tvTitle;
        image = itemView.findViewById(R.id.image);
        tvTitle = itemView.findViewById(R.id.tvTitles);
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.arr_premium_images);
        image.setImageResource(imgs.getResourceId(position, -1));
        tvTitle.setText(titleList.get(position));
        container.addView(itemView);
        itemView.setOnClickListener(v -> {
            if (clicked != null)
                clicked.onPagerItemClick();
        });


        return itemView;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    public interface OnItemClicked {
        void onPagerItemClick();
    }

}
