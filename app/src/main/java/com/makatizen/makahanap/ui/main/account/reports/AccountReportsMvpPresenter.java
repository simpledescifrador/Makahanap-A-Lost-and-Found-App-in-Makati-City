package com.makatizen.makahanap.ui.main.account.reports;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface AccountReportsMvpPresenter<V extends AccountReportsMvpView & BaseMvpView> extends Presenter<V> {

    void loadAccountReports();

    void checkUpdateAccountReports();
}
