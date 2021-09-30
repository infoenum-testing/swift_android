package com.swiftdating.app.ui.where_do_you_live;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.swiftdating.app.R;
import com.swiftdating.app.common.CommonDialogs;
import com.swiftdating.app.common.CommonUtils;
import com.swiftdating.app.data.preference.SharedPreference;
import com.swiftdating.app.model.BaseModel;
import com.swiftdating.app.model.requestmodel.LocationModel;
import com.swiftdating.app.model.requestmodel.PremiumTokenCountModel;
import com.swiftdating.app.model.responsemodel.ProfileOfUser;
import com.swiftdating.app.ui.base.BaseActivity;
import com.swiftdating.app.ui.homeScreen.fragment.SearchFragment;
import com.swiftdating.app.ui.homeScreen.viewmodel.HomeViewModel;
import com.swiftdating.app.ui.loginScreen.LoginActivity;

import java.util.Arrays;
import java.util.List;

import im.delight.android.location.SimpleLocation;

public class WhereYouLiveActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener, CommonDialogs.onProductConsume, PurchasesUpdatedListener, BillingClientStateListener {

    private static final String TAG = "WhereYouLiveActivity";
    private final int AUTOCOMPLETE_REQUEST_CODE = 1001;
    String lat, lon;
    boolean isFromPlace = false;
    Bitmap smallMarkerBitmap;
    private GoogleMap mMap;
    private SimpleLocation simpleLocation;
    private LatLng latLng;
    private TextView tv_addres;
    private HomeViewModel homeViewModel;
    private ProgressDialog mProgressDialog;
    private SharedPreference sp;
    private BillingClient billingClient;
    private ConstraintLayout constraint_main;
    private double price;
    private int selectedPosition;
    private boolean isLocationNull = false;
    private boolean isFromCrtLoc = false;
    private String productId;


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
        /*bp = new BillingProcessor(WhereYouLiveActivity.this, LICENSE_KEY, WhereYouLiveActivity.this);
        bp.initialize();*/
    }

    private void initViews() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(this);
        sp = new SharedPreference(this);
        constraint_main = findViewById(R.id.constraint_main);
        tv_addres = findViewById(R.id.tv_addres);
        ImageView image_back = findViewById(R.id.image_back);
        LinearLayout cardSearch = findViewById(R.id.llcardSearch);

        Button btn_save_loc = findViewById(R.id.btn_save_loc);
        Button btn_set_crt_loc = findViewById(R.id.btn_set_crt_loc);
        image_back.setOnClickListener(this::oClick);
        btn_set_crt_loc.setOnClickListener(this::oClick);
        btn_save_loc.setOnClickListener(this::oClick);
        cardSearch.setOnClickListener(this::oClick);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.sendLatLongResponse().observe(WhereYouLiveActivity.this, resource -> {
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
                        showSnackBar(constraint_main, model.getMessage());
                        ProfileOfUser obj = new Gson().fromJson(sp.getUser(), ProfileOfUser.class);
                        Log.e(TAG, "initViews:1--- " + obj.getLatitude());
                        Log.e(TAG, "initViews:2--- " + obj.getLongitude());


                        obj.setLatitude("" + model.getLatitude());
                        obj.setLongitude("" + model.getLongitude());

                        Log.e(TAG, "initViews:3 " + obj.getLatitude());
                        Log.e(TAG, "initViews:4 " + obj.getLongitude());

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
        });

        homeViewModel.addPremiumResponse().observe(this, resource -> {
            if (resource == null) return;
            switch (resource.status) {
                case LOADING:
                    break;
                case SUCCESS:
                    hideLoading();
                    if (resource.data.getSuccess()) {
                        sp.savePremium(true);
                        showSnackBar(constraint_main, resource.data.getMessage());
                       /* tvPremium.setVisibility(View.INVISIBLE);
                        tvUnlimitedView.setVisibility(View.INVISIBLE);*/
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


        int height = 100;
        int width = 70;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location_icon);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarkerBitmap = Bitmap.createScaledBitmap(b, width, height, false);
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
            case R.id.llcardSearch:
                ActionPlacesAPI();
                break;
            case R.id.btn_save_loc:
                if (CommonUtils.checkLocationEnabled(this)) {
                    if (sp.getPremium()) {
                        if (!isLocationNull) {
                            showLoading();

                            homeViewModel.sendLatLong(new LocationModel("" + latLng.latitude, "" + latLng.longitude));
                        } else {
                            new AlertDialog.Builder(this).setMessage("You cannot choose this location.").setPositiveButton("OK", null).show();
                        }
                    } else {
                        CommonDialogs.PremuimPurChaseDialog(WhereYouLiveActivity.this, WhereYouLiveActivity.this, sp);
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
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerBitmap)));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 11);
        mMap.animateCamera(location);
        mMap.setOnCameraChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    isFromPlace = true;
                    tv_addres.setText(place.getAddress());
                    latLng = place.getLatLng();
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerBitmap)).title("Set Workout Location"));
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
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.pink));
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
            mMap.addMarker(new MarkerOptions().position(cameraPosition.target).title(cameraPosition.toString()).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerBitmap)));
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
        selectedPosition = tokensNum;
        if (billingClient != null && billingClient.isReady()) {
            if (tokenType.equalsIgnoreCase("PremiumPurchase")) {
                price = CommonDialogs.PremiumPriceList.get(selectedPos).getPriceValue();
                productId = CommonDialogs.PremiumArr[selectedPos];
                BillingFlowParams params = BillingFlowParams.newBuilder().setSkuDetails(CommonDialogs.PremiumSkuList.get(selectedPos)).build();
                billingClient.launchBillingFlow(this, params);
            }
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null && !list.isEmpty() && billingClient != null && billingClient.isReady()) {
            Purchase purchase = list.get(0);
            ConsumeParams params = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
            billingClient.consumeAsync(params, (billingResult1, s) -> {
                showLoading();
                homeViewModel.addPremiumRequest(new PremiumTokenCountModel("1",
                        productId,
                        price,
                        selectedPosition,
                        purchase.getOrderId(),
                        purchase.getPurchaseToken(),
                        CommonUtils.getDateForPurchase(purchase.getPurchaseTime()),
                        purchase.getSignature(),
                        BaseActivity.purchaseState));
            });

        }
    }

    @Override
    public void onBillingServiceDisconnected() {

    }

    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        Log.e(TAG, "onBillingSetupFinished: ");
        if (CommonDialogs.vipTokenPriceList.size() == 0 || CommonDialogs.timeTokenPriceList.size() == 0 || CommonDialogs.crushTokenPriceList.size() == 0 || CommonDialogs.PremiumPriceList.size() == 0 || CommonDialogs.DeluxePriceList.size() == 0) {
            CommonDialogs.onBillingInitialized(billingClient);
        }
    }
}