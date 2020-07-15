package com.android.sdk13.craft.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleViewHolder> {

    public List<T> list;
    public Context mContext;
    public RecyclerView recyclerView;
    public static final int HEAD = 992;

    public BaseRecycleAdapter(List<T> list, Context mContext, RecyclerView recyclerView) {
        this.list = list;
        this.mContext = mContext;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BaseRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int ItemType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutID(ItemType),viewGroup,false);
        return setHolder(view,ItemType);
    }

    protected abstract BaseRecycleViewHolder setHolder(View view, int ItemType);

    protected abstract int getLayoutID(int ItemType);

    public interface onClickListener{
        void onClick(int i);
    }

   protected onClickListener onClickListener = null;

    public void setOnClickListener(BaseRecycleAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
