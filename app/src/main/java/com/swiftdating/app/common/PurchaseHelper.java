package com.swiftdating.app.common;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

public class PurchaseHelper {
    private static BillingClient billingClient;

    public static BillingClient getBillingInstance(Context context, PurchasesUpdatedListener updatedListener) {
        billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(updatedListener).build();
        return billingClient;
    }

    public void setConnectionListener(BillingClientStateListener listener) {
        billingClient.startConnection(listener);
    }
}
