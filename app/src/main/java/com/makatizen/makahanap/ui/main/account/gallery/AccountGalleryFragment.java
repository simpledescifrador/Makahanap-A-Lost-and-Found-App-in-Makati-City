package com.makatizen.makahanap.ui.main.account.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class AccountGalleryFragment extends BaseFragment implements AccountGalleryMvpView, OnItemClickListener {

    @BindView(R.id.account_gallery_rv_images)
    RecyclerView mAccountGalleryRvImages;

    @Inject
    ImageGalleryAdapter mImageGalleryAdapter;

    @Inject
    AccountGalleryMvpPresenter<AccountGalleryMvpView> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_gallery, container, false);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setUnBinder(ButterKnife.bind(this, view));
        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
        //Show Selected Image
        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
        intent.putExtra(IntentExtraKeys.SELECTED_POSITION, position);
        intent.putStringArrayListExtra(IntentExtraKeys.ITEM_IMAGES, (ArrayList<String>) mImageGalleryAdapter.getData());
        startActivity(intent);
    }

    @Override
    public void setAccountItemImages(final List<String> itemImages) {
        mAccountGalleryRvImages.setVisibility(View.VISIBLE);
        mImageGalleryAdapter.setData(itemImages);
    }

    @Override
    protected void init() {
        mAccountGalleryRvImages.setAdapter(mImageGalleryAdapter);
        mAccountGalleryRvImages.setLayoutManager(new GridLayoutManager(getContext(), 3));
        RecyclerItemUtils.addTo(mAccountGalleryRvImages).setOnItemClickListener(this);
        mPresenter.loadAccountItemImages();
    }
}
