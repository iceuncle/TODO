package com.todo.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.todo.R;


/**
 * Created by 15828 on 2016/7/25.
 */
public class ImageButtonText extends LinearLayout implements View.OnClickListener {

    private ImageView imgView;
    private TextView textView;
    private boolean checked = false;
    private OnImageButtonTextClickListener mOnImageButtonTextClickListener;

    public ImageButtonText(Context context) {
        super(context, null);
    }

    public ImageButtonText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_img_text, this, true);

        this.imgView = (ImageView) findViewById(R.id.imgview);
        this.textView = (TextView) findViewById(R.id.textview);

        this.setClickable(true);
        this.setFocusable(true);
//        checked = DataSetting.IsFocused(context);

        this.setOnClickListener(this);

    }

    public void setImgView(ImageView imgView){
        this.imgView = imgView;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }


    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    @Override
    public void onClick(View v) {
        mOnImageButtonTextClickListener.OnImageButtonTextClick(v);
    }

    public interface OnImageButtonTextClickListener {
        void OnImageButtonTextClick(View v);
    }

    public void setmOnImageButtonTextClickListener(OnImageButtonTextClickListener mOnImageButtonTextClickListener) {
        this.mOnImageButtonTextClickListener = mOnImageButtonTextClickListener;
    }
}
