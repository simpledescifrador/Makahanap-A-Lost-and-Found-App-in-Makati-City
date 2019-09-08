package com.makatizen.makahanap.ui.image_viewer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.makatizen.makahanap.R;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ImageViewerAdapter extends PagerAdapter {

    private Context mContext;

    private List<String> mImages = new ArrayList<>();

    @Inject
    public ImageViewerAdapter(final Context context) {
        mContext = context;
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, final int position, @NonNull final Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_image_viewer, container, false);
        final ImageView itemImage = view.findViewById(R.id.item_image_viewer_iv);
        Glide.with(mContext)
                .load(mImages.get(position))
                .into(itemImage);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object o) {
        return view == o;
    }

    void setImages(List<String> images) {
        mImages = images;
        notifyDataSetChanged();
    }
}
