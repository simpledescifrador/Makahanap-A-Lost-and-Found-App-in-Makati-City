package com.makatizen.makahanap.ui.main.account.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.utils.DateUtils;
import javax.inject.Inject;

public class AccountAboutFragment extends BaseFragment implements AccountAboutMvpView {

    @BindView(R.id.account_about_tv_address)
    TextView mAccountAboutTvAddress;

    @BindView(R.id.account_about_tv_age)
    TextView mAccountAboutTvAge;

    @BindView(R.id.account_about_tv_civil_status)
    TextView mAccountAboutTvCivilStatus;

    @BindView(R.id.account_about_tv_date_created)
    TextView mAccountAboutTvDateCreated;

    @BindView(R.id.account_about_tv_email)
    TextView mAccountAboutTvEmail;

    @BindView(R.id.account_about_tv_gender)
    TextView mAccountAboutTvGender;

    @BindView(R.id.account_about_tv_phone)
    TextView mAccountAboutTvPhone;

    @Inject
    AccountAboutMvpPresenter<AccountAboutMvpView> mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_about, container, false);
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
    public void setAccountAboutData(final MakahanapAccount makahanapAccount) {
        mAccountAboutTvAge.setText(String.valueOf(makahanapAccount.getAge()) + " years old");
        mAccountAboutTvAddress.setText(makahanapAccount.getAddress());
        mAccountAboutTvEmail.setText(makahanapAccount.getEmailAddress());
        mAccountAboutTvPhone.setText(makahanapAccount.getContactNumber());

        switch (makahanapAccount.getGender()) {
            case MALE:
                mAccountAboutTvGender.setText("Male");
                break;
            case FEMALE:
                mAccountAboutTvGender.setText("Female");
                break;
        }
        switch (makahanapAccount.getCivilStatus()) {
            case SINGLE:
                mAccountAboutTvCivilStatus.setText("Single");
                break;
            case MARRIED:
                mAccountAboutTvCivilStatus.setText("Married");
                break;
            case WIDOWED:
                mAccountAboutTvCivilStatus.setText("Widowed");
                break;
            case DIVORCED:
                mAccountAboutTvCivilStatus.setText("Divorced");
                break;
            case COMMON_LAW:
                mAccountAboutTvCivilStatus.setText("Common Law");
                break;
        }

        mAccountAboutTvDateCreated.setText(DateUtils.DateFormat(makahanapAccount.getDateCreated()));
    }

    @Override
    protected void init() {
        mPresenter.loadAccountAbout();
    }
}
