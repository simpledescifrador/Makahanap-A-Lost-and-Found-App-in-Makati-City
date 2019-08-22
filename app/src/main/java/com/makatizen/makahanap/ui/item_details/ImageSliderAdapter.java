package com.makatizen.makahanap.ui.item_details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ImageSliderAdapter extends PagerAdapter {

    interface OnItemClickListener {

        void onItemClick(List<String> imageList);
    }

    public static int LOOPS_COUNT = 100;

    private Context mContext;

    private List<String> mImageList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    @Inject
    public ImageSliderAdapter(final Context context) {
        mContext = context;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .load(mImageList.get(position))
                .into(imageView);
        container.addView(imageView, 0);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                mOnItemClickListener.onItemClick(mImageList);
            }
        });
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object o) {
        return view == o;
    }

    void setImages(List<String> images) {
        mImageList = images;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
