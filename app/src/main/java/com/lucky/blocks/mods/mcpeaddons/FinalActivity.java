package com.lucky.blocks.mods.mcpeaddons;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.yodo1.mas.nativeads.Yodo1MasNativeAdView;

import org.json.JSONException;

import java.io.File;

public class FinalActivity extends AppCompatActivity {
    private static final String TAG = "MEOW";
    public String archive_name;
    public InterstitialAd mInterstitialAd;
    FrameLayout frameLayout;
    Yodo1MasNativeAdView nativeAdView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_final);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_house_homepage_2_svgrepo_com__1_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.archive_name = getIntent().getExtras().getString("archive_name");
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        this.nativeAdView = (Yodo1MasNativeAdView) getLayoutInflater().inflate(R.layout.yodo_native_adview, (ViewGroup) null);
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayout, this.nativeAdView);
//        this.mInterstitialAd = ApplicationManager.getInstance().getInterAd();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayout, this.nativeAdView);
    }

    public void onButtonClick(View view) {
        Uri uriForFile = FileProvider.getUriForFile(this, "com.lucky.blocks.mods.mcpeaddons.provider", new File(this.archive_name));
        String str = this.archive_name.contains("mcpack") ? "application/mcpack" : "application/mcworld";
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uriForFile, str);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "ERRORKA: " + e);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error).setMessage(R.string.error_content).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.create().show();
        }
    }

    public void back_to_menu(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onStop() {
        super.onStop();
//        ApplicationManager.getInstance().changeAd(3);
    }
}
