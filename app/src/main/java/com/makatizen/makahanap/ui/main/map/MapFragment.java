package com.makatizen.makahanap.ui.main.map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.VectorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseFragment;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.PermissionUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RequestCodes;
import com.makatizen.makahanap.utils.SimpleDividerItemDecoration;
import com.makatizen.makahanap.utils.enums.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFragment extends BaseFragment implements MapMvpView, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, RecyclerItemUtils.OnItemClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final float DEFAULT_ZOOM = 14f;
    @Inject
    MapMvpPresenter<MapMvpView> mPresenter;
    @Inject
    MapItemsAdapter mMapItemsAdapter;
    @Inject
    PermissionUtils mPermissionUtils;
    @Inject
    BottomSheetDialog mBottomSheetDialog;
    @BindView(R.id.main_map_refresh_btn)
    ImageButton mMainMapRefreshBtn;
    @BindView(R.id.main_map_current_location_btn)
    ImageButton mMainMapCurrentLocationBtn;
    @BindView(R.id.main_map_updating)
    LinearLayout mMainMapUpdating;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setUnBinder(ButterKnife.bind(this, view));
        init();
        return view;
    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable vectorDrawable = (VectorDrawable) getContext().getDrawable(id);

            int h = vectorDrawable.getIntrinsicHeight();
            int w = vectorDrawable.getIntrinsicWidth();

            vectorDrawable.setBounds(0, 0, w, h);

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);

        } else {
            return BitmapDescriptorFactory.fromResource(id);
        }
    }

    @Override
    protected void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);
        AutocompleteSupportFragment searchPlacesOnMap = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.main_map_autocomplete_fragment);
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(14.530872, 121.022232),
                new LatLng(14.568527, 121.045865));
        searchPlacesOnMap.setLocationBias(bounds);
        // Specify the types of place data to return.
        searchPlacesOnMap.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));
        searchPlacesOnMap.a.setTextSize(14);
        searchPlacesOnMap.a.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        // Set up a PlaceSelectionListener to handle the response.
        searchPlacesOnMap.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }

            @Override
            public void onPlaceSelected(Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        place.getLatLng(), 17f));
                mPresenter.getLocationItems(place.getId(), place.getName());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Get the current location of the device and set the position of the map.
        if (mLocationPermissionGranted) {
            mPresenter.loadMapItems();
        } else {
            getLocationPermission();
        }
    }

    private void getLocationPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            mLocationPermissionGranted = true;
                            mPresenter.loadMapItems();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            mPermissionUtils.showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            return isMapsEnabled();
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, RequestCodes.ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    public boolean isServicesOK() {
        Log.d("GoogleServices", "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("GoogleServices", "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("GoogleServices", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, 0);
            dialog.show();
        } else {
            Toast.makeText(getContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCodes.ENABLE_GPS: {
                if (mLocationPermissionGranted) {
                    mPresenter.loadMapItems();
                } else {
                    getLocationPermission();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void showCurrentLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = location;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            mMap.setMyLocationEnabled(true);
                        } else {
                            Log.d("Map", "Current location is null. Using defaults.");
                            LatLng defaultLocation = new LatLng(14.556586, 121.023415);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void setMarkers(List<FeedItem> feedItems) {
        mMap.setOnMarkerClickListener(this);
        for (FeedItem item : feedItems) {
            int markerLogo = (item.getItemType() == Type.LOST) ? R.drawable.ic_map_marker_lost : R.drawable.ic_map_marker_found;
            createMarker(item.getLocationLatlng().latitude, item.getLocationLatlng().longitude, item.getItemTitle(), "", markerLogo);
        }
    }

    @Override
    public void setMapMarker(String tag, String title, String snippet, LatLng location) {
        mMainMapUpdating.setVisibility(View.GONE);
        mMainMapRefreshBtn.setEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        createMarker(location.latitude, location.longitude, title, snippet, R.drawable.map_icon)
                .setTag(tag);
    }

    @Override
    public void showLocationItems(List<FeedItem> feedItems, String title, int lost, int found) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_map_items, null);
        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.show();

        RecyclerView mapItemRv = dialogView.findViewById(R.id.map_items_rv);
        TextView locationTitle = dialogView.findViewById(R.id.map_items_location);
        locationTitle.setText(title);

        TextView lostText = dialogView.findViewById(R.id.map_items_lost);
        TextView foundText = dialogView.findViewById(R.id.map_items_found);

        lostText.setText("Lost: " + lost);
        foundText.setText("Found: " + found);
        mapItemRv.setAdapter(mMapItemsAdapter);
        mapItemRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mapItemRv.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        RecyclerItemUtils.addTo(mapItemRv).setOnItemClickListener(this);
        Collections.reverse(feedItems);
        mMapItemsAdapter.setData(feedItems);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mPresenter.getLocationItems(marker.getTag().toString(), marker.getTitle());
        return false;
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        int itemId = mMapItemsAdapter.getItem(position).getItemId();
        Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
        intent.putExtra(IntentExtraKeys.ITEM_ID, itemId);
        startActivityForResult(intent, RequestCodes.ITEM_DETAILS);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        mPresenter.getLocationItems(marker.getTag().toString(), marker.getTitle());
    }

    @OnClick({R.id.main_map_refresh_btn, R.id.main_map_current_location_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_map_refresh_btn:
                mMainMapRefreshBtn.setEnabled(false);
                mMainMapUpdating.setVisibility(View.VISIBLE);
                mPresenter.updateMap();
                break;
            case R.id.main_map_current_location_btn:
                showCurrentLocation();
                break;
        }
    }
}
