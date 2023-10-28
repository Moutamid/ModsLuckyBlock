package com.lucky.blocks.mods.mcpeaddons;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Logger;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.measurement.api.AppMeasurementSdk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class DownloadActivity extends AppCompatActivity  {
    private static final String TAG = "MEOW";

    public TextView description;
    public DownloadManager downloadManager;
    public Dialog downloadPopUp;
    public ImageView image;
    public String image_url;
    private AdView mAdView;
    public InterstitialAd mInterstitialAd;
    NativeAdView nativeAdView;
    NativeAdView nativeAdViewPopUp;

    public Map map;
    public int map_id;
    public TextView name;
    public Button popup_btn;
    public TextView popup_percent;
    public TextView popup_speed;
    public TextView popup_text;
    public ProgressBar progressBar;
    public TextView rating;
    public TextView version;
    public TextView views;
    public Boolean downloaded = false;
    FrameLayout frameLayout;
    FrameLayout frameLayoutPopUp;
    String lang;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_download);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.name = (TextView) findViewById(R.id.title);
        Dialog dialog = new Dialog(this);
        this.downloadPopUp = dialog;
        dialog.setContentView(R.layout.download_window);
        this.downloadPopUp.setCanceledOnTouchOutside(false);
        this.downloadPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.downloadPopUp.getWindow().setLayout(-1, -2);
        this.popup_text = (TextView) this.downloadPopUp.findViewById(R.id.popup_text);
        this.popup_btn = (Button) this.downloadPopUp.findViewById(R.id.popup_btn);
        this.progressBar = (ProgressBar) this.downloadPopUp.findViewById(R.id.progressBar);
        this.popup_percent = (TextView) this.downloadPopUp.findViewById(R.id.percent);
        this.popup_speed = (TextView) this.downloadPopUp.findViewById(R.id.speed);
        this.map_id = getIntent().getExtras().getInt("item_id");
        this.lang = Locale.getDefault().getLanguage();
        getMap();
        this.name.setText(this.map.getName());
        this.image = (ImageView) findViewById(R.id.image);
        this.image_url = this.map.getThumbnail();


        Glide.with((FragmentActivity) this).load(this.image_url).thumbnail(Glide.with((FragmentActivity) this).load(Integer.valueOf((int) R.drawable.spinner))).fitCenter().transition(DrawableTransitionOptions.withCrossFade()).into(this.image);
        this.downloadManager = new DownloadManager.Builder().context(this).downloader(OkHttpDownloader.create(new OkHttpClient.Builder().build())).threadPoolSize(3).logger(new Logger() {
            @Override
            public void log(String str) {
                Log.d("TAG", str);
            }
        }).build();
        new Random().nextInt(10);
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        this.nativeAdView = (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified_bold, (ViewGroup) null);
        ApplicationManager.getInstance().loadAd(this.frameLayout, this.nativeAdView, 2);
        this.mInterstitialAd = ApplicationManager.getInstance().getInterAd();
        new Random().nextInt(10);
        this.frameLayoutPopUp = (FrameLayout) this.downloadPopUp.findViewById(R.id.fl_adplaceholder);
        this.nativeAdViewPopUp = (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, (ViewGroup) null);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ApplicationManager.getInstance().loadAd(this.frameLayout, this.nativeAdView, 2);
        ApplicationManager.getInstance().loadMainNative(this.frameLayoutPopUp, this.nativeAdViewPopUp, 1);

    }

    public void onButtonClick(View view) {
        this.downloadPopUp.show();
        this.popup_text.setText(R.string.downloading);
        this.popup_btn.setEnabled(false);
        this.popup_btn.setBackground(getResources().getDrawable(R.drawable.btn_border_inactive));
        this.downloadManager.add(new DownloadRequest.Builder().url(this.map.getUrl()).downloadCallback(new Callback()).retryTime(3).retryInterval(3L, TimeUnit.SECONDS).progressInterval(1L, TimeUnit.SECONDS).priority(Priority.HIGH).allowedNetworkTypes(1).destinationFilePath(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + this.map.getUrl().substring(this.map.getUrl().lastIndexOf(47) + 1)).build());
    }

    public void onNextButtonClick(View view) {
        if (this.downloadPopUp.isShowing()) {
            this.downloadPopUp.dismiss();
        }
        String archiveName = this.map.getArchiveName();
        Intent intent = new Intent(this, FinalActivity.class);
        intent.putExtra("archive_name", archiveName);
        startActivity(intent);
    }

    public void getMap() {
        String string;
        String string2;
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset()).getJSONArray("items");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                int i2 = jSONObject.getInt("id");
                if (i2 == this.map_id) {
                    if (this.lang != "ru") {
                        string = jSONObject.getString("name_en");
                        string2 = jSONObject.getString("description_en");
                    } else {
                        string = jSONObject.getString(AppMeasurementSdk.ConditionalUserProperty.NAME);
                        string2 = jSONObject.getString("description");
                    }
                    String string3 = jSONObject.getString("views");
                    String string4 = jSONObject.getString("archive");
                    String string5 = jSONObject.getString("image");
                    float parseFloat = Float.parseFloat(jSONObject.getString("rating"));
                    String string6 = jSONObject.getString("version");
                    String string7 = jSONObject.getString("downloads");
                    String string8 = jSONObject.getString("rating");
                    String string9 = jSONObject.getString("type");
                    this.map = new Map(i2, string, string2, string4, string5, getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + string4.substring(string4.lastIndexOf(47) + 1), string3, parseFloat, string6, string7, string8, string9, 0, 0);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void onStop() {
        super.onStop();
        ApplicationManager.getInstance().changeAd(2);
        ApplicationManager.getInstance().changeMainAd(1);
    }

    private class Callback extends DownloadCallback {
        private long startSize;
        private long startTimestamp;

        private Callback() {
            this.startTimestamp = 0L;
            this.startSize = 0L;
        }

        @Override
        public void onRetry(int i) {
        }

        @Override
        public void onStart(int i, long j) {
            this.startTimestamp = System.currentTimeMillis();
        }

        @Override
        public void onProgress(int i, long j, long j2) {
            int i2 = (int) ((((float) j) * 100.0f) / ((float) j2));
            if (i2 == 100) {
                i2 = 0;
            }
            DownloadActivity.this.progressBar.setProgress(i2);
            this.startSize = j;
            TextView textView = DownloadActivity.this.popup_percent;
            textView.setText(i2 + "%");
            TextView textView2 = DownloadActivity.this.popup_speed;
            textView2.setText((((int) (((j - this.startSize) * 1000) / ((long) ((int) ((System.currentTimeMillis() - this.startTimestamp) + 1))))) / 1024) + " kb/s");
        }

        @Override
        public void onSuccess(int i, String str) {
            DownloadActivity.this.progressBar.setProgress(100);
            DownloadActivity.this.popup_percent.setText("100%");
            DownloadActivity.this.popup_speed.setText("0 kb/s");
            DownloadActivity.this.popup_text.setText(R.string.downloaded);
            DownloadActivity.this.popup_btn.setEnabled(true);
            DownloadActivity.this.popup_btn.setBackground(DownloadActivity.this.getResources().getDrawable(R.drawable.btn_border));
            DownloadActivity.this.popup_btn.setText(R.string.next);
            DownloadActivity.this.downloaded = true;
        }

        @Override
        public void onFailure(int i, int i2, String str) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
            builder.setTitle(R.string.error).setMessage(str).setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i3) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog create = builder.create();
            if (DownloadActivity.this.isFinishing()) {
                return;
            }
            create.show();
        }
    }
    public String loadJSONFromAsset() {
        try {
            InputStream open = getAssets().open("maps.json");
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
