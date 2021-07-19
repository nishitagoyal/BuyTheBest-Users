package com.something.groceryapp.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.something.groceryapp.R;
import com.something.groceryapp.model.SliderItem;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_custom_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setSliderImageView(sliderItems.get(position));
        if(position == sliderItems.size()-2)
        {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView sliderImageView;
        public SliderViewHolder(@NonNull View itemView)
        {
            super(itemView);
            sliderImageView = itemView.findViewById(R.id.slider_image);
        }

        void setSliderImageView(SliderItem sliderItem)
        {
            sliderImageView.setImageResource(sliderItem.getSliderImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}
