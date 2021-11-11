package com.swiftdating.app.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.SkuDetails;
import com.google.android.material.snackbar.Snackbar;

import com.swiftdating.app.R;


public abstract class BaseFragment extends Fragment {

    public BaseActivity mActivity;
    public Context mContext;
    protected BillingClient fragClient;


    /**
     * Override for set binding variable
     *
     * @return variable id
     */

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) getActivity();
            this.mContext = context;
            mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            if (BaseActivity.client!=null)
            fragClient = BaseActivity.client;
            else {
                mActivity.initializeBillingClient();
            }
        }
    }

    protected void setOnPurchaseListener(BaseActivity.OnPurchaseListener onPurchaseListener) {
        BaseActivity.setOnPurchaseListener(onPurchaseListener);

        /*if (mContext instanceof BaseActivity) {
            Log.e(TAG, "setOnPurchaseListener: its instanceof BaseActivity" );
            BaseActivity.setOnPurchaseListener(onPurchaseListener);
        }*/
    }

    private static final String TAG = "BaseFragment";
    protected void  callPurchaseDetail(String subscriptionId, String purchaseToken){
        Log.e(TAG, "callPurchaseDetail: "+subscriptionId+"   "+purchaseToken );
        if (mContext instanceof BaseActivity){
            getBaseActivity().callPurchaseDetail(subscriptionId,purchaseToken);
        }
    }
    protected void queryPurchasesAsync(BaseActivity.OnQueryPurchasesListener queryPurchasesListener){
        if (mContext instanceof BaseActivity){
            getBaseActivity().queryPurchasesAsync(queryPurchasesListener);
        }
    }
    protected void queryPurchaseHistoryAsync(BaseActivity.OnQueryPurchasesHistoryListener listener){
        if (mContext instanceof BaseActivity){
            getBaseActivity().queryPurchaseHistoryAsync(listener);
        }
    }

    protected BillingFlowParams getBillingFlowParam(SkuDetails sku){
        return BillingFlowParams.newBuilder().setSkuDetails(sku).build();
    }
    protected ConsumeParams getConsumeParam(String purchaseToken) {
        return ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build();
    }
    protected AcknowledgePurchaseParams getAcknowledgeParams(String purchaseToken) {
        return AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchaseToken).build();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

    /**
     * show message to user in toast
     *
     * @param message message that need to display
     */
    public void showToast(String message) {
        Toast toast = Toast.makeText(mActivity, message, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.custom_toast_background);
        TextView text = view.findViewById(android.R.id.message);
        text.setPadding(30, 0, 30, 0);
        text.setTextColor(Color.WHITE);
        toast.show();
    }

    /**
     * show message to user in snackBar
     *
     * @param v       : view where snackBar need to be attached
     * @param message : message that need to display
     */
    public void showSnackBar(View v, String message) {
        Snackbar snack = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pink));
        TextView tv = view.findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }


    /*
     *** requesting permission
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionCG() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(), Manifest.permission.CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, getBaseActivity().PERMISSION_REQUEST_CODE_CG);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, getBaseActivity().PERMISSION_REQUEST_CODE_CG);
        }
    }


    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);

        boolean isNetworkConnected();
    }
}
