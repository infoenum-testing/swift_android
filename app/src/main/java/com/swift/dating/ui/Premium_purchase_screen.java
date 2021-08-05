package com.swift.dating.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.swift.dating.R;
import com.swift.dating.common.CommonDialogs;
import com.swift.dating.common.InAppPriceValue;
import com.swift.dating.data.network.CallRestoreApi;
import com.swift.dating.data.preference.SharedPreference;
import com.swift.dating.model.requestmodel.PremiumTokenCountModel;
import com.swift.dating.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Premium_purchase_screen extends BaseActivity {
    public static List<InAppPriceValue> PremiumPriceList = new ArrayList<>();
    private Context ctx;
    int indexOfSelectedLayout = 0;
    String subscriptiontype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_buy_premium_dialog);
        oNCreateData();
    }

    private void oNCreateData() {
        /*
        ctx = this;

        LinearLayout[] layouts = new LinearLayout[4];
        layouts[0] = findViewById(R.id.rb5Likes);
        layouts[1] = findViewById(R.id.rb25Likes);
        layouts[2] = findViewById(R.id.tokens10Lay);
        layouts[3] = findViewById(R.id.tokens20Lay);
        for (int i = 0; i < layouts.length; i++) {
            int finalI = i;
            layouts[i].setOnClickListener(v -> {
                indexOfSelectedLayout = finalI;
                unSelectAll(layouts);
                layouts[finalI].setBackgroundResource(R.drawable.bg_premium_selected);
            });
        }
        Button btn_continue = findViewById(R.id.btn_continue);
        getSupportFragmentManager().beginTransaction().replace(R.id.sliderFragmentFrameLayout, new slider_fragment()).commit();

        btn_continue.setOnClickListener(v -> {
            if (indexOfSelectedLayout == 0) {
                clickListener.onClickToken("PremiumPurchase", 1, indexOfSelectedLayout);//9.99
            } else if (indexOfSelectedLayout == 1) {
                clickListener.onClickToken("PremiumPurchase", 3, indexOfSelectedLayout);//24.99
            } else if (indexOfSelectedLayout == 2) {
                clickListener.onClickToken("PremiumPurchase", 6, indexOfSelectedLayout);//39.99
            } else {
                clickListener.onClickToken("PremiumPurchase", 12, indexOfSelectedLayout);//59.99
            }
            //dialog.dismiss();
        });

        TextView[] tvToken1Price = new TextView[4];
        tvToken1Price[0] = findViewById(R.id.tvToken1Price);
        tvToken1Price[1] = findViewById(R.id.tvToken5Price);
        tvToken1Price[2] = findViewById(R.id.tvToken10Price);
        tvToken1Price[3] = findViewById(R.id.tvToken20Price);

        TextView tv_restore = findViewById(R.id.tv_restore);
        tv_restore.setOnClickListener(v -> {
            PremiumTokenCountModel model;
            if ((myBp != null && myBp.isInitialized())) {
                showLoading();
                model = checkExistingSubscriptionForPremium(myBp, ctx);
                if (model != null) {
                    new CallRestoreApi().callApi(model, new SharedPreference(ctx), subscriptiontype, new CommonDialogs.onPurchaseRestore() {
                        @Override
                        public void onError(String msg) {
                            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                            hideLoading();
                            finish();
                        }

                        @Override
                        public void onSucces() {
                            Toast.makeText(ctx, "Purchase Restored Successfully", Toast.LENGTH_SHORT).show();
                            hideLoading();
                            finish();
                        }
                    });
                } else {
                    //Toast.makeText(ctx,"Purchase Restore unavailable.",Toast.LENGTH_SHORT).show();
                    hideLoading();
                    //dialog.dismiss();
                }
            }
        });
        if (PremiumPriceList.size() > 0) {
            for (int i = 0; i < tvToken1Price.length; i++) {
                if (!TextUtils.isEmpty(PremiumPriceList.get(i).getPriceTxt())) {
                    tvToken1Price[i].setText(PremiumPriceList.get(i).getPriceTxt());
                }
            }
        }

        findViewById(R.id.ivclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
    }

    private void unSelectAll(LinearLayout[] layouts) {
        for (LinearLayout layout : layouts) {
            layout.setBackgroundResource(R.drawable.img_rectangle_outline);
        }
    }


    public PremiumTokenCountModel checkExistingSubscriptionForPremium(BillingProcessor bp, Context ctx) {
        String productid = "";
        double price = 0.0;
        if (bp.loadOwnedPurchasesFromGoogle()) {
            if (bp.getSubscriptionTransactionDetails("premium_1") != null ||
                    bp.getSubscriptionTransactionDetails("premium_3") != null ||
                    bp.getSubscriptionTransactionDetails("premium_6") != null ||
                    bp.getSubscriptionTransactionDetails("premium_12") != null) {

                if (bp.getSubscriptionTransactionDetails("premium_1") != null) {
                    productid = "premium_1";
                    price = PremiumPriceList.get(0).getPriceValue();
                } else if (bp.getSubscriptionTransactionDetails("premium_3") != null) {
                    productid = "premium_3";
                    price = PremiumPriceList.get(1).getPriceValue();
                } else if (bp.getSubscriptionTransactionDetails("premium_6") != null) {
                    productid = "premium_6";
                    price = PremiumPriceList.get(2).getPriceValue();
                } else if (bp.getSubscriptionTransactionDetails("premium_12") != null) {
                    productid = "premium_12";
                    price = PremiumPriceList.get(3).getPriceValue();
                }
                subscriptiontype = "1";
                Date c = bp.getSubscriptionTransactionDetails(productid).purchaseInfo.purchaseData.purchaseTime;
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
                String formattedDate = df.format(c);
                return new PremiumTokenCountModel(subscriptiontype, productid, price, Integer.parseInt(productid.split("_")[1]), bp.getSubscriptionTransactionDetails(productid).purchaseInfo.purchaseData.orderId, bp.getSubscriptionTransactionDetails(productid).purchaseInfo.purchaseData.purchaseToken, formattedDate, bp.getSubscriptionTransactionDetails(productid).purchaseInfo.signature, bp.getSubscriptionTransactionDetails(productid).purchaseInfo.purchaseData.purchaseState.toString());
            } else {
                new AlertDialog.Builder(ctx).setTitle("Nothing to restore").setMessage("No previous purchases were found").setPositiveButton("ok", null).show();
                return null;
            }
        } else {
            new AlertDialog.Builder(ctx).setTitle("Nothing to restore").setMessage("No previous purchases were found").setPositiveButton("ok", null).show();
            return null;
        }
    }

}