package com.swift.dating.ui.viewpagerScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.swift.dating.R;
import com.swift.dating.data.network.CallServer;
import com.swift.dating.model.responsemodel.ImageForUser;

public class ViewPagerImageAdapter extends PagerAdapter {

    private Context context;
    private List<ImageForUser> list;
    private LayoutInflater mLayoutInflater;

    ViewPagerImageAdapter(Context context, List<ImageForUser> list) {
        this.context = context;
        this.list = list;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @NotNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ZoomageView imageView =itemView.findViewById(R.id.imageView);
//        CommonUtils.setImageUsingFresco(imageView, CallServer.BASEIMAGE + list.get(position).getImageUrl(),1);
        Glide.with(imageView).load(CallServer.BaseImage + list.get(position).getImageUrl()).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}