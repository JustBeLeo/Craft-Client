package com.android.sdk13.craft.independent_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Chat;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;

public class ChatAdapter extends BaseRecycleAdapter<Chat> {

    public ChatAdapter(ArrayList<Chat> list, Context mContext, RecyclerView recyclerView) {
        super( list, mContext, recyclerView );
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        if (ItemType == 1) {
            return new SendHolder( view );
        } else
            return new ReceiveHolder( view );
    }

    @Override
    protected int getLayoutID(int ItemType) {
        if (ItemType == 1) {
            return R.layout.item_chat_send;
        } else
            return R.layout.item_chat_receive;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        Chat chat = list.get( i );
        if (holder instanceof SendHolder){
            Glide.with( mContext )
                    .load( chat.getAvatarUrl() )
                    .circleCrop()
                    .into( ((SendHolder) holder).ivItemCsAvatar );
            ((SendHolder) holder).tvItemCsText.setText( chat.getText() );

        }else if (holder instanceof ReceiveHolder){
            Glide.with( mContext )
                    .load( chat.getAvatarUrl() )
                    .circleCrop()
                    .into( ((ReceiveHolder) holder).ivItemCrAvatar );
            ((ReceiveHolder) holder).tvItemCrText.setText( chat.getText() );
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get( position ).isSend())
            return 1;
        else
            return 0;
    }

    class SendHolder extends BaseRecycleViewHolder {
        @BindView(R.id.tv_item_cs_text)
        TextView tvItemCsText;
        @BindView(R.id.iv_item_cs_avatar)
        ImageView ivItemCsAvatar;
        public SendHolder(View itemView) {
            super( itemView );
        }
    }

    class ReceiveHolder extends BaseRecycleViewHolder {
        @BindView(R.id.tv_item_cr_text)
        TextView tvItemCrText;
        @BindView(R.id.iv_item_cr_avatar)
        ImageView ivItemCrAvatar;
        public ReceiveHolder(View itemView) {
            super( itemView );
        }
    }
}
