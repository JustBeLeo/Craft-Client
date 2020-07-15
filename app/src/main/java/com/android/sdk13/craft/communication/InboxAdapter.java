package com.android.sdk13.craft.communication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.InboxBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;

public class InboxAdapter extends BaseRecycleAdapter<InboxBean> {

    public InboxAdapter(ArrayList<InboxBean> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        return new Holder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        return R.layout.item_inbox;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if(holder instanceof  Holder){
            InboxBean inboxBean = list.get( i );
            Glide.with( mContext )
                    .load( inboxBean.getAvatar() )
                    .into( ((Holder) holder).ivIAvatar );
            ((Holder) holder).tvIUser.setText( inboxBean.getUser() );
            ((Holder) holder).tvText.setText( inboxBean.getText() );
            ((Holder) holder).tvDate.setText( inboxBean.getDate());
            if (onClickListener!=null){
                holder.itemView.setOnClickListener( v -> onClickListener.onClick( i ) );
            }
        }
    }

    class Holder extends BaseRecycleViewHolder {
        @BindView(R.id.iv_item_inbox_avatar)
        ImageView ivIAvatar;
        @BindView(R.id.tv_item_inbox_user)
        TextView tvIUser;
        @BindView(R.id.tv_item_inbox_text)
        TextView tvText;
        @BindView(R.id.tv_item_inbox_date)
        TextView tvDate;
        public Holder(View itemView) {
            super( itemView );
        }
    }
}
