package com.lucky.blocks.mods.mcpeaddons;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.coolerfall.download.DownloadManager;
import com.fxn.stash.Stash;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.kobakei.ratethisapp.RateThisApp;
import com.yodo1.mas.nativeads.Yodo1MasNativeAdView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class DetailActivity extends AppCompatActivity implements AppsAdapter.OnCardClickListener {
    private static final String[] PERMISSIONS = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final String TAG = "MEOW";
    public TextView description;
    public TextView downloads;
    public Map map;
    public int map_id;
    public TextView name;
    public TextView rating;
    public ImageView thumbnail, banner;
    public TextView version;
    public TextView views;
    ImageView closeButton;
    FrameLayout frameLayout;
    FrameLayout frameLayoutPopUp;
    String lang;
    TextView mapName;
    Yodo1MasNativeAdView nativeAdView;
    Yodo1MasNativeAdView nativeAdViewPopUp;
    private SliderItem app;
    private Button button;
    private DownloadManager downloadManager;
    private Dialog infoPopUp;
    private NativeAd nativeAd;
    private ProgressDialog pDialog;
    private Dialog ratePopUp;
    private Button rate_btn;
    private Button rate_popup_btn;
    private RatingBar ratingBar;
    private LinearLayout recommend_layout;
    private RecyclerView recyclerView;
    private List<SliderItem> appList = new ArrayList();
    private AppsAdapter adapter = new AppsAdapter(this, this.appList);
    private List<SliderItem> sliderItemList = new ArrayList();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.name = (TextView) findViewById(R.id.title);
        this.description = (TextView) findViewById(R.id.description);
        this.views = (TextView) findViewById(R.id.views);
        this.downloads = (TextView) findViewById(R.id.downloads);
        this.rating = (TextView) findViewById(R.id.rating);
        this.version = (TextView) findViewById(R.id.version);
        this.thumbnail = (ImageView) findViewById(R.id.thumbnail);
        this.banner = (ImageView) findViewById(R.id.banner);
        this.button = (Button) findViewById(R.id.button);
        this.recommend_layout = (LinearLayout) findViewById(R.id.recommend_layout);
        this.closeButton = (ImageView) findViewById(R.id.popup_close);
        Dialog dialog = new Dialog(this);
        this.infoPopUp = dialog;
        dialog.setContentView(R.layout.popup_window);
        this.infoPopUp.setCanceledOnTouchOutside(true);
        this.infoPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.infoPopUp.getWindow().setLayout(-1, -2);
        Dialog dialog2 = new Dialog(this);
        this.ratePopUp = dialog2;
        dialog2.setContentView(R.layout.rate_popup_window);
        this.ratePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.rate_btn = (Button) findViewById(R.id.rate_btn);
        this.rate_popup_btn = (Button) this.ratePopUp.findViewById(R.id.rate_popup_btn);
        RatingBar ratingBar = (RatingBar) this.ratePopUp.findViewById(R.id.ratingBar);
        this.ratingBar = ratingBar;
        this.ratingBar.setStepSize(1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar2, float f, boolean z) {
                Log.d("RATING", "f  = " + f + "\t\t z = " + z);
                Log.d("RATING", "ratingBar2  = " + ratingBar2.getRating());
            }
        });
        this.map_id = getIntent().getExtras().getInt("item_id");
        this.lang = Locale.getDefault().getLanguage();
        RateThisApp.Config config = new RateThisApp.Config(1, 1);
        config.setTitle(R.string.rate_title);
        config.setMessage(R.string.rate_message);
        config.setYesButtonText(R.string.rate_rate);
        config.setNoButtonText(R.string.rate_thanks);
        config.setCancelButtonText(R.string.rate_cancel);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);
        new Random().nextInt(10);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.adapter.setOnCardClickListener(this);



        getMap();
        TextView textView = (TextView) this.infoPopUp.findViewById(R.id.map_name);
        this.mapName = textView;
        textView.setText(((Object) getText(R.string.chosen)) + " " + this.map.getName() + ". " + ((Object) getText(R.string.want_to_install)));
        if (this.map.getType().equals("map")) {
            SpannableString spannableString = new SpannableString("  " + getString(R.string.install_map));
            spannableString.setSpan(new ImageSpan(this, (int) R.drawable.ic_download_svgrepo_com__3_, 2), 0, 1, 33);
            this.button.setText(spannableString);
        } else {
            SpannableString spannableString2 = new SpannableString("  " + getString(R.string.install_addon));
            spannableString2.setSpan(new ImageSpan(this, (int) R.drawable.ic_download_svgrepo_com__3_, 2), 0, 1, 33);
            this.button.setText(spannableString2);
        }


        this.frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        this.nativeAdView = (Yodo1MasNativeAdView) getLayoutInflater().inflate(R.layout.yodo_native_adview, (ViewGroup) null);
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayout, this.nativeAdView);
        this.frameLayoutPopUp = (FrameLayout) this.infoPopUp.findViewById(R.id.fl_adplaceholder);
        this.nativeAdViewPopUp = (Yodo1MasNativeAdView) getLayoutInflater().inflate(R.layout.yodo_native_adview, (ViewGroup) null);
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayoutPopUp, this.nativeAdViewPopUp);
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayout, this.nativeAdView);
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayoutPopUp, this.nativeAdViewPopUp);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayout, this.nativeAdView);
        ApplicationManager.getInstance().loadNativeYODO(this.frameLayoutPopUp, this.nativeAdViewPopUp);

    }

    @Override
    public void onCardClick(View view, int i) {
        this.app = this.appList.get(i);
        Intent intent = new Intent("android.intent.action.VIEW");
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(this.app.getUrl());
        intent.setData(Uri.parse(sb.toString()));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            startActivity(new Intent(DetailActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void download_click(View view) {
        this.infoPopUp.show();
    }

    public void onPopupButtonClick(View view) {
        if (this.infoPopUp.isShowing()) {
            this.infoPopUp.dismiss();
        }
        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra("item_id", this.map_id);
        startActivity(intent);
    }

    public void onRateButtonClick(View view) {
        this.ratePopUp.show();
        this.ratePopUp.getWindow().setLayout(-1, -2);
    }

    public void onRatePopupButtonClick(View view) {
/*        java.util.Map<String, Object> rate = new HashMap<>();
        if (this.ratingBar.getRating() == 1.0) {
            rate.put("star1", (this.map.getRating().getStar1() + 1));
        } else if (this.ratingBar.getRating() == 2.0) {
            rate.put("star2", (this.map.getRating().getStar2() + 1));
        } else if (this.ratingBar.getRating() == 3.0) {
            rate.put("star3", (this.map.getRating().getStar3() + 1));
        } else if (this.ratingBar.getRating() == 4.0) {
            rate.put("star4", (this.map.getRating().getStar4() + 1));
        } else if (this.ratingBar.getRating() == 5.0) {
            rate.put("star5", (this.map.getRating().getStar5() + 1));
        }
        Constants.databaseReference().child(this.map.getType()).child(String.valueOf(this.map.getId())).child("rating")
                .updateChildren(rate);*/
        if (this.ratePopUp.isShowing()) {
            this.ratePopUp.dismiss();
        }
        this.rate_btn.setText(getBaseContext().getString(R.string.vote_counted));
        this.rate_btn.setEnabled(false);
        Toast.makeText(this, getBaseContext().getString(R.string.vote_thanks), 1).show();
    }

    public void getMap() {

        this.map = (Map) Stash.getObject("ITEM", Map.class);
/*        java.util.Map<String, Object> view = new HashMap<>();
        view.put("views", (this.map.getViews() + 1));
        Constants.databaseReference().child(map.getType()).child(String.valueOf(map.getId())).updateChildren(view);*/

        this.name.setText(this.map.getName());
        this.views.setText(this.map.getViews() + "");
        this.downloads.setText(this.map.getDownloads());
        Glide.with(this).load(this.map.getImage()).into(this.banner);

        double rating = this.map.getRating();

        if (rating == 0.0f) {
            this.rating.setText(getString(R.string.no_rating));
        } else {
            this.rating.setText(String.valueOf(this.map.getRating()));
        }
        if (this.map.getVersion().equals("null") || this.map.getVersion().trim().length() == 0) {
            this.version.setText(getString(R.string.no_version));
        } else {
            this.version.setText(this.map.getVersion());
        }
        if (Build.VERSION.SDK_INT >= 24) {
            this.description.setText(Html.fromHtml(this.map.getDescription(), 1));
        } else {
            this.description.setText(Html.fromHtml(this.map.getDescription()));
        }
        this.recyclerView.setAdapter(this.adapter);
    }

    public void onCloseClick(View view) {
        if (infoPopUp.isShowing()) {
            infoPopUp.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        ApplicationManager.getInstance().changeMainAd(0);
//        ApplicationManager.getInstance().changeAd(1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
