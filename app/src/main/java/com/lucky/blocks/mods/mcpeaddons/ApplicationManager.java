package com.lucky.blocks.mods.mcpeaddons;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.bumptech.glide.load.Key;
import com.fxn.stash.Stash;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.error.Yodo1MasError;
import com.yodo1.mas.nativeads.Yodo1MasNativeAdView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class ApplicationManager extends android.app.Application implements LifecycleObserver {
    private static ApplicationManager mInstance = null;
    private static boolean sIsLocationTrackingEnabled = true;
    private AdLoader adLoader;
    public NativeAdView adView;
    public AdLoader.Builder builder;
    private Activity currentActivity;
    private InterstitialAd mInterstitialAd;
    private NativeAd mainNativeAd;
    private NativeAd nativeAd;
    public List<NativeAd> adList = new ArrayList();
    public List<NativeAd> mainAdList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public static void setLocationTrackingEnabled(boolean z) {
        sIsLocationTrackingEnabled = z;
    }

    public static boolean isIsLocationTrackingEnabled() {
        return sIsLocationTrackingEnabled;
    }

    @Override 
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Stash.init(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
     //   preloadMainNative();

    }


    public void LoadIntersttialAd(){
        InterstitialAd.load(this, getResources().getString(R.string.admob_interstitial), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() { 
            @Override 
            public void onAdLoaded(InterstitialAd interstitialAd) {
                ApplicationManager.this.mInterstitialAd = interstitialAd;
            }

            @Override 
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                ApplicationManager.this.mInterstitialAd = null;
            }
        });
    }
    public InterstitialAd getInterAd() {
        return this.mInterstitialAd;
    }

    public void ShowInterstitialAd() {
        if (ApplicationManager.this.mInterstitialAd != null) {
            ApplicationManager.this.mInterstitialAd.show(this.currentActivity);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    public void setAdList(List<NativeAd> list) {
        this.adList = list;
    }


    public void changeAd(final Integer num) {
        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.admob_native));
        this.builder = builder;
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() { 
            @Override 
            public void onNativeAdLoaded(NativeAd nativeAd) {
                if (ApplicationManager.this.adList.size() > 0) {
                    ApplicationManager.this.adList.set(num.intValue(), nativeAd);
                    Log.e("ADS", "extra ad loaded");
                }
            }
        }).withAdListener(new AdListener() { 
            @Override 
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }
        });
        this.builder.build().loadAd(new AdRequest.Builder().build());
    }

    public void  loadNativeYODO(FrameLayout frameLayout, Yodo1MasNativeAdView nativeAdView){
        if (nativeAd != null) {
            nativeAdView.loadAd();
            if (frameLayout != null) {
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            }
        }
    }

    public void loadAd(FrameLayout frameLayout, NativeAdView nativeAdView, Integer num) {
        if (this.adList.size() == 0) {
            return;
        }
        NativeAd nativeAd = this.adList.get(num.intValue());
        this.nativeAd = nativeAd;
        if (nativeAd != null) {
            populateNativeAdView(nativeAd, nativeAdView);
            if (frameLayout != null) {
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            }
        }
    }

    public void preloadMainNative() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.admob_native));
        this.builder = builder;
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() { 
            @Override 
            public void onNativeAdLoaded(NativeAd nativeAd) {
                ApplicationManager.this.mainAdList.add(nativeAd);
                Log.e("ADS", "Main ad loaded");
            }
        }).withAdListener(new AdListener() { 
            @Override 
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }

            @Override 
            public void onAdClicked() {
                super.onAdClicked();
            }
        });
        this.builder.build().loadAds(new AdRequest.Builder().build(), 2);
    }

    public void loadMainNative(FrameLayout frameLayout, NativeAdView nativeAdView, Integer num) {
        if (this.mainAdList.size() <= 1) {
            return;
        }
        NativeAd nativeAd = this.mainAdList.get(num.intValue());
        this.mainNativeAd = nativeAd;
        if (nativeAd != null) {
            populateNativeAdView(nativeAd, nativeAdView);
            if (frameLayout != null) {
                frameLayout.removeAllViews();
                frameLayout.addView(nativeAdView);
            }
        }
    }

    public void changeMainAd(final Integer num) {
        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.admob_native));
        this.builder = builder;
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() { 
            @Override 
            public void onNativeAdLoaded(NativeAd nativeAd) {
                if (ApplicationManager.this.mainAdList.size() > 0) {
                    ApplicationManager.this.mainAdList.set(num.intValue(), nativeAd);
                    Log.e("ADS", "extra ad loaded");
                }
            }
        }).withAdListener(new AdListener() { 
            @Override 
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }
        });
        this.builder.build().loadAd(new AdRequest.Builder().build());
    }

    public void preloadAds() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getResources().getString(R.string.admob_native));
        this.builder = builder;
        AdLoader build = builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() { 
            @Override 
            public void onNativeAdLoaded(NativeAd nativeAd) {
                ApplicationManager.this.adList.add(nativeAd);
                Log.e("ADS", "New ad loaded");
            }
        }).withAdListener(new AdListener() { 
            @Override 
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }
        }).build();
        this.adLoader = build;
        build.loadAds(new AdRequest.Builder().build(), 4);
    }

    public void populateNativeAdView(NativeAd nativeAd, NativeAdView nativeAdView) {
        nativeAdView.setMediaView((MediaView) nativeAdView.findViewById(R.id.ad_media));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        nativeAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        if (nativeAd.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(View.GONE);
        } else {
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        nativeAdView.setNativeAd(nativeAd);
        nativeAd.getMediaContent().getVideoController();
    }

    public static synchronized ApplicationManager getInstance() {
        ApplicationManager ApplicationManager;
        synchronized (ApplicationManager.class) {
            ApplicationManager = mInstance;
        }
        return ApplicationManager;
    }

    public String loadJSONFromAsset() {
        try {
            InputStream open = getBaseContext().getAssets().open("maps.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String loadJSONFromAssetA() {
        try {
            InputStream open = getBaseContext().getAssets().open("apps.json");
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, Key.STRING_CHARSET_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}