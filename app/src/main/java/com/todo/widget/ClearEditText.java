package com.todo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.todo.R;


/**
 * Created by ditclear on 16/7/20.
 */
public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {
    int focusColor;
    int unFocusColor;
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable, mLeftDrawable;
    private Drawable mWrongDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;
    private OnClearListener mOnClearListener;

    public ClearEditText(Context context) {
        super(context, null);
        init(null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        super(context, attrs);
        init(attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        focusColor = ContextCompat.getColor(getContext(), R.color.deep_gray);
        unFocusColor = ContextCompat.getColor(getContext(), R.color.dark_gray);
        mClearDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_clear);
        mWrongDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.wrong_tag_1);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ClearEditText);
        int resId = array.getResourceId(R.styleable.ClearEditText_leftDrawable, -1);

        if (resId != -1) {
            mLeftDrawable = ContextCompat.getDrawable(getContext(), resId);
        }

        // 默认设置隐藏图标
        setDrawable(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);


    }

    //设置删除图片
    private void setDrawable(boolean showError) {
        if (showError) {
            setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, mWrongDrawable, null);
            setTextColor(Color.RED);
            return;
        }
        if (length() == 0 || !hasFocus) {
            setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, mClearDrawable, null);

        }

    }

    @Override
    public void setError(CharSequence error) {

        setDrawable(!TextUtils.isEmpty(error));
        super.setError(error, mWrongDrawable);

    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 - 图标到控件右边的间距 - 图标的宽度 和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] == mClearDrawable) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                    if (mOnClearListener != null) {
                        mOnClearListener.onClear();
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏 如果文本设置 enable = false 不显示清除按钮
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (isEnabled()) {
            setHintTextColor(hasFocus ? focusColor : unFocusColor);
        }
        setDrawable(false);
    }

    private void resetTextColor(EditText editText) {
        if (editText.getCurrentTextColor() == Color.RED) {
            editText.setTextColor(Color.BLACK);
        }

    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setDrawable(false);
        if (s.toString().trim().length() == 0) {
            if (mOnClearListener != null) {
                mOnClearListener.onClear();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        resetTextColor(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        setDrawable(false);

    }

    public void setOnClearListener(OnClearListener onClearListener) {
        this.mOnClearListener = onClearListener;
    }

    public interface OnClearListener {
        void onClear();
    }

}