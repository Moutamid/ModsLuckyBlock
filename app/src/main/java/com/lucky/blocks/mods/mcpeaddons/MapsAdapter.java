package com.lucky.blocks.mods.mcpeaddons;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.yodo1.mas.Yodo1Mas;
import com.yodo1.mas.interstitial.Yodo1MasInterstitialAd;
import com.yodo1.mas.nativeads.Yodo1MasNativeAdView;

import java.util.List;

@SuppressWarnings("ALL")
public class MapsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int AD_TYPE = 1;
    private static final int CONTENT_TYPE = 2;
    private static final String RATING = "RATING";
    private static OnCardClickListener mListener;
    private String ADMOB_AD_UNIT_ID;
    private Context mContext;
    private Activity activty;
    private List<Map> mapList;
    private NativeAd nativeAd;

    public MapsAdapter(Context mContext, Activity activty, List<Map> mapList) {
        this.mContext = mContext;
        this.activty = activty;
        this.mapList = mapList;
    }

    @Override
    public int getItemViewType(int i) {
        return (i == 100 || i == 500) ? 1 : 2;
    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        mListener = onCardClickListener;
    }
    class adViewHolder extends RecyclerView.ViewHolder {
        Yodo1MasNativeAdView adView;

        public adViewHolder(Yodo1MasNativeAdView nativeAdView) {
            super(nativeAdView);
            this.adView = nativeAdView;
        }
    }

    @Override 
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != 1) {
            return new contentViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false));
        }
        return new adViewHolder((Yodo1MasNativeAdView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.yodo_native_adview, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == 2) {
            contentViewHolder contentviewholder = (contentViewHolder) viewHolder;
            Map map = this.mapList.get(i);
            contentviewholder.title.setText(map.getName());
            contentviewholder.views.setText(map.getViews());
            contentviewholder.downloads.setText(map.getDownloads());
            contentviewholder.rating.setText(map.getRating()+"");
            if (map.getVersion().equals("null") || map.getVersion().trim().length() == 0) {
                contentviewholder.version.setText(this.mContext.getResources().getString(R.string.no_version));
            } else {
                contentviewholder.version.setText(map.getVersion());
            }
            Glide.with(this.mContext).load(map.getImage()).thumbnail(Glide.with(this.mContext).load(Integer.valueOf((int) R.drawable.spinner))).fitCenter().transition(DrawableTransitionOptions.withCrossFade()).into(contentviewholder.thumbnail);
        } else if (getItemViewType(i) == 1) {
            ApplicationManager.getInstance().loadNativeYODO(null, ((adViewHolder) viewHolder).adView);
        }
    }


    @Override
    public int getItemCount() {
        return this.mapList.size();
    }

    interface OnCardClickListener {
        void onCardClick(View view, int i);
    }

    public class contentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView downloads;
        Button install_btn;
        TextView rating;
        RatingBar ratingBar;
        TextView ratingText;
        ImageView thumbnail;
        TextView title;
        TextView type;
        TextView version;
        View view;
        TextView views;

        public contentViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
            this.title = (TextView) view.findViewById(R.id.title);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.views = (TextView) view.findViewById(R.id.views);
            this.rating = (TextView) view.findViewById(R.id.rating);
            this.version = (TextView) view.findViewById(R.id.version);
            this.downloads = (TextView) view.findViewById(R.id.downloads);
        }

        @Override
        public void onClick(View view) {
            MapsAdapter.mListener.onCardClick(view, getAdapterPosition());
//            ApplicationManager.getInstance().ShowInterstitialAd();

//            Yodo1Mas.getInstance().showInterstitialAd(activty);
            boolean isLoaded = Yodo1MasInterstitialAd.getInstance().isLoaded();
            Log.d("ADCHECKER", "isLoaded: " + isLoaded);
            if(isLoaded) Yodo1MasInterstitialAd.getInstance().showAd(activty, "YourAppKey");
        }
    }

}
