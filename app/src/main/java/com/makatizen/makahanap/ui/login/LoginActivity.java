package com.makatizen.makahanap.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.loader.LoaderActivity;
import com.makatizen.makahanap.ui.register.RegisterActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    @BindView(R.id.login_btn_login)
    Button mLoginBtnLogin;

    @BindView(R.id.login_et_makatizen_number)
    MaskedEditText mLoginEtMakatizenNumber;

    @BindView(R.id.login_et_password)
    EditText mLoginEtPassword;

    @BindView(R.id.login_iv_app_logo)
    ImageView mLoginIvAppLogo;

    @BindView(R.id.login_tv_forgot_password)
    TextView mLoginTvForgotPassword;

    @BindView(R.id.login_tv_register)
    TextView mLoginTvRegister;

    @Inject
    LoginPresenter<LoginMvpView> mPresenter;
    @BindView(R.id.login_iv_password_toggle)
    ImageButton mLoginIvPasswordToggle;

    private boolean mShowPassword = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_login);

        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onLoginFailed() {
        mLoginBtnLogin.setEnabled(true);
        mLoginBtnLogin.setText("Login");
    }

    @Override
    public void onLoginRequest() {
        //Change Button text to loading . . .
        mLoginBtnLogin.setEnabled(false);
        mLoginBtnLogin.setText("Loading . . .");
    }

    @Override
    public void onLoginSuccessful(final int accountId) {
        //Process to the app
        mLoginBtnLogin.setEnabled(false);
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoaderActivity.class);
        intent.putExtra(IntentExtraKeys.ACCOUNT_ID, accountId);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.login_btn_login, R.id.login_tv_register, R.id.login_iv_password_toggle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                String makatizenNumber = mLoginEtMakatizenNumber.getRawText();
                String password = mLoginEtPassword.getText().toString();

                boolean isValidDetails = mPresenter.validateLoginDetails(makatizenNumber, password);

                if (isValidDetails) {
                    mPresenter.doAppLogin(makatizenNumber, password);
                }

                break;
            case R.id.login_tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_iv_password_toggle:
                if (mShowPassword) {
                    mPresenter.passwordToggle(true);
                } else {
                    mPresenter.passwordToggle(false);
                }
                break;
        }
    }

    @Override
    public void removeErrors() {
        mLoginEtMakatizenNumber.setError(null);
        mLoginEtPassword.setError(null);
    }

    @Override
    public void setMakatizenNumberError(final int stringId) {
        mLoginEtMakatizenNumber.setError(getResources().getString(stringId));
        mLoginEtMakatizenNumber.requestFocus();
    }

    @Override
    public void setPasswordError(final int stringId) {
        mLoginEtPassword.setError(getResources().getString(stringId));
        mLoginEtPassword.requestFocus();
    }

    @Override
    public void showPassword() {
        mLoginEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mLoginIvPasswordToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_grey_700_24dp));
        mShowPassword = false;
    }

    @Override
    public void hidePassword() {
        mLoginEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        mLoginIvPasswordToggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_grey_700_24dp));
        mShowPassword = true;

    }

    @Override
    protected void init() {

    }

}
