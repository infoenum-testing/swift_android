package com.swift.dating.ui.where_do_you_live;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import com.swift.dating.R;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.common.CommonUtils;
import com.swift.dating.data.network.Resource;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.BaseModel;
import com.swift.dating.model.requestmodel.DeluxeTokenCountModel;
import com.swift.dating.model.requestmodel.LocationModel;
import com.swift.dating.model.responsemodel.ProfileOfUser;
import com.swift.dating.ui.homeScreen.fragment.SearchFragment;
import com.swift.dating.ui.homeScreen.viewmodel.HomeViewModel;
import com.swift.dating.ui.loginScreen.LoginActivity;
import im.delight.android.location.SimpleLocation;

import static com.swift.dating.common.AppConstants.LICENSE_KEY;

public class WhereYouLiveActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, CommonDialogs.onProductConsume, BillingProcessor.IBillingHandler {

    private static final String TAG = "WhereYouLiveActivity";
    String lat, lon;
    boolean isFromPlace = false;
    private GoogleMap mMap;
    private ImageView image_back;
    private Button btn_set_crt_loc, btn_save_loc;
    private SimpleLocation simpleLocation;
    private LatLng latLng;
    private LinearLayout addressLayout;
    private TextView tv_addres;
    private int AUTOCOMPLETE_REQUEST_CODE = 1001;
    private HomeViewModel homeViewModel;
    private ProgressDialog mProgressDialog;
    private SharedPreference sp;
    private BillingProcessor bp;
    private ConstraintLayout constraint_main;
    private String productId, tokenSType;
    private double price;
    private int selectedPosition;
    private boolean isLocationNull = false;
    private boolean isFromCrtLoc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        simpleLocation = new SimpleLocation(this);
        initViews();
        initBillingProcess();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDUuwEiAxFjmCcFkCm-DVIaKhSJRba_mtI");//getResources().getString(R.string.google_maps_key)
        }
    }

    /**
     * **  Method to Initialize Billing Process
     */
    private void initBillingProcess() {
        bp = new BillingProcessor(WhereYouLiveActivity.this, LICENSE_KEY, WhereYouLiveActivity.this);
        bp.initialize();
    }

    private void initViews() {
        sp = new SharedPreference(this);
        constraint_main = findViewById(R.id.constraint_main);
        addressLayout = findViewById(R.id.addressLayout);
        tv_addres = findViewById(R.id.tv_addres);
        image_back = findViewById(R.id.image_back);
        btn_save_loc = findViewById(R.id.btn_save_loc);
        btn_set_crt_loc = findViewById(R.id.btn_set_crt_loc);
        image_back.setOnClickListener(this::oClick);
        btn_set_crt_loc.setOnClickListener(this::oClick);
        btn_save_loc.setOnClickListener(this::oClick);
        addressLayout.setOnClickListener(this::oClick);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.sendLatLongResponse().observe(WhereYouLiveActivity.this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(Resource<BaseModel> resource) {
                if (resource == null)
                    return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackBar(constraint_main, resource.data.getMessage());
                        if (resource.data.getError() != null && resource.data.getError().getCode().equalsIgnoreCase("401"))
                            openActivityOnTokenExpire();
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getError() != null && resource.data.getError().getCode().contains("401")) {
                            openActivityOnTokenExpire();
                        } else {
                            BaseModel model = resource.data;
                            // showSnackBar(constraint_main, model.getMessage());
                            ProfileOfUser obj = new Gson().fromJson(sp.getUser(), ProfileOfUser.class);
                            obj.setLatitude("" + model.getLatitude());
                            obj.setLongitude("" + model.getLongitude());
                            sp.saveUserData(obj, sp.getProfileCompleted());
                            sp.saveLocation(true);
                            if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.SearchResponse))) {
                                sp.removeKey(SearchFragment.SearchResponse);
                            }
                            if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.FilterResponse))) {
                                sp.removeKey(SearchFragment.FilterResponse);
                            }
                            sp.saveisSettingsChanged(true);
                            finish();
                        }
                        break;
                }
            }
        });

        homeViewModel.addDeluxeResponse().observe(this, new Observer<Resource<BaseModel>>() {
            @Override
            public void onChanged(Resource<BaseModel> resource) {
                if (resource == null)
                    return;
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        hideLoading();
                        if (resource.data.getSuccess()) {
                            sp.savePremium(false);
                            sp.saveDeluxe(true);
                        } else if (resource.code == 401) {
                            openActivityOnTokenExpire();
                        } else {
                            showSnackBar(constraint_main, "Something went wrong");
                        }
                        break;
                    case ERROR:
                        hideLoading();
                        showSnackBar(constraint_main, resource.message);
                        break;
                }
            }
        });
        Intent intent = getIntent();
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("lat")) && !TextUtils.isEmpty(intent.getStringExtra("lon"))) {
            lat = intent.getStringExtra("lat");
            lon = intent.getStringExtra("lon");
            tv_addres.setText(CommonUtils.getAddress(this, lat, lon));
            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        } else {
            latLng = new LatLng(simpleLocation.getLatitude(), simpleLocation.getLongitude());
        }
    }

    public void openActivityOnTokenExpire() {
        String deviceToken = sp.getDeviceToken();
        sp.clearData();
        sp.saveDeviceToken(deviceToken);
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void oClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.addressLayout:
                ActionPlacesAPI();
                break;
            case R.id.btn_save_loc:
                if (CommonUtils.checkLocationEnabled(this)) {
                    if (sp.getDeluxe()) {
                        if (!isLocationNull) {
                            showLoading();
                            homeViewModel.sendLatLong(new LocationModel("" + latLng.latitude, "" + latLng.longitude));
                        } else {
                            new AlertDialog.Builder(this).setMessage("You cannot choose this location.").setPositiveButton("OK", null).show();
                        }
                    } else {
                        CommonDialogs.DeluxePurChaseDialog(WhereYouLiveActivity.this, WhereYouLiveActivity.this);
                    }
                }
                break;
            case R.id.btn_set_crt_loc:
                if (CommonUtils.checkLocationEnabled(this)) {
                    if (sp.isLocation()) {
                        sp.saveisSettingsChanged(true);
                        sp.saveLocation(false);
                        if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.SearchResponse))) {
                            sp.removeKey(SearchFragment.SearchResponse);
                        }
                        if (!TextUtils.isEmpty(sp.getMyString(SearchFragment.FilterResponse))) {
                            sp.removeKey(SearchFragment.FilterResponse);
                        }
                    }
                    isFromCrtLoc = true;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                }

                break;
        }
    }

    private void ActionPlacesAPI() {
        final List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(WhereYouLiveActivity.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.clear();
        try {
            latLng = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
            if (isFromCrtLoc) {
                isFromCrtLoc = false;
                if (!sp.isLocation()) {
                    ProfileOfUser obj = new Gson().fromJson(sp.getUser(), ProfileOfUser.class);
                    obj.setLatitude("" + latLng.latitude);
                    obj.setLongitude("" + latLng.longitude);
                    sp.saveUserData(obj, sp.getProfileCompleted());
                }
            }
            tv_addres.setText(CommonUtils.getAddress(WhereYouLiveActivity.this, "" + latLng.latitude, "" + latLng.longitude));
        } catch (Exception ignored) {
        }
        mMap.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        mMap.animateCamera(location);
        mMap.setOnCameraChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    isFromPlace = true;
                    tv_addres.setText(place.getAddress());
                    latLng = place.getLatLng();
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Set Workout Location"));
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 11);
                    mMap.animateCamera(location);
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void showLoading() {
        if (mProgressDialog == null || !mProgressDialog.isShowing())
            mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void showSnackBar(View v, String text) {
        Snackbar snack = Snackbar.make(v, text, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryTextColor));
        TextView tv = view.findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (!isFromPlace) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(cameraPosition.target).title(cameraPosition.toString()));
            Log.e(TAG, "onCameraChange: " + latLng);
            String placename = CommonUtils.getAddress(WhereYouLiveActivity.this, "" + latLng.latitude, "" + latLng.longitude);
            if (!TextUtils.isEmpty(placename)) {
                tv_addres.setText(placename);
                isLocationNull = false;
            } else {
                isLocationNull = true;
                new AlertDialog.Builder(this).setMessage("You cannot choose this location.").setPositiveButton("OK", null).show();
            }
            latLng = cameraPosition.target;
        } else {
            isFromPlace = false;
        }
    }

    @Override
    public void onClickToken(String tokenType, int tokensNum, int selectedPos) {
        tokenSType = tokenType;
        selectedPosition = tokensNum;
        if (tokenType.equalsIgnoreCase("DeluxePurChase")) {
            price = CommonDialogs.DeluxePriceList.get(selectedPos).getPriceValue();
            productId = CommonDialogs.DeluxeArr[selectedPos];
            bp.subscribe(WhereYouLiveActivity.this, productId);
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        //if (tokenSType.equalsIgnoreCase("DeluxePurChase")) {
        bp.consumePurchase(productId);
        homeViewModel.addDeluxeRequest(new DeluxeTokenCountModel("2", productId, price, selectedPosition, details.purchaseInfo.purchaseData.orderId, details.purchaseInfo.purchaseData.purchaseToken, CommonUtils.getDateForPurchase(details), details.purchaseInfo.signature, details.purchaseInfo.purchaseData.purchaseState.toString()));
        // }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d(TAG, "onPurchaseHistoryRestored: ");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d(TAG, "onBillingError: ");
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
        if (CommonDialogs.vipTokenPriceList.size() == 0 || CommonDialogs.timeTokenPriceList.size() == 0 || CommonDialogs.crushTokenPriceList.size() == 0 || CommonDialogs.PremiumPriceList.size() == 0 || CommonDialogs.DeluxePriceList.size() == 0) {
            CommonDialogs.onBillingInitialized(bp);
        }
        CommonDialogs.setBilling(bp);
    }

}