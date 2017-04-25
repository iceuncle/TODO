package com.todo.vendor.recyleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;


import com.todo.BR;

import java.util.List;

/**
 * Base Data Binding RecyclerView Adapter.
 */
public abstract class BaseViewAdapter<T> extends RecyclerView.Adapter<BindingViewHolder> {

    protected final LayoutInflater mLayoutInflater;

    protected List<T> mCollection;
    protected Presenter mPresenter;
    protected Decorator mDecorator;

    public BaseViewAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        final Object item = mCollection.get(position);
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().setVariable(BR.presenter, getPresenter());
        holder.getBinding().executePendingBindings();
        if (mDecorator != null) {
            mDecorator.decorator(holder, position, getItemViewType(position));
        }
    }

    @Override
    public int getItemCount() {
        return mCollection.size();
    }

    public void remove(int position) {
        notifyItemRemoved(position);
        mCollection.remove(position);
    }

    public void remove(T item) {
        int pos = mCollection.indexOf(item);
        notifyItemRemoved(pos);
        mCollection.remove(item);
    }

    public void clear() {
        mCollection.clear();
        notifyDataSetChanged();
    }

    public void setDecorator(Decorator decorator) {
        mDecorator = decorator;
    }

    protected Presenter getPresenter() {
        return mPresenter;
    }

    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    public interface Presenter {

    }

    public interface Decorator {
        void decorator(BindingViewHolder holder, int position, int viewType);
    }
}