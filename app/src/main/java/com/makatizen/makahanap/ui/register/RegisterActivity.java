package com.makatizen.makahanap.ui.register;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.MakatizenAccount;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.loader.LoaderActivity;
import com.makatizen.makahanap.ui.login.LoginActivity;
import com.makatizen.makahanap.ui.register.email_verification.EmailVerificationActivity;
import com.makatizen.makahanap.ui.register.sms_verification.SmsVerificationActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;

import java.util.Objects;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.register_password_toggle)
    ImageButton mRegisterPasswordToggle;
    @BindView(R.id.register_to_login)
    TextView mRegisterToLogin;
    @BindView(R.id.register_policy)
    TextView mRegisterPolicy;
    @BindView(R.id.register_password_requirements_lbl)
    TextView mRegisterPasswordRequirementsLbl;
    @BindView(R.id.register_password_requirements)
    TextView mRegisterPasswordRequirements;
    private boolean mShowPassword = false;

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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.EMAIL_VERIFICATION:
                    validMakatizenId();
                    break;
                case RequestCodes.SMS_VERIFICATION:
                    validMakatizenId();
                    break;

            }
        }
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
        intent.putExtra(IntentExtraKeys.ACCOUNT_ID, accountId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.register_policy, R.id.register_back_btn, R.id.register_makatizen_verify_btn, R.id.register_now_btn, R.id.register_password_toggle, R.id.register_to_login})
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
            case R.id.register_password_toggle:
                if (mShowPassword) {
                    mPresenter.passwordToggle(true);
                } else {
                    mPresenter.passwordToggle(false);
                }
                break;
            case R.id.register_to_login:
                Intent intentRegister = new Intent(this, LoginActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.register_policy:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_webview);
                dialog.setTitle("Our Privacy Policy");
                dialog.getWindow().setLayout(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();

                WebView webView = dialog.findViewById(R.id.web_view);

                webView.loadUrl("http://makahanap.x10host.com/Deadlinks_controller/privacy_policy");
                break;
        }
    }

    @Override
    public void removeErrors() {
        mRegisterMakatizenNumberEt.setError(null);
        mRegisterPasswordEt.setError(null);
        mRegisterConfirmPasswordEt.setError(null);
    }

    @Override
    public void resetButtonText() {
        mRegisterMakatizenVerifyBtn.setText("Verify");
        mRegisterNowBtn.setText("Register");
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

        mRegisterPasswordRequirementsLbl.setVisibility(View.VISIBLE);
        mRegisterPasswordRequirements.setVisibility(View.VISIBLE);
        mRegisterPasswordLbl.setVisibility(View.VISIBLE);
        mRegisterConfirmPasswordLbl.setVisibility(View.VISIBLE);
        mRegisterPasswordEt.setVisibility(View.VISIBLE);
        mRegisterConfirmPasswordEt.setVisibility(View.VISIBLE);
        mRegisterTermsAndPolicyTv.setVisibility(View.VISIBLE);
        mRegisterNowBtn.setVisibility(View.VISIBLE);
        mRegisterPolicy.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPassword() {
        mRegisterPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mRegisterPasswordToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_grey_700_24dp));
        mShowPassword = false;
    }

    @Override
    public void hidePassword() {
        mRegisterPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        mRegisterPasswordToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_grey_700_24dp));
        mShowPassword = true;
    }

    @Override
    public void showVerificationOption(final String email, final String contactNumber) {
        mRegisterMakatizenVerifyBtn.setText("Verify");
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.verification_option);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.show();

        TextView emailText = dialog.findViewById(R.id.verification_option_email_address);
        final TextView phoneNumberText = dialog.findViewById(R.id.verification_option_contact_number);

        //Partially hide the email address by 4 characters
        final String mask = "*****";
        final int at = email.indexOf("@");
        if (at > 2) {
            final int maskLen = Math.min(Math.max(at / 2, 2), 4);
            final int start = (at - maskLen) / 2;
            emailText.setText(email.substring(0, start) + mask.substring(0, maskLen) + email.substring(start + maskLen));
        }

//        emailText.setText(email.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*")); //Show First 3 Characters
        char ch1 = contactNumber.charAt(0);
        String formattedPhoneNumber = "";
        formattedPhoneNumber = contactNumber.startsWith("0") ? "63" + contactNumber.substring(1) : contactNumber;
        phoneNumberText.setText("+" + formattedPhoneNumber);

        dialog.findViewById(R.id.verification_option_via_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Error Registering Token", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                mPresenter.emailVerification(email, token);
                            }
                        });
                dialog.dismiss();
            }
        });

        final String finalFormattedPhoneNumber = formattedPhoneNumber;
        dialog.findViewById(R.id.verification_option_via_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.smsVerification(finalFormattedPhoneNumber);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void successfulEmailVerificationRequest(String email) {
        Intent intent = new Intent(this, EmailVerificationActivity.class);
        intent.putExtra(IntentExtraKeys.EMAIL, email);
        startActivityForResult(intent, RequestCodes.EMAIL_VERIFICATION);
    }

    @Override
    public void successfulSmsVerificationRequest(String requestId, String phoneNumber) {
        Intent intent = new Intent(this, SmsVerificationActivity.class);
        intent.putExtra(IntentExtraKeys.REQUEST_ID, requestId);
        intent.putExtra(IntentExtraKeys.PHONE_NUMBER, phoneNumber);
        startActivityForResult(intent, RequestCodes.SMS_VERIFICATION);
    }

    @Override
    protected void init() {
        showBackButton(true);
        setTitle("Register");
        getSupportActionBar().setElevation(0);
    }
}
