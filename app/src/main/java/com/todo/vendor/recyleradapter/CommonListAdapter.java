package com.todo.vendor.recyleradapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ditclear on 16/7/15.
 * 通用的listView适配器
 *
 * @param <D>  数据集中的类型
 * @param <VH> viewHolder
 */
public abstract class CommonListAdapter<D, VH> extends BaseAdapter {

    public List<D> mList;
    public D[] mArray;
    public Context mContext;
    public LayoutInflater mInflater;

    public CommonListAdapter(Context context, List<D> array) {
        mContext = context;
        mList = array;
        mInflater = LayoutInflater.from(context);
    }

    //专门给订单列表的包裹列表使用的构造方法
    public CommonListAdapter(Context context, List<D> array, boolean isForOrderListPackage) {
        mContext = context;
        mList = new ArrayList<>();
        mList.addAll(array);
        mInflater = LayoutInflater.from(context);
    }

    public CommonListAdapter(Context context, D[] array) {
        mContext = context;
        mArray = array;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return mList == null ? (mArray == null ? 0 : mArray.length) : mList.size();
    }

    @Override
    public D getItem(int position) {
        return mList == null ? mArray[position] : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH viewHolder = null;
        if (convertView == null) {
            convertView = inflateView();
            viewHolder = initViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
        }
        render(viewHolder, getItem(position), position);
        return convertView;
    }

    public abstract VH initViewHolder(View convertView);

    public abstract void render(VH viewHolder, D d, int position);

    public abstract View inflateView();

}
