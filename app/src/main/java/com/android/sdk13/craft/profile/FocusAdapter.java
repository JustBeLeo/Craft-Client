package com.android.sdk13.craft.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.sdk13.craft.R;
import com.android.sdk13.craft.base.BaseRecycleAdapter;
import com.android.sdk13.craft.base.BaseRecycleViewHolder;
import com.android.sdk13.craft.entity.Focus;
import com.android.sdk13.craft.entity.User;
import com.android.sdk13.craft.utils.NetConfig;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName:    Craft
 * Package:        com.android.sdk13.craft.profile
 * ClassName:      FocusAdapter
 * Description:
 * Author:         Leo
 * CreateDate:     2020/7/10 0:02
 * UpdateUser:     更新者
 * UpdateDate:     2020/7/10 0:02
 * UpdateRemark:   更新说明
 * Version:        1.0
 */
class FocusAdapter extends BaseRecycleAdapter<Focus> {

    int type;

    public FocusAdapter(List<Focus> list, Context mContext, RecyclerView recyclerView,int type) {
        super(list, mContext, recyclerView);
        this.type = type;
    }

    @Override
    protected BaseRecycleViewHolder setHolder(View view, int ItemType) {
        return new FocusHolder(view);
    }

    @Override
    protected int getLayoutID(int ItemType) {
        return R.layout.item_inbox;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecycleViewHolder holder, int i) {
        if (holder instanceof FocusHolder) {
            User user;
            if (type == 1){
                user = list.get(i).getFocus_user();
            }
            else {
                user = list.get(i).getUser();
            }
            ((FocusHolder) holder).tvItemInboxDate.setText("");
            ((FocusHolder) holder).tvItemInboxUser.setText(user.getUsername());
            ((FocusHolder) holder).tvItemInboxText.setText(user.getMotto());
            Glide.with(mContext)
                    .load(NetConfig.URL + user.getAvatar_url())
                    .circleCrop()
                    .into(((FocusHolder) holder).ivItemInboxAvatar);
        }
    }

    static class FocusHolder extends BaseRecycleViewHolder {
        @BindView(R.id.iv_item_inbox_avatar)
        ImageView ivItemInboxAvatar;
        @BindView(R.id.tv_item_inbox_user)
        TextView tvItemInboxUser;
        @BindView(R.id.tv_item_inbox_text)
        TextView tvItemInboxText;
        @BindView(R.id.tv_item_inbox_date)
        TextView tvItemInboxDate;

        FocusHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
