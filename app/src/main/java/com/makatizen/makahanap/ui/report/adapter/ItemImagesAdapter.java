package com.makatizen.makahanap.ui.report.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.ItemImageDetails;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.ui.report.adapter.ItemImagesAdapter.ViewHolder;
import java.util.ArrayList;
import javax.inject.Inject;

public class ItemImagesAdapter extends BaseRecyclerViewAdapter<ViewHolder, ItemImageDetails> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.add_iv_image)
        ImageView mAddIvImage;

        @BindView(R.id.add_tv_image_size)
        TextView mAddTvImageSize;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    private Context mContext;

    @Inject
    public ItemImagesAdapter(Context context) {
        mContext = context;
        setData(new ArrayList<ItemImageDetails>());
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        ItemImageDetails imageDetails = getItem(position);

        Glide.with(mContext)
                .load(imageDetails.getPath())
                .into(viewHolder.mAddIvImage);

        viewHolder.mAddTvImageSize.setText(imageDetails.getSize());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_images, viewGroup, false));
    }
}
