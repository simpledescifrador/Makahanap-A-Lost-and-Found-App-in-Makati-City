package com.makatizen.makahanap.ui.register.email_verification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailVerificationActivity extends BaseActivity implements EmailVerificationMvpView {
    @Inject
    EmailVerificationMvpPresenter<EmailVerificationMvpView> mPresenter;
    @BindView(R.id.email_verification_code)
    EditText mEmailVerificationCode;
    @BindView(R.id.email_verification_continue_btn)
    Button mEmailVerificationContinueBtn;
    @BindView(R.id.email_verification_expiration)
    TextView mEmailVerificationExpiration;
    @BindView(R.id.email_verification_resend)
    TextView mEmailVerificationResend;
    private String mEmail = "";
    private CountDownTimer countDownTimer;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getExtras() != null) {
                String email = intent.getStringExtra("email");
                String code = intent.getStringExtra("code");
                mEmailVerificationCode.setText(code);
                Toast.makeText(EmailVerificationActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                mPresenter.verifyCode(email, code);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver, new IntentFilter("com.makatizen.makahanap.email_verification"));
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_email_verification);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void init() {
        mEmail = getIntent().getStringExtra(IntentExtraKeys.EMAIL);
        showBackButton(true);
        setTitle("Verify Email");
        getSupportActionBar().setElevation(0);
        mPresenter.startExpirationCountdown();
    }

    @OnClick({R.id.email_verification_continue_btn, R.id.email_verification_resend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.email_verification_continue_btn:
                String verificationCode = mEmailVerificationCode.getText().toString();
                boolean isValid = mPresenter.validateCode(verificationCode);

                if (isValid) {
                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
                    mPresenter.verifyCode(mEmail, verificationCode);
                }

                break;
            case R.id.email_verification_resend:
                Toast.makeText(this, "Resending...", Toast.LENGTH_SHORT).show();
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(EmailVerificationActivity.this, "Error Registering Token", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                mPresenter.emailVerification(mEmail, token);
                            }
                        });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        countDownTimer.cancel();
        mPresenter.detachView();
    }

    @Override
    public void setExpirationTime(long expirationTime) {
        countDownTimer = new CountDownTimer(expirationTime, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                mEmailVerificationExpiration.setText(timeLeftFormatted);
            }

            public void onFinish() {
                mEmailVerificationExpiration.setText("Expired!");
            }

        }.start();
    }

    @Override
    public void setCodeError(String error) {
        mEmailVerificationCode.setError(error);
        mEmailVerificationCode.requestFocus();
    }

    @Override
    public void onSuccessfulValidation() {
        Toast.makeText(this, "Email is verified successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        countDownTimer.cancel();
        finish();
    }

    @Override
    public void onSuccessResendVerificationCode() {
        Toast.makeText(this, "New email verification code was sent to you", Toast.LENGTH_LONG).show();
    }
}
