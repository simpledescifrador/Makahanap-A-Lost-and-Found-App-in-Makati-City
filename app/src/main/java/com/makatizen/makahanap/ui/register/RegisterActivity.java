package com.makatizen.makahanap.ui.register;

import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.makatizen.makahanap.BuildConfig;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.MakatizenAccount;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.loader.LoaderActivity;
import com.makatizen.makahanap.ui.login.LoginActivity;
import com.makatizen.makahanap.ui.main.MainActivity;
import javax.inject.Inject;

public class RegisterActivity extends BaseActivity implements RegisterMvpView {

    @Inject
    RegisterMvpPresenter<RegisterMvpView> mPresenter;

    @BindView(R.id.register_back_btn)
    ImageButton mRegisterBackBtn;

    @BindView(R.id.register_confirm_password_et)
    EditText mRegisterConfirmPasswordEt;

    @BindView(R.id.register_confirm_password_lbl)
    TextView mRegisterConfirmPasswordLbl;

    @BindView(R.id.register_iv_makatizencard)
    ImageView mRegisterIvMakatizencard;

    @BindView(R.id.register_layout)
    ScrollView mRegisterLayout;

    @BindView(R.id.register_makatizen_number_et)
    MaskedEditText mRegisterMakatizenNumberEt;

    @BindView(R.id.register_makatizen_verify_btn)
    Button mRegisterMakatizenVerifyBtn;

    @BindView(R.id.register_now_btn)
    Button mRegisterNowBtn;

    @BindView(R.id.register_password_et)
    EditText mRegisterPasswordEt;

    @BindView(R.id.register_password_lbl)
    TextView mRegisterPasswordLbl;

    @BindView(R.id.register_terms_and_policy_tv)
    TextView mRegisterTermsAndPolicyTv;

    @BindView(R.id.register_tv_makatizenNumberLabel)
    TextView mRegisterTvMakatizenNumberLabel;

    @BindView(R.id.register_tv_verifiedMakatizenCard)
    TextView mRegisterTvVerifiedMakatizenCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);

        setContentView(R.layout.activity_register);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    public void invalidMakatizenId() {
        mRegisterMakatizenNumberEt.setError("Makatizen ID not found");
    }

    @Override
    public void makatizenDataSuccess(final MakatizenAccount makatizenAccount) {

        //Get All Makatizen Data to a New Makahanap Account
        MakahanapAccount newAccount = new MakahanapAccount();
        newAccount.setPassword(mRegisterPasswordEt.getText().toString());
        newAccount.setMakatizenNumber(makatizenAccount.getMakatizenNumber());
        newAccount.setFirstName(makatizenAccount.getFirstName());
        newAccount.setMiddleName(makatizenAccount.getMiddleName());
        newAccount.setLastName(makatizenAccount.getLastName());
        newAccount.setAge(makatizenAccount.getAge());
        newAccount.setAddress(makatizenAccount.getAddress());
        newAccount.setGender(makatizenAccount.getGender());
        newAccount.setCivilStatus(makatizenAccount.getCivilStatus());
        newAccount.setContactNumber(makatizenAccount.getContactNumber());
        newAccount.setEmailAddress(makatizenAccount.getEmailAddress());
        newAccount.setProfileImageUrl(makatizenAccount.getProfileImageUrl());

        //Continue Register to Makahanap
        mPresenter.registerAccount(newAccount);
    }

    @Override
    public void onRegisterSuccessful(final int accountId) {
        //Proceed to the app
        mRegisterNowBtn.setEnabled(false);
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoaderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void resetButtonText() {
        mRegisterMakatizenVerifyBtn.setText("Verify");
        mRegisterNowBtn.setText("Register");
    }

    @OnClick({R.id.register_back_btn, R.id.register_makatizen_verify_btn, R.id.register_now_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_back_btn:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.register_makatizen_verify_btn:
                String makatizenId = mRegisterMakatizenNumberEt.getRawText();
                mRegisterMakatizenVerifyBtn.setText("Verifying . . .");
                mPresenter.verifyMakatizenId(makatizenId);
                break;
            case R.id.register_now_btn:
                String password = mRegisterPasswordEt.getText().toString();
                String confirmPassword = mRegisterConfirmPasswordEt.getText().toString();
                boolean isValidPassword = mPresenter.validatePassword(password);

                if (isValidPassword) {
                    boolean isPasswordMatched = mPresenter.verifyPasswordIsMatch(password, confirmPassword);
                    if (isPasswordMatched) {
                        mRegisterNowBtn.setText("Loading . . .");
                        mPresenter.getMakatizenData(mRegisterMakatizenNumberEt.getRawText());
                    }
                }
                break;
        }
    }

    @Override
    public void setConfirmPasswordError(final int stringId) {
        mRegisterConfirmPasswordEt.setError(getResources().getString(stringId));
        mRegisterConfirmPasswordEt.requestFocus();
        mRegisterConfirmPasswordEt.selectAll();
    }

    @Override
    public void setPasswordError(final int stringId) {
        mRegisterPasswordEt.setError(getResources().getString(stringId));
        mRegisterPasswordEt.requestFocus();
    }

    @Override
    public void validMakatizenId() {
        mRegisterMakatizenNumberEt.setEnabled(false);
        mRegisterMakatizenVerifyBtn.setEnabled(false);
        mRegisterPasswordEt.requestFocus();

        Toast.makeText(this, "Makatizen Card Verified", Toast.LENGTH_SHORT).show();

        //Show Register Fields
        mRegisterMakatizenVerifyBtn.setVisibility(View.GONE);
        mRegisterTvVerifiedMakatizenCard.setVisibility(View.VISIBLE);

        mRegisterPasswordLbl.setVisibility(View.VISIBLE);
        mRegisterConfirmPasswordLbl.setVisibility(View.VISIBLE);
        mRegisterPasswordEt.setVisibility(View.VISIBLE);
        mRegisterConfirmPasswordEt.setVisibility(View.VISIBLE);
        mRegisterTermsAndPolicyTv.setVisibility(View.VISIBLE);
        mRegisterNowBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void init() {

    }
}
