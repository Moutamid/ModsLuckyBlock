package com.lucky.blocks.mods.mcpeaddons;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;

public class SubscriptionActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        ((Button)findViewById(R.id.start)).setOnClickListener(v -> {
            bp.subscribe(SubscriptionActivity.this, Constants.VIP_MONTH);
        });
        ((ImageView)findViewById(R.id.close)).setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}