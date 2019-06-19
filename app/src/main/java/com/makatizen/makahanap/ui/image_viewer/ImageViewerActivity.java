package com.makatizen.makahanap.ui.image_viewer;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ImageViewerActivity extends BaseActivity {

    @Inject
    ImageViewerAdapter mImageViewerAdapter;

    @BindView(R.id.image_viewer_ibtn_back)
    ImageButton mImageViewerIbtnBack;

    @BindView(R.id.image_viewer_tv_selected)
    TextView mImageViewerTvCounter;

    @BindView(R.id.image_viewer_tv_total)
    TextView mImageViewerTvTotal;

    @BindView(R.id.image_viewer_vp_content)
    ViewPager mImageViewerVpContent;

    private List<String> images = new ArrayList<>();

    private int selectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        setContentView(R.layout.activity_image_viewer);
        setUnBinder(ButterKnife.bind(this));

        init();
    }

    @OnClick(R.id.image_viewer_ibtn_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void init() {
        images = getIntent().getStringArrayListExtra(IntentExtraKeys.ITEM_IMAGES);
        selectedPosition = getIntent().getIntExtra(IntentExtraKeys.SELECTED_POSITION, 0);
        mImageViewerTvTotal.setText(" OF " + images.size());

        mImageViewerVpContent.setAdapter(mImageViewerAdapter);
        mImageViewerAdapter.setImages(images);
        mImageViewerVpContent.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int i) {

            }

            @Override
            public void onPageScrolled(final int i, final float v, final int i1) {

            }

            @Override
            public void onPageSelected(final int i) {
                mImageViewerTvCounter.setText(String.valueOf(i + 1));
            }
        });
        mImageViewerVpContent.setCurrentItem(selectedPosition);
    }
}
