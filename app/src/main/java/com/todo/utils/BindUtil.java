package com.todo.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.todo.MyApplication;
import com.todo.data.bean.Picture;

/**
 * Created by tianyang on 2017/5/9.
 */

public class BindUtil {

    @BindingAdapter("imgUrl")
    public static void setImgView(ImageView imgView, Picture picture) {
        if (picture.isAdd()) {
            Glide.with(MyApplication.instance())
                    .load(picture.getImgId())
                    .into(imgView);
        } else {
            Glide.with(MyApplication.instance())
                    .load(picture.getImgRes())
                    .into(imgView);
        }
    }
}
