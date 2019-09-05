package com.makatizen.makahanap.ui.main.account.reports;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.utils.DateUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class AccountReportsAdapter extends BaseRecyclerViewAdapter<AccountReportsAdapter.ViewHolder, FeedItem> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_item_iv_account_image)
        CircleImageView mFeedItemIvAccountImage;

        @BindView(R.id.feed_item_iv_image)
        ImageView mFeedItemIvImage;

        @BindView(R.id.feed_item_tv_account_name)
        TextView mFeedItemTvAccountName;

        @BindView(R.id.feed_item_tv_created_at)
        TextView mFeedItemTvCreatedAt;

        @BindView(R.id.feed_item_tv_description)
        TextView mFeedItemTvDescription;

        @BindView(R.id.feed_item_tv_location)
        TextView mFeedItemTvLocation;

        @BindView(R.id.feed_item_tv_title)
        TextView mFeedItemTvTitle;

        @BindView(R.id.classic_item_report_type)
        TextView mFeedItemTvType;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private Context mContext;

    @Inject
    public AccountReportsAdapter(final Context context) {
        mContext = context;
        setData(new ArrayList<FeedItem>());
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position,
            @NonNull final List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals("item_title")) {
                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                    holder.mFeedItemTvTitle.setText(o.getString(key));
                }
                if (key.equals("item_description")) {
                    holder.mFeedItemTvDescription.setText(o.getString(key));
                }

                if (key.equals("item_location")) {
                    holder.mFeedItemTvLocation.setText(o.getString(key));
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        FeedItem feedItem = getItem(i);
        RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(ApiConstants.BASE_URL + feedItem.getItemImageUrl())
                .apply(requestOptions)
                .into(viewHolder.mFeedItemIvImage);

        viewHolder.mFeedItemTvLocation.setText("@ " + feedItem.getItemLocation());
        viewHolder.mFeedItemTvTitle.setText(feedItem.getItemTitle());
        viewHolder.mFeedItemTvAccountName.setText(feedItem.getAccountName());
        viewHolder.mFeedItemTvCreatedAt.setText(DateUtils.DateToTimeFormat(feedItem.getItemCreatedAt()));

        if (feedItem.getItemDescription().isEmpty()) {
            //remove description if it's empty
            viewHolder.mFeedItemTvDescription.setVisibility(View.GONE);
        } else {
            viewHolder.mFeedItemTvDescription.setVisibility(View.GONE);
            viewHolder.mFeedItemTvDescription.setText(feedItem.getItemDescription());
        }

        switch (feedItem.getItemType()) {
            case LOST:
                viewHolder.mFeedItemTvType.setText("Lost");
                viewHolder.mFeedItemTvType.setTextColor(mContext.getResources().getColor(R.color.defaultRed));
                break;
            case FOUND:
                viewHolder.mFeedItemTvType.setText("Found");
                viewHolder.mFeedItemTvType.setTextColor(mContext.getResources().getColor(R.color.defaultGreen));
                break;
        }

        Glide.with(mContext)
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + feedItem.getAccountImageUrl())
                .apply(requestOptions)
                .into(viewHolder.mFeedItemIvAccountImage);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feed, viewGroup, false));
    }

    public void updateFeedItemList(List<FeedItem> feedItemsList) {
        DiffUtil.DiffResult diffResult = DiffUtil
                .calculateDiff(new AccountReportsDiffCallback(getData(), feedItemsList));
        diffResult.dispatchUpdatesTo(this);

        getData().clear();
        setDataNoNotify(feedItemsList);

    }
}
