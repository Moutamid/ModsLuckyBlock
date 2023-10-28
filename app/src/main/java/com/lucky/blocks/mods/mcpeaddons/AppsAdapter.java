package com.lucky.blocks.mods.mcpeaddons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

@SuppressWarnings("ALL")
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.MyViewHolder> {
    private static OnCardClickListener mListener;
    private List<SliderItem> appList;
    private Context mContext;

    public AppsAdapter(Context context, List<SliderItem> list) {
        this.mContext = context;
        this.appList = list;
    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        mListener = onCardClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        SliderItem sliderItem = this.appList.get(i);
        myViewHolder.title.setText(sliderItem.getDescription());
        Glide.with(this.mContext).load(sliderItem.getImageUrl()).into(myViewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return this.appList.size();
    }

    public interface OnCardClickListener {
        void onCardClick(View view, int i);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button install_btn;
        ImageView thumbnail;
        TextView title;
        View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            view.setOnClickListener(this);
            this.title = (TextView) view.findViewById(R.id.title);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }

        @Override
        public void onClick(View view) {
            AppsAdapter.mListener.onCardClick(view, getAdapterPosition());
        }
    }
}
