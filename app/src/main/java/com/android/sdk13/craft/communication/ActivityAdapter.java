package com.android.sdk13.craft.communication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Activity;
import com.android.sdk13.craft.entity.OffLine;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ActivityAdapter extends BaseRecycleAdapter<Activity> {

    public ActivityAdapter(List<Activity> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        return new Holder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        return R.layout.item_offline;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if(holder instanceof Holder){
            Activity item = list.get( i );
            ((Holder) holder).tvTitle.setText( item.getName() );
            ((Holder) holder).tvText.setText( item.getContent() );
            ((Holder) holder).tvCount.setText( item.getMember_count() + "" );
            ((Holder) holder).tvLocate.setText( item.getSite() +"" );
            Glide.with( mContext )
                    .load(NetConfig.URL + item.getCover_url() )
                    .into( ((Holder) holder).ivCover );
        }
        if(mOnClick!=null){
            holder.itemView.setOnClickListener( v -> mOnClick.onclick( i ) );
        }
    }

    public interface OnClickListener{
        void onclick(int i);
    }

    public void setmOnClick(OnClickListener mOnClick) {
        this.mOnClick = mOnClick;
    }

    OnClickListener mOnClick = null;

    class Holder extends BaseRecycleViewHolder {
        @BindView(R.id.tv_item_offline_title)
        TextView tvTitle;
        @BindView(R.id.tv_item_offline_text)
        TextView tvText;
        @BindView(R.id.tv_item_offline_locate)
        TextView tvLocate;
        @BindView(R.id.tv_item_offline_count)
        TextView tvCount;
        @BindView( R.id.iv_item_offline_cover )
        ImageView ivCover;
        Holder(View itemView) {
            super( itemView );
        }
    }
}
