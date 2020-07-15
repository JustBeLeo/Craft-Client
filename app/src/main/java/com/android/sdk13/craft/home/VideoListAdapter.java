package com.android.sdk13.craft.home;

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
import com.android.sdk13.craft.entity.Article;
import com.android.sdk13.craft.entity.Video;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;

public class VideoListAdapter extends BaseRecycleAdapter<Video> {

    public VideoListAdapter(List<Video> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int i) {
        return new myHolder( view );
    }

    @Override
    protected int getLayoutID(int i) {
        return R.layout.item_small;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if (holder instanceof myHolder) {
            Video item = list.get( i );
            //装填数据
            Glide.with( mContext )
                    .load(NetConfig.URL + item.getCover_url() )
                    .into(((myHolder) holder).ivCover);
            ((myHolder) holder).tvTitle.setText( item.getName() );
            ((myHolder) holder).tvType.setText( item.getType().getName() );
            ((myHolder) holder).tvDetail.setText( item.getDes() );
            ((myHolder) holder).tvCollect.setText( String.format( "%d", item.getFavor_count() ) );
            ((myHolder) holder).tvWatch.setText( String.format( "%d", item.getClick_count() ) );

            //添加监听
            if (mOnClickListener != null) {
                holder.itemView.setOnClickListener( (v) -> {
                    mOnClickListener.onClick( i );
                } );
            }
        }
    }

    interface onClickListener {
        void onClick(int i);
    }

    onClickListener mOnClickListener = null;

    public void setmOnClickListener(onClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    class myHolder extends BaseRecycleViewHolder {

        @BindView(R.id.iv_item_small_cover)
        ImageView ivCover;
        @BindView(R.id.tv_item_small_type)
        TextView tvType;
        @BindView(R.id.tv_item_small_title)
        TextView tvTitle;
        @BindView(R.id.tv_item_small_watch)
        TextView tvWatch;
        @BindView(R.id.tv_item_small_collect)
        TextView tvCollect;
        @BindView( R.id.tv_item_small_detail )
        TextView tvDetail;
        public myHolder(View itemView) {
            super( itemView );
        }
    }
}
