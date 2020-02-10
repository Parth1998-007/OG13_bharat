package com.organicbharat.image_utils;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.organicbharat.R;
import com.organicbharat.utils.AppLog;
//Java class for Image binding in whole app
public class ImageBinding {
    private static final String TAG = "ImageBinding";

   /* @BindingAdapter({"app:userImageUrl"})
    public static void loadUserImage(ImageView view, String imageUrl) {
        AppLog.e(TAG,"loadImage : "+imageUrl);
        Drawable defaultImage = view.getContext().getResources().getDrawable(R.drawable.icon_user_default);
        RequestOptions requestOption=new RequestOptions().placeholder(defaultImage).error(defaultImage);
        if(!TextUtils.isEmpty(imageUrl))
            Glide.with(view.getContext()).load(imageUrl).apply(requestOption).thumbnail(0.1f).into(view);
        else
            Glide.with(view.getContext()).load(defaultImage).into(view);
    }
    */
//load method is loading images with GLIDE library
    public static void loadImage(ImageView view, String imageUrl) {
        AppLog.e(TAG,"loadImage : "+imageUrl);
        Drawable defaultImage = view.getContext().getResources().getDrawable(R.drawable.place_holder_400_400);
        RequestOptions requestOption=new RequestOptions().placeholder(defaultImage).error(defaultImage);
        if(!TextUtils.isEmpty(imageUrl))
            Glide.with(view.getContext()).load(imageUrl).apply(requestOption).thumbnail(0.1f).into(view);
        else
            view.setImageResource(R.drawable.place_holder_400_400);
    }

}
