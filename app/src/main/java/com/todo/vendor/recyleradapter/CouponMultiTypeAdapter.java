package com.todo.vendor.recyleradapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.LayoutRes;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 选择优惠券
 *
 * @author markzhai on 16/8/23
 */
public class CouponMultiTypeAdapter extends BaseViewAdapter<Object> {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private CouponMultiTypeAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(CouponMultiTypeAdapter.OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public interface MultiViewTyper {
        int getViewType(Object item);
    }

    protected ArrayList<Integer> mCollectionViewType;

    private ArrayMap<Integer, Integer> mItemTypeToLayoutMap = new ArrayMap<>();

    public CouponMultiTypeAdapter(Context context) {
        this(context, null);
    }

    public CouponMultiTypeAdapter(Context context, Map<Integer, Integer> viewTypeToLayoutMap) {
        super(context);
        mCollection = new ArrayList<>();
        mCollectionViewType = new ArrayList<>();
        if (viewTypeToLayoutMap != null && !viewTypeToLayoutMap.isEmpty()) {
            mItemTypeToLayoutMap.putAll(viewTypeToLayoutMap);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingViewHolder(
                DataBindingUtil.inflate(mLayoutInflater, getLayoutRes(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(final BindingViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    public void addViewTypeToLayoutMap(Integer viewType, Integer layoutRes) {
        mItemTypeToLayoutMap.put(viewType, layoutRes);
    }

    @Override
    public int getItemViewType(int position) {
        return mCollectionViewType.get(position);
    }

    public void set(List viewModels, int viewType) {
        mCollection.clear();
        mCollectionViewType.clear();

        if (viewModels == null) {
            add(null, viewType);
        } else {
            addAll(viewModels, viewType);
        }
    }

    public void set(List viewModels, MultiViewTyper viewTyper) {
        mCollection.clear();
        mCollectionViewType.clear();

        addAll(viewModels, viewTyper);
    }

    public void add(Object viewModel, int viewType) {
        mCollection.add(viewModel);
        mCollectionViewType.add(viewType);
        notifyItemInserted(0);
    }

    public void add(int position, Object viewModel, int viewType) {
        mCollection.add(position, viewModel);
        mCollectionViewType.add(position, viewType);
        notifyItemInserted(position);
    }

    public void addAll(List viewModels, int viewType) {
        mCollection.addAll(viewModels);
        for (int i = 0; i < viewModels.size(); ++i) {
            mCollectionViewType.add(viewType);
        }
        notifyDataSetChanged();
    }

    public void addAll(int position, List viewModels, int viewType) {
        mCollection.addAll(position, viewModels);
        for (int i = 0; i < viewModels.size(); i++) {
            mCollectionViewType.add(position + i, viewType);
        }
        notifyItemRangeChanged(position, viewModels.size() - position);
    }

    public void addAll(List viewModels, MultiViewTyper multiViewTyper) {
        mCollection.addAll(viewModels);
        for (int i = 0; i < viewModels.size(); ++i) {
            mCollectionViewType.add(multiViewTyper.getViewType(viewModels.get(i)));
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mCollectionViewType.remove(position);
        super.remove(position);
    }

    public void clear() {
        mCollectionViewType.clear();
        super.clear();
    }

    @LayoutRes
    protected int getLayoutRes(int viewType) {
        return mItemTypeToLayoutMap.get(viewType);
    }
}