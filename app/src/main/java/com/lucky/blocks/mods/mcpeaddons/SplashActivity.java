package com.lucky.blocks.mods.mcpeaddons;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.lucky.blocks.mods.mcpeaddons.ApplicationManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity{
    private static final long COUNTER_TIME = 3;
    private static final String LOG_TAG = "SplashActivity";
    private AdLoader adLoader;
    public AdLoader.Builder builder;
    DilatingDotsProgressBar mDilatingDotsProgressBar;
    private long secondsRemaining;
    public List<NativeAd> adList = new ArrayList();
    int SPLASH_TIME = 2000;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        preloadAds();
    }

    public void preloadAds() {
        String string = getResources().getString(R.string.admob_native);
        new Intent(this, MainActivity.class);
        this.builder = new AdLoader.Builder(this, string);
        DilatingDotsProgressBar dilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        this.mDilatingDotsProgressBar = dilatingDotsProgressBar;
        dilatingDotsProgressBar.showNow();
        AdLoader build = this.builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() { 
            @Override 
            public void onNativeAdLoaded(NativeAd nativeAd) {
                SplashActivity.this.adList.add(nativeAd);
                Log.e("ADS", "New ad loaded");
                if (SplashActivity.this.adLoader.isLoading()) {
                    return;
                }
                ApplicationManager.getInstance().setAdList(SplashActivity.this.adList);
                SplashActivity.this.createTimer(3L);
            }
        }).withAdListener(new AdListener() { 
            @Override 
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                SplashActivity.this.createTimer(3L);
            }
        }).build();
        this.adLoader = build;
        build.loadAds(new AdRequest.Builder().build(), 4);
    }

    public void createTimer(long j) {
        final TextView textView = (TextView) findViewById(R.id.timer);
        new CountDownTimer(j * 1000, 1000L) {
            @Override
            public void onTick(long j2) {
                secondsRemaining = (j2 / 1000) + 1;
                TextView textView2 = textView;
                textView2.setText(getString(R.string.splash_counter) + " " + secondsRemaining);
            }

            @Override
            public void onFinish() {
                secondsRemaining = 0L;
                textView.setText(getString(R.string.done));
                android.app.Application application = getApplication();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}
