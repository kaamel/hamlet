package com.genesis.hamlet.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by kaamel on 10/11/17.
 */

public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
        /*Glide.with(view.getContext()).load(url).placeholder(R.drawable.drawable_placeholder).error(
                R.drawable.drawable_placeholder).into(view);*/
    }
}
