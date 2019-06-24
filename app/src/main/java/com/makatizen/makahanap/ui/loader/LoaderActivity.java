package com.makatizen.makahanap.ui.loader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.main.MainActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import javax.inject.Inject;

public class LoaderActivity extends BaseActivity implements LoaderMvpView {

    @BindView(R.id.loader_iv_logo)
    ImageView mLoaderIvLogo;

    @BindView(R.id.loader_ts_messages)
    TextSwitcher mLoaderTsMessages;

    @Inject
    LoaderPresenter<LoaderMvpView> mPresenter;

    private int mCurrentIndex = 0;

    private String[] mLoadingMessages = {
            "Preparing app resources",
            "Getting your account data",
            "Retrieving all barangay repositories",
            "Sending your registration token",
            "Loading the app . . ."
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this); /*  */
        mPresenter.attachView(this);
        setContentView(R.layout.activity_loader);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void nextLoadingMessage() {
        mCurrentIndex++; // Increment by 1 to switch text message
        mLoaderTsMessages.setText(mLoadingMessages[mCurrentIndex]);
    }

    @Override
    public void onCompletedLoader() {
        // Load Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finishAfterTransition();
        } else {
            finish();
        }

    }

    @Override
    public void onSuccessGetAccountData() {
        // Next Task is to Get the All barangay data
        mPresenter.getAllBarangayData();
    }

    @Override
    public void onSuccessGetAllBarangayData() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM_TOKEN", "getInstanceId failed", task.getException());
                            Toast.makeText(LoaderActivity.this, "Error Registering Token", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        mPresenter.registerAccountToken(token);
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("FCM_TOKEN", token);
//                        Toast.makeText(LoaderActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void init() {
        final Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateAnimation.setFillAfter(true);

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mLoaderTsMessages.setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                TextView messageText = new TextView(LoaderActivity.this);
                messageText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                messageText.setTextSize(14);
                messageText.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                return messageText;
            }
        });

        // Declare the in and out animations and initialize them
        final Animation inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        final Animation outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        mLoaderTsMessages.setInAnimation(inAnimation);
        mLoaderTsMessages.setOutAnimation(outAnimation);

        //Init the first Loading message
        mLoaderTsMessages.setText(mLoadingMessages[mCurrentIndex]);

        mLoaderIvLogo.startAnimation(rotateAnimation); //Start Loading Animation

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int accountId = getIntent().getIntExtra(IntentExtraKeys.ACCOUNT_ID, 0);
                mPresenter.getCurrentAccountData(accountId);
            }
        }, 2000);
    }
}
