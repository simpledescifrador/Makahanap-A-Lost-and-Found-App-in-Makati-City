package com.makatizen.makahanap.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.makatizen.makahanap.R;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import java.util.Objects;
import javax.inject.Inject;

public class AppAlertDialog {

    public enum AlertType {
        INFO, SUCCESS, WARNING, DANGER
    }

    private Activity mActivity;

    @Inject
    public AppAlertDialog(final Activity activity) {
        mActivity = activity;
    }

    public Dialog showCustomDialog(AlertType alertType, String title, String message, String positiveText, String animationPath) {
        Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.dialog_custom_alert);
        TextView titleText = dialog.findViewById(R.id.custom_alert_tv_title);
        TextView messageText = dialog.findViewById(R.id.custom_alert_tv_message);
        LottieAnimationView animationView = dialog.findViewById(R.id.custom_alert_animation);
        Button positiveBtn = dialog.findViewById(R.id.custom_alert_btn_positive);

        titleText.setText(title);
        messageText.setText(message);
        positiveBtn.setText(positiveText);

        switch (alertType) {
            case INFO:
                positiveBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.all_btn_info));
                break;
            case SUCCESS:
                positiveBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.all_btn_success));
                animationView.setAnimation("animations/star_success.json");
                break;
            case WARNING:
                positiveBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.all_btn_warning));
                animationView.setAnimation("animations/warning.json");
                break;
            case DANGER:
                positiveBtn.setBackground(mActivity.getResources().getDrawable(R.drawable.all_btn_danger));
                animationView.setAnimation("animations/error_404.json");
                break;
        }

        if (animationPath != null) {
            animationView.setAnimation(animationPath);
        }

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        dialog.setCancelable(false);
        return dialog;
    }

    public FancyAlertDialog.Builder showSuccessAlertDialog(String title, String message, String positiveText,
            String negativeText) {
        FancyAlertDialog.Builder alertdialogBuilder = new FancyAlertDialog.Builder(mActivity)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(mActivity.getResources().getColor(R.color.defaultGreen))
                .setPositiveBtnBackground(mActivity.getResources().getColor(R.color.defaultGreen))
                .setPositiveBtnText(positiveText)
                .setNegativeBtnText(negativeText)
                .setAnimation(Animation.POP)
                .setIcon(R.drawable.ic_check_white_24dp, Icon.Visible);
        return alertdialogBuilder;
    }
}
