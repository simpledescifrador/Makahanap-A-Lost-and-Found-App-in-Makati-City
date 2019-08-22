package com.makatizen.makahanap.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.makatizen.makahanap.R;

public class RecyclerItemUtils {

    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnLongClickListener {

        void onLongClicked(RecyclerView recyclerView, int position, View v);
    }

    private OnItemClickListener onItemClickListener;

    private OnLongClickListener onLongClickListener;

    public static RecyclerItemUtils addTo(RecyclerView view) {
        RecyclerItemUtils support = (RecyclerItemUtils) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new RecyclerItemUtils(view);
        }
        return support;
    }

    private RecyclerItemUtils(final RecyclerView recyclerView) {
        recyclerView.setTag(R.id.item_click_support, this);
        recyclerView.addOnChildAttachStateChangeListener(
                new RecyclerView.OnChildAttachStateChangeListener() {
                    @Override
                    public void onChildViewAttachedToWindow(View view) {
                        if (onItemClickListener != null) {
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onItemClickListener != null) {
                                        RecyclerView.ViewHolder holder =
                                                recyclerView.getChildViewHolder(v);
                                        onItemClickListener.onItemClicked(recyclerView,
                                                holder.getAdapterPosition(), v);
                                    }
                                }
                            });
                        }

                        if (onLongClickListener != null) {
                            view.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(final View view) {
                                    if (onLongClickListener != null) {
                                        RecyclerView.ViewHolder holder =
                                                recyclerView.getChildViewHolder(view);

                                        onLongClickListener
                                                .onLongClicked(recyclerView, holder.getAdapterPosition(), view);
                                    }
                                    return true;
                                }
                            });
                        }
                    }

                    @Override
                    public void onChildViewDetachedFromWindow(View view) {

                    }
                });
    }

    public RecyclerItemUtils setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
        return this;
    }

    public RecyclerItemUtils setOnLongClickListener(OnLongClickListener listener) {
        onLongClickListener = listener;
        return this;
    }
}
