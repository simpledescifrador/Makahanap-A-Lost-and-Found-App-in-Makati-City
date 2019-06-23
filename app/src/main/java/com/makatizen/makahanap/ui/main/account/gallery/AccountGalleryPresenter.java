package com.makatizen.makahanap.ui.main.account.gallery;

import android.util.Log;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class AccountGalleryPresenter<V extends AccountGalleryMvpView> extends BasePresenter<V>
        implements AccountGalleryMvpPresenter<V> {

    private static final String TAG = "AccountGallery";

    @Inject
    AccountGalleryPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadAccountItemImages() {
        int accountId = getDataManager().getCurrentAccount().getId();
        getDataManager().getAccountItemImages(accountId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new MaybeObserver<List<String>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Load Account Item Images", e);
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final List<String> imageUrls) {
                        Log.d(TAG, "onSuccess: Load Account item Images");
                        if (imageUrls != null && isViewAttached()) {
                            Collections.reverse(imageUrls);
                            List<String> fixedImagesUrls = new ArrayList<>();
                            for (String s:imageUrls) {
                                fixedImagesUrls.add(ApiConstants.BASE_URL + s);
                            }
                            getMvpView().setAccountItemImages(fixedImagesUrls);
                        }
                    }
                });
    }
}
