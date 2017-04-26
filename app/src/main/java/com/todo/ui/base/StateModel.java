package com.todo.ui.base;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;


import com.todo.MyApplication;
import com.todo.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 页面描述：状态页面设置模型
 * <p>
 * Created by ditclear on 2017/2/24.
 */

public class StateModel extends BaseObservable {

    private Context mContext = MyApplication.instance();

    @EmptyState
    private int emptyState = EmptyState.NORMAL;

    private boolean empty;


    public int getEmptyState() {
        return emptyState;
    }

    /**
     * 设置状态
     *
     * @param emptyState
     */
    public void setEmptyState(@EmptyState int emptyState) {
        this.emptyState = emptyState;
        notifyChange();
    }


    /**
     * 根据异常显示状态
     *
     * @param e
     */
    public void bindThrowable(Throwable e) {
        if (e instanceof EmptyException) {
            setEmptyState(((EmptyException) e).getEmptyType());
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            //网络未连接
        }
    }

    public boolean isEmpty() {
        return this.emptyState != EmptyState.NORMAL;
    }

    /**
     * 显示进度条
     *
     * @return
     */
    public boolean isProgressing() {
        return this.emptyState == EmptyState.PROGRESS;
    }

    /**
     * 获取空状态
     *
     * @return
     */
    @Bindable
    public String getCurrentStateLabel() {

        int resId = R.string.no_data;

        switch (emptyState) {

            case EmptyState.EMPTY_DATA:
                resId = R.string.no_data;
                break;
            case EmptyState.EMPTY_MUSIC:
                resId = R.string.no_music;
                break;
            case EmptyState.EMPTY_RECORDER:
                resId = R.string.no_recorder;
                break;
            case EmptyState.EMPTY_KIND_SCHEDULE:
                resId = R.string.no_kind_schedule;
                break;
            case EmptyState.EMPTY_DAIBAN_SCHEDULE:
                resId = R.string.no_daiban_schedule;
                break;
            case EmptyState.EMPTY_GUOQI_SCHEDULE:
                resId = R.string.no_guoqi_schedule;
                break;
            case EmptyState.EMPTY_WANCHENG_SCHEDULE:
                resId = R.string.no_wancheng_schedule;
                break;
            case EmptyState.NORMAL:
                break;
            case EmptyState.PROGRESS:
                resId = R.string.nothing;
                break;
        }
        return mContext.getString(resId);
    }

    @Bindable
    public Drawable getEmptyIconRes() {

        @DrawableRes
        int resId = R.mipmap.none_data;

        switch (emptyState) {
            case EmptyState.EMPTY_DATA:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.EMPTY_MUSIC:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.EMPTY_RECORDER:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.EMPTY_KIND_SCHEDULE:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.EMPTY_DAIBAN_SCHEDULE:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.EMPTY_GUOQI_SCHEDULE:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.EMPTY_WANCHENG_SCHEDULE:
                resId = R.mipmap.none_data;
                break;
            case EmptyState.NORMAL:
                break;
            case EmptyState.PROGRESS:

                break;
        }
        return ContextCompat.getDrawable(mContext, resId);
    }

}
