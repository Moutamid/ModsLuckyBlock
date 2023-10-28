package com.lucky.blocks.mods.mcpeaddons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends SliderViewAdapter<SliderAdapterVH> {
    private Context context;
    private List<SliderItem> mSliderItems;

    public GalleryAdapter(Context context, List<SliderItem> list) {
        new ArrayList();
        this.context = context;
        this.mSliderItems = list;
    }

    public void renewItems(List<SliderItem> list) {
        this.mSliderItems = list;
        notifyDataSetChanged();
    }

    public void deleteItem(int i) {
        this.mSliderItems.remove(i);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup viewGroup) {
        return new SliderAdapterVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_slider, (ViewGroup) null));
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH sliderAdapterVH, int i) {
        Glide.with(this.context).load(this.mSliderItems.get(i).getImageUrl()).thumbnail(Glide.with(this.context).load(Integer.valueOf((int) R.drawable.spinner))).fitCenter().transition(DrawableTransitionOptions.withCrossFade()).into(sliderAdapterVH.imageViewBackground);
    }

    @Override
    public int getCount() {
        return this.mSliderItems.size();
    }
}
