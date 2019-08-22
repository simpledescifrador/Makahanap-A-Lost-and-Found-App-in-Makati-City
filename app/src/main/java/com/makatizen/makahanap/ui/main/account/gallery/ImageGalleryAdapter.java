package com.makatizen.makahanap.ui.main.account.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.ui.main.account.gallery.ImageGalleryAdapter.ViewHolder;
import java.util.ArrayList;
import javax.inject.Inject;

public class ImageGalleryAdapter extends BaseRecyclerViewAdapter<ViewHolder, String> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_gallery_iv_image)
        ImageView mImageGalleryIvImage;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private Context mContext;

    @Inject
    public ImageGalleryAdapter(final Context context) {
        mContext = context;
        setData(new ArrayList<String>());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        String imageUrl = getItem(i);
        Glide.with(mContext)
                .load(imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(600, 600))
                .into(viewHolder.mImageGalleryIvImage);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_gallery, viewGroup, false));
    }
}
