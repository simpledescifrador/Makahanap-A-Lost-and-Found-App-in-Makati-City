package com.makatizen.makahanap.ui.item_details;

import android.os.Bundle;
import butterknife.ButterKnife;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import javax.inject.Inject;

public class ItemDetailsActivity extends BaseActivity implements ItemDetailsMvpView {

    @Inject
    ItemDetailsMvpPresenter<ItemDetailsMvpView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_item_details);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void init() {

    }
}
