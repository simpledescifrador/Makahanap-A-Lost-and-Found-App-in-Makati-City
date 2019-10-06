package com.makatizen.makahanap.ui.main.account;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.chat.ChatActivity;
import com.makatizen.makahanap.ui.login.LoginActivity;
import com.makatizen.makahanap.ui.main.account.about.AccountAboutFragment;
import com.makatizen.makahanap.ui.main.account.change_password.ChangePasswordActivity;
import com.makatizen.makahanap.ui.main.account.gallery.AccountGalleryFragment;
import com.makatizen.makahanap.ui.main.account.reports.AccountReportsFragment;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends BaseFragment implements AccountMvpView {
    private static AccountFragment instance;
    @BindView(R.id.account_iv_chat)
    ImageView mAccountIvChat;
    @BindView(R.id.account_iv_image)
    CircleImageView mAccountIvImage;
    @BindView(R.id.account_iv_more)
    ImageView mAccountIvMore;
    @BindView(R.id.account_tab_layout)
    TabLayout mAccountTabLayout;
    @BindView(R.id.account_tv_found_count)
    TextView mAccountTvFoundCount;
    @BindView(R.id.account_tv_lost_count)
    TextView mAccountTvLostCount;
    @BindView(R.id.account_tv_name)
    TextView mAccountTvName;
    @BindView(R.id.account_tv_rating)
    TextView mAccountTvRating;
    @BindView(R.id.account_tv_returned_count)
    TextView mAccountTvReturnedCount;
    @BindView(R.id.account_view_pager)
    ViewPager mAccountViewPager;
    @BindView(R.id.feed_account_account_layout)
    ConstraintLayout mFeedAccountAccountLayout;
    @Inject
    AccountMvpPresenter<AccountMvpView> mPresenter;
    @BindView(R.id.account_chat_btn)
    Button mAccountChatBtn;
    private PopupMenu mPopupMenu;

    public static AccountFragment getInstance() {
        if (instance == null) {
            instance = new AccountFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

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
    public void onSuccessLogout() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick({R.id.account_iv_more, R.id.account_iv_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.account_iv_more:
                mPopupMenu.show();
                break;
            case R.id.account_iv_chat:
                mPresenter.showAccountMessages();
                break;
        }
    }

    @Override
    public void openChat(final int accountId) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentExtraKeys.ACCOUNT_ID, accountId);
        startActivity(intent);
    }

    @Override
    public void setAccountData(final MakahanapAccount makahanapAccount) {
        //Load Account Image
        Glide.with(getContext())
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + makahanapAccount.getProfileImageUrl())
                .into(mAccountIvImage);
        //Set Account Name
        mAccountTvName.setText(makahanapAccount.getFirstName() + " " + makahanapAccount.getLastName());
    }

    @Override
    public void setFoundCount(final int count) {
        mAccountTvFoundCount.setText(String.valueOf(count));
    }

    @Override
    public void setLostCount(final int count) {
        mAccountTvLostCount.setText(String.valueOf(count));
    }

    @Override
    public void setReturnedCount(final int count) {
        mAccountTvReturnedCount.setText(String.valueOf(count));
    }

    @Override
    public void setHonestyRating(String rating) {
        mAccountTvRating.setText(rating);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.CHANGE_PASSWORD:
                    Toast.makeText(getContext(), "You are about to logout in 5 secs", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();
                            mPresenter.requestAccountLogout();
                        }
                    }, 5000);
                    break;
            }
        }
    }

    public void refreshData() {
        mPresenter.getFoundCount();
        mPresenter.getLostCount();
        mPresenter.getReturnedCount();
    }

    @Override
    protected void init() {
        mPresenter.loadAccountData(); //Load Account Data
        setUpViewPagerAdapter();

        //Setup Popup Menu Option
        mPopupMenu = new PopupMenu(getContext(), mAccountIvMore, Gravity.CENTER);
        mPopupMenu.getMenuInflater().inflate(R.menu.menu_account_more, mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.account_more_change_password:
                        startActivityForResult(new Intent(getActivity(), ChangePasswordActivity.class), RequestCodes.CHANGE_PASSWORD);
                        break;
                    case R.id.account_more_logout:
                        new Builder(getContext())
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to logout?")
                                .setPositiveButton("Yes", new OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogInterface, final int i) {
                                        Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();
                                        mPresenter.requestAccountLogout();
                                    }
                                }).setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                dialogInterface.cancel();
                            }
                        }).create().show();
                        break;
                }
                return true;
            }
        });
        mPresenter.getFoundCount();
        mPresenter.getLostCount();
        mPresenter.getReturnedCount();
        mPresenter.getHonestyRating();
    }

    private void setUpViewPagerAdapter() {
        AccountPagerAdapter accountPagerAdapter = new AccountPagerAdapter(getFragmentManager());
        accountPagerAdapter.addFragment(new AccountAboutFragment());
        accountPagerAdapter.addFragment(new AccountReportsFragment());
        accountPagerAdapter.addFragment(new AccountGalleryFragment());

        mAccountViewPager.setOffscreenPageLimit(accountPagerAdapter.getCount() - 1);
        mAccountViewPager.setAdapter(accountPagerAdapter);
        mAccountTabLayout.setupWithViewPager(mAccountViewPager);
    }

    @OnClick(R.id.account_chat_btn)
    public void onViewClicked() {
        mPresenter.showAccountMessages();
    }
}
