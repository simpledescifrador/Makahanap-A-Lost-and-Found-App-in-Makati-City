package com.makatizen.makahanap.ui.transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.AppAlertDialog;
import com.makatizen.makahanap.utils.DateDialogFragmentV2;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.TimeDialogFragment;

import java.util.Arrays;
import java.util.Calendar;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetTransactionActivity extends BaseActivity implements MeetTransactionMvpView, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Inject
    MeetTransactionMvpPresenter<MeetTransactionMvpView> mPresenter;
    @BindView(R.id.meetup_date)
    MaskedEditText mMeetupDate;
    @BindView(R.id.meetup_datepicker_btn)
    ImageButton mMeetupDatepickerBtn;
    @BindView(R.id.meetup_time)
    EditText mMeetupTime;
    @BindView(R.id.meetup_timepicker_btn)
    ImageButton mMeetupTimepickerBtn;
    @BindView(R.id.meetup_submit_btn)
    Button mMeetupSubmitBtn;
    @Inject
    AppAlertDialog mAppAlertDialog;

    private LocationData mLocationData = new LocationData();
    private String mTimeSelected = "";
    private int mTransactionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);

        setContentView(R.layout.activity_meet_transaction);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void init() {
        showBackButton(true);
        setTitle("Setup meeting place");
        mTransactionId = getIntent().getIntExtra(IntentExtraKeys.TRANSACTION_ID, 0);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.meetup_location_fragment);
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(14.530872, 121.022232),
                new LatLng(14.568527, 121.045865));
        autocompleteFragment.setLocationBias(bounds);
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteFragment.a.setTextSize(14);
        autocompleteFragment.a.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }

            @Override
            public void onPlaceSelected(Place place) {
                mLocationData.setId(place.getId());
                mLocationData.setName(place.getName());
                mLocationData.setAddress(place.getAddress());
                mLocationData.setLatlng(place.getLatLng());
            }
        });
    }

    @OnClick({R.id.meetup_datepicker_btn, R.id.meetup_timepicker_btn, R.id.meetup_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.meetup_datepicker_btn:
                mMeetupDate.setError(null);
                DateDialogFragmentV2 datePicker = new DateDialogFragmentV2();
                datePicker.show(getSupportFragmentManager(), "date_picker");
                break;
            case R.id.meetup_timepicker_btn:
                mMeetupTime.setError(null);
                TimeDialogFragment timePicker = new TimeDialogFragment();
                timePicker.show(getSupportFragmentManager(), "time_picker");
                break;
            case R.id.meetup_submit_btn:
                final String dateTime = mMeetupDate.getText() + " " + mTimeSelected;
                boolean isValid = mPresenter.validateForm(mLocationData, mMeetupDate.getText().toString(), mTimeSelected);
                if (isValid) {
                    new AlertDialog.Builder(this)
                            .setTitle("Submit")
                            .setMessage("Are you sure to submit now?")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.submitMeetingSetup(mLocationData, dateTime, String.valueOf(mTransactionId));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = String.format("%d", year) + String.format("%02d", month + 1) + String
                .format("%02d", dayOfMonth);
        mMeetupDate.setText(selectedDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        int hour = hourOfDay;
        int minutes = minute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        String mTime = new StringBuilder().append(hour).append(':')
                .append(min).append(" ").append(timeSet).toString();
        mTimeSelected = hourOfDay + ":" + minute;
        mMeetupTime.setText(mTime);
    }

    @Override
    public void onSubmitSuccess(final int meetUpId) {
        final Dialog dialog = mAppAlertDialog.showCustomDialog(AppAlertDialog.AlertType.SUCCESS, "Meetup Place Submit Success",
                "Thank you for your kindness", "Okay", null);
        if (dialog == null) {
            return;
        }
        dialog.findViewById(R.id.custom_alert_btn_positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(IntentExtraKeys.MEET_ID, meetUpId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dialog.findViewById(R.id.custom_alert_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(IntentExtraKeys.MEET_ID, meetUpId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void setDateError(String dateError) {
        mMeetupDate.setError(dateError);
        mMeetupDate.requestFocus();
    }

    @Override
    public void setTimeError(String timeError) {
        mMeetupTime.setError(timeError);
        mMeetupTime.requestFocus();
    }
}
