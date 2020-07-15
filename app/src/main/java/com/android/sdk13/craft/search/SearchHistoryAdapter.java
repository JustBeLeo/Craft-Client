package com.android.sdk13.craft.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/*
 * @Author sdk13
 * @Description 搜索历史记录的适配器
 * @Date 11:59 2020/6/26
 **/
public class SearchHistoryAdapter extends BaseRecycleAdapter<String> {

    public SearchHistoryAdapter(ArrayList<String> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        return new Holder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        return R.layout.item_history;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if(holder instanceof Holder){
            if(onTextClick !=null)
                ((Holder) holder).tvText.setOnClickListener( v -> onTextClick.onClick( i ) );
            if(onDeleteClick !=null){
                ((Holder) holder).ivDelete.setOnClickListener( v -> onDeleteClick.onClick( i ) );
            }
            ((Holder) holder).tvText.setText( list.get( i ) );
        }
    }

    interface OnClickListener{
        void onClick(int i);
    }

    private OnClickListener onTextClick = null;

    private OnClickListener onDeleteClick = null;

    public void setOnTextClick(OnClickListener onTextClick) {
        this.onTextClick = onTextClick;
    }

    public void setOnDeleteClick(OnClickListener onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }



    class Holder extends BaseRecycleViewHolder {

        @BindView(R.id.tv_item_history_text)
        TextView tvText;
        @BindView(R.id.iv_item_history_delete)
        ImageView ivDelete;

        Holder(View itemView) {
            super( itemView );
        }
    }
}
