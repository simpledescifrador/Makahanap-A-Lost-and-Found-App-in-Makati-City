package com.makatizen.makahanap.ui.register.sms_verification;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsVerificationActivity extends BaseActivity implements SmsVerificationMvpView {

    private final int MAX_ATTEMPTS = 2;
    @Inject
    SmsVerificationMvpPresenter<SmsVerificationMvpView> mPresenter;
    @BindView(R.id.sms_verification_code)
    EditText mSmsVerificationCode;
    @BindView(R.id.sms_verification_continue_btn)
    Button mSmsVerificationContinueBtn;
    @BindView(R.id.sms_verification_attempts)
    TextView mSmsVerificationAttempts;
    @BindView(R.id.sms_verification_resend)
    TextView mSmsVerificationResend;
    @BindView(R.id.sms_verification_expiration)
    TextView mSmsVerificationExpiration;
    private String mRequestId;
    private String mPhoneNumber;
    private int attempts = 0;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_sms_verification);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void init() {
        mRequestId = getIntent().getStringExtra(IntentExtraKeys.REQUEST_ID);
        mPhoneNumber = getIntent().getStringExtra(IntentExtraKeys.PHONE_NUMBER);
        showBackButton(true);
        setTitle("Verify Phone Number");
        getSupportActionBar().setElevation(0);
        mPresenter.startExpirationCountdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest(mRequestId);
        countDownTimer.cancel();
        mPresenter.detachView();
    }

    @OnClick({R.id.sms_verification_continue_btn, R.id.sms_verification_resend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sms_verification_continue_btn:
                String code = mSmsVerificationCode.getText().toString();
                boolean isValid = mPresenter.validateCode(code, attempts);

                if (isValid) {
                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
                    mPresenter.checkRequest(code, mRequestId);
                }
                break;
            case R.id.sms_verification_resend:
                Toast.makeText(this, "Requesting a new code", Toast.LENGTH_SHORT).show();
                mPresenter.resendRequest(mPhoneNumber, mRequestId);
                break;
        }
    }

    @Override
    public void setCodeError(String error) {
        mSmsVerificationCode.setError(error);
        mSmsVerificationCode.requestFocus();
    }

    @Override
    public void consumeAttempts() {
        attempts++;
        int validAttempts = MAX_ATTEMPTS - attempts;
        mSmsVerificationAttempts.setText(String.valueOf(validAttempts));
    }

    @Override
    public void onSuccessfulVerification() {
        Toast.makeText(this, "Phone Number is successfully verified", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void resetAttempts() {
        mSmsVerificationAttempts.setText(String.valueOf(MAX_ATTEMPTS));
    }

    @Override
    public void onSuccessfulResendCode(String requestId) {
        mRequestId = requestId;
        mSmsVerificationCode.setText("");
        mSmsVerificationCode.requestFocus();
    }

    @Override
    public void setExpirationTime(long expirationTime) {
        countDownTimer = new CountDownTimer(expirationTime, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                mSmsVerificationExpiration.setText(timeLeftFormatted);
            }

            public void onFinish() {
                mSmsVerificationExpiration.setText("Expired!");
            }

        }.start();
    }

}
