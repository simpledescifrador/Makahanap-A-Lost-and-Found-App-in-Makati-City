package com.makatizen.makahanap.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.utils.DateUtils;
import com.makatizen.makahanap.utils.enums.Type;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends BaseRecyclerViewAdapter<SearchAdapter.ViewHolder, FeedItem> {

    private Context mContext;

    @Inject
    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
        setData(new ArrayList<FeedItem>());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.classic_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final FeedItem feedItem = getItem(position);

        //Set Report Type
        if (feedItem.getItemType() == Type.LOST) {
            viewHolder.classicItemReportType.setText("Lost");
            viewHolder.classicItemReportType.setTextColor(mContext.getResources().getColor(R.color.defaultRed));
        } else {
            viewHolder.classicItemReportType.setText("Found");
            viewHolder.classicItemReportType.setTextColor(mContext.getResources().getColor(R.color.defaultGreen));
        }


        //Set Date
        viewHolder.classicItemDate.setText(DateUtils.DateToTimeFormat(feedItem.getItemCreatedAt()));
        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.placeholder_bg)
                .error(R.color.placeholder_bg).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .override(300, 300);
        //Set Title
        viewHolder.classicItemTitle.setText(feedItem.getItemTitle());

        //Set Location
        viewHolder.classicItemLocation.setText("@ " + feedItem.getItemLocation());

        //Set Item Image
        Glide.with(mContext)
                .load(ApiConstants.BASE_URL + feedItem.getItemImageUrl())
                .apply(requestOptions)
                .into(viewHolder.classicItemImage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.classic_item_image)
        ImageView classicItemImage;
        @BindView(R.id.classic_item_title)
        TextView classicItemTitle;
        @BindView(R.id.classic_item_date)
        TextView classicItemDate;
        @BindView(R.id.classic_item_report_type)
        TextView classicItemReportType;
        @BindView(R.id.classic_item_location)
        TextView classicItemLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
