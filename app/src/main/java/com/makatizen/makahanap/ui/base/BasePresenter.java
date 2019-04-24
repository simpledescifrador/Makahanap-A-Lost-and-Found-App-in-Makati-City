package com.makatizen.makahanap.ui.base;

import android.support.annotation.NonNull;
import com.makatizen.makahanap.data.DataManager;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Inject;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends BaseMvpView> implements Presenter<V> {

    public static class MvpViewNotAttachedException extends RuntimeException {

        MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(BaseMvpView) before"
                    + " requesting data to the Presenter");
        }
    }

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private DataManager mDataManager;

    private V mvpView;

    @Inject
    public BasePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(@NonNull V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        mvpView = null;
        mCompositeDisposable.dispose();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public V getMvpView() {
        return mvpView;
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    protected void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException();
        }
    }
}
