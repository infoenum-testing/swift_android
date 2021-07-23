package com.swift.dating.ui.viewpagerScreen;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jsibbold.zoomage.ZoomageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.swift.dating.R;
import com.swift.dating.model.responsemodel.InstagramImageModel;

public class ViewPagerImageAdapter2 extends PagerAdapter {

    private Context context;
    private List<InstagramImageModel.Datum> list;
    private LayoutInflater mLayoutInflater;

    ViewPagerImageAdapter2(Context context, List<InstagramImageModel.Datum> list) {
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
        imageView.setImageURI(Uri.parse(list.get(position).getMediaUrl()));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}