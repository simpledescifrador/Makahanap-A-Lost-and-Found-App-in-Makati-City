package com.makatizen.makahanap.ui.main.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.report.person.ReportPersonActivity;
import com.makatizen.makahanap.ui.report.personal_thing.ReportPersonalThingActivity;
import com.makatizen.makahanap.ui.report.pet.ReportPetActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;
import com.makatizen.makahanap.utils.enums.Type;
import java.util.Objects;
import javax.inject.Inject;

public class HomeFragment extends BaseFragment implements HomeMvpView {

    @BindView(R.id.home_report_found)
    CardView mHomeReportFound;

    @BindView(R.id.home_report_lost)
    CardView mHomeReportLost;

    @Inject
    HomeMvpPresenter<HomeMvpView> mPresenter;

    private Dialog mDialog;

    private TextView mOptionTvTitle;

    private LinearLayout mTypePtOption, mTypePetOption, mTypePersonOption;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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

    @OnClick({R.id.home_report_lost, R.id.home_report_found})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_report_lost:
                mPresenter.reportItem(Type.LOST);
                break;
            case R.id.home_report_found:
                mPresenter.reportItem(Type.FOUND);
                break;
        }
    }

    @Override
    public void showOptionDialog(final Type type) {
        mOptionTvTitle = mDialog.findViewById(R.id.option_tv_title);
        mTypePtOption = mDialog.findViewById(R.id.type_pt_option);
        mTypePetOption = mDialog.findViewById(R.id.type_pet_option);
        mTypePersonOption = mDialog.findViewById(R.id.type_person_option);
        mDialog.show();

        switch (type) {
            case LOST:
                mOptionTvTitle.setText(getResources().getString(R.string.title_type_lost));
                break;
            case FOUND:
                mOptionTvTitle.setText(getResources().getString(R.string.title_type_found));
                break;
        }

        mTypePtOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), ReportPersonalThingActivity.class);
                intent.putExtra(IntentExtraKeys.TYPE, type);
                startActivityForResult(intent, RequestCodes.REPORT_PT);
                mDialog.dismiss();
            }
        });

        mTypePetOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), ReportPetActivity.class);
                intent.putExtra(IntentExtraKeys.TYPE, type);
                startActivityForResult(intent, RequestCodes.REPORT_PET);
                mDialog.dismiss();
            }
        });

        mTypePersonOption.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getActivity(), ReportPersonActivity.class);
                intent.putExtra(IntentExtraKeys.TYPE, type);
                startActivityForResult(intent, RequestCodes.REPORT_PERSON);
                mDialog.dismiss();
            }
        });
    }

    @Override
    protected void init() {
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.dialog_type);
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
    }
}
