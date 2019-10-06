package com.makatizen.makahanap.ui.main.account.change_password;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordMvpView {

    @Inject
    ChangePasswordMvpPresenter<ChangePasswordMvpView> mPresenter;
    @BindView(R.id.change_pass_password_et)
    EditText mChangePassPasswordEt;
    @BindView(R.id.change_pass_confirm_pass_et)
    EditText mChangePassConfirmPassEt;
    @BindView(R.id.change_pass_btn)
    Button mChangePassBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_change_password);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void init() {
        showBackButton(true);
        setTitle("Change Password");
        getSupportActionBar().setElevation(0);
    }

    @Override
    public void setPasswordError(String error) {
        mChangePassPasswordEt.setError(error);
        mChangePassPasswordEt.requestFocus();
    }

    @Override
    public void setConfirmPasswordError(String error) {
        mChangePassConfirmPassEt.setError(error);
        mChangePassConfirmPassEt.requestFocus();
    }

    @Override
    public void onSuccessChangePassword() {
        Toast.makeText(this, "Successfully Change Password", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.change_pass_btn)
    public void onViewClicked() {
        final String newPassword = mChangePassPasswordEt.getText().toString().trim();
        String confirmPassword = mChangePassConfirmPassEt.getText().toString().trim();
        boolean isValid = mPresenter.validateNewPassword(newPassword);
        if (isValid) {
            boolean isConfirmed = mPresenter.confirmPassword(newPassword, confirmPassword);
            if (isConfirmed) {
                LayoutInflater layoutInflater = LayoutInflater
                        .from(getApplicationContext());
                View promptView = layoutInflater
                        .inflate(R.layout.dialog_input, null);
                final EditText input = promptView
                        .findViewById(R.id.input_et);
                input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new AlertDialog.Builder(this)
                        .setTitle("Enter your current password")
                        .setCancelable(true)
                        .setView(promptView)
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.changePassword(newPassword, input.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        }
    }
}
