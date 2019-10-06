package com.makatizen.makahanap.ui.login.forgot_password;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.register.email_verification.EmailVerificationActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordMvpView {

    @Inject
    ForgotPasswordMvpPresenter<ForgotPasswordMvpView> mPresenter;
    @BindView(R.id.forgot_pass_makatizen_number)
    MaskedEditText mForgotPassMakatizenNumber;
    @BindView(R.id.forgot_pass_send_btn)
    Button mForgotPassSendBtn;
    @BindView(R.id.forgot_pass_password_req_lbl)
    TextView mForgotPassPasswordReqLbl;
    @BindView(R.id.forgot_pass_password_req)
    TextView mForgotPassPasswordReq;
    @BindView(R.id.forgot_pass_password_lbl)
    TextView mForgotPassPasswordLbl;
    @BindView(R.id.forgot_pass_password_et)
    EditText mForgotPassPasswordEt;
    @BindView(R.id.forgot_pass_confirm_password_lbl)
    TextView mForgotPassConfirmPasswordLbl;
    @BindView(R.id.forgot_pass_confirm_password_et)
    EditText mForgotPassConfirmPasswordEt;
    @BindView(R.id.forgot_pass_change_pass_btn)
    Button mForgotPassChangePassBtn;
    @BindView(R.id.forgot_pass_makatizen_number_lbl)
    TextView mForgotPassMakatizenNumberLbl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_forgot_password);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void init() {
        showBackButton(true);
        setTitle("Forgot Password");
        getSupportActionBar().setElevation(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick({R.id.forgot_pass_send_btn, R.id.forgot_pass_change_pass_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_pass_send_btn:
                String makatizenNumber = mForgotPassMakatizenNumber.getRawText();
                mPresenter.checkMakatizenNumber(makatizenNumber);
                break;
            case R.id.forgot_pass_change_pass_btn:
                String makatizen = mForgotPassMakatizenNumber.getRawText();
                String passwd = mForgotPassPasswordEt.getText().toString();
                String confPasswd = mForgotPassConfirmPasswordEt.getText().toString();

                boolean isValid = mPresenter.validatePassword(passwd, confPasswd);
                if (isValid) {
                    mPresenter.resetPassword(makatizen, passwd);
                }
                break;
        }
    }

    @Override
    public void setMakatizenNumberError(String error) {
        mForgotPassMakatizenNumber.setError(error);
        mForgotPassMakatizenNumber.requestFocus();
    }

    @Override
    public void onSuccessRegisteredMakatizen(final String email) {
        //Partially hide the email address by 4 characters
        final String mask = "*****";
        final int at = email.indexOf("@");
        if (at > 2) {
            final int maskLen = Math.min(Math.max(at / 2, 2), 4);
            final int start = (at - maskLen) / 2;

            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("The code will be sending to " + email.substring(0, start) + mask.substring(0, maskLen) + email.substring(start + maskLen))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mPresenter.sendEmailVerification(email);
                        }
                    })
                    .setNegativeButton("CANCEl", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();

        }


    }

    @Override
    public void successEmailVerification(String email) {
        Intent intent = new Intent(this, EmailVerificationActivity.class);
        intent.putExtra(IntentExtraKeys.EMAIL, email);
        startActivityForResult(intent, RequestCodes.EMAIL_VERIFICATION);
    }

    @Override
    public void setPasswordError(int resId) {
        mForgotPassPasswordEt.setError(getResources().getString(resId));
        mForgotPassPasswordEt.requestFocus();
    }

    @Override
    public void setConfirmPasswordError(int resId) {
        mForgotPassConfirmPasswordEt.setError(getResources().getString(resId));
        mForgotPassConfirmPasswordEt.requestFocus();
    }

    @Override
    public void onSuccessResetPassword() {
        Toast.makeText(this, "Your password is successfully changed.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFailedResetPassword() {
        Toast.makeText(this, "Failed to change your password.", Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void removeErrors() {
        mForgotPassMakatizenNumber.setError(null);
        mForgotPassMakatizenNumber.setError(null);
        mForgotPassConfirmPasswordEt.setError(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.EMAIL_VERIFICATION:
                    mForgotPassMakatizenNumberLbl.setVisibility(View.GONE);
                    mForgotPassMakatizenNumber.setVisibility(View.GONE);
                    mForgotPassSendBtn.setVisibility(View.GONE);

                    mForgotPassPasswordReqLbl.setVisibility(View.VISIBLE);
                    mForgotPassPasswordReq.setVisibility(View.VISIBLE);
                    mForgotPassPasswordLbl.setVisibility(View.VISIBLE);
                    mForgotPassPasswordEt.setVisibility(View.VISIBLE);
                    mForgotPassConfirmPasswordLbl.setVisibility(View.VISIBLE);
                    mForgotPassConfirmPasswordEt.setVisibility(View.VISIBLE);
                    mForgotPassChangePassBtn.setVisibility(View.VISIBLE);

                    break;

            }
        }
    }
}
