package com.lucky.blocks.mods.mcpeaddons;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;

@SuppressWarnings("ALL")
class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
    Button button;
    ImageView imageGifContainer;
    ImageView imageViewBackground;
    View itemView;
    TextView textViewDescription;

    public SliderAdapterVH(View view) {
        super(view);
        this.itemView = view;
        this.imageViewBackground = (ImageView) view.findViewById(R.id.iv_auto_image_slider);
        this.imageGifContainer = (ImageView) view.findViewById(R.id.iv_gif_container);
        this.textViewDescription = (TextView) view.findViewById(R.id.tv_auto_image_slider);
        this.button = (Button) view.findViewById(R.id.button);
    }
}
